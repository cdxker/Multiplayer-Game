package Game.UI;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.scene.FXGLMenu;
import com.almasb.fxgl.scene.SceneFactory;

public class SceneCreator extends SceneFactory {
    @Override
    public FXGLMenu newMainMenu(GameApplication app) {
        return new MainMenu(app);
    }

    public FXGLMenu newGameMenu(GameApplication app) {
        return new GameMenu(app);
    }
}
