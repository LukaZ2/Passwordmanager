package me.passwordmanager.file.exception;

import java.io.IOException;

/**
 * Thrown when a database fails to load
 */
public class DatabaseLoadException extends IOException {
    /**
     * Instantiates a new Database load exception.
     *
     * @param message the message
     */
    public DatabaseLoadException(String message) {
        super(message);
    }
}
