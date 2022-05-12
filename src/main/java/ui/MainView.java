package ui;

import data.Algorithm;
import data.Result;
import di.Injection;
import extensions.Message;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.data.category.IntervalCategoryDataset;
import org.jfree.data.gantt.Task;
import org.jfree.data.gantt.TaskSeries;
import org.jfree.data.gantt.TaskSeriesCollection;
import org.jfree.data.time.SimpleTimePeriod;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

import static java.awt.FlowLayout.CENTER;

public class MainView implements View {

    private static final int MAIN_FRAME_WIDTH = 720;
    private static final int MAIN_FRAME_HEIGHT = 900;
    private static final int WIDGET_MARGIN = 12;

    private static final String[] ALGORITHM_LIST = {
            "First Come, First Served",
            "Shortest Job First",
            "Priority Scheduling",
            "Round Robin"
    };
    public static final String[] PROCESS_COLUMN_NAME = {
            "Process No.",
            "Burst Time",
            "Arrival Time",
            "Priority",
            "Color"
    };
    private static final String[] OUTPUT_PROCESS_COLUMN_NAME = {
            "Process No.",
            "Wait Time",
            "Complete Time"
    };

    private final MainViewModel viewModel = Injection.provideMainViewModel();

    private final CompositeDisposable disposables = new CompositeDisposable();

    private final JFrame mainFrame;
    private final JPanel processPanel;
    private final JPanel outputPanel;
    private final JButton settingButton;
    private final JButton runningButton;
    private final JFreeChart ganttChart;
    private ProcessTableModel processTableModel;
    private DefaultTableModel outputTableModel;
    private JTable processTable;
    private JTable outputTable;
    private JComboBox<String> algorithmComboBox;
    private JLabel resultLabel;

    public MainView() {
        mainFrame = new JFrame("App");
        processPanel = new JPanel();
        outputPanel = new JPanel();
        settingButton = new JButton("Setting");
        runningButton = new JButton("Run");
        ganttChart = ChartFactory.createGanttChart("Processing Time", null, null, createDataset());
        mainFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                super.windowClosed(e);
                disposables.clear();
                disposables.dispose();
            }
        });
    }

    @Override
    public void setupViews() {
        setupProcessPanel();
        setupOutputPanel();
        setupMainFrame();
        bindViewModels();
    }

    @Override
    public void bindViewModels() {
        disposables.add(
                viewModel.getTableModels()
                        .observeOn(Schedulers.computation())
                        .subscribe(models -> {
                            processTableModel = new ProcessTableModel(PROCESS_COLUMN_NAME);
                            models.forEach(model -> processTableModel.addRow(model));
                            processTable.setModel(processTableModel);
                        })
        );

        disposables.add(
                viewModel.getSelectedAlgorithm()
                        .observeOn(Schedulers.computation())
                        .subscribe(algorithm -> {
                            String selectedItem = (String) algorithmComboBox.getSelectedItem();
                            int mode = 0;
                            if (Objects.equals(selectedItem, ALGORITHM_LIST[1])) {
                                mode = 1;
                            } else if (Objects.equals(selectedItem, ALGORITHM_LIST[2])) {
                                mode = 2;
                            } else if (Objects.equals(selectedItem, ALGORITHM_LIST[3])) {
                                mode = 3;
                            }
                            if (algorithm.ordinal() != mode) {
                                algorithmComboBox.setSelectedIndex(algorithm.ordinal());
                            }
                        })
        );

        disposables.add(
                viewModel.getLatestResult()
                        .observeOn(Schedulers.computation())
                        .subscribe(this::renderResult)
        );

        disposables.add(
                viewModel.getAlertMessage()
                        .observeOn(Schedulers.computation())
                        .subscribe(Message::showAlertMessage)
        );

        disposables.add(
                viewModel.getErrorMessage()
                        .observeOn(Schedulers.computation())
                        .subscribe(Message::showErrorMessage)
        );
    }

    private void setupMainFrame() {
        mainFrame.setTitle("2017112622_박재민_운영체제_과제_1");
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setSize(MAIN_FRAME_WIDTH + WIDGET_MARGIN * 3, MAIN_FRAME_HEIGHT + WIDGET_MARGIN * 3);
        mainFrame.setVisible(true);
        mainFrame.setLayout(new FlowLayout(CENTER, 0, 0));
        mainFrame.setLocationRelativeTo(null);
        mainFrame.setBackground(Color.gray);
        mainFrame.add(processPanel);
        mainFrame.add(outputPanel);
        mainFrame.revalidate();
    }

    private void setupProcessPanel() {
        processPanel.setBorder(new TitledBorder("Process"));
        processPanel.setPreferredSize(new Dimension(MAIN_FRAME_WIDTH, (int) Math.round(MAIN_FRAME_HEIGHT * 0.4)));
        JPanel optionPanel = new JPanel(new FlowLayout());
        JLabel optionChoiceLabel = new JLabel("Choose the Scheduling Algorithm : ");
        algorithmComboBox = new JComboBox<>(ALGORITHM_LIST);
        optionPanel.add(optionChoiceLabel);
        optionPanel.add(algorithmComboBox);

        algorithmComboBox.addActionListener(e -> {
            String selectedItem = (String) algorithmComboBox.getSelectedItem();
            if (Objects.equals(selectedItem, ALGORITHM_LIST[0])) {
                viewModel.setAlgorithm(Algorithm.FCFS);
            } else if (Objects.equals(selectedItem, ALGORITHM_LIST[1])) {
                viewModel.setAlgorithm(Algorithm.SJF);
            } else if (Objects.equals(selectedItem, ALGORITHM_LIST[2])) {
                viewModel.setAlgorithm(Algorithm.PRIORITY);
            } else {
                viewModel.setAlgorithm(Algorithm.RR);
            }
        });

        settingButton.addActionListener(e -> {
            new SettingDialog(mainFrame, "Details of Process").setVisible(true);
        });

        runningButton.addActionListener(e -> viewModel.run());

        optionPanel.add(settingButton);
        optionPanel.add(runningButton);
        processTable = new JTable();
        DefaultTableCellRenderer renderer = (DefaultTableCellRenderer) processTable.getTableHeader().getDefaultRenderer();
        renderer.setHorizontalAlignment(SwingConstants.CENTER);
        processTable.getTableHeader().setDefaultRenderer(renderer);
        JScrollPane scrollPane = new JScrollPane(processTable);
        processTable.setFillsViewportHeight(true);
        processTable.setPreferredSize(new Dimension(MAIN_FRAME_WIDTH - (WIDGET_MARGIN * 2), (int) Math.round(MAIN_FRAME_HEIGHT * 0.3)));
        scrollPane.setPreferredSize(new Dimension(MAIN_FRAME_WIDTH - (WIDGET_MARGIN * 2), (int) Math.round(MAIN_FRAME_HEIGHT * 0.3)));
        processPanel.add(optionPanel);
        processPanel.add(scrollPane);
    }

    private void setupGanttChart() {
        CategoryPlot plot = ganttChart.getCategoryPlot();
        DateAxis axis = (DateAxis) plot.getRangeAxis();
        axis.setMaximumDate(new Date(19));
        axis.setDateFormatOverride(new SimpleDateFormat("SSS"));
    }

    private void setupOutputPanel() {
        outputPanel.setBorder(new TitledBorder("Output"));
        outputPanel.setPreferredSize(new Dimension(MAIN_FRAME_WIDTH, (int) Math.round(MAIN_FRAME_HEIGHT * 0.6)));
        setupGanttChart();
        ChartPanel chartPanel = new ChartPanel(ganttChart) {
            @Override
            public Dimension getPreferredSize() {
                return new Dimension(MAIN_FRAME_WIDTH - (WIDGET_MARGIN * 2), (int) Math.round(MAIN_FRAME_HEIGHT * 0.35));
            }
        };
        outputPanel.add(chartPanel);
        JPanel resultPanel = new JPanel(new FlowLayout());
        outputTable = new JTable(outputTableModel);
        DefaultTableCellRenderer renderer = (DefaultTableCellRenderer) outputTable.getTableHeader().getDefaultRenderer();
        renderer.setHorizontalAlignment(SwingConstants.CENTER);
        outputTable.getTableHeader().setDefaultRenderer(renderer);
        JScrollPane scrollPane = new JScrollPane(outputTable);
        outputTable.setFillsViewportHeight(true);
        outputTable.setPreferredSize(new Dimension((MAIN_FRAME_WIDTH - (WIDGET_MARGIN * 2)) / 2, (int) Math.round(MAIN_FRAME_HEIGHT * 0.2)));
        scrollPane.setPreferredSize(new Dimension((MAIN_FRAME_WIDTH - (WIDGET_MARGIN * 2)) / 2, (int) Math.round(MAIN_FRAME_HEIGHT * 0.2)));
        resultLabel = new JLabel("<html><body style='text-align:left;'>Average Wait Time : _ <br />Average Turnaround Time : _ .</body></html>", JLabel.LEFT);
        resultPanel.add(scrollPane);
        resultPanel.add(resultLabel);
        outputPanel.add(resultPanel);
    }

    private IntervalCategoryDataset createDataset() {
        TaskSeries series = new TaskSeries("P1");
        series.add(new Task("P1", new SimpleTimePeriod(1, 3)));
        TaskSeries series2 = new TaskSeries("P2");
        series2.add(new Task("P2", new SimpleTimePeriod(3, 5)));
        TaskSeriesCollection dataset = new TaskSeriesCollection();
        dataset.add(series);
        dataset.add(series2);
        return dataset;
    }

    private void renderResult(Result result) {
        resultLabel.setText(
                "<html><body style='text-align:left;'>Average Wait Time : " + result.getAverageWaitTime() +
                        "<br />Average Turnaround Time : " + result.getAverageTurnaroundTime() + ".</body></html>"
        );
        outputTableModel = new DefaultTableModel(OUTPUT_PROCESS_COLUMN_NAME, 0);
        TaskSeriesCollection dataset = new TaskSeriesCollection();
        result.getResult().forEach(taskResult -> {
            dataset.add(taskResult.getSeries());
            outputTableModel.addRow(new Object[]{taskResult.getProcessId(), taskResult.getWaitTime(), taskResult.getCompleteTime()});
        });
        outputTable.setModel(outputTableModel);
        ganttChart.getCategoryPlot().setDataset(dataset);
    }
}
