package me.passwordmanager.file.exception;

import java.io.IOException;

public class DatabaseLoadException extends IOException {
    public DatabaseLoadException(String message) {
        super(message);
    }
}
