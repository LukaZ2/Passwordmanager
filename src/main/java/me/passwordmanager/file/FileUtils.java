package me.passwordmanager.file;

import me.passwordmanager.file.exception.FileCreateException;
import me.passwordmanager.file.exception.IncorrectFileTypeException;

import java.io.*;
import java.nio.file.Paths;

public class FileUtils {

    public static File getFile(String path) throws IOException {
        return getFile(path, false);
    }
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

    public static String getFileName(String path) {
        return new File(path).getName();
    }

    public static String getFileDirectoryPath(String path) {
        return new File(path).getParentFile().getPath();
    }

    public static String getAbsoluteDirectoryPath(String path) {
        return new File(path).getParentFile().getAbsolutePath();
    }

    public static String getWorkingDirectory() {
        return Paths.get("").toAbsolutePath().toString();
    }


}
