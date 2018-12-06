package Game.FileUtilties;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

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
     * Helpful method that takes a string path, turns it into a File object,
     * and then returns whatever .toString() gives when called on the File
     * object but the string has also has concatenated a \ at the end. This is
     * helpful because it lets File take care of the issue of whether there is
     * a \ at the end. File gives a string without that slash at the end.
     * @param path The path to be returned with one slash at the end.
     * @return The parameter, path, with a slash at the end.
     */
    public static String getDirectoryWithSlash(String path) {
        return new File(path).toString() + "\\";
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
