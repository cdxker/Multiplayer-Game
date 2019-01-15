package Game.Map;

import javafx.geometry.Point2D;


/**
 * Representation of a game tile
 */
public class Tile {
    private String uniqueId;
    private String type;
    private Point2D pos;

    /**
     * Creates a TILE object with a uniqueId, type, and position.
     */
    public Tile(String uniqueId, String type, Point2D pos) {
        this.uniqueId = uniqueId;
        this.type = type;
        this.pos = pos;
    }

    /**
     * Creates a TILE object using the object's HashCode as the uniqueId.
     * Helpful if you want something else to come up with a HashCode rather than yourself.
     */
    public Tile(String type, Point2D pos) {
        this.type = type;
        this.pos = pos;
        this.uniqueId = Integer.toString(this.getHashCode());
    }

    /**
     * Useful constructor for removing a certain tile out of a Set.
     */
    public Tile(String uniqueId) {
        this.uniqueId = uniqueId;
        this.pos = null;
        this.type = null;
    }

    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Point2D getPos() {
        return pos;
    }

    public void setPos(Point2D pos) {
        this.pos = pos;
    }

    /**
     * This method is used to determine if a TILE object is a valid object. A
     * valid TILE object is defined as having none of its class fields as null.
     *
     * @return returns true if the uniqueId, type, and pos fields of the object
     * are not null.
     */
    public boolean isValid() {
        return this.uniqueId != null && this.type != null && this.pos != null;
    }

    /**
     * Two TILE objects are equal if they have matching uniqueId's.
     */
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Tile))
            return false;
        Tile other = (Tile) o;
        return this.getUniqueId().equals(other.getUniqueId());
    }

    /**
     * This hashcode method is overridden in order to force the use of the overridden equals method.
     * Use getHashCode() instead if you need the TILE object's hashcode.
     */
    @Override
    public int hashCode() {
        return 0;
    }

    public int getHashCode() {
        return super.hashCode();
    }

    @Override
    public String toString() {
        return String.format("%s TILE x:%f, y:%f, id: %s", type, pos.getX(), pos.getY(), uniqueId);
    }
}
