package Game;

import Game.Map.MapBuilder;
import Game.Map.MapNotFoundException;
import Game.Map.MapUtilities;
import Game.Map.PlayerScreen;
import Game.UI.SceneCreator;
import Game.components.*;
import Game.components.powerups.PowerUpComponent;
import Game.components.powerups.PowerUps;
import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.entity.Entities;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.extra.entity.effect.Effect;
import com.almasb.fxgl.extra.entity.effect.EffectComponent;
import com.almasb.fxgl.physics.CollisionHandler;
import com.almasb.fxgl.settings.GameSettings;
import javafx.geometry.Point2D;
import javafx.scene.input.KeyCode;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

import java.io.IOException;

import static Game.Map.MapReader.getBuiltInMap;
import static Game.Map.MapReader.getMap;
import static com.almasb.fxgl.app.DSLKt.onKey;
import static com.almasb.fxgl.app.DSLKt.spawn;



public class GameApp extends GameApplication {

    Entity car;
    MapBuilder map;

    @Override
    protected void initSettings(GameSettings settings) {
        settings.setWidth(1920);
        settings.setHeight(1080);
        settings.setTitle("Bullet Hail");
        settings.setVersion("0.1");

        settings.setIntroEnabled(false);
        settings.setMenuEnabled(true);
        settings.setSceneFactory(new SceneCreator());
        settings.setFullScreenAllowed(false);
    }

    @Override
    protected void initInput() {
        // TODO: extract this to a method
        onKey(KeyCode.A, "Left", () -> {
            getGameWorld().getEntitiesByComponent(MovementComponent.class).forEach((entity) -> {
                MovementComponent component = entity.getComponent(MovementComponent.class);
                component.steerLeft();
            });

        });
        onKey(KeyCode.D, "Right", () -> {
            getGameWorld().getEntitiesByComponent(MovementComponent.class).forEach((entity) -> {
                MovementComponent component = entity.getComponent(MovementComponent.class);
                component.steerRight();
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

        onKey(KeyCode.SPACE, "Fire Bullet", ()->{
            getGameWorld().getEntitiesByComponent(GunCompoent.class).forEach((entity)->{
                GunCompoent gun = entity.getComponent(GunCompoent.class);
                gun.shootBullet();
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
                carHealth.add(-damage.getDamage());
            }
        });

        getPhysicsWorld().addCollisionHandler(new CollisionHandler(EntityType.Car, EntityType.Tile) {
            @Override
            protected void onCollision(Entity car, Entity tile) {
                MovementComponent carMovement = car.getComponent(MovementComponent.class);
                FrictionComponent friction = tile.getComponent(FrictionComponent.class);
                carMovement.incrAccelerationDrag(friction.getDrag());
            }
        });

        getPhysicsWorld().addCollisionHandler(new CollisionHandler(EntityType.Car, EntityType.PowerUp) {
            @Override
            protected void onCollision(Entity car, Entity powerUpEntity) {
                EffectComponent carEffects = car.getComponent(EffectComponent.class);
                PowerUpComponent powerUp = powerUpEntity.getComponent(PowerUpComponent.class);
                carEffects.startEffect(powerUp.getEffect());
            }
        });
        getPhysicsWorld().setGravity(0, 0);
    }

    @Override
    protected void onUpdate(double tpf) {
        map.update();
    }

    public static void main(String[] args) {
        try {
            MapUtilities.createCustomMapsDir();
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        }

        System.out.println("Done!");
        launch(args);
    }

    @Override
    protected void initGame() {
        getGameWorld().addEntityFactory(new CarFactory());
        getGameWorld().addEntityFactory(new TileFactory());

        car = spawn("Car", 40, 40);

        try {
            PlayerScreen screen = new PlayerScreen(new Rectangle(0, 0, getWidth(), getHeight()), car);
            map = new MapBuilder(getBuiltInMap("curvyalley"), 64, screen);
            System.out.println(map);
        } catch (MapNotFoundException e) {
            System.out.println("Fail");
            e.printStackTrace();
        }
    }

    public void gameOver() {
        getDisplay().showConfirmationBox("Play again?", (yes) -> {
            if (yes) startNewGame();
            else exit();
        });
    }


}