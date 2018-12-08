package Game.Map;

import javafx.geometry.Point2D;

import java.io.IOException;
import java.util.HashSet;

import static Game.Map.MapWriter.writeMapToDisk;
import static Game.Utilities.FileUtilities.writeString;


/**
 * Useful methods that do not naturally belong to the other classes in the Map package.
 */
public class MapUtilities {
    private static final String builtInMapsDir = "maps/"; // Path rooted in FXGL's resources's json folder.
    // Must use forward slashes instead of backward slashes
    // because of FXGL.
    private static final String customMapsDir = "CustomMaps\\";

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

    public static void createExampleMap() throws IOException {
        HashSet<Tile> tiles = new HashSet<>();
        tiles.add(new Tile("wood", new Point2D(750, 300)));
        tiles.add(new Tile("ice", new Point2D(250, 300)));
        tiles.add(new Tile("boost", new Point2D(500, 300)));
        Map map = new Map("ExampleMap", tiles);
        writeMapToDisk(map);
    }

    public static String getCustomMapsDir() {
        return customMapsDir + "\\";
    }

    public static String getBuiltInMapsDir() {
        return builtInMapsDir + "/";
    }

    public static String getBuiltInMapsFullDir() {
        return "/assets/json/" + getBuiltInMapsDir();
    }
}