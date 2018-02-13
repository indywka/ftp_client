package View;

public interface ConnectListener {
    void needConnect(String login, char[] password, String host, String port);

    void needDisconnect();
}
