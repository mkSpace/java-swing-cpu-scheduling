import di.Injection;
import ui.View;

public class App {

    private final View view = Injection.provideView();

    public App() {
        setupViews();
    }

    public static void main(String[] args) {
        new App();
    }

    private void setupViews() {
        view.setupViews();
    }

}
