package vip.youwe.shell.core.imp;

import vip.youwe.shell.core.shell.ShellEntity;

import javax.swing.*;

public interface Plugin {

    void init(ShellEntity paramShellEntity);

    JPanel getView();
}
