package ui;

import di.Injection;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;

import static extensions.Message.showErrorMessage;

public class ProcessDialog extends JDialog {

    public static final int UNKNOWN_PROCESS = -1;

    private static final int DIALOG_WIDTH = 720;
    private static final int DIALOG_HEIGHT = 820;

    private final MainViewModel viewModel = Injection.provideMainViewModel();

    private final JPanel arrivalTimePanel;
    private final JPanel burstTimePanel;
    private final JPanel priorityPanel;
    private final JPanel colorPanel;
    private final JPanel colorChooserPanel;
    private final JPanel buttonPanel;
    private final JButton okButton = new JButton("OK");
    private final JButton cancelButton = new JButton("Cancel");
    private final Type type;
    private final int processId;
    private JSpinner arrivalTimeSpinner;
    private JSpinner burstTimeSpinner;
    private JSpinner prioritySpinner;
    private JLabel colorResult;

    public ProcessDialog(JFrame owner, String title, Type type, int processId) {
        super(owner, title);
        this.type = type;
        this.processId = processId;
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
        if (type == Type.EDIT && processId != UNKNOWN_PROCESS) {
            renderProcess(viewModel.findByProcessId(processId));
        }
        bindViewModels();
    }

    private void setupViews() {
        JLabel arrivalTimeLabel = new JLabel("1. Arrival Time : ");
        SpinnerNumberModel arrivalTimeModel = new SpinnerNumberModel();
        arrivalTimeSpinner = new JSpinner(arrivalTimeModel);
        arrivalTimePanel.add(arrivalTimeLabel);
        arrivalTimePanel.add(arrivalTimeSpinner);
        JLabel burstTimeLabel = new JLabel("2. Burst Time : ");
        SpinnerNumberModel burstTimeModel = new SpinnerNumberModel();
        burstTimeSpinner = new JSpinner(burstTimeModel);
        burstTimePanel.add(burstTimeLabel);
        burstTimePanel.add(burstTimeSpinner);
        JLabel priorityLabel = new JLabel("3. Priority Time : ");
        SpinnerNumberModel priorityModel = new SpinnerNumberModel();
        prioritySpinner = new JSpinner(priorityModel);
        priorityPanel.add(priorityLabel);
        priorityPanel.add(prioritySpinner);
        JLabel colorLabel = new JLabel("4. Color : ");
        colorResult = new JLabel("Choose the Color");
        colorResult.setPreferredSize(new Dimension(200, 25));
        colorResult.setBorder(new LineBorder(Color.CYAN, 1));
        colorPanel.add(colorLabel);
        colorPanel.add(colorResult);
        JColorChooser colorChooser = new JColorChooser();
        colorChooser.getSelectionModel().addChangeListener(e -> {
            colorResult.setText(convertColorToString(colorChooser.getColor()));
        });
        colorChooser.setPreferredSize(new Dimension(DIALOG_WIDTH, DIALOG_HEIGHT / 10 * 4));
        colorChooserPanel.add(colorChooser);

        okButton.addActionListener(e -> {
            Color selectedColor = colorChooser.getColor();
            if (selectedColor == null) {
                showErrorMessage("Color를 선택해주세요.");
                return;
            }
            int arrivalTime = (Integer) arrivalTimeSpinner.getModel().getValue();
            int burstTime = (Integer) burstTimeSpinner.getModel().getValue();
            int priority = (Integer) prioritySpinner.getModel().getValue();
            if (burstTime <= 0) {
                showErrorMessage("burstTime은 1 이상이어야 합니다.");
                return;
            }
            if (type == Type.ADD) {
                viewModel.addProcess(arrivalTime, burstTime, priority, selectedColor);
            } else if (type == Type.EDIT && processId != UNKNOWN_PROCESS) {
                viewModel.editByProcessId(processId, arrivalTime, burstTime, priority, selectedColor);
            } else {
                showErrorMessage("에러입니다. 이 창을 닫습니다.");
            }
            setVisible(false);
        });
        cancelButton.addActionListener(e -> {
            setVisible(false);
        });

        buttonPanel.add(okButton);
        buttonPanel.add(cancelButton);
    }

    private void bindViewModels() {

    }

    private void renderProcess(data.Process process) {
        SpinnerNumberModel arrivalTimeModel = (SpinnerNumberModel) arrivalTimeSpinner.getModel();
        arrivalTimeModel.setValue(process.getArrivalTime());
        SpinnerNumberModel burstTimeModel = (SpinnerNumberModel) burstTimeSpinner.getModel();
        burstTimeModel.setValue(process.getArrivalTime());
        SpinnerNumberModel priorityModel = (SpinnerNumberModel) prioritySpinner.getModel();
        priorityModel.setValue(process.getArrivalTime());
        colorResult.setText(convertColorToString(process.getColor()));
    }

    private String convertColorToString(Color color) {
        int red = color.getRed();
        int green = color.getGreen();
        int blue = color.getBlue();
        return "r=" + red + ", g=" + green + ", b=" + blue;
    }

    public enum Type {
        ADD, EDIT
    }
}
