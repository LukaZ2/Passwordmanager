package me.passwordmanager.file.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.IOException;

/**
 * Thrown when an atomic file is corrupted
 */
@AllArgsConstructor
public class AtomicFileCorruptedException extends IOException {
    @Getter
    private final boolean hasBackup;
}
