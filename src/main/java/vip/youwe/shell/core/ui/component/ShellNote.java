package vip.youwe.shell.core.ui.component;

import vip.youwe.shell.core.Db;
import vip.youwe.shell.core.shell.ShellEntity;
import vip.youwe.shell.utils.Log;
import vip.youwe.shell.utils.functions;

import javax.swing.*;
import java.awt.*;

public class ShellNote extends JPanel {

    private ShellEntity shellEntity;
    private String noteData;
    private String shellId;
    private String lastNoteMd5;
    private RTextArea textArea;
    private boolean state;

    public ShellNote(ShellEntity entity) {
        this.shellEntity = entity;
        this.shellId = this.shellEntity.getId();
        super.setLayout(new BorderLayout(1, 1));
        String noteData = Db.getShellNote(this.shellId);
        this.lastNoteMd5 = functions.md5(noteData);
        this.textArea = new RTextArea();
        this.textArea.setText(noteData);
        this.state = true;
        Thread thread = new Thread(new Runnable() {
            public void run() {
                while (ShellNote.this.state) {
                    try {
                        Thread.sleep(10000L);
                        ShellNote.this.updateDbNote();
                    } catch (InterruptedException e) {
                        Log.error(e);
                    }
                }
            }
        });
        thread.start();
        super.add(new JScrollPane(this.textArea));
    }

    public void updateDbNote() {
        String noteData = this.textArea.getText();
        String md5 = functions.md5(noteData);
        if (!this.lastNoteMd5.equals(md5)) {
            Db.updateShellNote(this.shellId, noteData);
        }
    }
}
