package vip.youwe.shell.core.ui.component.dialog;

import vip.youwe.shell.core.shell.ShellEntity;
import vip.youwe.shell.core.ui.component.GBC;
import vip.youwe.shell.core.ui.component.model.DbInfo;
import vip.youwe.shell.utils.Log;
import vip.youwe.shell.utils.automaticBindClick;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class DatabaseSetting extends JDialog {

    private JComboBox<String> dbTypeComboBox;
    private JTextField dbHostTextField;
    private JTextField dbPortTextField;
    private JTextField dbUserNameTextField;
    private JTextField dbPasswordTextField;
    private JLabel dbTypeLabel;
    private JLabel dbHostLabel;
    private JLabel dbPortLabel;
    private JLabel dbUserNameLabel;
    private JLabel dbPasswordLabel;
    private JButton updateButton;
    private Dimension TextFieldDim = new Dimension(200, 23);
    private Dimension labelDim = new Dimension(150, 23);
    private DbInfo dbInfo;

    public DatabaseSetting(ShellEntity shellEntity, DbInfo dbInfo) {
        super(shellEntity.getFrame(), "DbInfo Setting", true);
        String[] databaseTypeArray = shellEntity.getPayloadModel().getAllDatabaseType();
        Container c = getContentPane();
        setLayout(new GridBagLayout());
        this.dbInfo = dbInfo;
        GBC gbcLDbType = (new GBC(0, 0)).setInsets(5, -40, 0, 0);
        GBC gbcDbType = (new GBC(1, 0, 3, 1)).setInsets(5, 20, 0, 0);
        GBC gbcLDbHost = (new GBC(0, 1)).setInsets(5, -40, 0, 0);
        GBC gbcDbHost = (new GBC(1, 1, 3, 1)).setInsets(5, 20, 0, 0);
        GBC gbcLDbPort = (new GBC(0, 2)).setInsets(5, -40, 0, 0);
        GBC gbcDbPort = (new GBC(1, 2, 3, 1)).setInsets(5, 20, 0, 0);
        GBC gbcLDbUserName = (new GBC(0, 3)).setInsets(5, -40, 0, 0);
        GBC gbcDbUserName = (new GBC(1, 3, 3, 1)).setInsets(5, 20, 0, 0);
        GBC gbcLDbPassword = (new GBC(0, 4)).setInsets(5, -40, 0, 0);
        GBC gbcDbPassword = (new GBC(1, 4, 3, 1)).setInsets(5, 20, 0, 0);
        GBC gbcUpdate = (new GBC(1, 5, 4, 1)).setInsets(5, 20, 0, 0);
        this.updateButton = new JButton("Update Db Info");
        this.dbTypeLabel = new JLabel("数据库类型");
        this.dbHostLabel = new JLabel("数据库主机");
        this.dbPortLabel = new JLabel("数据库端口");
        this.dbUserNameLabel = new JLabel("数据库用户名");
        this.dbPasswordLabel = new JLabel("数据库密码");
        this.dbTypeLabel.setPreferredSize(this.labelDim);
        this.dbHostLabel.setPreferredSize(this.labelDim);
        this.dbPortLabel.setPreferredSize(this.labelDim);
        this.dbUserNameLabel.setPreferredSize(this.labelDim);
        this.dbPasswordLabel.setPreferredSize(this.labelDim);

        this.dbHostTextField = new JTextField(dbInfo.getDbHost());
        this.dbPortTextField = new JTextField(Integer.toString(dbInfo.getDbPort()));
        this.dbUserNameTextField = new JTextField(dbInfo.getDbUserName());
        this.dbPasswordTextField = new JTextField(dbInfo.getDbPassword());
        this.dbTypeComboBox = new JComboBox(databaseTypeArray);
        this.dbTypeComboBox.setEditable(false);
        this.dbTypeComboBox.setSelectedItem(dbInfo.getDbType());
        this.dbTypeComboBox.setPreferredSize(this.TextFieldDim);

        this.dbHostTextField.setPreferredSize(this.TextFieldDim);
        this.dbPortTextField.setPreferredSize(this.TextFieldDim);
        this.dbUserNameTextField.setPreferredSize(this.TextFieldDim);
        this.dbPasswordTextField.setPreferredSize(this.TextFieldDim);
        this.updateButton.setPreferredSize(this.TextFieldDim);

        c.add(this.dbTypeLabel, gbcLDbType);
        c.add(this.dbTypeComboBox, gbcDbType);
        c.add(this.dbHostLabel, gbcLDbHost);
        c.add(this.dbHostTextField, gbcDbHost);
        c.add(this.dbPortLabel, gbcLDbPort);
        c.add(this.dbPortTextField, gbcDbPort);
        c.add(this.dbUserNameLabel, gbcLDbUserName);
        c.add(this.dbUserNameTextField, gbcDbUserName);
        c.add(this.dbPasswordLabel, gbcLDbPassword);
        c.add(this.dbPasswordTextField, gbcDbPassword);
        c.add(this.updateButton, gbcUpdate);

        automaticBindClick.bindJButtonClick(this, this);
        setSize(460, 270);
        setLocationRelativeTo(shellEntity.getFrame());
        setDefaultCloseOperation(2);
        setVisible(true);
    }

    private void updateButtonClick(ActionEvent actionEvent) {
        try {
            String dbType = (String) this.dbTypeComboBox.getSelectedItem();
            if (dbType.trim().length() > 0) {
                this.dbInfo.setDbHost(this.dbHostTextField.getText());
                this.dbInfo.setDbPort(Integer.parseInt(this.dbPortTextField.getText()));
                this.dbInfo.setDbUserName(this.dbUserNameTextField.getText());
                this.dbInfo.setDbPassword(this.dbPasswordTextField.getText());
                this.dbInfo.setDbType(dbType);
                JOptionPane.showMessageDialog(this, "success", "提示", 1);
            } else {
                JOptionPane.showMessageDialog(this, "no selected DbType", "提示", 2);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "提示", 2);
            Log.error(e);
        }
        dispose();
    }
}
