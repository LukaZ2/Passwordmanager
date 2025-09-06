package me.passwordmanager.file.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.IOException;

@AllArgsConstructor
public class AtomicFileCorruptedException extends IOException {
    @Getter
    private final boolean hasBackup;
}
