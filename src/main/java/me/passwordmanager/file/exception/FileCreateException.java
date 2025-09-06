package me.passwordmanager.file.exception;

import java.io.IOException;

public class FileCreateException extends IOException {
    public FileCreateException(String message) {
        super(message);
    }
}
