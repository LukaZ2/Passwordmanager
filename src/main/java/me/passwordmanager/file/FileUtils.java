package me.passwordmanager.file;

import me.passwordmanager.file.exception.FileCreateException;
import me.passwordmanager.file.exception.IncorrectFileTypeException;

import java.io.*;
import java.nio.file.Paths;

/**
 * The type File utils.
 */
public class FileUtils {

    /**
     * Get a file object from path
     *
     * @param path the path
     * @return the file
     * @throws IOException file doesn't exist
     */
    public static File getFile(String path) throws IOException {
        return getFile(path, false);
    }

    /**
     * Gets file object from path
     *
     * @param path      the path
     * @param createNew whether to create the file if it does not exist
     * @return the file
     * @throws IOException thrown when file does not exist (createNew = false) or file couldn't be created (createNew = true)
     */
    public static File getFile(String path, boolean createNew) throws IOException {
        File file = new File(path);
        if(file.exists()) {
            if(!file.isFile()) throw new IncorrectFileTypeException(path + " is not a file.");
            return file;
        }
        if(!createNew) throw new FileNotFoundException(path);
        if(!file.createNewFile()) throw new FileCreateException(path);
        return file;
    }

    /**
     * Gets file name from path
     *
     * @param path the path
     * @return the file name
     */
    public static String getFileName(String path) {
        return new File(path).getName();
    }

    /**
     * Gets file directory path from file path
     *
     * @param path the path
     * @return the file directory path
     */
    public static String getFileDirectoryPath(String path) {
        return new File(path).getParentFile().getPath();
    }

    /**
     * Gets absolute directory path.
     *
     * @param path the path
     * @return the absolute directory path
     */
    public static String getAbsoluteDirectoryPath(String path) {
        return new File(path).getParentFile().getAbsolutePath();
    }

    /**
     * Gets working directory of program.
     *
     * @return the working directory
     */
    public static String getWorkingDirectory() {
        return Paths.get("").toAbsolutePath().toString();
    }


}
