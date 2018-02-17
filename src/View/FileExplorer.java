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
    private int indexPopMenu = -1;
    JTree tree = new JTree();


    FileExplorer() {

        menu = new JPopupMenu();

        setLayout(new BorderLayout());
        model = new DefaultListModel<>();
        JList<String> list = new JList<>(this.model);
        list.addMouseListener(new ListenMouse());
        this.add(list);


        JMenuItem itemSuppr = new JMenuItem("DELETE");
        itemSuppr.addActionListener(arg0 -> delete(FileExplorer.this.indexPopMenu));
        this.menu.add(itemSuppr);

        JMenuItem itemInfo = new JMenuItem("INFO");
        itemInfo.addActionListener(arg0 -> info(path));
        this.menu.add(itemInfo);

        tree.addMouseListener(new ListenMouse());
    }

    String getCurrentPath() {
        return this.path;
    }

    void clear() {
        this.model.clear();
    }

    public abstract void setPath(String path);

    protected abstract void selected(int index);

    protected abstract void delete(int index);

    protected abstract void info(String path);

    void addListener(FileExplorerListener listener) {
        this.listeners.add(listener);
    }

    void notifySelectedFile(String path) {
        for (FileExplorerListener listener : this.listeners) listener.selectedFile(path);
    }

    //обработка событий для мыши
    // 1) если нажали два раза ЛКМ, то происходит переход , иначе ничего
    // 2) при нажитии ПКМ открыается menu
    private class ListenMouse extends MouseAdapter {

        @Override
        public void mouseClicked(MouseEvent e) {
            if (e.getButton() == MouseEvent.BUTTON1) {
                TreePath selPath = tree.getPathForLocation(e.getX(), e.getY());
                if (e.getClickCount() == 1) {
                    System.out.println(selPath.getLastPathComponent());
                } else if (e.getClickCount() == 2) {
                    System.out.println("Double" + selPath.getLastPathComponent());
                }
            }
        }


        @Override
        public void mouseEntered(MouseEvent e) {
            checkPopup(e);
        }

        @Override
        public void mouseExited(MouseEvent e) {
            checkPopup(e);
        }

        @Override
        public void mousePressed(MouseEvent e) {
            checkPopup(e);
        }

        void checkPopup(MouseEvent event) {
            if (event.isPopupTrigger()) {
                    int row = tree.getClosestRowForLocation(event.getX(), event.getY());
                    tree.setSelectionRow(row);
                    menu.show(FileExplorer.this, event.getX(), event.getY());
            }
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            checkPopup(e);
        }


    }



}
