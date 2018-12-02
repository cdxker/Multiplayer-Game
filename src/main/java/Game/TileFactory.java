package Game;

import Game.components.TileComponent;
import com.almasb.fxgl.entity.*;
import com.almasb.fxgl.entity.components.CollidableComponent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class TileFactory implements EntityFactory {

    public Entities.EntityBuilder genericTile(SpawnData data) {
        return Entities.builder()
                .from(data)
                .with(new TileComponent())
                .with(new CollidableComponent(true));
    }

    @Spawns("wood")
    public Entity newWoodTile(SpawnData data) {

        return genericTile(data)
                .type(TileType.wood)
                .viewFromNodeWithBBox(new Rectangle(50, 50, Color.BROWN))
                .build();
    }


    @Spawns("ice")
    public Entity newIceTile(SpawnData data) {

        return genericTile(data)
                .type(TileType.ice)
                .viewFromNodeWithBBox(new Rectangle(50, 50, Color.BLUE))
                .build();
    }
}
