package vip.youwe.shell.core.ui.component;

import vip.youwe.shell.core.Encoding;
import vip.youwe.shell.core.imp.Payload;
import vip.youwe.shell.core.shell.ShellEntity;
import vip.youwe.shell.core.ui.component.dialog.DatabaseSetting;
import vip.youwe.shell.core.ui.component.model.DbInfo;
import vip.youwe.shell.core.ui.imp.ActionDblClick;
import vip.youwe.shell.core.ui.model.DatabaseSql;
import vip.youwe.shell.utils.Log;
import vip.youwe.shell.utils.automaticBindClick;
import vip.youwe.shell.utils.functions;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.Arrays;
import java.util.Vector;

public class ShellDatabasePanel extends JPanel {

    private static final String[] EXEC_TYPES = {"select", "update"};
    private static final String[] SQL_EXAMPLE = {"SELECT 1;", "SELECT COUNT(1) FROM tableName", "SELECT VERSION();"};

    private ShellEntity shellEntity;

    private JSplitPane splitPane;
    private JButton execButton;
    private JButton dbsetButton;
    private DataTree dblist;
    private DataView dataView;
    private RTextArea sqlCommand;
    private JScrollPane dblistpane;
    private JScrollPane datalistpane;
    private JScrollPane commandpane;
    private JComboBox<String> execTypeComboBox;
    private JComboBox<String> commonsql;
    private JLabel statusLabel;
    private JLabel execTypeLabel;
    private JLabel sql_listLabel;
    private DefaultMutableTreeNode databaseTreeNode;
    private Payload payload;
    private DbInfo dbInfo;
    private Encoding encoding;
    private JPopupMenu dataViewPopupMenu;
    private JPopupMenu dblistPopupMenu;

    public ShellDatabasePanel(ShellEntity shellEntity) {
        this.shellEntity = shellEntity;
        this.payload = this.shellEntity.getPayloadModel();
        this.encoding = Encoding.getEncoding(shellEntity);
        this.dbInfo = new DbInfo();
        this.splitPane = new JSplitPane();
        this.databaseTreeNode = new DefaultMutableTreeNode("Database");
        this.splitPane.setOrientation(0);

        this.statusLabel = new JLabel("state");
        this.execTypeLabel = new JLabel("Exec Type");
        this.sql_listLabel = new JLabel("SQL Statement");

        this.dblist = new DataTree("", this.databaseTreeNode);
        this.dblistpane = new JScrollPane(this.dblist);
        this.dblistpane.setPreferredSize(new Dimension(25, 0));
        this.dblist.setShowsRootHandles(true);
        this.dblist.setRootVisible(false);
        this.execTypeComboBox = new JComboBox(EXEC_TYPES);

        this.dataView = new DataView(null, null, -1, -1);
        this.datalistpane = new JScrollPane(this.dataView);
        this.dataView.setAutoCreateRowSorter(true);
        this.dataView.setAutoResizeMode(0);
        this.datalistpane.setPreferredSize(new Dimension(0, 0));

        this.sqlCommand = new RTextArea();
        this.commandpane = new JScrollPane(this.sqlCommand);
        this.sqlCommand.setText("");

        this.commonsql = new JComboBox(SQL_EXAMPLE);
        this.commonsql.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ShellDatabasePanel.this.sqlCommand.setText((String) ShellDatabasePanel.this.commonsql.getSelectedItem());
            }
        });

        this.dbsetButton = new JButton("DbInfoConfig");

        this.dataViewPopupMenu = new JPopupMenu();
        JMenuItem copyselectItem = new JMenuItem("复制选中");
        copyselectItem.setActionCommand("copySelected");
        JMenuItem copyselectedLineItem = new JMenuItem("复制选中行");
        copyselectedLineItem.setActionCommand("copyselectedLine");
        JMenuItem exportAllItem = new JMenuItem("导出");
        exportAllItem.setActionCommand("exportData");
        this.dataViewPopupMenu.add(copyselectItem);
        this.dataViewPopupMenu.add(copyselectedLineItem);
        this.dataViewPopupMenu.add(exportAllItem);
        this.dataView.setRightClickMenu(this.dataViewPopupMenu);
        automaticBindClick.bindMenuItemClick(this.dataViewPopupMenu, null, this);

        this.dblistPopupMenu = new JPopupMenu();
        JMenuItem countTableItem = new JMenuItem("Count");
        countTableItem.setActionCommand("countTable");
        this.dblistPopupMenu.add(countTableItem);
        automaticBindClick.bindMenuItemClick(this.dblistPopupMenu, null, this);
        this.dblist.setChildPopupMenu(this.dblistPopupMenu);

        this.execButton = new JButton("Exec SQL");

        setLayout(new GridBagLayout());
        GBC gbcleft = (new GBC(0, 0, 2, 4)).setFill(3).setWeight(0.0D, 1.0D).setIpad(200, 0);
        GBC gbcright1 = (new GBC(2, 0, 5, 1)).setFill(1).setWeight(1.0D, 0.7D).setInsets(0, 5, 0, 0);
        GBC gbcright2_1 = (new GBC(2, 1, 1, 1)).setFill(0).setInsets(0, 5, 0, 0);
        GBC gbcright2_2 = (new GBC(3, 1, 1, 1)).setFill(2).setWeight(1.0D, 0.0D);
        GBC gbcright2_3 = (new GBC(4, 1, 1, 1)).setFill(0);
        GBC gbcright2_4 = (new GBC(5, 1, 1, 1)).setFill(2).setWeight(1.0D, 0.0D);
        GBC gbcright2_5 = (new GBC(6, 1, 1, 1)).setFill(0);
        GBC gbcright3 = (new GBC(2, 2, 8, 1)).setFill(1).setWeight(1.0D, 0.3D).setInsets(0, 5, 0, 0);
        GBC gbcright4_1 = (new GBC(2, 3, 5, 1)).setFill(2).setWeight(1.0D, 0.0D).setInsets(0, 5, 0, 0);
        GBC gbcstatus = (new GBC(0, 4, 9, 1)).setFill(2).setWeight(1.0D, 0.0D);

        add(this.dblistpane, gbcleft);
        add(this.datalistpane, gbcright1);
        add(this.execTypeLabel, gbcright2_1);
        add(this.execTypeComboBox, gbcright2_2);
        add(this.sql_listLabel, gbcright2_3);
        add(this.commonsql, gbcright2_4);
        add(this.commandpane, gbcright3);
        add(this.dbsetButton, gbcright2_5);
        add(this.execButton, gbcright4_1);
        add(this.statusLabel, gbcstatus);
        automaticBindClick.bindJButtonClick(this, this);

        this.dblist.setActionDbclick(new ActionDblClick() {
            public void dblClick(MouseEvent e) {
                ShellDatabasePanel.this.fileDataTreeDbClick(e);
            }
        });
    }

    private void fileDataTreeDbClick(MouseEvent e) {
        String[] s = this.dblist.GetSelectFile().split("/");
        if (s.length == 1) {
            fillDbListByTable(s[0]);
        } else if (s.length == 2) {
            fillDataviewByDT(s[0], s[1]);
        }
    }

    private void dbsetButtonClick(ActionEvent actionEvent) {
        DatabaseSetting action = new DatabaseSetting(this.shellEntity, this.dbInfo);
        (new Thread(new Runnable() {
            public void run() {
                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        ShellDatabasePanel.this.fillDbListByDatabase();
                    }
                });
            }
        })).start();
    }

    private void fillDbListByDatabase() {
        this.dblist.removeAll();
        String sqlString = DatabaseSql.sqlMap.get(String.format("%s-getAllDatabase", this.dbInfo.getDbType().toLowerCase()));
        if (sqlString != null) {
            this.sqlCommand.setText(sqlString);
            String result = this.payload.execSql(this.dbInfo.getDbType(), this.dbInfo.getDbHost(), this.dbInfo.getDbPort(), this.dbInfo.getDbUserName(), this.dbInfo.getDbPassword(), "select", sqlString);
            if (showData(result) && this.dataView.getModel().getColumnCount() == 1) {

                Vector rows = this.dataView.getModel().getDataVector();
                for (int i = 0; i < rows.size(); i++) {
                    Vector row = (Vector) rows.get(i);
                    this.dblist.AddNote(row.get(0).toString());
                }
            }
        } else {
            Log.error(String.format("Fill Database Fail! NO SQL %s", this.dbInfo.getDbType()));
        }
    }

    private void fillDbListByTable(String databaseName) {
        String sqlString = DatabaseSql.sqlMap.get(String.format("%s-getTableByDatabase", this.dbInfo.getDbType().toLowerCase()));
        if (sqlString != null) {
            sqlString = String.format(sqlString, databaseName);
            this.sqlCommand.setText(sqlString);
            String result = this.payload.execSql(this.dbInfo.getDbType(), this.dbInfo.getDbHost(), this.dbInfo.getDbPort(), this.dbInfo.getDbUserName(), this.dbInfo.getDbPassword(), "select", sqlString);
            if (this.showData(result) && this.dataView.getModel().getColumnCount() == 1) {
                Vector rows = this.dataView.getModel().getDataVector();

                for (int i = 0; i < rows.size(); i++) {
                    Vector row = (Vector) rows.get(i);
                    this.dblist.AddNote(String.format("%s/%s", databaseName, row.get(0)));
                }
            }
        } else {
            Log.error(String.format("Fill Table Fail! NO SQL %s", this.dbInfo.getDbType()));
        }
    }

    private void fillDataviewByDT(String databaseName, String tableName) {
        String sqlString = DatabaseSql.sqlMap.get(String.format("%s-getTableDataByDT", this.dbInfo.getDbType().toLowerCase()));
        if (sqlString != null) {
            sqlString = String.format(sqlString, databaseName, tableName);
            this.sqlCommand.setText(sqlString);
            String result = this.payload.execSql(this.dbInfo.getDbType(), this.dbInfo.getDbHost(), this.dbInfo.getDbPort(), this.dbInfo.getDbUserName(), this.dbInfo.getDbPassword(), "select", sqlString);
            this.showData(result);
        } else {
            Log.error(String.format("Fill TableData Fail! NO SQL %s", this.dbInfo.getDbType()));
        }
    }

    private void execButtonClick(ActionEvent actionEvent) {
        String execSql = this.sqlCommand.getText();
        String execType = (String) this.execTypeComboBox.getSelectedItem();
        if (execSql != null && execSql.trim().length() > 0) {
            String result = this.payload.execSql(this.dbInfo.getDbType(), this.dbInfo.getDbHost(), this.dbInfo.getDbPort(), this.dbInfo.getDbUserName(), this.dbInfo.getDbPassword(), execType, execSql);
            showData(result);
        } else {
            JOptionPane.showMessageDialog(this, "SQL语句是空的", "提示", 2);
        }
    }

    public boolean showData(String data) {
        boolean state = false;
        if (data != null) {
            String[] datas = data.split("\n");
            Vector<String> columns = new Vector<>();
            Vector<Vector<String>> rowsVector = new Vector<>();
            if (datas[0].equals("ok")) {
                if (datas.length > 1) {
                    formatSqlResult(datas[1], columns);
                    DefaultTableModel tableModel = this.dataView.getModel();
                    tableModel.setColumnIdentifiers(columns);
                    for (int i = 2; i < datas.length; i++) {
                        Vector<String> row = new Vector<>();
                        formatSqlResult(datas[i], row);
                        rowsVector.add(row);
                    }
                    showData(rowsVector);
                    state = true;
                } else {
                    Vector<String> row = new Vector<String>();
                    row.add("");
                    rowsVector.add(row);
                    this.dataView.getModel().setColumnIdentifiers(row);
                    showData(rowsVector);
                }
            } else {
                JOptionPane.showMessageDialog(this, data, "提示", 2);
                Log.error(data);
            }
        } else {
            Log.error("exec SQL Result Is Null");
        }
        return state;
    }

    public void showData(Vector<Vector<String>> rowsVector) {
        this.dataView.AddRows(rowsVector);
        this.dataView.getModel().fireTableDataChanged();
    }

    public void formatSqlResult(String row, Vector<String> destVector) {
        String[] line = row.split("\t");
        for (int i = 0; i < line.length; i++) {
            destVector.add(this.encoding.Decoding(functions.base64Decode(line[i])));
        }
    }

    private void copySelectedMenuItemClick(ActionEvent e) {
        int columnIndex = this.dataView.getSelectedColumn();
        if (columnIndex != -1) {
            Object o = this.dataView.getValueAt(this.dataView.getSelectedRow(), this.dataView.getSelectedColumn());
            if (o != null) {
                String value = (String) o;
                Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(value), null);
                JOptionPane.showMessageDialog(this, "复制成功", "提示", 1);
            } else {
                JOptionPane.showMessageDialog(this, "选中列是空的", "提示", 2);
            }
        } else {
            JOptionPane.showMessageDialog(this, "未选中列", "提示", 2);
        }
    }

    private void copyselectedLineMenuItemClick(ActionEvent e) {
        int columnIndex = this.dataView.getSelectedColumn();
        if (columnIndex != -1) {
            String[] o = this.dataView.GetSelectRow1();
            if (o != null) {
                String value = Arrays.toString(o);
                this.dataView.GetSelectRow1();
                Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(value), null);
                JOptionPane.showMessageDialog(this, "复制成功", "提示", 1);
            } else {
                JOptionPane.showMessageDialog(this, "选中列是空的", "提示", 2);
            }
        } else {
            JOptionPane.showMessageDialog(this, "未选中列", "提示", 2);
        }
    }

    private void countTableMenuItemClick(ActionEvent e) {
        String[] s = this.dblist.GetSelectFile().split("/");
        if (s.length == 2) {
            String sqlString = DatabaseSql.sqlMap.get(String.format("%s-getCountByDT", this.dbInfo.getDbType().toLowerCase()));
            if (sqlString != null) {
                sqlString = String.format(sqlString, s[0], s[1]);
                this.sqlCommand.setText(sqlString);
                String result = this.payload.execSql(this.dbInfo.getDbType(), this.dbInfo.getDbHost(), this.dbInfo.getDbPort(), this.dbInfo.getDbUserName(), this.dbInfo.getDbPassword(), "select", sqlString);
                showData(result);
            } else {
                Log.error(String.format("Fill TableData Fail! NO SQL %s", this.dbInfo.getDbType()));
            }
        }
    }

    private void exportDataMenuItemClick(ActionEvent e) {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileSelectionMode(0);
        chooser.setFileFilter(new FileNameExtensionFilter("*.csv", "csv"));
        chooser.showDialog(new JLabel(), "选择");
        File selectdFile = chooser.getSelectedFile();
        if (selectdFile != null) {
            String fileString = selectdFile.getAbsolutePath();
            if (!fileString.endsWith(".csv")) {
                fileString = fileString + ".csv";
            }
            if (functions.saveDataViewToCsv(this.dataView.getColumnVector(), this.dataView.getModel().getDataVector(), fileString)) {
                JOptionPane.showMessageDialog(this, "导出成功", "提示", 1);
            } else {
                JOptionPane.showMessageDialog(this, "导出失败", "提示", 1);
            }
        } else {
            Log.log("用户取消选择......");
        }
    }
}
