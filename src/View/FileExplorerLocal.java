package View;

import java.io.File;
import java.util.Date;

public class FileExplorerLocal extends FileExplorer {

    FileExplorerLocal() {
        createLocalTree();
    }

    public void setPath(String path) {
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


    @Override
    protected void info(String path) {
        this.path = path;
        File file = new File(path);
        ShowInfo inf = new ShowInfo();
        inf.setDir(file.getPath());
        inf.setName(file.getName());
        inf.setTypeFile(file.isDirectory() ? "dir" : "file");
        inf.setSize(file.length());
        inf.setDate(new Date(file.lastModified()));
        inf.setLocationRelativeTo(this);
        inf.setVisible(true);
    }


}
