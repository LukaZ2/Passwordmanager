package me.passwordmanager.file.exception;

import lombok.NoArgsConstructor;

import java.io.IOException;

/**
 * Thrown when there is an invalid file type (Eg. folder instead of file)
 */
@NoArgsConstructor
public class IncorrectFileTypeException extends IOException {
    /**
     * Instantiates a new Incorrect file type exception.
     *
     * @param message the message
     */
    public IncorrectFileTypeException(String message) {
        super(message);
    }
}
