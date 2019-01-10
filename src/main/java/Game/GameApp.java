package Game;

import Game.Map.MapBuilder;
import Game.Map.MapNotFoundException;
import Game.Map.MapUtilities;
import Game.UI.SceneCreator;
import Game.components.DamageComponent;
import Game.components.FrictionComponent;
import Game.components.HealthComponent;
import Game.components.MovementComponent;
import Game.components.powerups.PowerUpComponent;
import Game.components.powerups.PowerUps;
import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.extra.entity.effect.Effect;
import com.almasb.fxgl.extra.entity.effect.EffectComponent;
import com.almasb.fxgl.physics.CollisionHandler;
import com.almasb.fxgl.settings.GameSettings;
import javafx.geometry.Point2D;
import javafx.scene.input.KeyCode;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

import java.io.IOException;

import static Game.Map.MapReader.getMap;
import static com.almasb.fxgl.app.DSLKt.onKey;
import static com.almasb.fxgl.app.DSLKt.spawn;



public class GameApp extends GameApplication {

    Entity car;

    @Override
    protected void initSettings(GameSettings settings) {
        settings.setWidth(1920);
        settings.setHeight(1080);
        settings.setTitle("Bullet Hail");
        settings.setVersion("0.1");

        settings.setIntroEnabled(false);
        settings.setMenuEnabled(true);
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
                System.out.println("ouch");
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
                System.out.println(powerUp.getEffect());
                carEffects.startEffect(powerUp.getEffect());
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

        System.out.println("Done!");
        launch(args);
    }

    @Override
    protected void initGame() {
        getGameWorld().addEntityFactory(new CarFactory());
        getGameWorld().addEntityFactory(new TileFactory());
        //getGameWorld().addEntity(Entities.makeScreenBounds(40));

        try {
            MapBuilder.configureTileSize(128);
            MapBuilder.createMap(getMap("the first"));
        } catch (MapNotFoundException e) {
            e.printStackTrace();
        }

        car = spawn("car", 40, 40);
        Point2D velocity = new Point2D(10, 10);
        spawn("Ball", new SpawnData(30, 30).put("velocity", velocity));

        getGameScene().getViewport().setBounds(0, 0, getWidth(), getHeight() + 200);
        getGameScene().getViewport().bindToEntity(car, 40, 40);
        getGameWorld().addEntity(car);
        

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