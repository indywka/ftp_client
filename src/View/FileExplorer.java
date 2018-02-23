package View;

import FTP.controllerFTP;

import javax.swing.*;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public abstract class FileExplorer extends JPanel {
    controllerFTP pi = null;
    String path = "";
    private List<FileExplorerListener> listeners = new ArrayList<>();
    private JPopupMenu menu;
    JTree tree = new JTree();
    private JMenuItem itemDelete;
    private JMenuItem itemUpload;

    FileExplorer() {

        menu = new JPopupMenu();
        setLayout(new BorderLayout());

        itemDelete = new JMenuItem("DELETE");
        this.menu.add(itemDelete);

        itemUpload = new JMenuItem("UPLOAD");
        this.menu.add(itemUpload);

        tree.addMouseListener(new ListenMouse());
    }

    String getCurrentPath() {
        return this.path;
    }

    void clear() {
//        this.model.clear();
    }

    public abstract void setPath(String path) throws IOException;

    protected abstract void selected(String path) throws IOException;

    protected abstract void createTree();

    protected abstract void delete(String path) throws IOException;

    void addListener(FileExplorerListener listener) {
        this.listeners.add(listener);
    }

    void notifySelectedFile(String path) {
        for (FileExplorerListener listener : this.listeners) listener.selectedFile(path);
    }


    private class ListenMouse extends MouseAdapter {

        @Override
        public void mouseClicked(MouseEvent e) {
        }

        @Override
        public void mouseEntered(MouseEvent e) {
        }

        @Override
        public void mouseExited(MouseEvent e) {
        }

        @Override
        public void mousePressed(MouseEvent e) {

            if (e.getButton() == MouseEvent.BUTTON3) {

                if (e.getClickCount() == 1) {

                    menu.show(FileExplorer.this, e.getX(), e.getY());

                    TreePath selPath = tree.getPathForLocation(e.getX(), e.getY());
                    assert selPath != null;
                    String curPath = String.valueOf(selPath.getLastPathComponent());
                    itemDelete.addActionListener(arg1 -> {
                        try {
                            delete(curPath);
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }
                        createTree();
                    });
                    itemUpload.addActionListener(arg2 -> {
                        try {
                            selected(curPath);
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }
                    });

                }
            }
        }

        @Override
        public void mouseReleased(MouseEvent e) {
        }
    }
}
