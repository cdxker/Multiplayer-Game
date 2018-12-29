package Game;

import Game.components.FrictionComponent;
import Game.components.HealthComponent;
import com.almasb.fxgl.app.DSLKt;
import com.almasb.fxgl.entity.*;
import com.almasb.fxgl.entity.components.CollidableComponent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import static com.almasb.fxgl.app.DSLKt.*;

public class TileFactory implements EntityFactory {

    public Entities.EntityBuilder genericTile(SpawnData data) {
        return Entities.builder()
                .type(EntityType.Tile)
                .from(data)
                .with(new CollidableComponent(true));
    }

    @Spawns("SlowPowerUp")
    public Entity newDirtTile(SpawnData data) {

        return genericTile(data)
                .with(new FrictionComponent(-0.5)) // slows the car
                .viewFromNodeWithBBox(texture("SlowPowerUp.png", 32, 32))
                .build();
    }

    @Spawns("HealthPowerUp")
    public Entity newHealthPowerUp(SpawnData data){
        return genericTile(data)
                .with(new HealthComponent(80)) // TODO: create Health Power Up
                .viewFromNodeWithBBox(texture("HealthPowerUp.png"))
                .with(new FrictionComponent(0))
                .build();
    }

    @Spawns("border")
    public Entity newBorderTile(SpawnData data) {
        return genericTile(data)
                .with(new FrictionComponent(-.25))
                .viewFromNodeWithBBox(new Rectangle(50, 50, Color.RED))
                .build();
    }

    @Spawns("road")
    public Entity newRoadTile(SpawnData data) {

        return genericTile(data)
                .with(new FrictionComponent(0)) // restores the cars drag to normal
                .viewFromNodeWithBBox(new Rectangle(50, 50, Color.BLUE))
                .build();
    }

    @Spawns("SpeedPowerUp")
    public Entity newSpeedPowerUp(SpawnData data) {
        return genericTile(data)
                .with(new FrictionComponent(0.1)) // speeds up the car
                .viewFromNodeWithBBox(texture("SpeedPowerUp.png", 32, 32)) // TODO: Create set size from data
                .build();
    }


}
