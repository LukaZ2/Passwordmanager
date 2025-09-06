package me.passwordmanager.cryptography;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

public class CryptoUtils {

    public static final String PASSWORD_ALGORITHM = "PBKDF2WithHmacSHA256";
    public static final int PASSWORD_HASH_ITERATIONS = 50000;
    public static final int KEY_LENGTH = 256;

    public static SecretKey getSecretKeyFromPassword(String password, String salt) throws NoSuchAlgorithmException, InvalidKeySpecException {
        var secretKeyFactory = SecretKeyFactory.getInstance(PASSWORD_ALGORITHM);
        var spec = new PBEKeySpec(password.toCharArray(), salt.getBytes(), PASSWORD_HASH_ITERATIONS, KEY_LENGTH);
        return new SecretKeySpec(secretKeyFactory.generateSecret(spec).getEncoded(), "AES");
    }

}
