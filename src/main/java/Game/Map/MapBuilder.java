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

import java.util.List;
import java.util.Set;

import static com.almasb.fxgl.app.DSLKt.spawn;
import static com.almasb.fxgl.app.FXGL.*;
import static com.almasb.fxgl.app.FXGL.getGameWorld;

public class MapBuilder {

    private Pane pane = new Pane();
    private double tileSize = 64;
    private Entity entityToTrack;
    private GameWorld game = getGameWorld();
    private Map map;
    private Rectangle screen;

    int x = 0;
    int y = 0;


    public MapBuilder(Map map, Entity entityToTrack, double tileSize, double x, double y, double w, double h) {
        this.map = map;
        this.entityToTrack = entityToTrack;
        this.tileSize = tileSize;
        screen = new Rectangle(w, h);
        screen.setTranslateX(x);
        screen.setTranslateY(y);
        pane.setMaxSize(getAppWidth()*2, getAppHeight()*2);
        getGameScene().addUINode(pane);
        map.getTiles().removeIf(tile -> tile.getType().equals("Blank")); // Too difficult to change maps
    }

    /**
     * Clears the map of all tiles that are currently placed
     */
    public static void clearMap() {
        getGameWorld().getEntitiesByType(EntityType.Tile).forEach(Entity::removeFromWorld);
        getGameWorld().getEntitiesByType(EntityType.Wall).forEach(Entity::removeFromWorld);
    }

    public void update() {
        pane.getChildren().remove(0, pane.getChildren().size()); // clear screen
        clearMap();
        pane.setTranslateX(-entityToTrack.getPosition().getX() + pane.getWidth() / 2);
        pane.setTranslateY(-entityToTrack.getPosition().getY() + pane.getHeight() / 2);

        screen.setX(-entityToTrack.getPosition().getX() + pane.getWidth() / 2);
        screen.setY(-entityToTrack.getPosition().getY() + pane.getHeight() / 2);
        System.out.println(screen.getX());
        for (Tile t : map.getTiles()) {
            Point2D tile = t.getPos().multiply(tileSize);
            if (screen.contains(tile)) {
                // mabye we cna use the position component to change the X and Y values
                Entity e = spawn(t.getType(), new SpawnData(tile).put("tileSize", tileSize).put("Time", Duration.seconds(3)).put("Strength", 5.0));
                // Is there a way to reduce this?
                //e.setX(screen.getTranslateX());
                //e.setY(screen.getTranslateY());
                pane.getChildren().add(e.getView());
            }
        }
        List<Entity> entities = game.getEntitiesFiltered((entity -> {
            // While this takes up some memory this is not our problem
            Object type = entity.getType();
            boolean wall = type.equals(EntityType.Wall);
            boolean powerUp = type.equals(EntityType.PowerUp);
            boolean tile = type.equals(EntityType.Tile);
            return !wall && !powerUp && !tile;
        }));
        for(Entity entity: entities){
            pane.getChildren().add(entity.getView());
        } // Store these value in cache to increase performance while running

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
