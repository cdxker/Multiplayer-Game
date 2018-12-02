package Game.Map;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import javafx.geometry.Point2D;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Scanner;
import java.util.logging.Logger;

/**
 * This class is used for de-serializing an object from a file which means
 * taking a file that has information of an object's state and turning
 * that back into an actual object in memory.
 */
public class MapReader {
    public static final Logger logger = Logger.getLogger(MapReader.class.getName());

    public static Map createMapFromJson(String jsonStr) {
        JsonObject rootJson = getJsonObject(jsonStr);
        String name = getNameFromJson(rootJson);
        HashSet<Tile> tiles = getTilesFromJson(rootJson);
        return new Map(tiles, name);
    }

    /**
     * Reads the file in the CustomMap directory and returns a map
     *
     * @param filename The name of the map file (including the '.json')
     * @return A map to be used
     */
    public static Map readMapFromDisk(String filename) {
        Scanner scanner = null;
        try {
            scanner = new Scanner(new File(MapUtilities.getCustomMapsDir() + filename));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        logger.info(String.format("Parsing the map %s", MapUtilities.getCustomMapsDir() + filename));
        String text = scanner.useDelimiter("\\A").next(); // Uses magic to parse to the end of file

        logger.info(String.format("Text Parsed is %s", text));
        scanner.close();
        return createMapFromJson(text);
    }

    public static JsonObject getJsonObject(String jsonData) {
        JsonParser parserJson = new JsonParser();
        return parserJson.parse(jsonData).getAsJsonObject();
    }

    public static HashSet<Tile> getTilesFromJson(JsonObject rootJson) {
        HashSet<Tile> tiles = new HashSet<>();
        Iterator<JsonElement> tilesIter = rootJson.get("tiles").getAsJsonArray().iterator();
        while (tilesIter.hasNext()) {
            JsonObject tileJson = tilesIter.next().getAsJsonObject();
            String UID = getUniqueId(tileJson);
            String type = getType(tileJson);
            Point2D pos = getPosition(tileJson);
            tiles.add(new Tile(UID, type, pos));
        }
        return tiles;
    }

    public static String getUniqueId(JsonObject tileJson) {
        return tileJson.get("uniqueId").getAsString();
    }

    public static String getType(JsonObject tileJson) {
        return tileJson.get("type").getAsString();
    }

    public static Point2D getPosition(JsonObject tileJson) {
        JsonObject pos = tileJson.get("pos").getAsJsonObject();
        double x = pos.get("x").getAsDouble();
        double y = pos.get("y").getAsDouble();
        return new Point2D(x, y);
    }

    public static String getNameFromJson(JsonObject rootJson) {
        /* The replaceAll call removes " from the beginning and ending of the string. */
        return rootJson.get("name").toString().replaceAll("(?:^\")|(?:\"$)", "");
    }
}
