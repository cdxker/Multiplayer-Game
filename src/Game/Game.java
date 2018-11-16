package Game;

import Game.UI.SceneCreator;
import Game.components.MovementComponent;
import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.entity.Entities;
import com.almasb.fxgl.settings.GameSettings;
import javafx.scene.input.KeyCode;

import static com.almasb.fxgl.app.DSLKt.onKey;
import static com.almasb.fxgl.app.DSLKt.spawn;


public class Game extends GameApplication {

    @Override
    protected void initSettings(GameSettings settings) {
        settings.setWidth(1920);
        settings.setHeight(1080);
        System.out.println("hi");
        settings.setTitle("InitSample");
        settings.setVersion("0.1");

        settings.setMenuEnabled(true);
        settings.setSceneFactory(new SceneCreator());
    }

    @Override
    protected void initInput() {
        // TODO: extract this to a method
        onKey(KeyCode.A, "Left", ()->{
            getGameWorld().getEntitiesByComponent(MovementComponent.class).forEach((entity)->{
                MovementComponent component =  entity.getComponent(MovementComponent.class);
                component.left();
            });

        });
        System.out.println("h");
        onKey(KeyCode.D, "Right", ()->{
            getGameWorld().getEntitiesByComponent(MovementComponent.class).forEach((entity)->{
                MovementComponent component =  entity.getComponent(MovementComponent.class);
                component.right();
            });

        });

        onKey(KeyCode.W, "Speed up", ()->{
            getGameWorld().getEntitiesByComponent(MovementComponent.class).forEach((entity)->{
                MovementComponent component =  entity.getComponent(MovementComponent.class);
                component.speedUp();
            });

        });

        onKey(KeyCode.S, "Down", ()->{
            getGameWorld().getEntitiesByComponent(MovementComponent.class).forEach((entity)->{
                MovementComponent component =  entity.getComponent(MovementComponent.class);
                component.slowDown();
            });

        });
    }

    @Override
    protected void initGame() {
        getGameWorld().addEntityFactory(new CarFactory());
        getGameWorld().addEntity(Entities.makeScreenBounds(40));
        spawn("CAR", 30, 30);
    }

    public static void main(String[] args){
        launch(args);
    }
}