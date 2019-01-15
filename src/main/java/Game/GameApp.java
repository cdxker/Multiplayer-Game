package Game;

import Game.Map.MapBuilder;
import Game.Map.MapNotFoundException;
import Game.Settings.GlobalSettings;
import Game.UI.SceneCreator;
import Game.components.DamageComponent;
import Game.components.FrictionComponent;
import Game.components.HealthComponent;
import Game.components.MovementComponent;
import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.entity.Entities;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.physics.CollisionHandler;
import com.almasb.fxgl.settings.GameSettings;
import com.almasb.fxgl.settings.MenuItem;
import com.almasb.fxgl.util.Credits;
import javafx.geometry.Point2D;
import javafx.scene.input.KeyCode;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.EnumSet;

import static Game.Map.MapReader.getMap;
import static com.almasb.fxgl.app.DSLKt.onKey;
import static com.almasb.fxgl.app.DSLKt.spawn;



public class GameApp extends GameApplication {


    public static GlobalSettings globalSettings;

    static {
        try {
            String defaultConfig = "{" +
                    "\"widthRes\": 900," +
                    "\"heightRes\": 600," +
                    "\"introEnabled\": false" +
                    "}";

            globalSettings = new GlobalSettings("profiles\\GlobalSettings.json", defaultConfig);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    protected void initSettings(GameSettings settings) {
        settings.setWidth(globalSettings.getWidthRes());
        settings.setHeight(globalSettings.getHeightRes());
        settings.setIntroEnabled(globalSettings.isIntroEnabled());


        //// Not intended to be changed by players
        settings.setEnabledMenuItems(EnumSet.allOf(MenuItem.class));
        //settings.setDialogFactory(new DialogCreator());
        settings.setSceneFactory(new SceneCreator());
        settings.setFullScreenAllowed(true); // Forced Fullscreen if true/ Toggleable if false
        settings.setAppIcon("AppIcon.png");
        settings.setTitle("Bullet Hail");
        settings.setVersion("v1.0.0");
        settings.setCSS("main.css");
        settings.setManualResizeEnabled(false);
        //settings.setUIFactory();
        settings.setMenuEnabled(true);


        ArrayList<String> credits = new ArrayList<>(settings.getCredits().getList());
        credits.add("Denzell Ford");
        credits.add("Terry Garcia");
        credits.add("Tri Nguyen");
        credits.add("FXGL by AlmasB");
        settings.setCredits(new Credits(credits));
    }

    @Override
    protected void initUI() {
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
                System.out.println("ouch");
                carHealth.increment(-damage.getDamage());
            }
        });

        getPhysicsWorld().addCollisionHandler(new CollisionHandler(EntityType.Car, EntityType.Tile) {
            @Override
            protected void onCollision(Entity car, Entity tile) {
                MovementComponent carMovement = car.getComponent(MovementComponent.class);
                FrictionComponent friction = tile.getComponent(FrictionComponent.class);
                carMovement.setAccelerationDrag(friction.getDrag());
            }
        });
        getPhysicsWorld().setGravity(0, 0);
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    protected void initGame() {
        getGameWorld().addEntityFactory(new CarFactory());
        getGameWorld().addEntityFactory(new TileFactory());
        getGameWorld().addEntity(Entities.makeScreenBounds(40));

        try {
            MapBuilder.createMap(getMap("output"));
        } catch (MapNotFoundException e) {
            e.printStackTrace();
        }

        spawn("Car", 100, 100);
        //FXGL.getAudioPlayer().playMusic("car_hype_music.mp3");
        Point2D velocity = new Point2D(10, 10);
        spawn("Ball", new SpawnData(30, 30).put("velocity", velocity));

        System.out.println(getHeight());

        Text text = new Text("Enjoy the ball");
        text.setTranslateY(50);
        text.setTranslateX((getWidth() / 2.0) - 2);
        text.setTextAlignment(TextAlignment.CENTER);
        text.setFont(new Font(50));
        getGameScene().addUINode(text);
    }

    public void gameOver() {
        getDisplay().showConfirmationBox("Play again?", (yes) -> {
            if (yes) startNewGame();
            else exit();
        });
    }
}