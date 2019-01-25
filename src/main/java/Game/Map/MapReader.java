package Game.Map;

import com.almasb.fxgl.app.FXGL;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import javafx.geometry.Point2D;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;
import java.util.logging.Logger;

import static Game.Map.MapUtilities.*;
import static Game.Utilities.FileUtilities.getFilesFromDir;

/**
 * This class is used for de-serializing an object from a file which means
 * taking a file that has information of an object's state and turning
 * that back into an actual object in memory.
 */
public class MapReader {
    public static final Logger logger = Logger.getLogger(MapReader.class.getName());


    public static Map createMapFromJson(String jsonStr) throws JsonSyntaxException {
        JsonObject rootJson = getJsonObject(jsonStr);
        String name = getNameFromJson(rootJson);
        HashSet<Tile> tiles = getTilesFromJson(rootJson);
        Point2D gridSize = getGridSize(rootJson);
        Point2D startPosP1 = getStartPos("startPosP1", rootJson);
        Point2D startPosP2 = getStartPos("startPosP2", rootJson);
        return new Map(tiles, name, gridSize, startPosP1, startPosP2);
    }

    public static HashSet<Map> getMaps() {
        HashSet<Map> maps = new HashSet<>();
        maps.addAll(getCustomMaps());
        maps.addAll(getBuiltInMaps());
        return maps;
    }

    /**
     * Method for getting a certain map out of the custom maps and built-in
     * maps directories. First, it searches in the custom maps directory for the
     * map and returns any found maps. If no found map in custom maps directory,
     * it continues searching in the built-in maps directory and returns any
     * found maps. At this point, if map is not found, MapNotFoundException is
     * thrown.
     *
     * @param mapName Name of map to be returned
     * @return Map object of map with name mapName
     * @throws MapNotFoundException thrown if map cannot be found.
     */
    public static Map getMap(String mapName) throws MapNotFoundException {
        try {
            return getCustomMap(mapName);
        } catch (MapNotFoundException e) {
            return getBuiltInMap(mapName);
        }
    }

    public static Map getCustomMap(String mapName) throws MapNotFoundException {
        HashSet<Map> maps = getCustomMaps();
        for (Map map : maps) {
            if (map.getName().equals(mapName)) {
                return map;
            }
        }
        throw new MapNotFoundException("Requested map not found.");
    }

    public static Map getBuiltInMap(String mapName) throws MapNotFoundException, JsonSyntaxException {
        String filename = mapName + ".json";
        if (!FXGL.getAssetLoader().loadFileNames(getBuiltInMapsFullDir()).contains(filename)) {
            throw new MapNotFoundException("Requested map not found");
        }
        String strPath = getBuiltInMapsDir() + filename;
        List<String> lines = FXGL.getAssetLoader().loadJSON(strPath);
        String content = String.join("\n", lines);
        return createMapFromJson(content);
    }

    public static HashSet<Map> getBuiltInMaps() {
        List<String> filenames = FXGL.getAssetLoader().loadFileNames(getBuiltInMapsFullDir());
        HashSet<Map> maps = new HashSet<>();
        for (String filename : filenames) {
            try {
                String mapName = filename.replaceAll("\\.[^\\.]+$", "");
                maps.add(getBuiltInMap(mapName));
            } catch (MapNotFoundException | JsonSyntaxException e) {
                e.printStackTrace();
            }
        }
        return maps;
    }

    /**
     * This method is used for returning a HashSet of all maps in the custom maps directory.
     *
     * @return HashSet of Map objects that are serialized from files in CustomMaps directory.
     * If files could not be read from customMapsDir, an empty HashSet is returned.
     */
    public static HashSet<Map> getCustomMaps() {
        try {
            List<File> files = getFilesFromDir(getCustomMapsDir(), ".json");
            return MapReader.getSerializedMaps(files);
        } catch (IOException e) {
            //TODO: Log some stuff...
            return new HashSet<>();
        }
    }

    public static ArrayList<String> getMapNames() {
        ArrayList<String> names = new ArrayList<>();
        getMaps().forEach(map -> names.add(map.getName()));
        return names;
    }

    public static Map createMapFromFile(File file) throws IOException, JsonSyntaxException {
        return createMapFromJson(Files.readString(file.toPath()));
    }

    public static HashSet<Map> getSerializedMaps(File... files) {
        return getSerializedMaps(Arrays.asList(files));
    }

    /**
     * This method allows for the use of a list of File objects to be
     * serialized instead of using an array of File objects.
     *
     * @param files List of File objects to be serialized into Map objects.
     * @return An array of serialized Map objects from the files parameter.
     *         This method ignores any files that cause a IOException or
     *         JsonSyntaxException.
     */
    public static HashSet<Map> getSerializedMaps(List<File> files) {
        HashSet<Map> maps = new HashSet<>();
        for (File file : files) {
            try {
                maps.add(createMapFromFile(file));
            } catch (IOException | JsonSyntaxException e) {
                logger.warning(e.getStackTrace()[0].toString()); // TODO: Need to experiment with this...
            }
        }
        return maps;
    }

    /**
     * Method used for turning string JSON into a JsonObject
     *
     * @param jsonData A string containing JSON data to be turned into a JsonObject
     * @return A JsonObject of the jsonData.
     * @throws JsonSyntaxException Throws if jsonData is not syntactically valid JSON.
     */
    public static JsonObject getJsonObject(String jsonData) throws JsonSyntaxException {
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

    public static Point2D getGridSize(JsonObject tileJson) {
        JsonObject pos = tileJson.get("gridSize").getAsJsonObject();
        double x = pos.get("x").getAsDouble();
        double y = pos.get("y").getAsDouble();
        return new Point2D(x, y);
    }

    public static String getNameFromJson(JsonObject rootJson) {
        /* The replaceAll call removes " from the beginning and ending of the string. */
        return rootJson.get("name").toString().replaceAll("(?:^\")|(?:\"$)", "");
    }

    public static Point2D getStartPos(String fieldName, JsonObject rootJson) {
        JsonObject startPos = rootJson.get(fieldName).getAsJsonObject();
        double xPos = startPos.get("x").getAsDouble();
        double yPos = startPos.get("y").getAsDouble();
        return new Point2D(xPos, yPos);
    }
}
