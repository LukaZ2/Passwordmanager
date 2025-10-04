package me.passwordmanager.cryptography;

import org.apache.commons.lang3.RandomStringUtils;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

/**
 * Static utility class for cryptographic operations.
 */
public class CryptoUtils {

    /**
     * Specifies the password hashing algorithm.
     */
    public static final String PASSWORD_ALGORITHM = "PBKDF2WithHmacSHA256";
    /**
     * Specifies the password hashing iterations.
     */
    public static final int PASSWORD_HASH_ITERATIONS = 50000;
    /**
     * Specifies the encryption key length.
     */
    public static final int KEY_LENGTH = 256;

    /**
     * Gets secret key from a password.
     *
     * @param password the password string.
     * @param salt     the password salt used for preventing known-hash attacks.
     * @return the AES secret key generated for the password.
     * @throws NoSuchAlgorithmException thrown when PASSWORD_ALGORITHM is an invalid algorithm.
     * @throws InvalidKeySpecException  thrown when KEY_LENGTH or PASSWORD_HASH_ITERATIONS is invalid.
     */
    public static SecretKey getSecretKeyFromPassword(String password, String salt) throws NoSuchAlgorithmException, InvalidKeySpecException {
        var secretKeyFactory = SecretKeyFactory.getInstance(PASSWORD_ALGORITHM);
        var spec = new PBEKeySpec(password.toCharArray(), salt.getBytes(), PASSWORD_HASH_ITERATIONS, KEY_LENGTH);
        return new SecretKeySpec(secretKeyFactory.generateSecret(spec).getEncoded(), "AES");
    }

    /**
     * Generate random password string.
     *
     * @param length length of the generated string.
     * @return the generated string.
     */
    public static String generateRandomPassword(int length) {
        return RandomStringUtils.secureStrong().nextAscii(length);
    }

    /**
     * Generate random password string.
     *
     * @param min the minimum length.
     * @param max the maximum length.
     * @return the generated string.
     */
    public static String generateRandomPassword(int min, int max) {
        return RandomStringUtils.secureStrong().nextAscii(min, max);
    }

}
