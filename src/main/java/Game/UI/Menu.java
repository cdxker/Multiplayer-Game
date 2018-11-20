package Game.UI;

import com.almasb.fxgl.app.FXGL;
import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.scene.FXGLMenu;
import com.almasb.fxgl.scene.menu.MenuType;
import javafx.beans.binding.StringBinding;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

import static com.almasb.fxgl.app.FXGL.getAudioPlayer;
import static com.almasb.fxgl.app.FXGL.getGameScene;


public class Menu extends FXGLMenu {

    public Menu(GameApplication app, MenuType type) {
        super(app, type);
        switch (type) {
            case MAIN_MENU:
                Button startButton = createActionButton("New Game", () -> fireNewGame());
                Button exitButton = createActionButton("Exit", () -> fireExit());
                VBox menu = new VBox(50, startButton, exitButton);
                menu.setTranslateY(getGameScene().getHeight() / 2);
                menu.setTranslateY(getGameScene().getWidth() / 2);
                menuRoot.getChildren().add(menu);
            case GAME_MENU:
                break;
        }

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
}
