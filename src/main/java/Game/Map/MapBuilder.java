package Game.Map;


import Game.components.TileComponent;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static com.almasb.fxgl.app.DSLKt.spawn;
import static com.almasb.fxgl.app.FXGL.getGameWorld;

public class MapBuilder {

    /**
     * Clears the map of all tiles that are currently placed
     */
    public static void clearMap() {
        getGameWorld().getEntitiesByComponent(TileComponent.class).forEach(entity -> entity.removeFromWorld());
    }

    /**
     * Spawns the tiles onto the map based of map data
     *
     * @param map The map that contains the tiles
     */
    public static void createMap(Map map) {
        spawnTiles(map.getTiles());
    }

    /**
     * Spawns each tile on map based on map data
     *
     * @param tiles The tiles that want to be spawned
     *              TODO: make sure to check the tile is valid
     */
    public static void spawnTiles(Set<Tile> tiles) {
        for (Tile tile : tiles) {
            spawn(tile.getType(), tile.getPos().multiply(10)); // TODO: Dont forget im mulitplying position by 10 refactor?
        }
    }


    /**
     * Spawns each tile on map based on map data
     *
     * @param tiles The tiles that want to be spawned
     */
    public static void spawnTiles(Tile... tiles) {
        spawnTiles(new HashSet<>(Arrays.asList(tiles)));
    }

}
