package FTP;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class TransferTask implements Runnable {
    private List<TransferTaskListener> listeners;
    private BufferedInputStream in;
    private BufferedOutputStream out;
    private long size;

    public TransferTask(InputStream in, OutputStream out, long size) {
        this.in = new BufferedInputStream(in);
        this.out = new BufferedOutputStream(out);
        this.size = size;
        listeners = new ArrayList<>();
    }

    @Override
    public void run() {
        byte[] buff = new byte[4096];
        int read;
        long totalRead = 0;

        try {
            read = this.in.read(buff);

            while (read != -1 && totalRead < this.size + 1) {
                totalRead += read;
                notifyTransfer(totalRead);
                this.out.write(buff, 0, read);
                read = this.in.read(buff);
            }

            this.in.close();
            this.out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        notifyFinish();
    }

    private void notifyTransfer(long transfer) {
        for (TransferTaskListener listener : this.listeners) listener.transfer(transfer);
    }

    private void notifyFinish() {
        for (TransferTaskListener listener : this.listeners) listener.finish();
    }
}

