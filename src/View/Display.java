package View;

import FTP.TransferTask;
import FTP.controllerFTP;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.Socket;

public class Display extends JFrame {

    private controllerFTP pi = new controllerFTP();
    private Socket sock;
    private ConnectPan conn = new ConnectPan();
    private FileExplorerFTP expFTP = new FileExplorerFTP();

    public Display() {
        JPanel pan = new JPanel();
        pan.setLayout(new BoxLayout(pan, BoxLayout.Y_AXIS));
        setContentPane(pan);

        this.conn.setPreferredSize(new Dimension(800, 30));
        pan.add(this.conn);

        LogFTP log = new LogFTP();
        this.pi.addListener(log);
        log.setPreferredSize(new Dimension(800, 250));
        add(log);

        JPanel panTree = new JPanel();
        panTree.setLayout(new BoxLayout(panTree, BoxLayout.X_AXIS));
        FileExplorerLocal expLocal = new FileExplorerLocal();
        JScrollPane scLocal = new JScrollPane(expLocal);
        JScrollPane scFTP = new JScrollPane(this.expFTP);
        scLocal.setPreferredSize(new Dimension(400, 560));
        scFTP.setPreferredSize(new Dimension(400, 560));
        panTree.add(scLocal);
        panTree.add(scFTP);
        pan.add(panTree);


        this.conn.addListener(new ListenConnect());
        expLocal.addListener(new ListenExpLocal());
        this.expFTP.addListener(new ListenExpFTP());

    }

    private class ListenExpLocal implements FileExplorerListener {

        @Override
        public void selectedFile(String path) {
            if (!Display.this.pi.isConnected()) return;
            File file = new File(path);

            try {
                TransferTask trf = new TransferTask(
                        new FileInputStream(file),
                        Display.this.pi.upload(Display.this.expFTP.getCurrentPath() + "/" + file.getName()),
                        file.length());

                Thread th = new Thread(trf);
                th.start();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    private class ListenExpFTP implements FileExplorerListener {

        @Override
        public void selectedFile(String path) {
        }
    }

    private class ListenConnect implements ConnectListener {

        @Override
        public void needConnect(String login, char[] password, String host, String port) {
            try {
                Socket sock = new Socket(host, Integer.parseInt(port));

                BufferedReader read = new BufferedReader(new InputStreamReader(sock.getInputStream()));
                read.readLine();

                Display.this.pi.setInputStream(sock.getInputStream());
                Display.this.pi.setOutputStream(sock.getOutputStream());

                if (Display.this.pi.connect(login, new String(password))) {
                    Display.this.conn.setEnabled(false);
                    Display.this.sock = sock;
                    Display.this.expFTP.setPiFTP(pi);
                    Display.this.expFTP.setPath("/");
                } else {
                    Display.this.conn.setEnabled(true);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        @Override
        public void needDisconnect() {
            Display.this.expFTP.setPiFTP(null);
            Display.this.expFTP.clear();

            if (Display.this.sock != null) {
                try {
                    Display.this.sock.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Display.this.sock = null;
            }

            Display.this.conn.setEnabled(true);
            Display.this.pi.disconnect();
        }

    }
}
