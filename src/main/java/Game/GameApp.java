package Game;

import Game.Map.MapBuilder;
import Game.Map.MapUtilities;
import Game.Map.PlayerScreen;
import Game.UI.SceneCreator;
import Game.components.*;
import Game.components.powerups.PowerUpComponent;
import com.almasb.fxgl.app.FXGL;
import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.extra.entity.effect.EffectComponent;
import com.almasb.fxgl.input.Input;
import com.almasb.fxgl.input.UserAction;
import com.almasb.fxgl.physics.CollisionHandler;
import com.almasb.fxgl.settings.GameSettings;
import com.almasb.fxgl.util.Credits;
import javafx.scene.input.KeyCode;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.almasb.fxgl.app.DSLKt.spawn;



public class GameApp extends GameApplication {
    private Entity player1 = new Entity();
    private Entity player2 = new Entity();

    public static GlobalSettings globalSettings;
    public static MapBuilder map;
    public static Map<String, Object> cvars = new HashMap<>();

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
        settings.setSceneFactory(new SceneCreator());
        settings.setManualResizeEnabled(false);
        settings.setFullScreenAllowed(true); // Forced Fullscreen if true/ Toggleable if false
        settings.setAppIcon("AppIcon.png");
        settings.setTitle("Bullet Hail");
        settings.setVersion("v1.0.0");
        settings.setMenuEnabled(true);
        settings.setCSS("main.css");

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
        Input input = getInput(); // get input service

        /*
         * The next four blocks are the controls for player 1
         * In order: Up, Down, Left, Right
         */
        input.addAction(new UserAction("Speed Up 1") {
            @Override
            protected void onAction() {
                MovementComponent component = player1.getComponent(MovementComponent.class);
                component.speedUp();
            }
        }, KeyCode.W);

        input.addAction(new UserAction("Slow Down 1") {
            @Override
            protected void onAction() {
                MovementComponent component = player1.getComponent(MovementComponent.class);
                component.slowDown();
            }
        }, KeyCode.S);

        input.addAction(new UserAction("Steer Right 1") {
            @Override
            protected void onAction() {
                MovementComponent component = player1.getComponent(MovementComponent.class);
                component.steerRight();
            }
        }, KeyCode.D);

        input.addAction(new UserAction("Steer Left 1") {
            @Override
            protected void onAction() {
                MovementComponent component = player1.getComponent(MovementComponent.class);
                component.steerLeft();
            }
        }, KeyCode.A);

        /*
         * The next four blocks are the controls for player 2
         * In order: Up, Down, Left, Right
         */
        input.addAction(new UserAction("Speed Up 2") {
            @Override
            protected void onAction() {
                MovementComponent component = player2.getComponent(MovementComponent.class);
                component.speedUp();
            }
        }, KeyCode.UP);

        input.addAction(new UserAction("Slow Down 2") {
            @Override
            protected void onAction() {
                MovementComponent component = player2.getComponent(MovementComponent.class);
                component.slowDown();
            }
        }, KeyCode.DOWN);

        input.addAction(new UserAction("Steer Right 2") {
            @Override
            protected void onAction() {
                MovementComponent component = player2.getComponent(MovementComponent.class);
                component.steerRight();
            }
        }, KeyCode.RIGHT);

        input.addAction(new UserAction("Steer Left 2") {
            @Override
            protected void onAction() {
                MovementComponent component = player2.getComponent(MovementComponent.class);
                component.steerLeft();
            }
        }, KeyCode.LEFT);

        input.addAction(new UserAction("Fire Bullet 1") {
            @Override
            protected void onActionBegin() {
                GunComponent component = player1.getComponent(GunComponent.class);
                component.shootBullet();
            }
        }, KeyCode.F);

        input.addAction(new UserAction("Fire Bullet 2") {
            @Override
            protected void onActionBegin() {
                GunComponent component = player2.getComponent(GunComponent.class);
                component.shootBullet();
            }
        }, KeyCode.M);
    }

    @Override
    protected void initPhysics() {
        getPhysicsWorld().setGravity(0, 0);


        getPhysicsWorld().addCollisionHandler(new CollisionHandler(EntityType.Player1, EntityType.Bullet) {
            @Override
            protected void onCollision(Entity car, Entity bullet) {
                BulletComponent bul = bullet.getComponent(BulletComponent.class);
                if(bul.getParent().isType(EntityType.Player2)) {
                    HealthComponent carHealth = car.getComponent(HealthComponent.class);
                    DamageComponent damage = bullet.getComponent(DamageComponent.class);
                    carHealth.add(-damage.getDamage());

                    FXGL.getMasterTimer().runOnceAfter(bullet::removeFromWorld, Duration.seconds(0.01));
                }
            }
        });

        getPhysicsWorld().addCollisionHandler(new CollisionHandler(EntityType.Player1, EntityType.Tile) {
            @Override
            protected void onCollision(Entity car, Entity tile) {
                MovementComponent carMovement = car.getComponent(MovementComponent.class);
                FrictionComponent friction = tile.getComponent(FrictionComponent.class);
                carMovement.incrBaseAccelerationDrag(friction.getDrag());
            }
        });

        getPhysicsWorld().addCollisionHandler(new CollisionHandler(EntityType.Player1, EntityType.PowerUp) {
            @Override
            protected void onCollision(Entity car, Entity powerUpEntity) {
                EffectComponent carEffects = car.getComponent(EffectComponent.class);
                PowerUpComponent powerUp = powerUpEntity.getComponent(PowerUpComponent.class);
                carEffects.startEffect(powerUp.getEffect());
            }
        });

        getPhysicsWorld().addCollisionHandler(new CollisionHandler(EntityType.Player2, EntityType.Bullet) {
            @Override
            protected void onCollision(Entity car, Entity bullet) {
                BulletComponent bul = bullet.getComponent(BulletComponent.class);
                if(bul.getParent().isType(EntityType.Player1)) {
                    HealthComponent carHealth = car.getComponent(HealthComponent.class);
                    DamageComponent damage = bullet.getComponent(DamageComponent.class);
                    carHealth.add(-damage.getDamage());

                    FXGL.getMasterTimer().runOnceAfter(bullet::removeFromWorld, Duration.seconds(0.01));
                }
            }
        });

        getPhysicsWorld().addCollisionHandler(new CollisionHandler(EntityType.Player2, EntityType.Tile) {
            @Override
            protected void onCollision(Entity car, Entity tile) {
                MovementComponent carMovement = car.getComponent(MovementComponent.class);
                FrictionComponent friction = tile.getComponent(FrictionComponent.class);
                carMovement.incrBaseAccelerationDrag(friction.getDrag());
            }
        });

        getPhysicsWorld().addCollisionHandler(new CollisionHandler(EntityType.Player2, EntityType.PowerUp) {
            @Override
            protected void onCollision(Entity car, Entity powerUpEntity) {
                EffectComponent carEffects = car.getComponent(EffectComponent.class);
                PowerUpComponent powerUp = powerUpEntity.getComponent(PowerUpComponent.class);
                carEffects.startEffect(powerUp.getEffect());
            }
        });

        getPhysicsWorld().addCollisionHandler(new CollisionHandler(EntityType.Bullet, EntityType.Wall) {
            @Override
            protected void onCollision(Entity bullet, Entity tile) {
                FXGL.getMasterTimer().runOnceAfter(bullet::removeFromWorld, Duration.seconds(0.01));
            }
        });
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

        player1 = spawn("Player1", 40, 40);
        player2 = spawn("Player2", 0, 0);

        double tileSize = 64;
        PlayerScreen screen1 = new PlayerScreen(new Rectangle(0, 0, getWidth() / 2, getHeight()), player1);
        PlayerScreen screen2 = new PlayerScreen(new Rectangle(getWidth() / 2, 0, getWidth() / 2, getHeight()), player2);
        map = new MapBuilder((Game.Map.Map) getGameState().getObject("map"), 64, screen1, screen2);

        System.out.println(map);
    }

    @Override
    protected void initGameVars(Map<String, Object> vars) {
        vars.putAll(cvars);
    }

    public void gameOver() {
        getDisplay().showConfirmationBox("Play again?", (yes) -> {
            if (yes) startNewGame();
            else exit();
        });
    }


}