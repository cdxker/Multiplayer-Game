package Game.Map;


import Game.EntityType;
import com.almasb.fxgl.app.FXGL;
import com.almasb.fxgl.entity.SpawnData;
import javafx.geometry.Point2D;

import java.util.Set;

import static com.almasb.fxgl.app.DSLKt.spawn;
import static com.almasb.fxgl.app.FXGL.getGameWorld;

public class MapBuilder {

    /**
     * Clears the map of all tiles that are currently placed
     */
    public static void clearMap() {
        getGameWorld().getEntitiesByType(EntityType.Tile).forEach(entity -> entity.removeFromWorld());
    }

    /**
     * Spawns the tiles onto the map based of map data
     *
     * @param map The map that contains the tiles
     */
    public static void createMap(Map map) {
        spawnTiles(map.getTiles(), map.getGridSize());
    }

    /**
     * Spawns each tile on map based on map data
     *
     * @param tiles The tiles that want to be spawned
     */
    public static void spawnTiles(Set<Tile> tiles, Point2D gridSize) {
        System.out.println("width: " + FXGL.getAppWidth() + " Height: " + FXGL.getAppHeight());
        double tileWidth = FXGL.getAppWidth() / gridSize.getX();
        double tileHeight = FXGL.getAppHeight() / gridSize.getY();

        Point2D size = new Point2D(tileWidth, tileHeight);
        System.out.println(size);
        for (Tile tile : tiles) {
            System.out.println(tile);
            Point2D tilePos = new Point2D(tile.getPos().getX() * tileWidth, tile.getPos().getY() * tileHeight);
            spawn(tile.getType(), new SpawnData(tilePos).put("tileSize", size));
        }
    }

}
