package Game;

import Game.Map.MapReader;
import Game.Utilities.FileUtilities;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;


/**
 * This class is meant as a data structure, representing a group of settings that affect all profiles, that
 * can read/write settings from/to disk.
 * <p>
 * Json representation of settings file:
 * {
 * settingName1: value1,
 * settingName2: value2,
 * settingName3: value3,
 * ...
 * settingNameN: valueN
 * }
 */
public class GlobalSettings {
    private int widthRes;
    private int heightRes;
    private boolean introEnabled;
    private transient File settingsFile;
    private transient Gson gson = new GsonBuilder().setPrettyPrinting().create();


    /**
     * Constructs a GlobalSettings object.
     *
     * @param settingsFile  A File object that points to the location where the global settings file should reside.
     * @param defaultConfig A JsonObject representing the default configuration that will be used to replace a
     *                      missing global settings file.
     * @throws IOException
     */
    public GlobalSettings(File settingsFile, JsonObject defaultConfig) throws IOException {
        try {
            FileUtilities.ensureDirExist(settingsFile);
            if (settingsFile.createNewFile()) FileUtilities.writeString(settingsFile, gson.toJson(defaultConfig));
        } catch (IOException e) {
            throw new IOException("An IO error has occurred while checking if the default settings file config exists!");
        }

        JsonObject settings;
        try {
            settings = MapReader.getJsonObject(Files.readString(settingsFile.toPath()));
        } catch (IOException e) {
            throw new IOException("Could not read global settings file.");
        }

        this.settingsFile = settingsFile;

        this.widthRes = settings.get("widthRes").getAsInt();
        this.heightRes = settings.get("heightRes").getAsInt();
        this.introEnabled = settings.get("introEnabled").getAsBoolean();
    }

    public GlobalSettings(File settingsFile, String defaultConfig) throws IOException {
        this(settingsFile, MapReader.getJsonObject(defaultConfig));
    }

    public GlobalSettings(String stringPath, String defaultConfig) throws IOException {
        this(new File(stringPath), defaultConfig);
    }

    public int getWidthRes() {
        return widthRes;
    }

    public void setWidthRes(int widthRes) throws IOException {
        this.widthRes = widthRes;
        writeToDisk("Could not write resolution's width change to disk.");
    }

    public int getHeightRes() {
        return heightRes;
    }

    public void setHeightRes(int heightRes) throws IOException {
        this.heightRes = heightRes;
        writeToDisk("Could not write resolution's height change to disk.");
    }

    public boolean isIntroEnabled() {
        return introEnabled;
    }

    public void setIntroEnabled(boolean introEnabled) throws IOException {
        this.introEnabled = introEnabled;
        writeToDisk("Could not write introEnabled changes to disk.");
    }

    /**
     * Write serialize this GlobalSettings object to disk.
     *
     * @param errorMessage Error message to be used for exception if an exception occurs.
     * @throws IOException Thrown if cannot write to disk due to IO error.
     */
    public void writeToDisk(String errorMessage) throws IOException {
        try {
            FileUtilities.writeString(settingsFile, gson.toJson(this));
        } catch (IOException e) {
            throw new IOException(errorMessage);
        }
    }
}
