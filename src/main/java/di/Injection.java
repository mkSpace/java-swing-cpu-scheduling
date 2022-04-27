package di;

import ui.MainView;
import ui.MainViewModel;
import ui.View;

public class Injection {

    private static View view;
    private static MainViewModel viewModel;

    public static synchronized View provideView() {
        if (view == null) {
            view = new MainView();
        }
        return view;
    }

    public static synchronized MainViewModel provideMainViewModel() {
        if (viewModel == null) {
            viewModel = new MainViewModel();
        }
        return viewModel;
    }
}
