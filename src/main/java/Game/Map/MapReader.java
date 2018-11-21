package Game.Map;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import javafx.geometry.Point2D;

import java.util.HashSet;
import java.util.Iterator;

/*
 * This class is used for de-serializing an object from a file which means
 * taking a file that has information of an object's state and turning
 * that back into an actual object in memory.
 */
public class MapReader {
    public static Map createMapFromJson(String jsonStr) {
        JsonObject rootJson = getJsonObject(jsonStr);
        String name = getNameFromJson(rootJson);
        HashSet<Tile> tiles = getTilesFromJson(rootJson);
        return new Map(tiles, name);
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
        return rootJson.get("name").toString().replaceAll("(?:^\")|(?:\"$)", "");
    }
}
