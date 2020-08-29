package vip.youwe.shell.core.ui.component.model;

public class DbInfo {

    private String dbType = new String();
    private String dbHost = new String();
    private int dbPort = 0;
    private String dbUserName = new String();
    private String dbPassword = new String();


    public String getDbType() {
        return this.dbType;
    }


    public String getDbHost() {
        return this.dbHost;
    }


    public int getDbPort() {
        return this.dbPort;
    }


    public String getDbUserName() {
        return this.dbUserName;
    }


    public String getDbPassword() {
        return this.dbPassword;
    }


    public void setDbType(String dbType) {
        this.dbType = dbType;
    }


    public void setDbHost(String dbHost) {
        this.dbHost = dbHost;
    }


    public void setDbPort(int dbPort) {
        this.dbPort = dbPort;
    }


    public void setDbUserName(String dbUserName) {
        this.dbUserName = dbUserName;
    }


    public void setDbPassword(String dbPassword) {
        this.dbPassword = dbPassword;
    }


    public String toString() {
        return "DbInfo [dbType=" + this.dbType + ", dbHost=" + this.dbHost + ", dbPort=" + this.dbPort + ", dbUserName=" + this.dbUserName +
                ", dbPassword=" + this.dbPassword + "]";
    }
}
