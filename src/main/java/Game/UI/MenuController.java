package Game.UI;

import com.almasb.fxgl.app.FXGL;
import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.scene.FXGLMenu;
import com.almasb.fxgl.scene.menu.MenuType;
import javafx.animation.FadeTransition;
import javafx.beans.binding.StringBinding;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.util.Duration;

import static com.almasb.fxgl.app.FXGL.getAudioPlayer;


public class MenuController extends FXGLMenu {

    public MenuController(GameApplication app, MenuType type) {
        super(app, type);
        switch (type) {
            case MAIN_MENU:
            case GAME_MENU:

                break;
        }

    }


    /**
     * Provides a transition between multiple menus
     *
     * @param menu
     */
    @Override
    protected void switchMenuTo(Node menu) {
        Node oldMenu = menuRoot.getChildren().get(0);

        FadeTransition ft = new FadeTransition(Duration.seconds(0.33), oldMenu);
        ft.setToValue(0);
        ft.setOnFinished(e -> {
            menu.setOpacity(0);
            menuRoot.getChildren().set(0, menu);
            oldMenu.setOpacity(1);

            FadeTransition ft2 = new FadeTransition(Duration.seconds(0.33), menu);
            ft2.setToValue(1);
            ft2.play();
        });
        ft.play();
    }

    @Override
    protected void switchMenuContentTo(Node content) {
        super.switchMenuContentTo(content);
    }

    /**
     * Used to fancify the buttons/ style them as I want to
     *
     * @param name    The string received
     * @param command The command the button will execute
     * @return The stylized button
     */
    @Override
    protected Button createActionButton(String name, Runnable command) {
        Button button = new Button(name);
        button.setOnMouseClicked(e -> {
            getAudioPlayer().playSound("menu_click.wav");
            command.run();
        });
        return button;
    }

    /**
     * Used to fancify the buttons/ style them as I want to
     *
     * @param name    The string received
     * @param command The command the button will execute
     * @return The stylized button
     */
    @Override
    protected Button createActionButton(StringBinding name, Runnable command) {
        return createActionButton(name.getValue(), command);
    }

    @Override
    protected Node createBackground(double width, double height) {
        Rectangle bg = new Rectangle(width, height);

        return bg;
    }

    @Override
    protected Node createTitleView(String s) {
        return new Text("Hello");
    }

    @Override
    protected Node createVersionView(String version) {
        Text view = FXGL.getUIFactory().newText(version);
        view.setTranslateY(app.getHeight() - 2);
        return view;
    }

    @Override
    protected Node createProfileView(String profileName) {
        Text view = FXGL.getUIFactory().newText(profileName);
        view.setTranslateY(app.getHeight() - 20);
        view.setTranslateX(app.getWidth() - view.getLayoutBounds().getWidth());
        return view;
    }


    public class MainMenu extends VBox {

        public MainMenu() {
            Button startButton = createActionButton("New Game", () -> fireNewGame());
            Button optionsButton = createActionButton("Options", () -> {
                switchMenuTo(new OptionsMenu());
            });
            Button exitButton = createActionButton("Exit", () -> fireExit());
            VBox menu = new VBox(20, startButton, optionsButton, exitButton);
            menu.setTranslateY(app.getHeight() / 2);
            menu.setTranslateX(app.getWidth() / 2);
            menuRoot.getChildren().add(menu);

        }
    }

    public class OptionsMenu extends VBox {
        public OptionsMenu() {

        }
    }

}
