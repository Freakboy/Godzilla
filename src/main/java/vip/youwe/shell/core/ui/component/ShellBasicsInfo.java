package vip.youwe.shell.core.ui.component;

import vip.youwe.shell.core.shell.ShellEntity;

import java.awt.BorderLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class ShellBasicsInfo extends JPanel {

    private ShellEntity shellEntity;
    private RTextArea basicsInfoTextArea;

    public ShellBasicsInfo(ShellEntity shellEntity) {
        this.shellEntity = shellEntity;
        setLayout(new BorderLayout(1, 1));
        this.basicsInfoTextArea = new RTextArea();
        this.basicsInfoTextArea.setEditable(false);
        add(new JScrollPane(this.basicsInfoTextArea));
        this.basicsInfoTextArea.setText(shellEntity.getPayloadModel().getBasicsInfo());
    }
}
