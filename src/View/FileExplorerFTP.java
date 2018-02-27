package View;

import FTP.FTPFile;
import FTP.controllerFTP;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;
import java.util.ArrayList;
import java.util.List;

public class FileExplorerFTP extends FileExplorer implements TreeSelectionListener {

    String value = "/";
    String name = "";
    private JTree treeFtp = new JTree();

    FileExplorerFTP() {
        createTree();
    }

    @Override
    protected void createTree() {

    }

    @Override
    public void setPath(String path) {
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("/");
        buildTree("/", root);
        treeFtp.setModel(new DefaultTreeModel(root));
        treeFtp.getSelectionModel().setSelectionMode
                (TreeSelectionModel.SINGLE_TREE_SELECTION);
//        treeFtp.addMouseListener(new ListenMouseFtp());
        treeFtp.addTreeSelectionListener(this::valueChanged);
        this.add(treeFtp);
        this.path = path;
        System.out.println("Путь после построения дерева:\t" + this.path);
    }

    public void clearTree() {
        new DefaultMutableTreeNode(null);
        new JTree(new DefaultTreeModel(null));
        treeFtp.setModel(null);
    }

    private List<String> getDir(String path) {
        ArrayList<FTPFile> files = this.pi.getFiles(path);
        List<String> list = new ArrayList<>();
        if (files.size() != 0) {


            for (FTPFile fileC : files) {
                list.add(fileC.getName());
            }
            System.out.println(list);


        }
        return list;
    }

    void setPiFTP(controllerFTP pi) {
        this.pi = pi;
    }

    @Override
    protected void selected(String index) {
        notifySelectedFile(index);
    }

    @Override
    protected void delete(String path) {
        this.pi.delete(this.pi.getFile(path));
        setPath(this.path);
    }

    private void buildTree(String currentDir, DefaultMutableTreeNode model) {

        List<String> currentCraw = getDir(currentDir);

        for (String node : currentCraw) {
            DefaultMutableTreeNode currentNode = new DefaultMutableTreeNode(node);
            buildTree(currentDir + "/" + node, currentNode);
            model.add(currentNode);
        }
    }

    @Override
    public void valueChanged(TreeSelectionEvent e) {
        TreePath jTreeDirectories = e.getPath();
        Object elements[];
        value = "";
        name = String.valueOf(jTreeDirectories.getLastPathComponent());
        elements = jTreeDirectories.getPath();
        for (int i = 0; i < elements.length; i++) {
            value += elements[i];
            if (i + 1 < elements.length) value += "/";
        }
        System.out.println("Путь к данным:\t" + value + "\t\t\t\tИмя файла(absPath):\t" + name);
        this.path = value;
        System.out.println("Текущий путь к чему-то после выбора чего-то (CURRENT PATH):\t" + this.value);
//        delete(value);
//        takeTheAbsPath(name);
    }

    public String takeTheAbsPath(String name) {
        return name;
    }


//    private class ListenMouseFtp extends MouseAdapter {
//
//        @Override
//        public void mousePressed(MouseEvent e) {
//            DefaultMutableTreeNode node = (DefaultMutableTreeNode) treeFtp.getLastSelectedPathComponent();
//
//            if (node.isLeaf()) {
//            } else {
//                System.out.print("WTF??");
//            }
//        }
//    }
}
