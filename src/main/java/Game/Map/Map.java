package Game.Map;

import javafx.geometry.Point2D;

import java.util.Set;

public class Map {
    private Set<Tile> tiles;
    private String name;
    private Point2D gridSize;

    public Map(Set<Tile> tiles, String name, Point2D gridSize) {
        this.tiles = tiles;
        this.name = name;
        this.gridSize = gridSize;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Tile> getTiles() {
        return tiles;
    }

    public void setTiles(Set<Tile> tiles) {
        this.tiles = tiles;
    }

    /**
     * Adds a tile to the set of tiles if tile's uniqueId does belong to any other tile in the set of tiles.
     */
    public boolean addTile(Tile tile) {
        return tiles.add(tile);
    }

    /**
     * Removes a tile from the set of tiles using tile's uniqueId
     */
    public boolean removeTile(Tile tile) {
        return tiles.remove(tile);
    }

    @Override
    public String toString() {
        StringBuilder retStr = new StringBuilder("Map name:" + getName() + "\n");
        for (Object ti : getTiles().toArray()) {
            Tile tiles = (Tile) ti;
            retStr.append("UID:").append(tiles.getUniqueId()).append("\n");
            retStr.append("xPos:").append(tiles.getPos().getX()).append("\n");
            retStr.append("yPos:").append(tiles.getPos().getY()).append("\n");
            retStr.append("type:").append(tiles.getType()).append("\n");
        }
        return retStr.toString();
    }

    /**
     * Two Tile objects are equal if they have matching uniqueId's.
     */
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Map))
            return false;
        Map other = (Map) o;
        return this.getName().equals(other.getName());
    }

    /**
     * This hashcode method is overridden in order to force the use of the overridden equals method.
     * Use getHashCode() instead if you need the Tile object's hashcode.
     */
    @Override
    public int hashCode() {
        return 0;
    }

    public int getHashCode() {
        return super.hashCode();
    }

    public Point2D getGridSize() {
        return gridSize;
    }

    public void setGridSize(Point2D gridSize) {
        this.gridSize = gridSize;
    }
}