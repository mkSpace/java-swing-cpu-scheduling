package ui;

import di.Injection;
import io.reactivex.disposables.CompositeDisposable;
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
    private static final String[] PROCESS_COLUMN_NAME = {
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
    private final ProcessTableModel processTableModel;
    private final DefaultTableModel outputTableModel;
    private final JFreeChart ganttChart;

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
        processTableModel = new ProcessTableModel(PROCESS_COLUMN_NAME);
        outputTableModel = new DefaultTableModel(OUTPUT_PROCESS_COLUMN_NAME, 0);
    }

    @Override
    public void setupViews() {
        setupProcessPanel();
        setupOutputPanel();
        setupMainFrame();
        new ProcessDialog(mainFrame, "Add Process").setVisible(true);
    }

    @Override
    public void bindViewModels() {

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
        JComboBox<String> spinner = new JComboBox<>(ALGORITHM_LIST);
        optionPanel.add(optionChoiceLabel);
        optionPanel.add(spinner);
        optionPanel.add(settingButton);
        optionPanel.add(runningButton);
//        tableModel.addRow(new Object[]{"1", "2", "3", "4", Color.RED});
        JTable processTable = new JTable(processTableModel);
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
//        ganttChart.set
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
        JTable outputTable = new JTable(outputTableModel);
        DefaultTableCellRenderer renderer = (DefaultTableCellRenderer) outputTable.getTableHeader().getDefaultRenderer();
        renderer.setHorizontalAlignment(SwingConstants.CENTER);
        outputTable.getTableHeader().setDefaultRenderer(renderer);
        JScrollPane scrollPane = new JScrollPane(outputTable);
        outputTable.setFillsViewportHeight(true);
        outputTable.setPreferredSize(new Dimension((MAIN_FRAME_WIDTH - (WIDGET_MARGIN * 2)) / 2, (int) Math.round(MAIN_FRAME_HEIGHT * 0.2)));
        scrollPane.setPreferredSize(new Dimension((MAIN_FRAME_WIDTH - (WIDGET_MARGIN * 2)) / 2, (int) Math.round(MAIN_FRAME_HEIGHT * 0.2)));
        resultPanel.add(scrollPane);
        resultPanel.add(new JLabel("<html><body style='text-align:left;'>Average Wait Time : 4.8 <br />Average Turnaround Time : 8.8.</body></html>", JLabel.LEFT));
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
}
