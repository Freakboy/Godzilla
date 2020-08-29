package vip.youwe.shell.core.ui.component.dialog;

import vip.youwe.shell.core.ApplicationContext;
import vip.youwe.shell.core.Db;
import vip.youwe.shell.core.ui.MainActivity;
import vip.youwe.shell.core.ui.component.DataView;
import vip.youwe.shell.utils.Log;
import vip.youwe.shell.utils.automaticBindClick;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.File;
import java.util.Vector;

public class PluginManage extends JDialog {

    private DataView pluginView;
    private JButton addPluginButton;
    private JButton removeButton;
    private JButton cancelButton;
    private JButton refreshButton;
    private Vector<String> columnVector;
    private JSplitPane splitPane;

    public PluginManage() {
        super(MainActivity.getFrame(), "PluginManage", true);

        this.addPluginButton = new JButton("添加");
        this.removeButton = new JButton("移除");
        this.refreshButton = new JButton("取消");
        this.cancelButton = new JButton("刷新");
        this.splitPane = new JSplitPane();

        this.columnVector = new Vector();
        this.columnVector.add("pluginJarFile");
        this.pluginView = new DataView(null, this.columnVector, -1, -1);
        refreshPluginView();
        JPanel bottomPanel = new JPanel();
        bottomPanel.add(this.addPluginButton);
        bottomPanel.add(this.removeButton);
        bottomPanel.add(this.refreshButton);
        bottomPanel.add(this.cancelButton);
        this.splitPane.setOrientation(0);
        this.splitPane.setTopComponent(new JScrollPane(this.pluginView));
        this.splitPane.setBottomComponent(bottomPanel);

        this.splitPane.addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent e) {
                PluginManage.this.splitPane.setDividerLocation(0.85D);
            }
        });
        automaticBindClick.bindJButtonClick(this, this);
        add(this.splitPane);
        setSize(420, 420);
        setLocationRelativeTo(MainActivity.getFrame());
        setDefaultCloseOperation(2);
        setVisible(true);
    }

    private void refreshPluginView() {
        String[] pluginStrings = Db.getAllPlugin();
        Vector<Vector<String>> rows = new Vector<>();

        for (int i = 0; i < pluginStrings.length; i++) {
            String string = pluginStrings[i];
            Vector<String> rowVector = new Vector<>();
            rowVector.add(string);
            rows.add(rowVector);
        }
        this.pluginView.AddRows(rows);
        this.pluginView.getModel().fireTableDataChanged();
    }

    private void addPluginButtonClick(ActionEvent actionEvent) {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileFilter(new FileNameExtensionFilter("*.jar", "jar"));
        chooser.setFileSelectionMode(0);
        chooser.showDialog(new JLabel(), "选择");
        File selectdFile = chooser.getSelectedFile();
        if (selectdFile != null) {
            if (Db.addPlugin(selectdFile.getAbsolutePath()) == 1) {
                ApplicationContext.init();
                JOptionPane.showMessageDialog(this, "添加插件成功", "提示", 1);
            } else {
                JOptionPane.showMessageDialog(this, "添加插件失败", "提示", 2);
            }
        } else {
            Log.log("用户取消选择.....");
        }
        refreshPluginView();
    }

    private void removeButtonClick(ActionEvent actionEvent) {
        int rowIndex = this.pluginView.getSelectedRow();
        if (rowIndex != -1) {
            Object selectedItem = this.pluginView.getValueAt(rowIndex, 0);
            if (Db.removePlugin((String) selectedItem) == 1) {
                JOptionPane.showMessageDialog(this, "移除插件成功", "提示", 1);
            } else {
                JOptionPane.showMessageDialog(this, "移除插件失败", "提示", 2);
            }
        } else {
            JOptionPane.showMessageDialog(this, "没有选中插件", "提示", 2);
        }
        refreshPluginView();
    }

    private void cancelButtonClick(ActionEvent actionEvent) {
        dispose();
    }

    private void refreshButtonClick(ActionEvent actionEvent) {
        refreshPluginView();
    }
}
