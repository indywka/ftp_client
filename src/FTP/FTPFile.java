package FTP;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class FTPFile extends controllerFTP {
    public String absPath;
    Date date;
    boolean exist = false;
    protected String type;
    private int returnCode;

    public FTPFile(FTPFile parent, String child) {

    }

    public FTPFile() {
    }

    public String[] list() {
        ArrayList<FTPFile> files = this.getFiles("/");
        List<String> list = new ArrayList<String>();
        for (FTPFile filek : files) {
            list.add(filek.getName());
        }
        return list.toArray(new String[list.size()]);
    }

    public String getName() {
        int indx = this.absPath.lastIndexOf("/");
        return indx == -1 ? this.absPath : this.absPath.substring(indx + 1, this.absPath.length());
    }

    public boolean isDirectory() {
        return this.type.equals("dir") || this.type.equals("cdir") || this.type.equals("pdir");
    }

    public boolean isFile() throws IOException {
        return false;
    }

    public String getPath() {
        int indx = this.absPath.lastIndexOf("/");
        return indx == -1 || indx == 0 ? "/" : this.absPath.substring(0, indx);
    }


    public String getAbsPath() {
        return this.absPath;
    }
}
