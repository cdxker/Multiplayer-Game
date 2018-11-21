package Game.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import static Game.Map.MapReader.getNameFromJson;

/*
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
    public static String serializeMap(Map map) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(map);
    }

    public static void writeToDisk(Map map) {
        writeToDisk(serializeMap(map));
    }

    public static void writeToDisk(String json) {
        JsonObject rootJson = MapReader.getJsonObject(json);
        String mapName = getNameFromJson(rootJson);
        String fileName = mapName + ".json";
        List<String> lines = Arrays.asList(json.split("\n"));
        Path file = Paths.get(fileName);
        try {
            Files.write(file, lines, Charset.forName("UTF-8"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
