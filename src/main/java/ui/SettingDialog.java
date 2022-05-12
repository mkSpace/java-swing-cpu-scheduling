package ui;

import di.Injection;
import extensions.JTableUtils;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

import static java.awt.FlowLayout.CENTER;
import static ui.MainView.PROCESS_COLUMN_NAME;
import static ui.ProcessDialog.UNKNOWN_PROCESS;

public class SettingDialog extends JDialog {

    private static final int DIALOG_WIDTH = 720;
    private static final int DIALOG_HEIGHT = 500;

    private final MainViewModel viewModel = Injection.provideMainViewModel();

    private final CompositeDisposable disposables = new CompositeDisposable();

    private final JButton addButton = new JButton("Add");
    private final JButton editButton = new JButton("Edit");
    private final JButton deleteButton = new JButton("Delete");
    private final JButton okButton = new JButton("OK");

    private ProcessTableModel processTableModel;
    private JTable processTable;
    private JTextField quantumField;

    public SettingDialog(JFrame owner, String title) {
        super(owner, title);
        setLayout(new FlowLayout(CENTER, 0, 0));
        setSize(new Dimension(DIALOG_WIDTH, DIALOG_HEIGHT));
        setLocationRelativeTo(null);
        setupViews();
        bindViewModels();
    }

    private void setupViews() {
        JTabbedPane tabbedPane = new JTabbedPane();
        JPanel manualPanel = new JPanel(new FlowLayout());
        manualPanel.setBorder(BorderFactory.createTitledBorder("List of Processes"));
        manualPanel.setPreferredSize(new Dimension(DIALOG_WIDTH - 24, (int) Math.round(DIALOG_HEIGHT * 0.8)));
        tabbedPane.addTab("Manual", null, manualPanel, "");
        tabbedPane.setPreferredSize(new Dimension(DIALOG_WIDTH - 24, (int) Math.round(DIALOG_HEIGHT * 0.8)));
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setPreferredSize(new Dimension(DIALOG_WIDTH - 24, 40));
        JLabel quantumLabel = new JLabel("Quantum: ");
        quantumField = new JTextField();
        quantumField.setPreferredSize(new Dimension(100, 20));
        processTable = new JTable(processTableModel);
        DefaultTableCellRenderer renderer = (DefaultTableCellRenderer) processTable.getTableHeader().getDefaultRenderer();
        renderer.setHorizontalAlignment(SwingConstants.CENTER);
        processTable.getTableHeader().setDefaultRenderer(renderer);
        JScrollPane scrollPane = new JScrollPane(processTable);
        processTable.setFillsViewportHeight(true);
        processTable.setPreferredSize(new Dimension(DIALOG_WIDTH - 24, (int) Math.round(DIALOG_HEIGHT * 0.6)));
        scrollPane.setPreferredSize(new Dimension(DIALOG_WIDTH - 24, (int) Math.round(DIALOG_HEIGHT * 0.6)));

        addButton.addActionListener(e -> {
            new ProcessDialog((JFrame) getOwner(), "Add Process", ProcessDialog.Type.ADD, UNKNOWN_PROCESS).setVisible(true);
        });
        editButton.addActionListener(e -> {
            String processId = (String) JTableUtils.getSelectedFirstColumnOfRow(processTable);
            if (processId == null) {
                return;
            }
            new ProcessDialog((JFrame) getOwner(), "Edit Process", ProcessDialog.Type.EDIT, Integer.parseInt(processId)).setVisible(true);
        });
        deleteButton.addActionListener(e -> {
            String processId = (String) JTableUtils.getSelectedFirstColumnOfRow(processTable);
            if (processId == null) {
                return;
            }
            viewModel.deleteByProcessId(Integer.parseInt(processId));
        });
        okButton.addActionListener(e -> {
            viewModel.setQuantum(Integer.parseInt(quantumField.getText()));
            setVisible(false);
        });

        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(quantumLabel);
        buttonPanel.add(quantumField);
        manualPanel.add(buttonPanel);
        manualPanel.add(scrollPane);
        add(tabbedPane);
        add(okButton);
    }

    private void bindViewModels() {
        quantumField.setText(viewModel.getQuantum().toString());

        disposables.add(
                viewModel.getTableModels()
                        .observeOn(Schedulers.computation())
                        .subscribe(models -> {
                            processTableModel = new ProcessTableModel(PROCESS_COLUMN_NAME);
                            models.forEach(model -> processTableModel.addRow(model));
                            processTable.setModel(processTableModel);
                        })
        );
    }
}
