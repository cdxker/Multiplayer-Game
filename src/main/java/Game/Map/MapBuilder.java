package Game.Map;


import Game.EntityType;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.GameWorld;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.view.EntityView;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static com.almasb.fxgl.app.DSLKt.spawn;
import static com.almasb.fxgl.app.FXGL.*;
import static com.almasb.fxgl.app.FXGL.getGameWorld;


/**
 * Class that spawns in any entities that are needed to be
 * viewed by the player.
 */
public class MapBuilder {

    private ArrayList<PlayerScreen> screens;
    private double tileSize = 64;
    private GameWorld game = getGameWorld();
    private Map map;

    public MapBuilder(Map map, double tileSize, PlayerScreen... screens) {
        this.map = map;
        this.tileSize = tileSize;
        this.screens = new ArrayList<PlayerScreen>(Arrays.asList(screens));
        map.getTiles().removeIf(tile -> tile.getType().equals("Blank")); // Too difficult to change maps
        getGameScene().addUINodes(screens);
    }

    public void addScreen(PlayerScreen screen){
        screens.add(screen);
    }

    /**
     * Clears the map of all tiles that are currently placed
     */
    public static void clearMap() {
        getGameWorld().getEntitiesByType(EntityType.Tile).forEach(Entity::removeFromWorld);
        getGameWorld().getEntitiesByType(EntityType.Wall).forEach(Entity::removeFromWorld);
    }

    public void update() {
        clearMap(); // only remove tile and Health Components TODO:(test if removing this option increases performance)
        for (Tile t : map.getTiles()) {
            Point2D tile = t.getPos().multiply(tileSize);
            for(PlayerScreen screen : screens){
                if (screen.contains(tile)) {
                    spawn(t.getType(), new SpawnData(tile).put("tileSize", tileSize).put("Time", Duration.seconds(3)).put("Strength", 5.0));
                    // Is there a way to reduce this?
                }
            }
        }
        screens.forEach(PlayerScreen::onUpdate);
    }

    /**
     * Spawns the tiles onto the map based of map data
     *
     * @param map The map that contains the tiles
     */
    public void createMap(Map map) {
        spawnTiles(map.getTiles());
    }

    public void spawnTiles(Tile... tiles){
        spawnTiles(Set.of(tiles));
    }

    /**
     * Spawns each tile on map based on map data
     *
     * @param tiles The tiles that want to be spawned
     */
    public void spawnTiles(Set<Tile> tiles) {
        System.out.println("width: " + getAppWidth() + " Height: " + getAppHeight());
        getGameScene().getViewport().setBounds(0, 0, (int) (getAppWidth() * tileSize), (int)(getAppHeight() * tileSize));
        for (Tile tile : tiles) {
            if (!tile.getType().equals("Blank")) {
                System.out.println(tile);
                Point2D tilePos = tile.getPos().multiply(tileSize);
                spawn(tile.getType(), new SpawnData(tilePos).put("tileSize", tileSize).put("Time", Duration.seconds(2)).put("Strength", 5.0));
            }
        }
    }


    public double getTileSize() {
        return tileSize;
    }

    public void configureTileSize(double tileSize){
        tileSize = tileSize;
    }

    public void setMap(Map map) {
        this.map = map;
    }

}
