package Game.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.nio.file.Path;

import static Game.FileUtilties.FileUtilities.writeString;
import static Game.Map.MapReader.getNameFromJson;
import static Game.Map.MapUtilities.getCustomMapsDir;

/**
 * This class is used for taking maps and turning them into files that can be stored.
 * This is done by serializing the map objects into JSON files which basically means
 * taking an object's instance variables and storing that into a string. This class
 * uses Google's Gson package to do the serialization. Below is an example of the
 * serialization of a two tile map.
 {
  "tiles": [
    {
      "uniqueId": "793315160",
      "type": "wood",
      "pos": {
        "x": 0,
        "y": 0,
        "hash": 0
      }
    },
    {
      "uniqueId": "270397815",
      "type": "Ice",
      "pos": {
        "x": 0,
        "y": 10,
        "hash": 0
      }
    }
  ],
  "name": "map1"
}
 */
public class MapWriter {
    /**
     * Returns a string that is a serialized (disk representation) Map object.
     * @param map The Map object to be serialized.
     * @return A string that is the disk representation of map.
     */
    public static String serializeMap(Map map) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(map);
    }

    /**
     * Overloaded writeMapToDisk(String json) method that turns a memory
     * representation of a map into a disk representation of a map and writes
     * it to a file in {customMapsDir}\{name of map}.json
     * @return A Path object of the written file.
     */
    public static Path writeMapToDisk(Map map) throws IOException {
        return writeMapToDisk(serializeMap(map));
    }

    /**
     * Takes a disk representation of a map as a string and writes it to a file
     * in {customMapsDir}\{name of map}.json
     * @return A Path object of the written file.
     */
    public static Path writeMapToDisk(String json) throws IOException {
        JsonObject rootJson = MapReader.getJsonObject(json);
        String mapName = getNameFromJson(rootJson);
        return writeMapToDisk(json, mapName);
    }

    /**
     * Takes a disk representation of a map as a string and writes it to a file
     * in {customMapsDir}\{fileName}.json
     * @return A Path object of the written file.
     */
    public static Path writeMapToDisk(String json, String fileName) throws IOException {
        String fullPath = getCustomMapsDir() + fileName + ".json";
        return writeString(fullPath, json);
    }
}
