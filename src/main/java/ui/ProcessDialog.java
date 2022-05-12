package ui;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;

public class ProcessDialog extends JDialog {

    private static final int DIALOG_WIDTH = 720;
    private static final int DIALOG_HEIGHT = 820;

    private final JPanel arrivalTimePanel;
    private final JPanel burstTimePanel;
    private final JPanel priorityPanel;
    private final JPanel colorPanel;
    private final JPanel colorChooserPanel;
    private final JPanel buttonPanel;
    private final JButton okButton = new JButton("OK");
    private final JButton cancelButton = new JButton("Cancel");

    public ProcessDialog(JFrame owner, String title) {
        super(owner, title);
        setLayout(new FlowLayout(FlowLayout.LEFT));
        setSize(new Dimension(DIALOG_WIDTH, DIALOG_HEIGHT));
        setLocationRelativeTo(null);
        arrivalTimePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        arrivalTimePanel.setPreferredSize(new Dimension(DIALOG_WIDTH, DIALOG_HEIGHT / 10));
        burstTimePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        burstTimePanel.setPreferredSize(new Dimension(DIALOG_WIDTH, DIALOG_HEIGHT / 10));
        priorityPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        priorityPanel.setPreferredSize(new Dimension(DIALOG_WIDTH, DIALOG_HEIGHT / 10));
        colorPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        colorPanel.setPreferredSize(new Dimension(DIALOG_WIDTH, DIALOG_HEIGHT / 10));
        colorChooserPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonPanel = new JPanel(new FlowLayout());
        setupViews();

        add(arrivalTimePanel);
        add(burstTimePanel);
        add(priorityPanel);
        add(colorPanel);
        add(colorChooserPanel);
        add(buttonPanel);
        bindViewModels();
    }

    private void setupViews() {
        JLabel arrivalTimeLabel = new JLabel("1. Arrival Time : ");
        SpinnerNumberModel arrivalTimeModel = new SpinnerNumberModel();
        JSpinner arrivalTimeSpinner = new JSpinner(arrivalTimeModel);
        arrivalTimePanel.add(arrivalTimeLabel);
        arrivalTimePanel.add(arrivalTimeSpinner);
        JLabel burstTimeLabel = new JLabel("2. Burst Time : ");
        SpinnerNumberModel burstTimeModel = new SpinnerNumberModel();
        JSpinner burstTimeSpinner = new JSpinner(burstTimeModel);
        burstTimePanel.add(burstTimeLabel);
        burstTimePanel.add(burstTimeSpinner);
        JLabel priorityLabel = new JLabel("3. Priority Time : ");
        SpinnerNumberModel priorityModel = new SpinnerNumberModel();
        JSpinner prioritySpinner = new JSpinner(priorityModel);
        priorityPanel.add(priorityLabel);
        priorityPanel.add(prioritySpinner);
        JLabel colorLabel = new JLabel("4. Color : ");
        JLabel colorResult = new JLabel("Choose the Color");
        colorResult.setPreferredSize(new Dimension(200, 25));
        colorResult.setBorder(new LineBorder(Color.CYAN, 1));
        colorPanel.add(colorLabel);
        colorPanel.add(colorResult);
        JColorChooser colorChooser = new JColorChooser();
        colorChooser.getSelectionModel().addChangeListener(e -> {
            Color color = colorChooser.getColor();
            int red = color.getRed();
            int green = color.getGreen();
            int blue = color.getBlue();
            colorResult.setText("r=" + red + ", g=" + green + ", b=" + blue);
        });
        colorChooser.setPreferredSize(new Dimension(DIALOG_WIDTH, DIALOG_HEIGHT / 10 * 4));
        colorChooserPanel.add(colorChooser);

        okButton.addActionListener(e -> {

        });
        cancelButton.addActionListener(e -> {

        });

        buttonPanel.add(okButton);
        buttonPanel.add(cancelButton);
    }

    private void bindViewModels() {

    }
}
