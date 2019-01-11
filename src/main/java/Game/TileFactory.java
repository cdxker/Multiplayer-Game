package Game;

import Game.components.FrictionComponent;
import Game.components.HealthComponent;
import Game.components.powerups.PowerUps;
import com.almasb.fxgl.app.DSLKt;
import com.almasb.fxgl.entity.*;
import com.almasb.fxgl.entity.components.CollidableComponent;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import static com.almasb.fxgl.app.DSLKt.*;

public class TileFactory implements EntityFactory {

    // Special Tiles
    @Spawns("Wall")
    public Entity newWallTile(SpawnData data){
        Point2D size = data.get("tileSize");
        return Entities.builder()
                .from(data)
                .type(EntityType.Wall)
                .with(new CollidableComponent(true))
                .viewFromNodeWithBBox(new Rectangle(size.getX(), size.getY(), Color.BLACK))
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
        Point2D size = data.get("tileSize");
        return genericTile(data)
                .with(new FrictionComponent(0))
                .viewFromNodeWithBBox(new Rectangle(size.getX(), size.getY(), Color.gray(100)))
                .build();
    }

    @Spawns("blank")
    public Entity newBlankTile(SpawnData data){
        return genericTile(data).
                build();
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
        Point2D size = data.get("tileSize");
        return genericPowerUp(data)
                .viewFromNodeWithBBox(texture("HealthPowerUp.png", size.getX(), size.getY()))
                .with(PowerUps.HealthPowerUp(strength))
                .build();
    }

    @Spawns("SpeedPowerUp")
    public Entity newSpeedPowerUp(SpawnData data){
        Point2D size = data.get("tileSize");
        Duration time = data.get("Time");
        return genericPowerUp(data)
                .viewFromNodeWithBBox(texture("SpeedPowerUp.png", size.getX(), size.getY()))
                .with(PowerUps.SpeedPowerUp(time))
                .build();
    }

    @Spawns("SlowPowerUp")
    public Entity newSlowPowerUp(SpawnData data){
        Point2D size = data.get("tileSize");
        Duration time = data.get("Time");
        return genericPowerUp(data)
                .viewFromNodeWithBBox(texture("SlowPowerUp.png", size.getX(), size.getY()))
                .with(PowerUps.SlowPowerUp(time))
                .build();
    }
}
