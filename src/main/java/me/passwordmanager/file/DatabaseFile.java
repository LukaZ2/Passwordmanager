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


    /**
     * Create new database database file.
     *
     * @param directory the creation directory
     * @param name      the filename
     * @param password  the password
     * @return the database file
     * @throws IOException              thrown when database cannot be created
     * @throws NoSuchPaddingException   thrown when padding is invalid, normally not thrown
     * @throws NoSuchAlgorithmException thrown when encryption algorithm is invalid, normally not thrown
     * @throws InvalidKeySpecException  thrown when key specification is invalid, normally not thrown
     * @throws InvalidKeyException      thrown when encryption key is invalid, normally not thrown
     */
    public static DatabaseFile createNewDatabase(String directory, String name, String password) throws IOException, NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeySpecException, InvalidKeyException {
        if(directory.isBlank()) directory = FileUtils.getWorkingDirectory();
        if(!directory.isEmpty() && !directory.endsWith("/")) directory += "/";
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

    //Write unencrypted password salt and encrypted data to file
    @Override
    protected void writeContent(OutputStream outputStream) throws IOException {
        //Write password salt first
        outputStream.write(passwordSalt.getBytes(StandardCharsets.UTF_8), 0, passwordSalt.length());
        outputStream.flush();
        //Wrap stream in CipherStream for encryption
        try(var encryptedStream = cryptoHandler.wrapOutputStream(outputStream)) {
            //Write verify token to CipherStream
            encryptedStream.write(VERIFY_TOKEN.getBytes(StandardCharsets.UTF_8));
            //Convert database object to JSON and write to CipherStream
            var gson = new Gson();
            encryptedStream.write(gson.toJson(database).trim().getBytes(StandardCharsets.UTF_8));
        }
    }

    //Read password salt and decrypt data to memory
    @Override
    protected void readContentWrapped(InputStream inputStream) throws IOException {
        //Read unencrypted password salt
        try {
            passwordSalt = new String(StreamUtils.read(inputStream, PASSWORD_SALT_LENGTH), StandardCharsets.UTF_8);
            initCryptoHandler(password, passwordSalt);
        } catch (StreamCorruptedException | NoSuchAlgorithmException | InvalidKeySpecException |
                 NoSuchPaddingException | InvalidKeyException e) {
            throw new DatabaseLoadException("Failed to read password salt.");
        }

        //Wrap stream in CipherStream for decryption
        try(var encryptedStream = cryptoHandler.wrapInputStream(inputStream)) {
            //Read verify token from CipherStream to check password validity
            if(!checkVerifyToken(encryptedStream)) throw new InvalidPasswordException();
            //Parse JSON data from CipherStream and store in database field
            var gson = new Gson();
            var data = StreamUtils.readAll(encryptedStream);
            database = gson.fromJson(data.trim(), Database.class);
        }
    }

    /**
     * Check if the very token is correct (Password validity)
     *
     * @param stream the stream
     * @return if token is valid
     * @throws IOException thrown when data cannot be read
     */
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

    //Throw exception if file path is incorrect
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

    //Initialize new file after creation
    private void initNew(String password) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeySpecException, InvalidKeyException, IOException {
        database = new Database();
        passwordSalt = CryptoUtils.generateRandomPassword(PASSWORD_SALT_LENGTH);
        initCryptoHandler(password, passwordSalt);
        createNew();
        open();
        save();
    }
}
