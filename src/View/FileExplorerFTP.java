package View;

import FTP.FTPFile;
import FTP.controllerFTP;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
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
    public void setPath(String path) {
        this.path = path;
        buildTree("/", root);
        tree = new JTree(root);
        this.add(tree);
    }

    private List<String> getDir(String path) {
        this.path = path;
        ArrayList<FTPFile> files = this.pi.getFiles(path);
        List<String> list = new ArrayList<>();
        if (files.size() != 0) {
            for (FTPFile file : files) {
                list.add(file.getName());
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
        String path = this.path;
        FTPFile file = this.pi.getFile(path);

        if (file.isDirectory()) {
            setPath(file.getAbsPath());
        } else notifySelectedFile(path);
    }

    @Override
    protected void delete(String path) {
        if (!path.equals("..")) {
            this.pi.delete(this.pi.getFile(path));
            setPath(this.path);
        }

    }

    private void buildTree(String currentDir, DefaultMutableTreeNode model) {

        List<String> currentCraw = getDir(currentDir);

        for (String node : currentCraw) {
            DefaultMutableTreeNode currentNode = new DefaultMutableTreeNode(node);
            buildTree(currentDir + "/" + node, currentNode);
            model.add(currentNode);
        }


    }

}
