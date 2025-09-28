package me.passwordmanager.file;

import com.google.gson.Gson;
import lombok.Getter;
import me.passwordmanager.MainApplication;
import me.passwordmanager.cryptography.CryptoHandler;
import me.passwordmanager.cryptography.CryptoUtils;
import me.passwordmanager.database.Database;
import me.passwordmanager.file.exception.DatabaseLoadException;
import me.passwordmanager.file.exception.IncorrectFileTypeException;
import me.passwordmanager.file.exception.InvalidPasswordException;

import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.NoSuchPaddingException;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;

public class DatabaseFile extends AtomicFileWrapper {

    public static final int PASSWORD_SALT_LENGTH = 5;
    public static final String VERIFY_TOKEN = "1111111111";

    private String password;
    private String passwordSalt;
    private CryptoHandler cryptoHandler;

    private Database database;

    @Override
    protected void writeContent(OutputStream outputStream) throws IOException {
        try(var writer = new OutputStreamWriter(outputStream)) {
            writer.write(passwordSalt, 0, passwordSalt.length());
        }
        try(var writer = new OutputStreamWriter(cryptoHandler.wrapOutputStream(outputStream))) {
            var gson = new Gson();
            gson.toJson(database, writer);
        }
    }

    @Override
    protected void readContent(InputStream inputStream) throws IOException {
        try(var reader = new InputStreamReader(inputStream)) {
            try {
                passwordSalt = new String(StreamUtils.read(reader, PASSWORD_SALT_LENGTH));
                initCryptoHandler(password, passwordSalt);
            } catch (StreamCorruptedException | NoSuchAlgorithmException | InvalidKeySpecException |
                     NoSuchPaddingException | InvalidKeyException e) {
                throw new DatabaseLoadException("Failed to read password salt.");
            }
        }
        try(var reader = new InputStreamReader(cryptoHandler.wrapInputStream(inputStream))) {
            if(!checkVerifyToken(reader)) throw new InvalidPasswordException();
            var gson = new Gson();
            database = gson.fromJson(reader, Database.class);
        }
    }

    public boolean checkVerifyToken(InputStreamReader reader) throws IOException {
        String token = Arrays.toString(StreamUtils.read(reader, VERIFY_TOKEN.length()));
        return token.equals(VERIFY_TOKEN);
    }

    public DatabaseFile(String filePath) {
        super(filePath);
    }

    @Override
    public void checkFilePath() throws IOException {
        super.checkFilePath();
        if(!getFilePath().endsWith(MainApplication.DATABASE_FILE_EXTENSION)) throw new IncorrectFileTypeException();
    }

    private void initCryptoHandler(String password, String passwordSalt) throws NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, InvalidKeyException {
        cryptoHandler = new CryptoHandler(CryptoUtils.getSecretKeyFromPassword(password, passwordSalt));
    }

    public void open(String password) throws IOException {
        this.password = password;
        super.open();
        this.password = null;
    }
}
