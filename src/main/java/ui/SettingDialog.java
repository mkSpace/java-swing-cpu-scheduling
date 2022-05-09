package ui;

import javax.swing.*;
import java.awt.*;

public class SettingDialog extends JDialog {

    private static final int DIALOG_WIDTH = 720;
    private static final int DIALOG_HEIGHT = 500;

    private final JButton addButton = new JButton("Add");
    private final JButton editButton = new JButton("Edit");
    private final JButton deleteButton = new JButton("Delete");

    public SettingDialog(JFrame owner, String title) {
        super(owner, title);
        setLayout(new GridLayout(3, 1));
        setSize(new Dimension(DIALOG_WIDTH, DIALOG_HEIGHT));
        setLocationRelativeTo(null);
        setupViews();
    }

    private void setupViews() {
        JTabbedPane tabbedPane = new JTabbedPane();
        JPanel manualPanel = new JPanel(new FlowLayout());
        manualPanel.setBorder(BorderFactory.createTitledBorder("manual"));
        manualPanel.setSize(DIALOG_WIDTH,  (int) Math.round(DIALOG_HEIGHT * 0.8));
        tabbedPane.addTab("Manual", null, manualPanel, "");
        tabbedPane.setSize(DIALOG_WIDTH,  (int) Math.round(DIALOG_HEIGHT * 0.8));
        JLabel subtitleLabel = new JLabel("List of Processes");
        manualPanel.add(subtitleLabel);
        manualPanel.add(addButton);
        manualPanel.add(editButton);
        manualPanel.add(deleteButton);
        add(tabbedPane);
    }
}
