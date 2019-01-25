package Game;

import Game.Map.MapBuilder;
import Game.Map.MapNotFoundException;
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
import javafx.scene.input.KeyCode;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import java.util.Map;

import static Game.Map.MapReader.getBuiltInMap;
import static com.almasb.fxgl.app.DSLKt.spawn;

public class GameApp extends GameApplication {

    MapBuilder map;

    private Entity player1 = new Entity();
    private Entity player2 = new Entity();

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
    protected void initGameVars(Map<String, Object> vars) {
        vars.put("player1Wins", false);
        vars.put("player2Wins", false);
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
                carMovement.incrAccelerationDrag(friction.getDrag());
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
                carMovement.incrAccelerationDrag(friction.getDrag());
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

        getPhysicsWorld().addCollisionHandler(new CollisionHandler(EntityType.Player1, EntityType.FINISHLINE) {
            @Override
            protected void onCollision(Entity a, Entity b) {
                getGameState().setValue("player1Wins", true);
            }
        });

        getPhysicsWorld().addCollisionHandler(new CollisionHandler(EntityType.Player1, EntityType.FINISHLINE) {
            @Override
            protected void onCollision(Entity a, Entity b) {
                if (!getGameState().getBoolean("player2Wins")) {
                    getGameState().setValue("player1Wins", true);
                    getGameState().setValue("player2Wins", false);
                }
            }
        });

        getPhysicsWorld().addCollisionHandler(new CollisionHandler(EntityType.Player2, EntityType.FINISHLINE) {
            @Override
            protected void onCollision(Entity a, Entity b) {
                System.out.println("win");
                if (!getGameState().getBoolean("player1Wins")) {
                    getGameState().setValue("player2Wins", true);
                    getGameState().setValue("player1Wins", false);
                }
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
        boolean p1Wins = getGameState().getBoolean("player1Wins");
        boolean p2Wins = getGameState().getBoolean("player2Wins");

        if(p1Wins || p2Wins){
            endGame(p1Wins, p2Wins);
        }else {
            map.update();
        }
    }

    private void endGame(boolean p1Wins, boolean p2Wins) {
        var winText = FXGL.getUIFactory().newText("You win");
        var loseText = FXGL.getUIFactory().newText("You Lose");
        winText.setTranslateY(getHeight() / 2.0);

        if (p1Wins){
            winText.setTranslateX(getWidth() / 4.0);
            loseText.setTranslateY(getWidth()*3 / 4.0);
            getGameScene().addUINodes(winText, loseText);
        }else if(p2Wins){
            loseText.setTranslateX(getWidth() / 4.0);
            winText.setTranslateY(getWidth()*3 / 4.0);
            getGameScene().addUINodes(loseText, winText);
        }
    }

    public MapBuilder getMap() {
        return map;
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    protected void initGame() {
        System.out.println("Hello Initing");
        getGameWorld().addEntityFactory(new CarFactory());
        getGameWorld().addEntityFactory(new TileFactory());

        player1 = spawn("Player2", 0, 0);
        player2 = spawn("Player1", 40, 40);

        try {
            double tileSize = 64;
            System.out.println(map);
            PlayerScreen screen1 = new PlayerScreen(new Rectangle(0, 0, getWidth()/2.0, getHeight()), player1);
            PlayerScreen screen2 = new PlayerScreen(new Rectangle(getWidth()/2, 0, getWidth()/2.0, getHeight()), player2);
            map = new MapBuilder(getBuiltInMap("curvyalley"), tileSize, screen1, screen2);
        } catch (MapNotFoundException e) {
            System.out.println("Fail");
            e.printStackTrace();
        }
    }

    public void gameOver() {
        getDisplay().showConfirmationBox("Play again?", (yes) -> {
            if (!yes) startNewGame();
            else exit();
        });
    }


}