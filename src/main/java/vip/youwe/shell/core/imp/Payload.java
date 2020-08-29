package vip.youwe.shell.core.imp;

import vip.youwe.shell.core.shell.ShellEntity;
import vip.youwe.shell.utils.http.ReqParameter;

public interface Payload {

    void init(ShellEntity paramShellEntity);

    byte[] getPayload();

    String getFile(String paramString);

    String[] listFileRoot();

    byte[] downloadFile(String paramString);

    String getOsInfo();

    String getBasicsInfo();

    boolean include(String paramString, byte[] paramArrayOfByte);

    byte[] evalFunc(String paramString1, String paramString2, ReqParameter paramReqParameter);

    String execCommand(String paramString);

    boolean uploadFile(String paramString, byte[] paramArrayOfByte);

    boolean copyFile(String paramString1, String paramString2);

    boolean deleteFile(String paramString);

    boolean moveFile(String paramString1, String paramString2);

    boolean newFile(String paramString);

    boolean newDir(String paramString);

    boolean test();

    String execSql(String paramString1, String paramString2, int paramInt, String paramString3, String paramString4, String paramString5, String paramString6);

    String[] getAllDatabaseType();

    String currentDir();

    String currentUserName();
}
