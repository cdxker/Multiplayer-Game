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
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.util.Duration;

import static com.almasb.fxgl.app.FXGL.getAudioPlayer;


public class MenuController extends FXGLMenu {

    public MenuController(GameApplication app, MenuType type) {
        super(app, type);
        Node menu;
        switch (type) {
            case MAIN_MENU:
                menu = new MainMenu();
                break;
            case GAME_MENU:
                menu = new MainMenu(); // temp
                break;
            default:
                menu = new MainMenu();
        }
        menuRoot.getChildren().add(menu);

    }


    /**
     * Provides a transition between multiple menus
     *
     * @param menu the menu to swap to
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

    /**
     * Creates the background
     *
     * @param width  the width of the screen
     * @param height the height of the screen
     * @return A new background Node
     */
    @Override
    protected Node createBackground(double width, double height) {
        Rectangle bg = new Rectangle(width, height);
        bg.setFill(Color.BLACK);
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
            super(20);
            Button startButton = createActionButton("New Game", () -> fireNewGame()); // mabye change this to game types
            Button optionsButton = createActionButton("Options", () -> switchMenuTo(new OptionsMenu(this)));
            Button exitButton = createActionButton("Exit", () -> fireExit());
            getChildren().addAll(startButton, optionsButton, exitButton);
            setTranslateY(app.getHeight() / 2.0);
            setTranslateX(app.getWidth() / 16.0);
        }
    }

    public class OptionsMenu extends VBox {
        public OptionsMenu(Node previousMenu) {
            super(20);
            Button backButton = createActionButton("Back", () -> switchMenuTo(previousMenu));
        }
    }

}
