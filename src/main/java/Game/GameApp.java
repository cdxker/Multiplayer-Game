package Game;

import Game.Map.MapBuilder;
import Game.Map.PlayerScreen;
import Game.Map.Tile;
import Game.UI.SceneCreator;
import Game.components.*;
import Game.components.powerups.PowerUpComponent;
import com.almasb.fxgl.app.FXGL;
import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.audio.Music;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.extra.entity.effect.EffectComponent;
import com.almasb.fxgl.input.Input;
import com.almasb.fxgl.input.UserAction;
import com.almasb.fxgl.physics.CollisionHandler;
import com.almasb.fxgl.settings.GameSettings;
import com.almasb.fxgl.util.Credits;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.almasb.fxgl.app.DSLKt.*;


public class GameApp extends GameApplication {
    private static Logger logger = Logger.getLogger(GameApp.class.getName());
    private Entity player1 = new Entity();
    private Entity player2 = new Entity();
    public static HashMap<String, Game.Map.Map> cvars = new HashMap<>();

    public static GlobalSettings globalSettings;
    MapBuilder mapBuilder;
    Game.Map.Map map;
    double tileSize = 64;
    double UIElementSize = 64;

    ArrayList<Node> gameDataElements = new ArrayList<>();

    Music shoot, car_engine_loop;

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
        settings.setIntroEnabled(true);
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
                FXGL.getAudioPlayer().stopMusic(car_engine_loop);
                FXGL.getAudioPlayer().playMusic(car_engine_loop);
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
                FXGL.getAudioPlayer().stopMusic(car_engine_loop);
                FXGL.getAudioPlayer().playMusic(car_engine_loop);
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
                getAudioPlayer().stopMusic(shoot);
                getAudioPlayer().playMusic(shoot);
            }
        }, KeyCode.F);

        input.addAction(new UserAction("Fire Bullet 2") {
            @Override
            protected void onActionBegin() {
                GunComponent component = player2.getComponent(GunComponent.class);
                component.shootBullet();
                getAudioPlayer().stopMusic(shoot);
                getAudioPlayer().playMusic(shoot);
            }
        }, KeyCode.M);

        input.addAction(new UserAction("escape") {
            @Override
            protected void onAction() {
                logger.info("Exiting Game");
            }

        }, KeyCode.ESCAPE);
    }

    @Override
    protected void initGameVars(Map<String, Object> vars) {
        vars.put("player1Wins", false);
        vars.put("player2Wins", false);
        vars.put("gameOver", false);
    }

    @Override
    protected void initPhysics() {
        getPhysicsWorld().setGravity(0, 0);

        getPhysicsWorld().addCollisionHandler(new CollisionHandler(EntityType.Player1, EntityType.Bullet) {
            @Override
            protected void onCollision(Entity car, Entity bullet) {
                logger.info("Player 1 Hit!");
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
                carMovement.setAccelerationDrag(friction.getDrag());
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
                carMovement.setAccelerationDrag(friction.getDrag());
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
                if (!getGameState().getBoolean("player2Wins") || !getGameState().getBoolean("player1Wins")) {
                    logger.info("Player 1 Wins!");
                    getGameState().setValue("player1Wins", true);
                    getGameState().setValue("player2Wins", false);
                }
            }
        });

        getPhysicsWorld().addCollisionHandler(new CollisionHandler(EntityType.Player2, EntityType.FINISHLINE) {
            @Override
            protected void onCollision(Entity a, Entity b) {
                if (!getGameState().getBoolean("player2Wins") || !getGameState().getBoolean("player1Wins")) {
                    logger.info("Player 2 Wins!");
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
        if(!gameOver()) {
        mapBuilder.update();

        for(Node node: gameDataElements){
            removeUINode(node);
        }
        gameDataElements.clear();

        updateGameDataElements();

            boolean p2Wins = getGameState().getBoolean("player2Wins");
            boolean p1Wins = getGameState().getBoolean("player1Wins");

            if (p1Wins || p2Wins) {
                endGame(p1Wins, p2Wins);
            } else {
                mapBuilder.update();
            }
        }
    }

    private boolean gameOver() {
        return getGameState().getBoolean("gameOver");
    }

    private void endGame(boolean p1Wins, boolean p2Wins) {
        var winText = FXGL.getUIFactory().newText("You win");
        var loseText = FXGL.getUIFactory().newText("You Lose");
        winText.setTranslateY(getHeight() / 2.0);

        if (p1Wins){
            FXGL.getDisplay().showMessageBox("Congrats Player 1! You are the winner!", this::exitToMainMenu);
        }else if(p2Wins){
             FXGL.getDisplay().showMessageBox("Congrats Player 2! You are the winner!", this::exitToMainMenu);
        }
    }

    private void exitToMainMenu() {
        logger.info("Ending Game");
        FXGL.exit();
    }


    public MapBuilder getMap() {
        return mapBuilder;
    }

    public static void main(String[] args) {
        launch(args);
    }

    public static void setupLogger(){
        SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//dd/MM/yyyy
        Date now = new Date();
        String strDate = sdfDate.format(now);

        FileHandler handler = null;
        try {
            handler = new FileHandler(String.format("logs\\BulletHail-%s", strDate.replaceAll(":", "-")));
        } catch (IOException e) {
            e.printStackTrace();
        }
        handler.setLevel(Level.INFO);
        logger.addHandler(handler);
    }

    @Override
    protected void preInit() {
        setupLogger();

    }

    @Override
    protected void initGame() {
        getGameWorld().addEntityFactory(new CarFactory());
        getGameWorld().addEntityFactory(new TileFactory());

        shoot = FXGL.getAssetLoader().loadMusic("shoot.mp3");
        car_engine_loop = FXGL.getAssetLoader().loadMusic("car-engine-loop.mp3");

        getAudioPlayer().playMusic("engine-start.mp3");
        getAudioPlayer().loopBGM("game-bg.mp3");
        System.out.println(cvars);
        map = (Game.Map.Map)cvars.get("map");
        System.out.println(map);
        player1 = spawn("Player1", map.getStartPosP1());
        player2 = spawn("Player2", map.getStartPosP2());

        PlayerScreen screen1 = new PlayerScreen(new Rectangle(0, 0, getWidth()/2, getHeight()), player1);
        PlayerScreen screen2 = new PlayerScreen(new Rectangle(getWidth()/2, 0, getWidth()/2, getHeight()), player2);
        map.createTileGrid();
        mapBuilder = new MapBuilder(map, tileSize, screen1, screen2);

    }

    private void updateGameDataElements(){
        updateBorder();
        updateMinimap();
        updatePlayer();
        updateInputTutorial();
    }

    //// Get the input binding for specified action by player
    private String getInputKey(String actionName){
        return getInput().triggerProperty(getInput().getActionByName(actionName)).getValue().toString();
    }

    //// On-screen instruction to help player know which buttons to use
    private void updateInputTutorial(){
        String up1 = getInputKey("Speed Up 1");
        String down1 = getInputKey("Slow Down 1");
        String left1 = getInputKey("Steer Left 1");
        String right1 = getInputKey("Steer Right 1");
        String shoot1 = getInputKey("Fire Bullet 1");
        Text player1Input = new Text("Gun: " + shoot1 + "\n" + "Movement: " + up1 + " " + down1 + " " + left1 + " " + right1);
        player1Input.setFont(new Font(20));
        player1Input.setFill(Color.WHITE);

        String up2 = getInputKey("Speed Up 2");
        String down2 = getInputKey("Slow Down 2");
        String left2 = getInputKey("Steer Left 2");
        String right2 = getInputKey("Steer Right 2");
        String shoot2 = getInputKey("Fire Bullet 2");
        Text player2Input = new Text("Gun: " + shoot2 + "\n" + "Movement: " + up2 + " " + down2 + " " + left2 + " " + right2);
        player2Input.setFont(new Font(20));
        player2Input.setFill(Color.WHITE);

        gameDataElements.add(player1Input);
        gameDataElements.add(player2Input);

        //// Positioned at the bottom of each player'screen
        addUINode(player1Input,UIElementSize, getHeight()-UIElementSize+20);
        addUINode(player2Input,getWidth()/2 + UIElementSize, getHeight()-UIElementSize+20);
    }

    //// Display both player's health as a number out of maximum health
    private void updatePlayer(){
        HealthComponent health1 = player1.getComponent(HealthComponent.class);
        HealthComponent health2 = player2.getComponent(HealthComponent.class);

        Text healthLabel1 = new Text();
        if(health1.getHealth() <= 0){
            healthLabel1.setText("Player 1:\n" + (int)health1.getHealth() + "/" + (int)health1.getMaxHealth() + " HP\nDisabled\n");
        } else {
            healthLabel1.setText("Player 1:\n" + (int)health1.getHealth() + "/" + (int)health1.getMaxHealth() + " HP\n\n ");
        }

        healthLabel1.setFont(new Font(20));
        healthLabel1.setFill(Color.WHITE);

        Text healthLabel2 = new Text();
        if(health2.getHealth() <= 0){
            healthLabel2.setText("Player 2:\n" + (int)health2.getHealth() + "/" + (int)health2.getMaxHealth() + " HP\nDisabled\n");
        } else {
            healthLabel2.setText("Player 2:\n" + (int)health2.getHealth() + "/" + (int)health2.getMaxHealth() + " HP\n\n ");
        }
        healthLabel2.setFont(new Font(20));
        healthLabel2.setFill(Color.WHITE);

        VBox playerHealth = new VBox();
        playerHealth.getChildren().addAll(healthLabel1,healthLabel2);

        gameDataElements.add(playerHealth);

        //// positioned in the middle of the gameplay screen
        addUINode(playerHealth,getWidth()/2-UIElementSize+14,UIElementSize+20);

    }

    //// Surround the gameplay screen to divide the two player screens
    private void updateBorder(){

        //// Border around the screen
        Rectangle outBorder = new Rectangle(getWidth()-UIElementSize, getHeight()-UIElementSize, Color.TRANSPARENT);
        outBorder.setStroke(Color.BLACK);
        outBorder.setStrokeWidth(UIElementSize);

        //// Border between player screens
        Rectangle midBorder = new Rectangle(UIElementSize*2, getHeight(), Color.BLACK);

        Rectangle player1Border = new Rectangle(getWidth()/2-2*UIElementSize, getHeight()-2*UIElementSize, Color.TRANSPARENT);
        player1Border.setStroke(Color.RED);
        player1Border.setStrokeWidth(5);

        Rectangle player2Border = new Rectangle(getWidth()/2-2*UIElementSize, getHeight()-2*UIElementSize, Color.TRANSPARENT);
        player2Border.setStroke(Color.BLUE);
        player2Border.setStrokeWidth(5);

        gameDataElements.add(outBorder);
        gameDataElements.add(midBorder);
        gameDataElements.add(player1Border);
        gameDataElements.add(player2Border);

        addUINode(outBorder, UIElementSize/2, UIElementSize/2);
        addUINode(midBorder, getWidth()/2-UIElementSize,0);
        addUINode(player1Border, UIElementSize,UIElementSize);
        addUINode(player2Border, getWidth()/2+UIElementSize,UIElementSize);
    }

    //// Display a minimap of the map currently played with differentiated tiles and
    //// player position
    private void updateMinimap(){

        //// get the dimensions of the map currently being played
        int miniX = (int)map.getGridSize().getX();
        int miniY = (int)map.getGridSize().getY();

        //// write each tile on the current map as a single pixel on the minimap
        WritableImage minimap = new WritableImage(miniX + 2, miniY + 2);
        PixelWriter px = minimap.getPixelWriter();
        for(int x = 0; x < miniX; x++) {
            for (int y = 0; y < miniY; y++) {
                Tile tile = map.getTile(x, y);

                //// differentiate different tile types
                switch(tile.getType()) {
                    case "Wall":
                        px.setColor(x + 1, y + 1, Color.WHITE);
                        break;
                    case "Road":
                        px.setColor(x + 1, y + 1, Color.GRAY);
                        break;
                    case "Dirt":
                        px.setColor(x + 1, y + 1, Color.DARKGRAY);
                        break;
                    case "HealthPowerUp":
                    case "SpeedPowerUp":
                        px.setColor(x + 1, y + 1, Color.GREEN);
                        break;
                    case "SlowPowerUp":
                        px.setColor(x + 1, y + 1, Color.RED);
                        break;
                    default:
                        px.setColor(x + 1, y + 1, Color.BLACK);
                        break;
                }
            }
        }

        //// draw player positions on the minimap
        Point2D p1 = player1.getPosition();
        Point2D p2 = player2.getPosition();

        //// each player is represented with a 3x3 square instead of a single pixel
        for(int i = -1; i < 2; i++) {
            for(int j = -1; j < 2; j++) {
                int x1 = i + (int) (p1.getX() / UIElementSize);
                int y1 = j + (int) (p1.getY() / UIElementSize);
                int x2 = i + (int) (p2.getX() / UIElementSize);
                int y2 = j + (int) (p2.getY() / UIElementSize);
                if (x1 > 0 && x1 <= miniX && y1 > 0 && y1 <= miniY) {
                    px.setColor(x1, y1, Color.RED);
                }
                if (x2 > 0 && x2 <= miniX && y2 > 0 && y2 <= miniY) {
                    px.setColor(x2, y2, Color.BLUE);
                }
            }
        }

        for(int i = 0; i < miniX+2; i++){
            px.setColor(i, 0, Color.WHITE);
            px.setColor(i, miniY+1, Color.WHITE);
        }
        for(int i = 0; i < miniY+2; i++){
            px.setColor(0, i, Color.WHITE);
            px.setColor(miniX+1, i, Color.WHITE);
        }

        //// scale the map up so its features are more recognizable
        double minimapSize = 100;
        ImageView minimapView = new ImageView();
        minimapView.setImage(minimap);
        minimapView.setFitHeight(minimapSize);
        minimapView.setFitWidth(minimapSize);

        Text minimapLabel = new Text("Minimap");
        minimapLabel.setFont(new Font(20));
        minimapLabel.setFill(Color.WHITE);

        //// Combine minimap image and minimap label into one VBox
        VBox minimapBox = new VBox();
        minimapBox.getChildren().addAll(minimapLabel, minimapView);

        gameDataElements.add(minimapBox);

        //// positioned at the middle bottom of the screen
        addUINode(minimapBox,getWidth()/2-minimapSize/2,getHeight()-minimapSize-UIElementSize-20);
    }
}