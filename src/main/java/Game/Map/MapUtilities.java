package Game.Map;

import javafx.geometry.Point2D;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

import static Game.FileUtilties.FileUtilities.getDirectoryWithSlash;
import static Game.FileUtilties.FileUtilities.writeText;

/**
 * Useful methods that do not naturally belong to the other classes in the Map package.
 */
public class MapUtilities {
    private static String customMapsDir = "CustomMap\\";

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

    public static boolean doesCustomMapsDirExist() {
        return new File(customMapsDir).isDirectory();
    }

    public static void createCustomMapsDir() {
        new File(customMapsDir).mkdirs(); // Make required directories for customMapsDir if needed
        createExampleMap(); // Place an example map into the customMapsDir
        createReadMeFile();
    }

    public static void createReadMeFile() {
        String text = "This directory is used to load user-made maps from. Please place custom maps\n" +
                "here that are in JSON files with data that is structured appropriately. In\n" +
                "this directory, an example map has been provided.";
        String path = getCustomMapsDir() + "ReadMe.txt";
        writeText(path, text);
    }


    /**
     *  This method is useful because it will always give a directory with a slash at the end.
     */
    public static String getCustomMapsDir() {
        return getDirectoryWithSlash(customMapsDir);
    }

    public static void createExampleMap() {
        HashSet<Tile> tiles = new HashSet<>();
        tiles.add(new Tile("wood", new Point2D(0, 0)));
        tiles.add(new Tile("Ice", new Point2D(0, 10)));
        Map map = new Map(tiles, "ExampleMap");
        MapWriter.writeToDisk(map);
    }
}