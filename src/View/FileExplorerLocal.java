package View;

import java.io.File;

public class FileExplorerLocal extends FileExplorer {

    FileExplorerLocal() {
        createTree();
    }

    public void setPath(String path) {
    }


    @Override
    protected void createTree() {
        tree.setModel(null);
        File fileRoot = new File("C://Users//User//Desktop");
        FileTreeModel modelOfTree = new FileTreeModel(fileRoot);
        tree.setModel(modelOfTree);
        this.add(tree);
    }

    @Override
    protected void selected(String path) {
        this.path = path;
        if (path.equals("..")) {
            path = path.substring(0, path.lastIndexOf("/"));
            this.setPath(path);
        } else {
            File file = new File(path);
            if (file.isDirectory()) {
                this.setPath(path);
            } else this.notifySelectedFile(path);
        }
    }

    @Override
    protected void delete(String path) {
        File file = new File(path);
        String name = file.getName();
        if (!name.equals("..")) {
            //noinspection ResultOfMethodCallIgnored
            file.delete();
            setPath(this.path);
        }
    }


}
