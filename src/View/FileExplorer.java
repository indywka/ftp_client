package View;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public abstract class FileExplorer extends JPanel {
    DefaultListModel<String> model;
    String path = "";
    private List<FileExplorerListener> listeners = new ArrayList<>();
    private JList<String> list;
    private JPopupMenu menu;
    private int indexPopMenu = -1;

    FileExplorer() {
        menu = new JPopupMenu();
        setLayout(new BorderLayout());
        model = new DefaultListModel<>();
        list = new JList<>(this.model);
        this.list.addMouseListener(new ListenMouse());
        this.add(this.list);

        JMenuItem itemSuppr = new JMenuItem("DELETE");
        itemSuppr.addActionListener(arg0 -> delete(FileExplorer.this.indexPopMenu));
        this.menu.add(itemSuppr);

        JMenuItem itemInfo = new JMenuItem("INFO");
        itemInfo.addActionListener(arg0 -> info(FileExplorer.this.indexPopMenu));
        this.menu.add(itemInfo);

        this.list.addMouseListener(new ListenMouse());
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

    protected abstract void info(int index);

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
        private int lastIndex = -1;
        private int nbClick = -1;

        @Override
        public void mouseClicked(MouseEvent e) {
            if (e.getButton() == MouseEvent.BUTTON1) {
                int index = FileExplorer.this.list.locationToIndex(e.getPoint());
                if (index != this.lastIndex) this.nbClick = 0;
                this.lastIndex = index;
                this.nbClick++;
                if (this.nbClick == 2) {
                    this.nbClick = 0;
                    FileExplorer.this.selected(index);
                }
            } else {
                this.lastIndex = -1;
            }
            checkPopup(e);
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
                FileExplorer.this.indexPopMenu = FileExplorer.this.list.locationToIndex(event.getPoint());
                menu.show(FileExplorer.this, event.getX(), event.getY());

            }
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            checkPopup(e);
        }

    }
}
