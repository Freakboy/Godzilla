package vip.youwe.shell.core.ui;

import vip.youwe.shell.core.ApplicationContext;
import vip.youwe.shell.core.Db;
import vip.youwe.shell.core.annotation.PluginnAnnotation;
import vip.youwe.shell.core.imp.Plugin;
import vip.youwe.shell.core.shell.ShellEntity;
import vip.youwe.shell.core.ui.component.*;
import vip.youwe.shell.utils.Log;

import javax.swing.*;
import java.util.HashMap;
import java.util.Map;

public class ShellManage extends JFrame {

    private JTabbedPane tabbedPane;
    private ShellEntity shellEntity;
    private ShellExecCommandPanel shellExecCommandPanel;
    private ShellBasicsInfo shellBasicsInfo;
    private ShellFileManager shellFileManager;
    private ShellDatabasePanel shellDatabasePanel;
    private Map<String, Plugin> pluginMap;
    private static final Map<String, String> CN_HASH_MAP = new HashMap();

    static {
        CN_HASH_MAP.put("MemoryShell", "内存SHELL");
        CN_HASH_MAP.put("JRealCmd", "虚拟终端");
        CN_HASH_MAP.put("CRealCmd", "虚拟终端");
        CN_HASH_MAP.put("Screen", "屏幕截图");
        CN_HASH_MAP.put("CShapDynamicPayload", "ShellCode加载");
        CN_HASH_MAP.put("PZipE", "Zip管理");
        CN_HASH_MAP.put("CZipE", "Zip管理");
        CN_HASH_MAP.put("JZipE", "Zip管理");
        CN_HASH_MAP.put("P_Eval_Code", "代码执行");
        CN_HASH_MAP.put("payload", "有效载荷");
        CN_HASH_MAP.put("secretKey", "密钥");
        CN_HASH_MAP.put("password", "密码");
        CN_HASH_MAP.put("cryption", "加密器");
        CN_HASH_MAP.put("PROXYHOST", "代理主机");
        CN_HASH_MAP.put("PROXYPORT", "代理端口");
        CN_HASH_MAP.put("CONNTIMEOUT", "连接超时");
        CN_HASH_MAP.put("READTIMEOUT", "读取超时");
        CN_HASH_MAP.put("PROXY", "代理类型");
        CN_HASH_MAP.put("REMARK", "备注");
        CN_HASH_MAP.put("ENCODING", "编码");
    }

    public ShellManage(String shellId) {
        this.pluginMap = new HashMap();
        this.shellEntity = Db.getOneShell(shellId);
        this.tabbedPane = new JTabbedPane();
        String titleString = String.format("Url:%s 有效载荷:%s 加密器:%s", this.shellEntity.getUrl(), this.shellEntity.getPayload(), this.shellEntity.getCryption());
        setTitle(titleString);
        boolean state = this.shellEntity.initShellOpertion();
        if (state) {
            init();
        } else {
            setTitle("初始化失败");
            JOptionPane.showMessageDialog(this, "初始化失败", "提示", 2);
            dispose();
        }
    }

    private void init() {
        this.shellEntity.setFrame(this);
        initComponent();
    }

    private void initComponent() {
        this.tabbedPane.addTab("基础信息", this.shellBasicsInfo = new ShellBasicsInfo(this.shellEntity));
        this.tabbedPane.addTab("命令执行", this.shellExecCommandPanel = new ShellExecCommandPanel(this.shellEntity));
        this.tabbedPane.addTab("文件管理", this.shellFileManager = new ShellFileManager(this.shellEntity));
        this.tabbedPane.addTab("数据库管理", this.shellDatabasePanel = new ShellDatabasePanel(this.shellEntity));
        this.tabbedPane.addTab("笔记", new ShellNote(this.shellEntity));
        loadPlugins();
        add(this.tabbedPane);
        setSize(1300, 600);
        setLocationRelativeTo(MainActivity.getFrame());
        setVisible(true);
        setDefaultCloseOperation(2);
    }

    public static String getCNName(String name) {
        for (String key : CN_HASH_MAP.keySet()) {
            if (key.toUpperCase().equals(name.toUpperCase())) {
                return CN_HASH_MAP.get(key);
            }
        }
        return name;
    }

    private String getPluginName(Plugin p) {
        PluginnAnnotation pluginAnnotation = p.getClass().getAnnotation(PluginnAnnotation.class);
        return pluginAnnotation.Name();
    }

    private void loadPlugins() {
        Plugin[] plugins = ApplicationContext.getAllPlugin(this.shellEntity.getPayload());

        for (int i = 0; i < plugins.length; i++) {
            try {
                Plugin plugin = plugins[i];
                plugin.init(this.shellEntity);
                this.tabbedPane.addTab(getCNName(getPluginName(plugin)), plugin.getView());
                this.pluginMap.put(getPluginName(plugin), plugin);
            } catch (Exception e) {
                Log.error(e);
            }
        }
    }

    public Plugin getPlugin(String pluginName) {
        return this.pluginMap.get(pluginName);
    }
}
