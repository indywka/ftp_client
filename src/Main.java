import View.Display;

import javax.swing.*;
import java.awt.*;


public class Main {
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        } catch (UnsupportedLookAndFeelException | IllegalAccessException | InstantiationException | ClassNotFoundException e) {
            System.err.println("Проблема с установкой темы ");
        }
        EventQueue.invokeLater(() -> {
            Display display = new Display();
            display.setSize(800, 600);
            display.setTitle("FTP клиент");
            display.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            display.setLocationRelativeTo(null);
            display.setVisible(true);
        });
    }
}
