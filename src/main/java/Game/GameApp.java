package Game;

import Game.Map.Map;
import Game.Map.MapBuilder;
import Game.Map.MapNotFoundException;
import Game.Map.MapReader;
import Game.Map.MapUtilities;
import Game.UI.SceneCreator;
import Game.components.DamageComponent;
import Game.components.HealthComponent;
import Game.components.MovementComponent;
import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.entity.Entities;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.physics.CollisionHandler;
import com.almasb.fxgl.settings.GameSettings;
import javafx.scene.input.KeyCode;

import static com.almasb.fxgl.app.DSLKt.*;

import java.io.File;
import java.io.IOException;

import static Game.Map.MapReader.getCustomMap;
import static com.almasb.fxgl.app.DSLKt.onKey;


public class GameApp extends GameApplication {

    @Override
    protected void initSettings(GameSettings settings) {
        settings.setWidth(1000);
        settings.setHeight(600);
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
    }

    @Override
    protected void initPhysics() {
        getPhysicsWorld().addCollisionHandler(new CollisionHandler(EntityType.Car, EntityType.Bullet) {
            @Override
            protected void onCollision(Entity car, Entity bullet) {
                HealthComponent carHealth = car.getComponent(HealthComponent.class);
                DamageComponent damage = bullet.getComponent(DamageComponent.class);

                carHealth.increment(-damage.getDamage());
            }
        });
        getPhysicsWorld().setGravity(0, 0);
    }

    public static void main(String[] args) {
        try {
            MapUtilities.createCustomMapsDir();
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        }

        try {
            Map exampleMap = getCustomMap("ExampleMap");
            System.out.println(exampleMap);
        } catch (MapNotFoundException e) {
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
        getGameWorld().addEntity(Entities.makeScreenBounds(40));
        spawn("Car", 30, 30);
        // Map is created in initGame
        try {
            MapBuilder.createMap(MapReader.createMapFromFile(new File("ExampleMap.json"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        //getAudioPlayer().playMusic("car_hype_music.mp3");
    }
}