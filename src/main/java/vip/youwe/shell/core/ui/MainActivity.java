package vip.youwe.shell.core.ui;

import vip.youwe.shell.core.ApplicationConfig;
import vip.youwe.shell.core.ApplicationContext;
import vip.youwe.shell.core.Db;
import vip.youwe.shell.core.ui.component.DataView;
import vip.youwe.shell.core.ui.component.dialog.AppSeting;
import vip.youwe.shell.core.ui.component.dialog.GenerateShellLoder;
import vip.youwe.shell.core.ui.component.dialog.PluginManage;
import vip.youwe.shell.core.ui.component.dialog.ShellSetting;
import vip.youwe.shell.utils.automaticBindClick;
import vip.youwe.shell.utils.functions;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.Vector;

public class MainActivity {

    private static JFrame mainActivityFrame;
    private JFrame frame;
    private JMenuBar menuBar;
    private JMenu targetMenu;
    private JMenu aboutMenu;
    private JMenu attackMenu;
    private JMenu configMenu;
    private DataView shellView;
    private JScrollPane shellViewScrollPane;
    private JPopupMenu shellViewPopupMenu;
    private Vector columnVector;

    public MainActivity() {
        ApplicationContext.init();

        if (functions.toBoolean(Db.getSetingValue("AppIsTip"))) {
            ApplicationConfig.invoke();
            JOptionPane.showMessageDialog(getFrame(),
                    "1.程序仅限服务器管理，切勿用于非法用途，非法使用造成的一切后果由自己承担，与本作者无关\n2.由于用户滥用造成的一切后果与作者无关。\n3.用户请自觉遵守当地法律法规，出现一切后果本项目作者概不负责\n4.本软件不得用于商业用途，仅做学习交流",
                    "如果您使用本软件默认同意以下协议条款.如果您不同意本协议的条款 ,否则请立即关闭并删除本软件.", 2);
        }
        initVariable();
    }

    private void initVariable() {
        this.frame = new JFrame();
        this.frame.setTitle(String.format("webshell tool\t V%s", "1.00"));
        this.frame.setLayout(new BorderLayout(1, 1));

        Vector<Vector<String>> rows = Db.getAllShell();
        this.columnVector = rows.get(0);
        rows.remove(0);
        this.shellView = new DataView(null, this.columnVector, -1, -1);
        this.shellView.AddRows(rows);

        this.frame.add(this.shellViewScrollPane = new JScrollPane(this.shellView));

        this.menuBar = new JMenuBar();
        this.targetMenu = new JMenu("目标");
        JMenuItem addShellMenuItem = new JMenuItem("添加");
        addShellMenuItem.setActionCommand("addShell");
        this.targetMenu.add(addShellMenuItem);
        this.attackMenu = new JMenu("管理");
        JMenuItem generateShellMenuItem = new JMenuItem("生成");
        generateShellMenuItem.setActionCommand("generateShell");
        this.attackMenu.add(generateShellMenuItem);
        this.configMenu = new JMenu("配置");
        JMenuItem pluginConfigMenuItem = new JMenuItem("插件配置");
        pluginConfigMenuItem.setActionCommand("pluginConfig");
        JMenuItem appConfigMenuItem = new JMenuItem("程序配置");
        appConfigMenuItem.setActionCommand("appConfig");
        this.configMenu.add(appConfigMenuItem);
        this.configMenu.add(pluginConfigMenuItem);
        this.aboutMenu = new JMenu("关于");
        JMenuItem aboutMenuItem = new JMenuItem("关于");
        aboutMenuItem.setActionCommand("about");
        this.aboutMenu.add(aboutMenuItem);

        automaticBindClick.bindMenuItemClick(this.targetMenu, null, this);
        automaticBindClick.bindMenuItemClick(this.attackMenu, null, this);
        automaticBindClick.bindMenuItemClick(this.configMenu, null, this);
        automaticBindClick.bindMenuItemClick(this.aboutMenu, null, this);

        this.menuBar.add(this.targetMenu);
        this.menuBar.add(this.attackMenu);
        this.menuBar.add(this.configMenu);
        this.menuBar.add(this.aboutMenu);
        this.frame.setJMenuBar(this.menuBar);

        this.shellViewPopupMenu = new JPopupMenu();
        JMenuItem copyselectItem = new JMenuItem("复制选中");
        copyselectItem.setActionCommand("copyShellViewSelected");
        JMenuItem interactMenuItem = new JMenuItem("进入");
        interactMenuItem.setActionCommand("interact");
        JMenuItem removeShell = new JMenuItem("移除");
        removeShell.setActionCommand("removeShell");
        JMenuItem editShell = new JMenuItem("编辑");
        editShell.setActionCommand("editShell");
        JMenuItem refreshShell = new JMenuItem("刷新");
        refreshShell.setActionCommand("refreshShellView");
        this.shellViewPopupMenu.add(interactMenuItem);
        this.shellViewPopupMenu.add(copyselectItem);
        this.shellViewPopupMenu.add(removeShell);
        this.shellViewPopupMenu.add(editShell);
        this.shellViewPopupMenu.add(refreshShell);
        this.shellView.setRightClickMenu(this.shellViewPopupMenu);
        automaticBindClick.bindMenuItemClick(this.shellViewPopupMenu, null, this);

        addEasterEgg();

        this.frame.setSize(1500, 600);
        this.frame.setLocationRelativeTo(null);

        this.frame.setVisible(true);

        this.frame.setDefaultCloseOperation(3);
    }


    private void addEasterEgg() {
        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(new KeyEventDispatcher() {
            public boolean dispatchKeyEvent(KeyEvent e) {
                if (e.getKeyCode() == 112 && ApplicationContext.easterEgg) {
                    ApplicationContext.easterEgg = false;
                    JOptionPane.showMessageDialog(MainActivity.getFrame(), "Hacker技术学的再好, 却无法入侵你的心,\n服务器入侵的再多,对你只有Guest,\n是我的DDOS造成了你的拒绝服务？\n还是我的WebShell再次被你查杀？\n你总有防火墙\n我始终停不掉\n想提权\n无奈JSP+MYSQL成为我们的障碍\n找不到你的注入点\n扫不出你的空口令\n所有对我的回应都用3DES加密\n你总是自定义文件格式\n我永远找不到你的入口点\n忽略所有异常\n却还是跟踪不到你的注册码\n是你太过完美,还是我太菜?\n虽然我们是不同的对象,都有隐私的一面,\n但我相信总有一天我会找到你的接口,把我的最真给你看!\n因为我是你的指针,在茫茫内存的堆栈中, 永远指向你那片天空,不孜不倦!\n我愿做你的内联,供你无限次的调用,直到海枯石烂!\n我愿做你的引用,和你同进退共生死,一起经受考验!\n只是我不愿苦苦地调试你的心情,最终沦为你的友元!\n如今我们已被MFC封装--事事变迁!\n如今我们已向COM走去--可想当年!\n没任何奢求,只愿做你最后的System!\n渗透玩的再强,我也不能提权进你的心\n免杀玩的再狠,我也过不了你的主防御\n外挂写的再叼,我也不能操控你对我的爱\n编程玩的再好,我也不能写出完美的爱情\n纵使我多么的不可一世,也不是你的System\n提权了再多的服务器，却永远成不了你的Root\n**But...... **\n那怕你的心再强大，我有0day在手\n主动防御再牛，我有R0\n击败你只是时间问题, 就算能操控，你的心早已经不属于我\n已被千人DownLoad\n完美的爱情写出来能怎样，终究会像游戏一样结束\n不是你的System也罢，但我有Guest用户，早晚提权进入你的管理员组\n\n也许，像你说的那样，我们是不同世界的人，因为我是乞丐而不是骑士\n人变了，是因为心跟着生活在变\n人生有梦，各自精彩\n燕雀安知鸿鹄之志!",
                            "提示", -1);
                    return true;
                }
                return false;
            }
        });
    }

    private void addShellMenuItemClick(ActionEvent e) {
        ShellSetting manage = new ShellSetting(null);
        refreshShellView();
    }

    private void generateShellMenuItemClick(ActionEvent e) {
        GenerateShellLoder generateShellLoder = new GenerateShellLoder();
    }

    private void pluginConfigMenuItemClick(ActionEvent e) {
        PluginManage pluginManage = new PluginManage();
    }

    private void appConfigMenuItemClick(ActionEvent e) {
        AppSeting appSeting = new AppSeting();
    }

    private void aboutMenuItemClick(ActionEvent e) {
        JOptionPane.showMessageDialog(getFrame(), "由BeichenDream强力驱动\n邮箱:beichendream@gmail.com\n我是学习者:76866561@qq.com", "About", -1);
        // functions.openBrowseUrl("https://github.com/BeichenDream/Godzilla");
    }

    private void copyShellViewSelectedMenuItemClick(ActionEvent e) {
        int columnIndex = this.shellView.getSelectedColumn();
        if (columnIndex != -1) {
            Object o = this.shellView.getValueAt(this.shellView.getSelectedRow(), this.shellView.getSelectedColumn());
            if (o != null) {
                String value = (String) o;
                Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(value), null);
                JOptionPane.showMessageDialog(this.shellView, "复制成功", "提示", 1);
            } else {
                JOptionPane.showMessageDialog(this.shellView, "选中列是空的", "提示", 2);
            }
        } else {
            JOptionPane.showMessageDialog(this.shellView, "未选中列", "提示", 2);
        }
    }

    private void removeShellMenuItemClick(ActionEvent e) {
        Object o = this.shellView.getValueAt(this.shellView.getSelectedRow(), 0);
        if (o != null && o.getClass().isAssignableFrom(String.class)) {
            String shellId = (String) o;
            if (Db.removeShell(shellId) > 0) {
                JOptionPane.showMessageDialog(this.shellView, "删除成功", "提示", 1);
                refreshShellView();
            } else {
                JOptionPane.showMessageDialog(this.shellView, "删除失败", "提示", 2);
            }
        }
    }

    private void editShellMenuItemClick(ActionEvent e) {
        Object o = this.shellView.getValueAt(this.shellView.getSelectedRow(), 0);
        if (o != null && o.getClass().isAssignableFrom(String.class)) {
            ShellSetting manage = new ShellSetting((String) o);
            refreshShellView();
        }
    }

    private void interactMenuItemClick(ActionEvent e) {
        String shellId = (String) this.shellView.getValueAt(this.shellView.getSelectedRow(), 0);
        ShellManage shellManage = new ShellManage(shellId);
    }

    private void refreshShellView() {
        Vector<Vector<String>> rowsVector = Db.getAllShell();
        rowsVector.remove(0);
        this.shellView.AddRows(rowsVector);
        this.shellView.getModel().fireTableDataChanged();
    }


    private void refreshShellViewMenuItemClick(ActionEvent e) {
        refreshShellView();
    }


    public JFrame getJFrame() {
        return this.frame;
    }


    public static JFrame getFrame() {
        return mainActivityFrame;
    }

    public static void main(String[] args) {
        MainActivity activity = new MainActivity();
        mainActivityFrame = activity.getJFrame();
    }
}
