package Game.Map;

import javafx.geometry.Point2D;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static Game.FileUtilties.FileUtilities.getDirectoryWithSlash;
import static Game.FileUtilties.FileUtilities.writeString;

/**
 * Useful methods that do not naturally belong to the other classes in the Map package.
 */
public class MapUtilities {
    private static String customMapsDir = "CustomMap\\";

    public static void printMapInfo(Map map) {
        System.out.println("Map name:" + map.getName());
        Set<Tile> t = map.getTiles();
        for (Object tile : t.toArray()) {
            Tile ti = (Tile) tile; // gtx 1080 ti
            System.out.println("UID:" + ti.getUniqueId());
            System.out.println("xPos:" + ti.getPos().getX());
            System.out.println("yPos:" + ti.getPos().getY());
            System.out.println("type:" + ti.getType());
        }
    }

    public static boolean doesCustomMapsDirExist() {
        return new File(customMapsDir).isDirectory();
    }

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
        tiles.add(new Tile("wood", new Point2D(0, 0)));
        tiles.add(new Tile("ice", new Point2D(0, 10)));
        Map map = new Map(tiles, "ExampleMap");
        MapWriter.writeMapToDisk(map);
    }
}