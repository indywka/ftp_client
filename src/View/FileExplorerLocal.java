package View;

import java.io.File;
import java.util.Date;

public class FileExplorerLocal extends FileExplorer {

    public void setPath(String path) {
        this.path = path;
        File fileRoot = new File(this.path + "/");
        this.model.clear();

        if (!this.path.isEmpty()) this.model.addElement("..");
        //
        for (File file : fileRoot.listFiles()) {
            this.model.addElement(file.getName());
        }

        updateUI();
    }

    @Override
    protected void selected(int index) {
        String path = this.path;
        String name = this.model.get(index);

        if (name.equals("..")) {
            path = path.substring(0, path.lastIndexOf("/"));
            this.setPath(path);
        } else {
            path = path + "/" + this.model.get(index);
            File file = new File(path);

            if (file.isDirectory()) {
                this.setPath(path);
            } else this.notifySelectedFile(path);

        }

    }

    @Override
    protected void delete(int index) {
        String name = this.model.get(index);

        if (!name.equals("..")) {
            File file = new File(this.path + "/" + name);
            file.delete();
            setPath(this.path);
        }

    }

    @Override
    protected void info(int index) {
        File file = new File(this.path + "/" + this.model.get(index));
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
