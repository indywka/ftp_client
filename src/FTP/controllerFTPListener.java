package FTP;

public interface controllerFTPListener {
    void receiveMsg(String msg);

    void sendMsg(String msg);

    void connected();

    void disconnected();
}
