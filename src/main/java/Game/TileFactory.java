package Game;

import Game.components.FrictionComponent;
import Game.components.HealthComponent;
import Game.components.powerups.PowerUps;
import com.almasb.fxgl.app.DSLKt;
import com.almasb.fxgl.entity.*;
import com.almasb.fxgl.entity.components.CollidableComponent;
import com.almasb.fxgl.physics.PhysicsComponent;
import com.almasb.fxgl.physics.box2d.dynamics.BodyType;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import static com.almasb.fxgl.app.DSLKt.*;

public class TileFactory implements EntityFactory {

    // Special Tiles
    @Spawns("Wall")
    public Entity newWallTile(SpawnData data){
        double size = data.get("tileSize");
        PhysicsComponent physics = new PhysicsComponent();
        physics.setBodyType(BodyType.STATIC);

        return Entities.builder()
                .from(data)
                .type(EntityType.Wall)
                .with(new CollidableComponent(true))
                .with(physics)
                .viewFromNodeWithBBox(new Rectangle(size, size, Color.BLACK))
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
        return genericTile(data)
                .with(new FrictionComponent(0))
                .viewFromNodeWithBBox(new Rectangle(size, size, Color.rgb(100, 100, 100)))
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
        return genericTile(data)
                .with(new FrictionComponent(-0.5))
                .viewFromNodeWithBBox(new Rectangle(size, size, Color.rgb(234,208,168)))
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
        return genericPowerUp(data)
                .viewFromNodeWithBBox(texture("HealthPowerUp.png", size, size))
                .with(PowerUps.HealthPowerUp(strength))
                .build();
    }

    @Spawns("SpeedPowerUp")
    public Entity newSpeedPowerUp(SpawnData data){
        double size = data.get("tileSize");
        Duration time = data.get("Time");
        return genericPowerUp(data)
                .viewFromNodeWithBBox(texture("SpeedPowerUp.png", size, size))
                .with(PowerUps.SpeedPowerUp(time))
                .build();
    }

    @Spawns("SlowPowerUp")
    public Entity newSlowPowerUp(SpawnData data){
        double size = data.get("tileSize");
        Duration time = data.get("Time");
        return genericPowerUp(data)
                .viewFromNodeWithBBox(texture("SlowPowerUp.png", size, size))
                .with(PowerUps.SlowPowerUp(time))
                .build();
    }
}
