package ui;

import javax.swing.*;
import java.awt.*;

public class SettingDialog extends JDialog {

    private static final int DIALOG_WIDTH = 720;
    private static final int DIALOG_HEIGHT = 500;

    public SettingDialog(JFrame owner, String title) {
        super(owner, title);
        setLayout(new FlowLayout());
        setSize(new Dimension(DIALOG_WIDTH, DIALOG_HEIGHT));
        setLocationRelativeTo(null);
        setupViews();
    }

    private void setupViews() {

    }
}
