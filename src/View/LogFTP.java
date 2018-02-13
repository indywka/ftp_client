package View;

import FTP.controllerFTPListener;

import javax.swing.*;
import java.awt.*;

public class LogFTP extends JPanel implements controllerFTPListener {
    private JTextArea text = new JTextArea();


    LogFTP() {
        setLayout(new BorderLayout());
        JScrollPane scroll = new JScrollPane(this.text);
        this.text.setFont(text.getFont().deriveFont(14f));
        this.text.setForeground(new Color(0, 0, 0));
        add(scroll);
    }

    @Override
    public void receiveMsg(String msg) {
        this.text.append("recv: " + msg + "\n");

    }

    @Override
    public void sendMsg(String msg) {
        this.text.append("send: " + msg + "\n");

    }

    @Override
    public void connected() {
        this.text.append("#### СОЕДИНЕНО! #####\n");
    }

    @Override
    public void disconnected() {
        this.text.append("#### РАЗЪЕДИНЕНО! ####\n");
    }


}
