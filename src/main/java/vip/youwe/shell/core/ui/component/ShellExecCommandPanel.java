package vip.youwe.shell.core.ui.component;

import vip.youwe.shell.core.imp.Payload;
import vip.youwe.shell.core.shell.ShellEntity;
import vip.youwe.shell.core.ui.component.menu.ShellPopMenu;
import vip.youwe.shell.utils.Log;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Arrays;

public class ShellExecCommandPanel extends JPanel {

    private static final long serialVersionUID = 1L;
    private int command_start;
    private int command_stop;
    private JToolBar bar;
    private JLabel status;
    private JTextPane console;
    private JScrollPane console_scroll;
    private Document shell_doc;
    private ArrayList<String> last_commands;
    private int num;
    private Payload shell;
    private String currentDir;
    private String currentUser;
    private String fileRoot;
    private String osInfo;
    private ShellPopMenu shellPopMenu;

    public ShellExecCommandPanel(ShellEntity shellEntity) {
        this.last_commands = new ArrayList();
        this.num = 1;
        this.shell = shellEntity.getPayloadModel();
        this.bar = new JToolBar();
        this.status = new JLabel("完成");
        this.bar.setFloatable(false);
        this.console = new JTextPane();
        this.console_scroll = new JScrollPane(this.console);
        this.shell_doc = this.console.getDocument();
        this.shellPopMenu = new ShellPopMenu(this, this.console);
        this.currentDir = this.shell.currentDir();
        this.currentUser = this.shell.currentUserName();
        this.fileRoot = Arrays.toString(shellEntity.getPayloadModel().listFileRoot());
        this.osInfo = this.shell.getOsInfo();
        this.status.setText("正在连接...请稍等");
        Thread thread_getpath = new Thread(new Runnable() {
            public void run() {
                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        try {
                            ShellExecCommandPanel.this.shell_doc.insertString(ShellExecCommandPanel.this.shell_doc.getLength(), String.format("currentDir:%s\nfileRoot:%s\ncurrentUser:%s\nosInfo:%s\n", ShellExecCommandPanel.this.currentDir, ShellExecCommandPanel.this.fileRoot, ShellExecCommandPanel.this.currentUser, ShellExecCommandPanel.this.osInfo), null);
                            ShellExecCommandPanel.this.shell_doc.insertString(ShellExecCommandPanel.this.shell_doc.getLength(), "\n" + ShellExecCommandPanel.this.currentDir + " >", null);
                        } catch (BadLocationException e) {
                            Log.error(e);
                        }
                        ShellExecCommandPanel.this.command_start = ShellExecCommandPanel.this.shell_doc.getLength();
                        ShellExecCommandPanel.this.console.setCaretPosition(ShellExecCommandPanel.this.shell_doc.getLength());
                        ShellExecCommandPanel.this.status.setText("完成");
                    }
                });
            }
        });

        thread_getpath.start();
        setLayout(new GridBagLayout());
        GBC gbcinfo = (new GBC(0, 0, 6, 1)).setFill(2).setWeight(100.0D, 0.0D);
        GBC gbcconsole = (new GBC(0, 1, 6, 1)).setFill(1).setWeight(0.0D, 10.0D);
        GBC gbcbar = (new GBC(0, 2, 6, 1)).setFill(2).setWeight(100.0D, 0.0D);
        textareaFocus f_listener = new textareaFocus();
        addFocusListener(f_listener);
        textareaKey key_listener = new textareaKey();
        this.console.addKeyListener(key_listener);

        this.bar.add(this.status);
        add(this.bar, gbcinfo);
        add(this.console_scroll, gbcconsole);
        add(this.bar, gbcbar);

        this.console.setCaretPosition(this.shell_doc.getLength());
        Color bgColor = Color.BLACK;
        UIDefaults defaults = new UIDefaults();
        defaults.put("TextPane[Enabled].backgroundPainter", bgColor);
        this.console.putClientProperty("Nimbus.Overrides", defaults);
        this.console.putClientProperty("Nimbus.Overrides.InheritDefaults", true);
        this.console.setBackground(bgColor);
        this.console.setForeground(Color.green);
        this.console.setBackground(Color.black);
        this.console.setCaretColor(Color.white);
        this.command_start = this.shell_doc.getLength();
    }

    private class textareaFocus extends FocusAdapter {
        public void focusGained(FocusEvent e) {
            ShellExecCommandPanel.this.console.requestFocus();
            ShellExecCommandPanel.this.console.setCaretPosition(ShellExecCommandPanel.this.shell_doc.getLength());
        }

        private textareaFocus() {
        }
    }

    private class textareaKey extends KeyAdapter {
        private textareaKey() {
        }

        public void keyPressed(KeyEvent arg0) {
            if (ShellExecCommandPanel.this.shell_doc.getLength() <= ShellExecCommandPanel.this.command_start && !arg0.isControlDown() && arg0.getKeyCode() == 8) {
                try {
                    String t = ShellExecCommandPanel.this.shell_doc.getText(ShellExecCommandPanel.this.console.getCaretPosition() - 1, 1);
                    ShellExecCommandPanel.this.shell_doc.insertString(ShellExecCommandPanel.this.console.getCaretPosition(), t, null);
                } catch (Exception exception) {
                }
            }

            if ((ShellExecCommandPanel.this.console.getCaretPosition() < ShellExecCommandPanel.this.command_start || ShellExecCommandPanel.this.console.getSelectionStart() < ShellExecCommandPanel.this.command_start || ShellExecCommandPanel.this.console.getSelectionEnd() < ShellExecCommandPanel.this.command_start) && !arg0.isControlDown()) {
                ShellExecCommandPanel.this.console.setEditable(false);
                ShellExecCommandPanel.this.console.setCaretPosition(ShellExecCommandPanel.this.shell_doc.getLength());
            } else if (arg0.isControlDown() && ShellExecCommandPanel.this.console.getCaretPosition() < ShellExecCommandPanel.this.command_start) {
                ShellExecCommandPanel.this.console.setEditable(false);
            } else {
                ShellExecCommandPanel.this.console.setEditable(true);
            }

            if (arg0.getKeyCode() == 10) {
                ShellExecCommandPanel.this.console.setCaretPosition(ShellExecCommandPanel.this.shell_doc.getLength());
            }
        }


        public void keyReleased(KeyEvent arg0) {
            ShellExecCommandPanel.this.command_stop = ShellExecCommandPanel.this.shell_doc.getLength();
            if (arg0.getKeyCode() == 10) {
                String tmp_cmd = null;
                try {
                    tmp_cmd = ShellExecCommandPanel.this.shell_doc.getText(ShellExecCommandPanel.this.command_start, ShellExecCommandPanel.this.command_stop - ShellExecCommandPanel.this.command_start);
                    tmp_cmd = tmp_cmd.replace("\n", "").replace("\r", "");
                    if (tmp_cmd.equals("cls") || tmp_cmd.equals("clear")) {
                        ShellExecCommandPanel.this.shell_doc.remove(0, ShellExecCommandPanel.this.shell_doc.getLength());
                        ShellExecCommandPanel.this.shell_doc.insertString(0, "\n" + ShellExecCommandPanel.this.currentDir + " >", null);
                        ShellExecCommandPanel.this.command_start = ShellExecCommandPanel.this.shell_doc.getLength();
                    } else {
                        ShellExecCommandPanel.this.status.setText("正在执行...请稍等");
                        try {
                            ShellExecCommandPanel.this.execute(ShellExecCommandPanel.this.shell_doc.getText(ShellExecCommandPanel.this.command_start, ShellExecCommandPanel.this.command_stop - ShellExecCommandPanel.this.command_start));
                        } catch (Exception e) {
                            ShellExecCommandPanel.this.status.setText("执行失败");
                            ShellExecCommandPanel.this.console.setEditable(true);
                        }
                    }
                    if (tmp_cmd.trim().length() > 0) {
                        ShellExecCommandPanel.this.last_commands.add(tmp_cmd);
                    }
                    ShellExecCommandPanel.this.num = ShellExecCommandPanel.this.last_commands.size();
                } catch (BadLocationException e) {

                    e.printStackTrace();
                }
            }

            if (arg0.getKeyCode() == 38) {
                ShellExecCommandPanel.this.console.setCaretPosition(ShellExecCommandPanel.this.command_start);
                try {
                    ShellExecCommandPanel.this.shell_doc.remove(ShellExecCommandPanel.this.command_start, ShellExecCommandPanel.this.shell_doc.getLength() - ShellExecCommandPanel.this.command_start);
                    ShellExecCommandPanel.this.shell_doc.insertString(ShellExecCommandPanel.this.command_start, ShellExecCommandPanel.this.key_up_action(), null);
                } catch (BadLocationException e) {

                    e.printStackTrace();
                }
            }

            if (arg0.getKeyCode() == 40) {
                ShellExecCommandPanel.this.console.setCaretPosition(ShellExecCommandPanel.this.command_start);
                try {
                    ShellExecCommandPanel.this.shell_doc.remove(ShellExecCommandPanel.this.command_start, ShellExecCommandPanel.this.shell_doc.getLength() - ShellExecCommandPanel.this.command_start);
                    ShellExecCommandPanel.this.shell_doc.insertString(ShellExecCommandPanel.this.command_start, ShellExecCommandPanel.this.key_down_action(), null);
                } catch (BadLocationException e) {

                    e.printStackTrace();
                }
            }
        }
    }

    public void execute(String command) {
        String result = "\n";
        try {
            if (command.trim().length() > 0) {
                result = result + this.shell.execCommand(command);
            } else {
                result = result + "NULL";
            }

            this.shell_doc.insertString(this.shell_doc.getLength(), result, null);

            this.shell_doc.insertString(this.shell_doc.getLength(), "\n" + this.currentDir + " >", null);
            this.command_start = this.shell_doc.getLength();
            this.console.setCaretPosition(this.shell_doc.getLength());
            this.status.setText("完成");
        } catch (Exception e) {
            try {
                this.shell_doc.insertString(this.shell_doc.getLength(), "\nNull", null);
                this.shell_doc.insertString(this.shell_doc.getLength(), "\n" + this.currentDir + " >", null);
                this.command_start = this.shell_doc.getLength();
                this.console.setCaretPosition(this.shell_doc.getLength());
            } catch (Exception e2) {
                Log.error(e2);
            }
        }
    }


    public String key_up_action() {
        this.num--;
        String last_command = null;
        if (this.num >= 0 && !this.last_commands.isEmpty()) {
            last_command = this.last_commands.get(this.num);
            last_command = last_command.replace("\n", "").replace("\r", "");
            return last_command;
        }
        return "";
    }


    public String key_down_action() {
        this.num++;
        String last_command = null;
        if (this.num < this.last_commands.size() && this.num >= 0) {
            last_command = this.last_commands.get(this.num);
            last_command = last_command.replace("\n", "").replace("\r", "");
            return last_command;
        }
        if (this.num < 0) {
            this.num = 0;
            return "";
        }
        this.num = this.last_commands.size();
        return "";
    }


    public static String toHexString(String s) {
        String str = "";
        for (int i = 0; i < s.length(); i++) {
            int ch = s.charAt(i);
            String s4 = Integer.toHexString(ch);
            str = str + s4;
        }
        return str;
    }
}
