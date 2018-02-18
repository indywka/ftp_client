package View;

import javax.swing.*;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public abstract class FileExplorer extends JPanel {

    DefaultListModel<String> model;
    String path = "";
    private List<FileExplorerListener> listeners = new ArrayList<>();
    private JPopupMenu menu;
    JTree tree = new JTree();
    private JMenuItem itemInfo, itemDelete, itemUpload;

    FileExplorer() {

        menu = new JPopupMenu();
        setLayout(new BorderLayout());
        model = new DefaultListModel<>();
        JList<String> list = new JList<>(this.model);
        list.addMouseListener(new ListenMouse());
        this.add(list);

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
        this.model.clear();
    }

    public abstract void setPath(String path);

    protected abstract void selected(String path);

    protected abstract void createTree();

    protected abstract void delete(String path);

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
                        delete(curPath);
                        createTree();
                    });
                    itemUpload.addActionListener(arg2 -> selected(curPath));

                }
            }
        }

        @Override
        public void mouseReleased(MouseEvent e) {
        }
    }
}
