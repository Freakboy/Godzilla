package vip.youwe.shell.core.ui.component.dialog;

import vip.youwe.shell.core.ui.component.GBC;
import vip.youwe.shell.core.ui.component.model.FileOpertionInfo;
import vip.youwe.shell.utils.automaticBindClick;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;

public class FileDialog extends JDialog {

    private JTextField srcFileTextField;
    private JTextField destFileTextField;
    private JLabel srcFileLabel;
    private JLabel destFileLabel;
    private JButton okButton;
    private JButton cancelButton;
    private FileOpertionInfo fileOpertionInfo;
    private JButton srcSelectdFileButton;
    private JButton destSelectdFileButton;
    private boolean state;
    Dimension TextFieldDim = new Dimension(500, 23);

    private FileDialog(Frame frame, String tipString, String srcFileString, String destString) {
        super(frame, tipString, true);

        this.fileOpertionInfo = new FileOpertionInfo();

        this.srcFileTextField = new JTextField("srcFileText", 30);
        this.destFileTextField = new JTextField("destText", 30);
        this.srcFileLabel = new JLabel("srcFile");
        this.destFileLabel = new JLabel("destFile");

        this.okButton = new JButton("ok");
        this.cancelButton = new JButton("cancel");
        this.srcSelectdFileButton = new JButton("select File");
        this.destSelectdFileButton = new JButton("select File");
        Dimension TextFieldDim = new Dimension(200, 23);
        GBC gbcLSrcFile = (new GBC(0, 0)).setInsets(5, -40, 0, 0);
        GBC gbcSrcFile = (new GBC(1, 0, 3, 1)).setInsets(5, 20, 0, 0);
        GBC gbcSrcSelectdFie = (new GBC(4, 0, 7, 1)).setInsets(5, 50, 0, 10);
        GBC gbcDestSelectdFie = (new GBC(4, 1, 7, 1)).setInsets(5, 50, 0, 10);
        GBC gbcLDestFile = (new GBC(0, 1)).setInsets(5, -40, 0, 0);
        GBC gbcDestFile = (new GBC(1, 1, 3, 1)).setInsets(5, 20, 0, 0);
        GBC gbcOkButton = (new GBC(0, 2, 2, 1)).setInsets(5, 20, 0, 0);
        GBC gbcCancelButton = (new GBC(2, 2, 1, 1)).setInsets(5, 20, 0, 0);
        this.srcFileTextField.setPreferredSize(TextFieldDim);
        this.destFileTextField.setPreferredSize(TextFieldDim);

        setLayout(new GridBagLayout());
        add(this.srcFileLabel, gbcLSrcFile);
        add(this.srcFileTextField, gbcSrcFile);
        add(this.srcSelectdFileButton, gbcSrcSelectdFie);
        add(this.destSelectdFileButton, gbcDestSelectdFie);
        add(this.destFileLabel, gbcLDestFile);
        add(this.destFileTextField, gbcDestFile);
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
                FileDialog.this.cancelButtonClick(null);
            }

            public void windowClosed(WindowEvent paramWindowEvent) {
            }

            public void windowActivated(WindowEvent paramWindowEvent) {
            }
        });
        this.srcFileTextField.setText(srcFileString);
        this.destFileTextField.setText(destString);
        setSize(650, 180);
        setLocationRelativeTo(frame);
        setDefaultCloseOperation(2);
        setVisible(true);
    }

    public FileOpertionInfo getResult() {
        return this.fileOpertionInfo;
    }

    private void okButtonClick(ActionEvent actionEvent) {
        this.fileOpertionInfo.setOpertionStatus(true);
        changeFileInfo();
    }

    private void cancelButtonClick(ActionEvent actionEvent) {
        this.fileOpertionInfo.setOpertionStatus(false);
        changeFileInfo();
    }

    private void changeFileInfo() {
        this.fileOpertionInfo.setSrcFileName(this.srcFileTextField.getText());
        this.fileOpertionInfo.setDestFileName(this.destFileTextField.getText());
        this.state = true;
        dispose();
    }

    private void srcSelectdFileButtonClick(ActionEvent actionEvent) {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileSelectionMode(0);
        chooser.showDialog(new JLabel(), "选择");
        File selectdFile = chooser.getSelectedFile();
        if (selectdFile != null) {
            String fileString = selectdFile.getAbsolutePath();
            this.srcFileTextField.setText(fileString);
        }
    }

    private void destSelectdFileButtonClick(ActionEvent actionEvent) {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileSelectionMode(0);
        chooser.showDialog(new JLabel(), "选择");
        File selectdFile = chooser.getSelectedFile();
        if (selectdFile != null) {
            String fileString = selectdFile.getAbsolutePath();
            this.destFileTextField.setText(fileString);
        }
    }

    public static FileOpertionInfo showFileOpertion(Frame frame, String title, String srcFileString, String destString) {
        return (new FileDialog(frame, title, srcFileString, destString)).getResult();
    }
}
