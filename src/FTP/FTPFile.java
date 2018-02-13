package FTP;

import java.util.Date;


public class FTPFile {
    String absPath;
    Date date;
    boolean exist = false;

    FTPFile() {
    }

    public String getName() {
        int indx = this.absPath.lastIndexOf("/");
        return indx == -1 ? this.absPath : this.absPath.substring(indx + 1, this.absPath.length());
    }

}
