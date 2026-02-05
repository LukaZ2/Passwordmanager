package me.passwordmanager.cryptography;

import javax.crypto.*;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * Class handling all cryptographic streams.
 */
public class CryptoHandler {

    /**
     * Specifies the stream transformation.
     */
    public static final String TRANSFORMATION = "AES/ECB/PKCS5Padding";

    private final Cipher encryptCipher;
    private final Cipher decryptCipher;

    /**
     * Instantiates a new Crypto handler.
     *
     * @param secretKey the secret key.
     * @throws NoSuchPaddingException   thrown when the padding specified in TRANSFORMATION is invalid.
     * @throws NoSuchAlgorithmException thrown when the algorithm specified in TRANSFORMATION is invalid.
     * @throws InvalidKeyException      thrown when the key is invalid.
     */
    public CryptoHandler(SecretKey secretKey) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException {

        this.encryptCipher = Cipher.getInstance(TRANSFORMATION);
        this.encryptCipher.init(Cipher.ENCRYPT_MODE, secretKey);

        this.decryptCipher = Cipher.getInstance(TRANSFORMATION);
        this.decryptCipher.init(Cipher.DECRYPT_MODE, secretKey);
    }

    /**
     * Wrap an InputStream instance into a CipherInputStream instance.
     *
     * @param inputStream the input stream.
     * @return the cipher input stream.
     */
    public CipherInputStream wrapInputStream(InputStream inputStream) {
        return new CipherInputStream(inputStream, decryptCipher);
    }

    /**
     * Wrap an OutputStream instance into a CipherOutputStream instance.
     *
     * @param outputStream the output stream.
     * @return the cipher output stream.
     */
    public CipherOutputStream wrapOutputStream(OutputStream outputStream) {
        return new CipherOutputStream(outputStream, encryptCipher);
    }

}
