package View;

import FTP.FTPFile;
import FTP.controllerFTP;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileExplorerFTP extends FileExplorer {

    private DefaultMutableTreeNode root = new DefaultMutableTreeNode("CATALOGS_OF_THIS_SERVER");

    FileExplorerFTP() {
        createTree();
    }

    @Override
    protected void createTree() {

    }

    @Override
    public void setPath(String path) throws IOException {
        this.path = path;
//        getDir(this.path);
        buildTree("/", root);
        tree = new JTree(root);

        this.add(tree);
    }

    private List<String> getDir(String path) {
        this.path = path;
        tree.setModel(null);
        ArrayList<FTPFile> files = this.pi.getFiles(path);
        List<String> list = new ArrayList<>();
        for (FTPFile file : files) {
            list.add(file.getName());
        }
        System.out.println(list);
        return list;
    }

    // так, я получил список директорий: List<String> list.
    //теперь надо обойти дерево, т.е.

    void setPiFTP(controllerFTP pi) {
        this.pi = pi;
        this.model.clear();
    }

    @Override
    protected void selected(String index) throws IOException {
        String path = this.path;
        FTPFile file = this.pi.getFile(path);

        if (file.isDirectory()) {
            setPath(file.getAbsPath());
        } else notifySelectedFile(path);
    }

    @Override
    protected void delete(String path) throws IOException {
        if (!path.equals("..")) {
            this.pi.delete(this.pi.getFile(path));
            setPath(this.path);
        }

    }

    private void buildTree(String currentdir, DefaultMutableTreeNode model) throws IOException {

        List<String> currentCraw = getDir(currentdir);

        for (String node : currentCraw) {
            DefaultMutableTreeNode currentnode = new DefaultMutableTreeNode(node);
//            buildTree(currentdir + "/" + node, currentnode);
            model.add(currentnode);
        }
    }

}
