package FTP;

import java.util.Date;

public class FTPFile extends controllerFTP {
    public String type;
    protected String perm;
    protected long size;
    String absPath = "";
    Date date;
    boolean exist = false;


    public FTPFile() {
    }

    public String getName() {
        int indx = this.absPath.lastIndexOf("/");
        return indx == -1 ? this.absPath : this.absPath.substring(indx + 1, this.absPath.length());
    }

    public boolean isDirectory() {
        return this.type.equals("dir") || this.type.equals("cdir") || this.type.equals("pdir");
    }

    public boolean isFile() {
        return this.type.equals("file");
    }

    public String getPath() {
        int indx = this.absPath.lastIndexOf("/");
        return indx == -1 || indx == 0 ? "/" : this.absPath.substring(0, indx);
    }

    public long size() {
        return this.size;
    }

    public String getPerm() {
        return this.perm;
    }

    public String getType() {
        return this.type;
    }

    public String getAbsPath() {

        return this.absPath;
    }
}
