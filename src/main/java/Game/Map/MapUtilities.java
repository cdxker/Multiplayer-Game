package Game.Map;

import java.util.Set;

/*
 * Useful methods that do not naturally belong to the other classes in the Map package.
 */
public class MapUtilities {
    public static void printMapInfo(Map map) {
        System.out.println("Map name:" + map.getName());
        Set<Tile> t = map.getTiles();
        for (Object tile : t.toArray()) {
            Tile ti = (Tile) tile;
            System.out.println("UID:" + ti.getUniqueId());
            System.out.println("xPos:" + ti.getPos().getX());
            System.out.println("yPos:" + ti.getPos().getY());
            System.out.println("type:" + ti.getType());
        }
    }
}