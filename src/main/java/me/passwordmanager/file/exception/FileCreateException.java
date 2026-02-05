package me.passwordmanager.file.exception;

import java.io.IOException;

/**
 * Thrown when a file cannot be created
 */
public class FileCreateException extends IOException {
    /**
     * Instantiates a new File create exception.
     *
     * @param message the message
     */
    public FileCreateException(String message) {
        super(message);
    }
}
