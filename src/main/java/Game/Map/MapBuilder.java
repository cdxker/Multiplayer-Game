package Game.Map;


import Game.EntityType;
import com.almasb.fxgl.app.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.SpawnData;
import javafx.geometry.Point2D;
import javafx.util.Duration;

import java.util.Set;

import static com.almasb.fxgl.app.DSLKt.spawn;
import static com.almasb.fxgl.app.FXGL.*;
import static com.almasb.fxgl.app.FXGL.getGameWorld;

public class MapBuilder {

    private static Point2D tileSize = new Point2D(64, 64);

    /**
     * Clears the map of all tiles that are currently placed
     */
    public static void clearMap() {
        getGameWorld().getEntitiesByType(EntityType.Tile).forEach(Entity::removeFromWorld);
    }

    /**
     * Spawns the tiles onto the map based of map data
     *
     * @param map The map that contains the tiles
     */
    public static void createMap(Map map) {
        spawnTiles(map.getTiles());
    }

    public static void spawnTiles(Tile... tiles){
        spawnTiles(Set.of(tiles));
    }

    /**
     * Spawns each tile on map based on map data
     *
     * @param tiles The tiles that want to be spawned
     */
    public static void spawnTiles(Set<Tile> tiles) {
        System.out.println("width: " + getAppWidth() + " Height: " + getAppHeight());
        getGameScene().getViewport().setBounds(0, 0, (int) (getAppWidth() * tileSize.getX()), (int)(getAppHeight() * tileSize.getY()));
        for (Tile tile : tiles) {
            if (!tile.getType().equals("Blank")) {
                System.out.println(tile);
                Point2D tilePos = new Point2D(tile.getPos().getX() * tileSize.getX(), tile.getPos().getY() * tileSize.getY());
                spawn(tile.getType(), new SpawnData(tilePos).put("tileSize", tileSize).put("Time", Duration.seconds(2)).put("Strength", 5.0));
            }
        }
    }

    public static void configureTileSize(Point2D tileSize){
        MapBuilder.tileSize = tileSize;
    }

    public static void configureTileSize(double tileSize){
        MapBuilder.tileSize = new Point2D(tileSize, tileSize);
    }
}
