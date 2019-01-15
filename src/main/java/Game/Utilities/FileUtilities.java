package Game.Utilities;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Class meant for storing useful methods for interacting with files on system
 * that do not naturally belong to another class.
 */
public class FileUtilities {
    /**
     * Helpful method to avoid repeating boilerplate code for writing text to a file.
     * @param stringPath Path to file location.
     * @param content String containing the content to be put in the file.
     * @param cs The character set to be used for the file.
     * @return the path of the written file as a Path.
     * @throws IOException if an I/O error occurs writing to or creating the
     *                     file, or the text cannot be encoded using the specified charset
     */
    public static Path writeString(String stringPath, String content, Charset cs) throws IOException {
        Path path = Paths.get(stringPath);
        ensureFileExists(stringPath);
        return Files.writeString(path, content, cs);
    }

    /**
     * Helpful method to avoid repeating boilerplate code for writing text to a file.
     *
     * @param file    File object pointing to location of file.
     * @param content String containing the content to be put in the file.
     * @return the path of the written file as a Path.
     * @throws IOException if an I/O error occurs writing to or creating the
     *                     file, or the text cannot be encoded using the specified charset
     */
    public static Path writeString(File file, String content) throws IOException {
        return Files.writeString(file.toPath(), content);
    }

    /**
     * Helpful overloaded method of writeText(String, String, Charset) that
     * uses the UTF-8 charset by default.
     * @param stringPath Path to file location.
     * @param content String containing the content to be put in the file.
     * @return the path of the written file as a Path.
     * @throws IOException if an I/O error occurs writing to or creating the
     *                     file, or the text cannot be encoded using the specified charset
     */
    public static Path writeString(String stringPath, String content) throws IOException {
        return writeString(stringPath, content, Charset.forName("UTF-8"));
    }

    /**
     * This method can be used to ensure that directories a path points through are created, if necessary, along with
     * the file the path points to.
     * @param stringPath The string path which points to a file. Can be relative or absolute.
     * @return Returns a File object of the stringPath
     */
    public static File ensureFileExists(String stringPath) throws IOException {
        return ensureFileExists(new File(stringPath));
    }

    /**
     * This method can be used to ensure that directories a path points through are created, if necessary, along with
     * the file the path points to.
     *
     * @param file the File object that points to a file.
     * @return Returns the file parameter
     */
    public static File ensureFileExists(File file) throws IOException {
        ensureDirExist(file);
        file.createNewFile();
        return file;
    }

    public static File ensureDirExist(String stringPath) {
        return ensureDirExist(new File(stringPath));
    }

    public static File ensureDirExist(File dir) {
        dir = dir.getAbsoluteFile();
        if (!dir.isDirectory()) {
            dir = dir.getParentFile();
        }
        dir.mkdirs();
        return dir;
    }

    public static List<File> getFilesFromDir(String stringPath, String extension) throws IOException {
        File dir = ensureDirExist(stringPath);
        List<File> files = new ArrayList<>();
        File[] dirFiles = dir.listFiles();
        if (dirFiles == null) {
            throw new IOException("Cannot read files from " + stringPath);
        }
        for (File file : dirFiles) {
            if (hasExtension(file, extension)) {
                files.add(file);
            }
        }
        return files;
    }

    public static boolean hasExtension(File file, String extension) {
        return file.getName().endsWith(extension);
    }
}
