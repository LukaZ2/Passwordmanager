package me.passwordmanager.file;

import lombok.Getter;
import me.passwordmanager.file.exception.DatabaseLoadException;

import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import java.io.*;
import java.nio.charset.StandardCharsets;

public class DatabaseFile extends AtomicFileWrapper {

    public static final int PASSWORD_SALT_LENGTH = 5;

    private String passwordSalt;

    @Override
    protected void writeContent(OutputStream outputStream) throws IOException {
        try(var writer = new OutputStreamWriter(outputStream)) {
            writer.write(passwordSalt, 0, passwordSalt.length());
        }
    }

    @Override
    protected void readContent(InputStream inputStream) throws IOException {
        try(var reader = new InputStreamReader(inputStream)) {
            try {
                passwordSalt = new String(StreamUtils.read(reader, PASSWORD_SALT_LENGTH));
            } catch (StreamCorruptedException e) {
                throw new DatabaseLoadException("Failed to read password salt.");
            }
        }
    }

    public DatabaseFile(String filePath) {
        super(filePath);
    }

    public void open() throws IOException {
        super.open();
    }
}
