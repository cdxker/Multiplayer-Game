package Game;

import Game.components.FrictionComponent;
import Game.components.ScreenComponent;
import Game.components.ScreenComponent2;
import Game.components.powerups.PowerUps;
import com.almasb.fxgl.entity.*;
import com.almasb.fxgl.entity.components.CollidableComponent;
import com.almasb.fxgl.physics.PhysicsComponent;
import com.almasb.fxgl.physics.box2d.dynamics.BodyType;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import static com.almasb.fxgl.app.DSLKt.texture;

public class TileFactory implements EntityFactory {

    // Special Tiles
    @Spawns("Wall")
    public Entity newWallTile(SpawnData data){
        double size = data.get("tileSize");
        PhysicsComponent physics = new PhysicsComponent();
        physics.setBodyType(BodyType.STATIC);

        Node view1 = new Rectangle(size, size, Color.BLACK);
        Node view2 = new Rectangle(size, size, Color.BLACK);

        return Entities.builder()
                .from(data)
                .type(EntityType.Wall)
                .with(new CollidableComponent(true))
                .with(physics)
                .viewFromNodeWithBBox(view1)
                .with(new ScreenComponent(view1))
                .with(new ScreenComponent2(view2))
                .build();
    }

    @Spawns("Finish Line")
    public Entity newFinsihLine(SpawnData data){
        double size = data.get("tileSize");

        Node view1 = texture("finishLine.png", size, size);
        Node view2 = texture("finishLine.png", size, size);
        
        return Entities.builder()
                .type(EntityType.FINISHLINE)
                .from(data)
                .with(new CollidableComponent(true))
                .viewFromNodeWithBBox(view1)
                .with(new ScreenComponent(view1))
                .with(new ScreenComponent2(view2))
                .build();
    }
    // Basic Friction Tiles

    private Entities.EntityBuilder genericTile(SpawnData data) {
        return Entities.builder()
                .type(EntityType.Tile)
                .from(data)
                .with(new CollidableComponent(true));
    }


    @Spawns("Road")
    public Entity newRoadTile(SpawnData data){
        double size = data.get("tileSize");
        Node view1 = new Rectangle(size, size, Color.rgb(100, 100, 100));
        Node view2 = new Rectangle(size, size, Color.rgb(100, 100, 100));

        return genericTile(data)
                .with(new FrictionComponent(0))
                .viewFromNodeWithBBox(view1)
                .with(new ScreenComponent(view1))
                .with(new ScreenComponent2(view2))
                .build();
    }

    @Spawns("Blank")
    public Entity newBlankTile(SpawnData data){
        return genericTile(data)
                .with(new FrictionComponent(0))
                .build();
    }

    @Spawns("Dirt")
    public Entity newDirtPowerUp(SpawnData data){
        double size = data.get("tileSize");
        Node view1 = new Rectangle(size, size, Color.rgb(234,208,168));
        Node view2 = new Rectangle(size, size, Color.rgb(234,208,168));

        return genericTile(data)
                .with(new FrictionComponent(-0.5))
                .viewFromNodeWithBBox(view1)
                .with(new ScreenComponent(view1))
                .with(new ScreenComponent2(view2))
                .build();
    }


    // PowerUp tiles

    private Entities.EntityBuilder genericPowerUp(SpawnData data){
        return Entities.builder()
                .type(EntityType.PowerUp)
                .from(data)
                .with(new CollidableComponent(true));
    }

    @Spawns("HealthPowerUp")
    public Entity newHealthPowerUp(SpawnData data){
        double strength = data.get("Strength");
        double size = data.get("tileSize");
        Node view1 = texture("HealthPowerUp.png", size, size);
        Node view2 = texture("HealthPowerUp.png", size, size);
        return genericPowerUp(data)
                .viewFromNodeWithBBox(view1)
                .with(new ScreenComponent(view1))
                .with(new ScreenComponent2(view2))
                .with(PowerUps.HealthPowerUp(strength))
                .build();
    }

    @Spawns("SpeedPowerUp")
    public Entity newSpeedPowerUp(SpawnData data){
        double size = data.get("tileSize");
        Duration time = data.get("Time");
        Node view1 = texture("SpeedPowerUp.png", size, size);
        Node view2 = texture("SpeedPowerUp.png", size, size);
        return genericPowerUp(data)
                .viewFromNodeWithBBox(view1)
                .with(new ScreenComponent(view1))
                .with(new ScreenComponent2(view2))
                .with(PowerUps.SpeedPowerUp(time))
                .build();
    }

    @Spawns("SlowPowerUp")
    public Entity newSlowPowerUp(SpawnData data){
        double size = data.get("tileSize");
        Duration time = data.get("Time");
        Node view1 = texture("SlowPowerUp.png", size, size);
        Node view2 = texture("SlowPowerUp.png", size, size);

        return genericPowerUp(data)
                .viewFromNodeWithBBox(view1)
                .with(new ScreenComponent(view1))
                .with(new ScreenComponent2(view2))
                .with(PowerUps.SlowPowerUp(time))
                .build();
    }
}
