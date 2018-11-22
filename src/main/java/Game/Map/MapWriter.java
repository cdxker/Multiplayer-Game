package Game.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import static Game.FileUtilties.FileUtilities.writeText;
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
     * Overloaded writeToDisk(String json) method that turns a memory
     * representation of a map into a disk representation of a map and writes
     * it to a file in {customMapsDir}\{name of map}.json
     * @return The string path of the written file.
     */
    public static String writeToDisk(Map map) {
        return writeToDisk(serializeMap(map));
    }

    /**
     * Takes a disk representation of a map as a string and writes it to a file
     * in {customMapsDir}\{name of map}.json
     * @return The string path of the written file.
     */
    public static String writeToDisk(String json) {
        JsonObject rootJson = MapReader.getJsonObject(json);
        String mapName = getNameFromJson(rootJson);
        return writeToDisk(json, mapName);
    }

    /**
     * Takes a disk representation of a map as a string and writes it to a file
     * in {customMapsDir}\{fileName}.json
     * @return The string path of the written file.
     */
    public static String writeToDisk(String json, String fileName) {
        String fullPath = getCustomMapsDir() + fileName + ".json";
        String finalPath = writeText(fullPath, json);
        System.out.println("Wrote \"" + fileName + ",\" a map, to " + finalPath);
        return finalPath;
    }
}
