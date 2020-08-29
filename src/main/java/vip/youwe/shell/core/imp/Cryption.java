package vip.youwe.shell.core.imp;

import vip.youwe.shell.core.shell.ShellEntity;

public interface Cryption {

    void init(ShellEntity paramShellEntity);

    byte[] encode(byte[] paramArrayOfByte);

    byte[] decode(byte[] paramArrayOfByte);

    boolean isSendRLData();

    byte[] generate(String paramString1, String paramString2);

    boolean check();
}
