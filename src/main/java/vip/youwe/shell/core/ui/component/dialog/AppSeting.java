package vip.youwe.shell.core.ui.component.dialog;

import vip.youwe.shell.core.ApplicationContext;
import vip.youwe.shell.core.Db;
import vip.youwe.shell.core.ui.MainActivity;
import vip.youwe.shell.core.ui.component.GBC;
import vip.youwe.shell.core.ui.component.RTextArea;
import vip.youwe.shell.utils.Log;
import vip.youwe.shell.utils.automaticBindClick;
import vip.youwe.shell.utils.functions;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Field;
import java.util.ArrayList;

public class AppSeting extends JDialog {

    private JTabbedPane tabbedPane;
    private JPanel globallHttpHeaderPanel;
    private JPanel setFontPanel;
    private JPanel coreConfigPanel;
    private JComboBox<String> fontNameComboBox;
    private JComboBox<String> fontTypeComboBox;
    private JComboBox<String> fontSizeComboBox;
    private JLabel testFontLabel;
    private JLabel currentFontLabel;
    private JButton updateFontButton;
    private JButton resetFontButton;
    private JLabel fontNameLabel;
    private JLabel fontTypeLabel;
    private JLabel fontSizeLabel;
    private JLabel currentFontLLabel;
    private JLabel isTipJLabel;
    private JCheckBox isTipCheckBox;
    private RTextArea headerTextArea;
    private JButton updateHeaderButton;
    private JLabel godModeLabel;
    private JCheckBox godModeCheckBox;

    public AppSeting() {
        super(MainActivity.getFrame(), "AppSeting", true);

        this.tabbedPane = new JTabbedPane();
        initSetFontPanel();
        initGloballHttpHeader();
        initCoreConfigPanel();

        this.tabbedPane.addTab("全局协议头", this.globallHttpHeaderPanel);
        this.tabbedPane.addTab("字体设置", this.setFontPanel);
        this.tabbedPane.addTab("核心配置", this.coreConfigPanel);

        add(this.tabbedPane);
        automaticBindClick.bindJButtonClick(this, this);
        setSize(650, 500);
        setLocationRelativeTo(MainActivity.getFrame());
        setVisible(true);
    }

    void initSetFontPanel() {
        Font currentFont = ApplicationContext.getFont();

        this.setFontPanel = new JPanel(new GridBagLayout());
        this.fontNameComboBox = new JComboBox(getAllFontName());
        this.fontTypeComboBox = new JComboBox(getAllFontType());
        this.fontSizeComboBox = new JComboBox(getAllFontSize());
        this.testFontLabel = new JLabel("你好\tHello");
        this.currentFontLabel = new JLabel(functions.toString(currentFont));

        this.currentFontLLabel = new JLabel("当前字体 : ");
        this.updateFontButton = new JButton("修改");
        this.resetFontButton = new JButton("重置");
        this.fontNameLabel = new JLabel("字体:    ");
        this.fontTypeLabel = new JLabel("字体类型 : ");
        this.fontSizeLabel = new JLabel("字体大小 : ");

        GBC gbcLFontName = (new GBC(0, 0)).setInsets(5, -40, 0, 0);
        GBC gbcFontName = (new GBC(1, 0, 3, 1)).setInsets(5, 20, 0, 0);
        GBC gbcLFontType = (new GBC(0, 1)).setInsets(5, -40, 0, 0);
        GBC gbcFontType = (new GBC(1, 1, 3, 1)).setInsets(5, 20, 0, 0);
        GBC gbcLFontSize = (new GBC(0, 2)).setInsets(5, -40, 0, 0);
        GBC gbcFontSize = (new GBC(1, 2, 3, 1)).setInsets(5, 20, 0, 0);
        GBC gbcLCurrentFont = (new GBC(0, 3)).setInsets(5, -40, 0, 0);
        GBC gbcCurrentFont = (new GBC(1, 3, 3, 1)).setInsets(5, 20, 0, 0);
        GBC gbcTestFont = new GBC(0, 4);
        GBC gbcUpdateFont = (new GBC(2, 5)).setInsets(5, -40, 0, 0);
        GBC gbcResetFont = (new GBC(1, 5, 3, 1)).setInsets(5, 20, 0, 0);

        this.setFontPanel.add(this.fontNameLabel, gbcLFontName);
        this.setFontPanel.add(this.fontNameComboBox, gbcFontName);
        this.setFontPanel.add(this.fontTypeLabel, gbcLFontType);
        this.setFontPanel.add(this.fontTypeComboBox, gbcFontType);
        this.setFontPanel.add(this.fontSizeLabel, gbcLFontSize);
        this.setFontPanel.add(this.fontSizeComboBox, gbcFontSize);
        this.setFontPanel.add(this.currentFontLLabel, gbcLCurrentFont);
        this.setFontPanel.add(this.currentFontLabel, gbcCurrentFont);
        this.setFontPanel.add(this.testFontLabel, gbcTestFont);
        this.setFontPanel.add(this.updateFontButton, gbcUpdateFont);
        this.setFontPanel.add(this.resetFontButton, gbcResetFont);

        this.fontNameComboBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent paramActionEvent) {
                AppSeting.this.testFontLabel.setFont(AppSeting.this.getSelectFont());
            }
        });
        this.fontTypeComboBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent paramActionEvent) {
                AppSeting.this.testFontLabel.setFont(AppSeting.this.getSelectFont());
            }
        });

        this.fontSizeComboBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent paramActionEvent) {
                AppSeting.this.testFontLabel.setFont(AppSeting.this.getSelectFont());
            }
        });
        if (currentFont != null) {
            this.fontNameComboBox.setSelectedItem(currentFont.getName());
            this.fontTypeComboBox.setSelectedItem(this.getFontType(currentFont.getStyle()));
            this.fontSizeComboBox.setSelectedItem(Integer.toString(currentFont.getSize()));
            this.testFontLabel.setFont(currentFont);
        }
    }

    void initGloballHttpHeader() {
        this.globallHttpHeaderPanel = new JPanel(new BorderLayout(1, 1));
        this.headerTextArea = new RTextArea();
        this.updateHeaderButton = new JButton("修改");
        this.headerTextArea.setText(ApplicationContext.getGloballHttpHeader());
        Dimension dimension = new Dimension();
        dimension.height = 30;
        JSplitPane splitPane = new JSplitPane();
        splitPane.setOrientation(0);
        JPanel bottomPanel = new JPanel();
        splitPane.setTopComponent(new JScrollPane(this.headerTextArea));
        bottomPanel.add(this.updateHeaderButton);
        bottomPanel.setMaximumSize(dimension);
        bottomPanel.setMinimumSize(dimension);
        splitPane.setBottomComponent(bottomPanel);
        splitPane.setResizeWeight(0.9D);
        this.globallHttpHeaderPanel.add(splitPane);
    }


    void initCoreConfigPanel() {
        this.coreConfigPanel = new JPanel(new GridBagLayout());
        this.godModeLabel = new JLabel("运行模式: ");
        this.godModeCheckBox = new JCheckBox("上帝模式", ApplicationContext.isGodMode());
        this.isTipJLabel = new JLabel("提示语");
        this.isTipCheckBox = new JCheckBox("开启", functions.toBoolean(Db.getSetingValue("AppIsTip")));

        GBC gbcLGodMode = (new GBC(0, 0)).setInsets(5, -40, 0, 0);
        GBC gbcGodMode = (new GBC(1, 0, 3, 1)).setInsets(5, 20, 0, 0);
        GBC gbcLIsTip = (new GBC(0, 1)).setInsets(5, -40, 0, 0);
        GBC gbcIsTip = (new GBC(1, 1, 3, 1)).setInsets(5, 20, 0, 0);

        this.coreConfigPanel.add(this.godModeLabel, gbcLGodMode);
        this.coreConfigPanel.add(this.godModeCheckBox, gbcGodMode);
        this.coreConfigPanel.add(this.isTipJLabel, gbcLIsTip);
        this.coreConfigPanel.add(this.isTipCheckBox, gbcIsTip);
        this.isTipCheckBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (Db.updateSetingKV("AppIsTip", Boolean.toString(AppSeting.this.isTipCheckBox.isSelected()))) {
                    JOptionPane.showMessageDialog(null, "修改成功!", "提示", 1);
                } else {
                    JOptionPane.showMessageDialog(null, "修改失败!", "提示", 2);
                }
            }
        });
        this.godModeCheckBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (ApplicationContext.setGodMode(AppSeting.this.godModeCheckBox.isSelected())) {
                    JOptionPane.showMessageDialog(null, "修改成功!", "提示", 1);
                } else {
                    JOptionPane.showMessageDialog(null, "修改失败!", "提示", 2);
                }
            }
        });
    }

    public Font getSelectFont() {
        try {
            String fontName = (String) this.fontNameComboBox.getSelectedItem();
            String fontType = (String) this.fontTypeComboBox.getSelectedItem();
            int fontSize = Integer.parseInt((String) this.fontSizeComboBox.getSelectedItem());
            Font font = new Font(fontName, Font.class.getDeclaredField(fontType).getInt(null), fontSize);
            return font;
        } catch (Exception e) {
            Log.error(e);
            return null;
        }
    }

    public String getFontType(int type) {
        try {
            Field[] fields = Font.class.getDeclaredFields();
            for (int i = 0; i < fields.length; i++) {
                Field field = fields[i];
                // if (field.getType().isAssignableFrom(int.class)
                if (field.getType().isAssignableFrom(Integer.TYPE)
                        && field.getName().indexOf("_") == -1
                        && field.getModifiers() == 25
                        && field.getInt(null) == type) {
                    return field.getName();
                }
            }

        } catch (Exception e) {
            Log.error(e);
            return null;
        }
        return null;
    }

    public static String[] getAllFontName() {
        ArrayList<String> arrayList = new ArrayList();
        GraphicsEnvironment e = GraphicsEnvironment.getLocalGraphicsEnvironment();
        Font[] fonts = e.getAllFonts();
        for (int i = 0; i < fonts.length; i++) {
            Font font = fonts[i];
            arrayList.add(font.getFontName());
        }

        return arrayList.toArray(new String[0]);
    }

    public static String[] getAllFontType() {
        ArrayList<String> arrayList = new ArrayList();
        Field[] fields = Font.class.getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            if (field.getType().isAssignableFrom(Integer.TYPE)
                    && field.getName().indexOf("_") == -1
                    && field.getModifiers() == 25) {
                arrayList.add(field.getName());
            }
        }

        return arrayList.toArray(new String[0]);
    }

    public static String[] getAllFontSize() {
        ArrayList<String> arrayList = new ArrayList();
        for (int i = 8; i < 48; i++) {
            arrayList.add(Integer.toString(i));
        }
        return arrayList.toArray(new String[0]);
    }

    private void updateFontButtonClick(ActionEvent actionEvent) {
        ApplicationContext.setFont(getSelectFont());
        JOptionPane.showMessageDialog(this, "修改成功! 重启程序生效!", "提示", 1);
    }

    private void resetFontButtonClick(ActionEvent actionEvent) {
        ApplicationContext.resetFont();
        JOptionPane.showMessageDialog(this, "重置成功! 重启程序生效!", "提示", 1);
    }

    private void updateHeaderButtonClick(ActionEvent actionEvent) {
        String header = this.headerTextArea.getText();
        if (ApplicationContext.updateGloballHttpHeader(header)) {
            JOptionPane.showMessageDialog(this, "修改成功!", "提示", 1);
        } else {
            JOptionPane.showMessageDialog(this, "修改失败!", "提示", 2);
        }
    }
}
