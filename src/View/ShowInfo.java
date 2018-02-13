package View;

import javax.swing.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ShowInfo extends JDialog {
    private JLabel name = new JLabel();
    private JLabel path = new JLabel();
    private JLabel size = new JLabel();
    private JLabel type = new JLabel();
    private JLabel date = new JLabel();

    ShowInfo() {
        JPanel left = new JPanel();
        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));

        left.add(new JLabel("INFO_DIR"));
        left.add(new JLabel("INFO_NAME"));
        left.add(new JLabel("INFO_TYPE"));
        left.add(new JLabel("INFO_SIZE"));
        left.add(new JLabel("INFO_DATE"));
        left.add(new JLabel("INFO_PERM)"));
        left.add(new JLabel("INFO_OWNER"));
        left.add(new JLabel("INFO_GROUP"));

        JPanel right = new JPanel();
        right.setLayout(new BoxLayout(right, BoxLayout.Y_AXIS));

        right.add(this.path);
        right.add(this.name);
        right.add(this.type);
        right.add(this.size);
        right.add(this.date);
        JLabel perm = new JLabel();
        right.add(perm);
        JLabel owner = new JLabel();
        right.add(owner);
        JLabel group = new JLabel();
        right.add(group);

        getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.X_AXIS));
        add(left);
        add(right);

        setSize(300, 200);
    }

    void setDir(String dir) {
        this.path.setText(dir);
    }

    public void setName(String name) {
        this.name.setText(name);
    }

    void setTypeFile(String type) {
        this.type.setText(type);
    }

    void setSize(long size) {
        this.size.setText(Long.toString(size));
    }

    void setDate(Date date) {
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        this.date.setText(df.format(date));
    }

}
