package View;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

public class ConnectPan extends JPanel {

    private List<ConnectListener> listeners = new ArrayList<>();

    private JTextField login = new JTextField("indywka");
    private JPasswordField password = new JPasswordField("min4ik97");
    private JTextField host = new JTextField("66.220.9.50");
    private JTextField port = new JTextField("21");
    private JButton button = new JButton();
    private boolean enabled = true;

    ConnectPan() {

        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        add(new JLabel("Логин"));
        add(this.login);
        add(new JLabel("Пароль"));
        add(this.password);
        add(new JLabel("Хост"));
        add(this.host);
        add(new JLabel("Порт"));
        add(this.port);

        ListenText lst = new ListenText();
        this.login.addKeyListener(lst);
        this.password.addKeyListener(lst);
        this.host.addKeyListener(lst);
        this.port.addKeyListener(lst);

        this.button = new JButton("Соединиться");
        button.setEnabled(true);
        this.button.addMouseListener(new ConnListener());
        add(this.button);

    }

    public void setEnabled(boolean enable) {
        this.login.setEnabled(enable);
        this.password.setEnabled(enable);
        this.host.setEnabled(enable);
        this.port.setEnabled(enable);
        this.button.setText(
                enable ? "Соединиться" : "Разъединиться");
        this.enabled = enable;
    }

    private boolean isEnable() {
        return this.enabled;
    }

    void addListener(ConnectListener listener) {
        this.listeners.add(listener);
    }

    private void notifyConnect(String login, char[] password, String host, String port) {
        for (ConnectListener listener : this.listeners)
            listener.needConnect(login, password, host, String.valueOf(port));
    }

    private void notifyDisconnect() {
        for (ConnectListener listener : this.listeners) listener.needDisconnect();
    }

    private class ListenText implements KeyListener {

        @Override
        public void keyPressed(KeyEvent e) {
        }

        @Override
        public void keyReleased(KeyEvent e) {
            ConnectPan th = ConnectPan.this;
            if (th.isEnable()) th.button.setEnabled
                    (
                            !th.login.getText().isEmpty()
                                    && th.password.getPassword().length != 0
                                    && !th.host.getText().isEmpty()
                                    && !th.port.getText().isEmpty()
                    );
        }

        @Override
        public void keyTyped(KeyEvent e) {
        }

    }

    private class ConnListener implements MouseListener {

        @Override
        public void mouseClicked(MouseEvent e) {
            if (ConnectPan.this.isEnable()) {
                ConnectPan.this.notifyConnect(
                        ConnectPan.this.login.getText(),
                        ConnectPan.this.password.getPassword(),
                        ConnectPan.this.host.getText(),
                        ConnectPan.this.port.getText());
            } else ConnectPan.this.notifyDisconnect();
        }

        @Override
        public void mouseEntered(MouseEvent e) {
        }

        @Override
        public void mouseExited(MouseEvent e) {
        }

        @Override
        public void mousePressed(MouseEvent e) {
        }

        @Override
        public void mouseReleased(MouseEvent e) {
        }

    }
}
