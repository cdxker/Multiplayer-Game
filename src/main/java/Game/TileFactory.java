package Game;

import Game.components.FrictionComponent;
import com.almasb.fxgl.entity.*;
import com.almasb.fxgl.entity.components.CollidableComponent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class TileFactory implements EntityFactory {

    public Entities.EntityBuilder genericTile(SpawnData data) {
        return Entities.builder()
                .type(EntityType.Tile)
                .from(data)
                .with(new CollidableComponent(true));
    }

    @Spawns("wood")
    public Entity newWoodTile(SpawnData data) {

        return genericTile(data)
                .with(new FrictionComponent(-0.5)) //
                .viewFromNodeWithBBox(new Rectangle(50, 50, Color.BROWN))
                .build();
    }

    @Spawns("ice")
    public Entity newIceTile(SpawnData data) {

        return genericTile(data)
                .with(new FrictionComponent(0))
                .viewFromNodeWithBBox(new Rectangle(50, 50, Color.BLUE))
                .build();
    }
}
