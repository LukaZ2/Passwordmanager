package me.passwordmanager.file.exception;

import lombok.NoArgsConstructor;

import java.io.IOException;

@NoArgsConstructor
public class IncorrectFileTypeException extends IOException {
    public IncorrectFileTypeException(String message) {
        super(message);
    }
}
