package ui;

import di.Injection;
import io.reactivex.disposables.CompositeDisposable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import static java.awt.FlowLayout.CENTER;

public class MainView implements View{

    private static final int MAIN_FRAME_WIDTH = 720;
    private static final int MAIN_FRAME_HEIGHT = 900;
    private static final int WIDGET_MARGIN = 12;

    private final MainViewModel viewModel = Injection.provideMainViewModel();

    private final CompositeDisposable disposables = new CompositeDisposable();

    private final JFrame mainFrame;
    private final JPanel processPanel;
    private final JPanel outputPanel;


    public MainView() {
        mainFrame = new JFrame("App");
        processPanel = new JPanel();
        outputPanel = new JPanel();
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
        setupMainFrame();
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

    @Override
    public void bindViewModels() {

    }
}
