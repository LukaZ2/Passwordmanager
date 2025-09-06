package me.passwordmanager.cryptography;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.crypto.*;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class CryptoHandler {

    public static final String TRANSFORMATION = "AES/ECB/PKCS5Padding";

    private final Cipher encryptCipher;
    private final Cipher decryptCipher;
    private final SecretKey secretKey;

    public CryptoHandler(SecretKey secretKey) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException {
        this.secretKey = secretKey;

        this.encryptCipher = Cipher.getInstance(TRANSFORMATION);
        this.encryptCipher.init(Cipher.ENCRYPT_MODE, secretKey);

        this.decryptCipher = Cipher.getInstance(TRANSFORMATION);
        this.decryptCipher.init(Cipher.DECRYPT_MODE, secretKey);
    }

    public CipherInputStream wrapInputStream(InputStream inputStream) {
        return new CipherInputStream(inputStream, decryptCipher);
    }
    public CipherOutputStream wrapOutputStream(OutputStream outputStream) {
        return new CipherOutputStream(outputStream, encryptCipher);
    }

}
