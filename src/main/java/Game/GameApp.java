package Game;

import Game.UI.SceneCreator;
import Game.components.DamageComponent;
import Game.components.HealthComponent;
import Game.components.MovementComponent;
import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.entity.Entities;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.physics.CollisionHandler;
import com.almasb.fxgl.settings.GameSettings;
import javafx.geometry.Point2D;
import javafx.scene.input.KeyCode;

import static com.almasb.fxgl.app.DSLKt.onKey;
import static com.almasb.fxgl.app.DSLKt.spawn;


public class GameApp extends GameApplication {

    @Override
    protected void initSettings(GameSettings settings) {
        settings.setWidth(960);
        settings.setHeight(540);
        settings.setTitle("Bullet Hail");
        settings.setVersion("0.1");

        settings.setIntroEnabled(false);
        settings.setMenuEnabled(false);
        settings.setSceneFactory(new SceneCreator());
    }

    @Override
    protected void initInput() {
        // TODO: extract this to a method
        onKey(KeyCode.A, "Left", () -> {
            getGameWorld().getEntitiesByComponent(MovementComponent.class).forEach((entity) -> {
                MovementComponent component = entity.getComponent(MovementComponent.class);
                component.left();
            });

        });
        onKey(KeyCode.D, "Right", () -> {
            getGameWorld().getEntitiesByComponent(MovementComponent.class).forEach((entity) -> {
                MovementComponent component = entity.getComponent(MovementComponent.class);
                component.right();
            });

        });

        onKey(KeyCode.W, "Speed up", () -> {
            getGameWorld().getEntitiesByComponent(MovementComponent.class).forEach((entity) -> {
                MovementComponent component = entity.getComponent(MovementComponent.class);
                component.speedUp();
            });

        });

        onKey(KeyCode.S, "Down", () -> {
            getGameWorld().getEntitiesByComponent(MovementComponent.class).forEach((entity) -> {
                MovementComponent component = entity.getComponent(MovementComponent.class);
                component.slowDown();
            });

        });
    }

    @Override
    protected void initPhysics() {
        getPhysicsWorld().addCollisionHandler(new CollisionHandler(EntityType.Car, EntityType.Bullet) {
            @Override
            protected void onCollision(Entity car, Entity bullet) {
                HealthComponent carHealth = car.getComponent(HealthComponent.class);
                DamageComponent damage = bullet.getComponent(DamageComponent.class);
                System.out.println("ouch");
                carHealth.increment(-damage.getDamage());
            }
        });
        getPhysicsWorld().setGravity(0, 0);
    }

    @Override
    protected void initGame() {
        getGameWorld().addEntityFactory(new CarFactory());
        getGameWorld().addEntity(Entities.makeScreenBounds(40));
        spawn("Car", 30, 30);

        Point2D velocity = new Point2D(-5, 8);
        spawn("Bouncy Bullet", new SpawnData(30, 30).put("velocity", velocity));

        getAudioPlayer().playMusic("car_hype_music.mp3");
    }

    public void gameOver() {
        getDisplay().showMessageBox("Wow You Suck", this::exit);
    }

    public static void main(String[] args) {
        launch(args);
    }
}