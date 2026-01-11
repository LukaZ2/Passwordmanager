package me.passwordmanager.file;

import com.google.gson.Gson;
import lombok.Getter;
import me.passwordmanager.MainApplication;
import me.passwordmanager.cryptography.CryptoHandler;
import me.passwordmanager.cryptography.CryptoUtils;
import me.passwordmanager.database.Database;
import me.passwordmanager.file.exception.DatabaseLoadException;
import me.passwordmanager.file.exception.FileCreateException;
import me.passwordmanager.file.exception.IncorrectFileTypeException;
import me.passwordmanager.file.exception.InvalidPasswordException;

import javax.crypto.CipherInputStream;
import javax.crypto.NoSuchPaddingException;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

/**
 * The type Database file to be serialized as JSON.
 */
public class DatabaseFile extends AtomicFileWrapper {

    public static DatabaseFile createNewDatabase(String directory, String name, String password) throws IOException, NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeySpecException, InvalidKeyException {
        if(!directory.endsWith("/")) directory += "/";
        var filePath = directory + name + "." + MainApplication.DATABASE_FILE_EXTENSION;
        if(new File(filePath).exists()) throw new FileCreateException(String.format("File \"%s\" already exists.", filePath));
        var databaseFile = new DatabaseFile(filePath);
        databaseFile.initNew(password);
        return databaseFile;
    }

    /**
     * Specifies password salt string length.
     */
    public static final int PASSWORD_SALT_LENGTH = 5;
    /**
     * Specifies the verify token string, which is used to check password validity.
     */
    public static final String VERIFY_TOKEN = "1111111111";

    private String password;
    private String passwordSalt;
    private CryptoHandler cryptoHandler;

    @Getter
    private Database database;

    @Override
    protected void writeContent(OutputStream outputStream) throws IOException {
        outputStream.write(passwordSalt.getBytes(StandardCharsets.UTF_8), 0, passwordSalt.length());
        outputStream.flush();
        try(var encryptedStream = cryptoHandler.wrapOutputStream(outputStream)) {
            encryptedStream.write(VERIFY_TOKEN.getBytes(StandardCharsets.UTF_8));
            var gson = new Gson();
            encryptedStream.write(gson.toJson(database).trim().getBytes(StandardCharsets.UTF_8));
        }
    }

    @Override
    protected void readContentWrapped(InputStream inputStream) throws IOException {
        try {
            passwordSalt = new String(StreamUtils.read(inputStream, PASSWORD_SALT_LENGTH), StandardCharsets.UTF_8);
            initCryptoHandler(password, passwordSalt);
        } catch (StreamCorruptedException | NoSuchAlgorithmException | InvalidKeySpecException |
                 NoSuchPaddingException | InvalidKeyException e) {
            throw new DatabaseLoadException("Failed to read password salt.");
        }

        try(var encryptedStream = cryptoHandler.wrapInputStream(inputStream)) {
            if(!checkVerifyToken(encryptedStream)) throw new InvalidPasswordException();
            var gson = new Gson();
            var data = StreamUtils.readAll(encryptedStream);
            database = gson.fromJson(data.trim(), Database.class);
        }
    }

    public boolean checkVerifyToken(CipherInputStream stream) throws IOException {
        var token = new String(StreamUtils.read(stream, VERIFY_TOKEN.length()), StandardCharsets.UTF_8);
        return token.equals(VERIFY_TOKEN);
    }

    /**
     * Instantiates a new Database file.
     *
     * @param filePath the file path of the database.
     */
    public DatabaseFile(String filePath) {
        super(filePath);
        var file = new File(filePath);
        if(file.exists()) return;
        database = new Database();
    }

    @Override
    public void checkFilePath() throws FileNotFoundException, IncorrectFileTypeException {
        super.checkFilePath();
        if(!getFilePath().endsWith(MainApplication.DATABASE_FILE_EXTENSION)) throw new IncorrectFileTypeException();
    }

    private void initCryptoHandler(String password, String passwordSalt) throws NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, InvalidKeyException {
        cryptoHandler = new CryptoHandler(CryptoUtils.getSecretKeyFromPassword(password, passwordSalt));
    }

    /**
     * Open the database file using the specified password.
     *
     * @param password the password string.
     * @throws IOException thrown when the file cannot be opened.
     */
    public void open(String password) throws IOException {
        this.password = password;
        super.open();
        super.readContentWrapped();
        this.password = null;
    }

    private void initNew(String password) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeySpecException, InvalidKeyException, IOException {
        database = new Database();
        passwordSalt = CryptoUtils.generateRandomPassword(PASSWORD_SALT_LENGTH);
        initCryptoHandler(password, passwordSalt);
        createNew();
        open();
        save();
    }
}
