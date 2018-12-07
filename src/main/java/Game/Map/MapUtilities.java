package Game.Map;

import javafx.geometry.Point2D;

import java.io.IOException;
import java.util.HashSet;

import static Game.FileUtilties.FileUtilities.getDirectoryWithSlash;
import static Game.FileUtilties.FileUtilities.writeString;

/**
 * Useful methods that do not naturally belong to the other classes in the Map package.
 */
public class MapUtilities {
    private static String customMapsDir = "CustomMap\\";

    public static void createCustomMapsDir() throws IOException {
        createExampleMap();
        createReadMeFile();
    }

    public static void createReadMeFile() throws IOException {
        String text = "This directory is used to load user-made maps from. Please place custom maps\n" +
                "here that are in JSON files with data that is structured appropriately. In\n" +
                "this directory, an example map has been provided.";
        String path = getCustomMapsDir() + "ReadMe.txt";
        writeString(path, text);
    }

    /**
     *  This method is useful because it will always give a directory with a slash at the end.
     */
    public static String getCustomMapsDir() {
        return getDirectoryWithSlash(customMapsDir);
    }

    public static void createExampleMap() throws IOException {
        HashSet<Tile> tiles = new HashSet<>();
        tiles.add(new Tile("wood", new Point2D(750, 300)));
        tiles.add(new Tile("ice", new Point2D(250, 300)));
        tiles.add(new Tile("boost", new Point2D(500, 300)));
        Map map = new Map("ExampleMap", tiles);
        MapWriter.writeMapToDisk(map);
    }
}