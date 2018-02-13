package FTP;

import java.io.*;
import java.net.Socket;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class controllerFTP {

    private List<controllerFTPListener> listeners = new ArrayList<>();
    private BufferedReader in;
    private BufferedWriter out;
    private boolean connected = false;
    private Type type;

    public boolean connect(String id, String passwd) {
        try {
            if (!command("USER " + id).startsWith("331 ")) return false;
            if (!command("PASS " + passwd).startsWith("230 ")) return false;

            if (isConnected()) {
                this.connected = false;
                notifyDisconnected();
            }

            this.connected = true;
            notifyConnected();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    private String getVal(String line) {
        int indxF = line.indexOf("modify");
        if (indxF == -1) return null;
        int indxL = line.indexOf(';', indxF);

        return line.substring(indxF + "modify".length() + 1, indxL);
    }

    private FTPFile parseLine(String line) {
        SimpleDateFormat parseDTF = new SimpleDateFormat("yyyyMMddHHmm");
        FTPFile file = new FTPFile();

        try {
            file.date = parseDTF.parse(getVal(line));
        } catch (ParseException e) {
            System.err.println(e.getMessage());
        }

        file.absPath = line.substring(line.lastIndexOf("; ") + 2, line.length());
        return file;
    }

    public synchronized List<FTPFile> getFiles(String path) {
        List<FTPFile> list;
        list = new ArrayList<>();

        if (this.type != Type.A) setMode(Type.A);
        Socket sock = PASV();
        if (sock == null) return list;

        try {
            BufferedReader read = new BufferedReader(new InputStreamReader(sock.getInputStream()));
            if (!command("MLSD " + path).startsWith("150")) return list;

            String str = read.readLine();
            while (str != null) {
                FTPFile file = parseLine(str);
                file.exist = true;

                list.add(file);
                str = read.readLine();
            }

            read.close();
            sock.close();

            notifyReceiveMsg(this.in.readLine());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return list;
    }

    public synchronized OutputStream upload(String absPath) {
        OutputStream out = null;
        if (this.type != Type.I && !setMode(Type.I)) return null;
        Socket sock = PASV();
        if (sock == null) return null;

        try {
            String log = command("STOR " + absPath);

            if (log.startsWith("125") || log.startsWith("150"))
                out = sock.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return out;
    }

    private Socket PASV() {
        Socket sock = null;
        String log;

        try {
            log = command("PASV");

            if (log.startsWith("227")) {
                String[] tab = log.substring(log.indexOf("(") + 1, log.indexOf(")")).split(",");
                String host = tab[0] + "." + tab[1] + "." + tab[2] + "." + tab[3];
                int port = (Integer.parseInt(tab[4]) << 8) + Integer.parseInt(tab[5]);

                sock = new Socket(host, port);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return sock;
    }

    public boolean isConnected() {
        return this.connected;
    }

    public void disconnect() {
        this.connected = false;
        notifyDisconnected();
    }

    private synchronized String command(String cmd) throws IOException {
        while (this.in.ready()) notifyReceiveMsg(this.in.readLine()); //secure clearing
        this.out.write(new String(cmd.getBytes(), "UTF-8") + "\r\n");
        this.out.flush();
        notifySendMsg(cmd);

        String str;
        try {
            str = this.in.readLine();
            assert str != null;
            if (str.startsWith("530") && isConnected()) {
                this.connected = false;
                notifyDisconnected();
            }

            notifyReceiveMsg(str);

        } catch (IOException e) {
            e.printStackTrace();

            if (isConnected()) {
                this.connected = false;
                notifyDisconnected();
            }

            throw e;
        }
        while (this.in.ready()) notifyReceiveMsg(this.in.readLine()); //secure clearing

        return str;
    }

    private boolean setMode(Type type) {
        try {
            if (command("TYPE " + type).startsWith("200")) {
                this.type = type;
                return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }

    public void setInputStream(InputStream in) {
        try {
            if (this.in != null) this.in.close();
            this.in = new BufferedReader(new InputStreamReader(in, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            System.err.println("WTF?");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setOutputStream(OutputStream out) {
        try {
            if (this.out != null) this.out.close();
            this.out = new BufferedWriter(new OutputStreamWriter(out, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            System.err.println("WTF?");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addListener(controllerFTPListener listener) {
        this.listeners.add(listener);
    }

    private void notifyReceiveMsg(String msg) {
        for (controllerFTPListener listener : this.listeners) listener.receiveMsg(msg);
    }

    private void notifySendMsg(String msg) {
        for (controllerFTPListener listener : this.listeners) listener.sendMsg(msg);
    }

    private void notifyConnected() {
        for (controllerFTPListener listener : this.listeners) listener.connected();
    }

    private void notifyDisconnected() {
        for (controllerFTPListener listener : this.listeners) listener.disconnected();
    }

    public enum Type {
        A, I
    }

}
