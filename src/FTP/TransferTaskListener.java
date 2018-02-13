package FTP;

public interface TransferTaskListener {
    void transfer(long transfer);

    void finish();
}
