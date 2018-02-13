package View;

import FTP.FTPFile;
import FTP.controllerFTP;

import java.util.List;

public class FileExplorerFTP extends FileExplorer {
    private controllerFTP pi = null;

    FileExplorerFTP() {

    }

    @Override
    public void setPath(String path) {
        this.path = path;
        this.model.clear();
        if (this.pi == null) return;

        List<FTPFile> files = this.pi.getFiles(path);
        for (FTPFile file : files) {
            this.model.addElement(file.getName());
        }
        repaint();
        updateUI();
    }

    void setPiFTP(controllerFTP pi) {
        this.pi = pi;
        this.model.clear();
    }

    @Override
    protected void selected(int index) {
    }

    @Override
    protected void delete(int index) {
    }

    @Override
    protected void info(int index) {
    }
}
