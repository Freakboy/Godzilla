package vip.youwe.shell.core.ui.component.dialog;

import javax.swing.*;
import java.awt.*;

public class ImageShowDialog extends JDialog {

    private JPanel panel;
    private JLabel imageLabel;

    private ImageShowDialog(Frame owner, ImageIcon imageIcon, String title, int width, int height) {
        super(owner, title, true);

        this.panel = new JPanel(new BorderLayout());
        this.imageLabel = new JLabel(imageIcon);


        this.panel.add(this.imageLabel);

        add(this.panel);

        setSize(width, height);
        setLocationRelativeTo(owner);
        setVisible(true);
    }


    public static void showImageDiaolog(Frame owner, ImageIcon imageIcon, String title, int width, int height) {
        width += 50;
        height += 50;
        if (title == null || title.trim().length() < 1) {
            title = String.format("image info Width:%s Height:%s", imageIcon.getIconWidth(), imageIcon.getIconHeight());
        }
        ImageShowDialog imageShowDialog = new ImageShowDialog(owner, imageIcon, title, width, height);
    }

    public static void showImageDiaolog(Frame owner, ImageIcon imageIcon, String title) {
        showImageDiaolog(owner, imageIcon, title, imageIcon.getIconWidth(), imageIcon.getIconHeight());
    }

    public static void showImageDiaolog(ImageIcon imageIcon, String title) {
        showImageDiaolog(null, imageIcon, title);
    }

    public static void showImageDiaolog(Frame owner, ImageIcon imageIcon) {
        showImageDiaolog(owner, imageIcon, null);
    }

    public static void showImageDiaolog(ImageIcon imageIcon) {
        showImageDiaolog(null, imageIcon);
    }
}
