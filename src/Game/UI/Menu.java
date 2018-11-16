package Game.UI;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.scene.FXGLMenu;
import com.almasb.fxgl.scene.menu.MenuType;
import com.almasb.fxgl.ui.FXGLButton;
import javafx.beans.binding.StringBinding;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;


public class Menu extends FXGLMenu {

    public Menu(GameApplication app, MenuType type) {
        super(app, type);
    }

    /**
     * Used to fancify the buttons/ style them as I want to
     *
     * @param s       The string received
     * @param command The command the button will execute
     * @return The stylized button
     */
    @Override
    protected Button createActionButton(String s, Runnable command) {
        FXGLButton button = new FXGLButton(s);
        button.setOnMouseClicked(e -> command.run());
        return button;
    }

    /**
     * Used to fancify the buttons/ style them as I want to
     *
     * @param stringBinding The string received
     * @param command       The command the button will execute
     * @return The stylized button
     */
    @Override
    protected Button createActionButton(StringBinding stringBinding, Runnable command) {
        FXGLButton button = new FXGLButton(stringBinding.toString());
        button.setOnMouseClicked(e -> command.run());
        return button;
    }

    @Override
    protected Node createBackground(double width, double height) {
        Rectangle rectangle = new Rectangle(width, height);
        return rectangle;
    }

    @Override
    protected Node createTitleView(String s) {
        return new Text("Hello");
    }

    @Override
    protected Node createVersionView(String s) {
        return null;
    }

    @Override
    protected Node createProfileView(String s) {
        return null;
    }
}
