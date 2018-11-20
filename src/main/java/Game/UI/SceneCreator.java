package Game.UI;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.scene.*;
import com.almasb.fxgl.scene.menu.FXGLDefaultMenu;
import com.almasb.fxgl.scene.menu.MenuType;
import org.jetbrains.annotations.NotNull;

/**
 * This is the outermost edge of the UI that
 * will wrap up calls to create UI Nodes.
 */
public class SceneCreator extends SceneFactory {

    @NotNull
    @Override
    public FXGLMenu newGameMenu(GameApplication app) {
        //TODO: Create your own
        return new FXGLDefaultMenu(app, MenuType.GAME_MENU);
    }

    @NotNull
    @Override
    public FXGLMenu newMainMenu(GameApplication app) {
        // TODO Create your own
        return new FXGLDefaultMenu(app, MenuType.MAIN_MENU);
    }

    @NotNull
    @Override
    public IntroScene newIntro() {
        return super.newIntro();
    }

    @NotNull
    @Override
    public LoadingScene newLoadingScene() {
        return super.newLoadingScene();
    }

    @NotNull
    @Override
    public FXGLScene newStartup() {
        return super.newStartup();
    }
}
