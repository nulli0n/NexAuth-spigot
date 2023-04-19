package su.nexmedia.auth.encryption.impl;

import org.jetbrains.annotations.NotNull;
import su.nexmedia.auth.api.encryption.IEncrypter;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

public class DigestEncrypter implements IEncrypter {

    private final String digName;

    public DigestEncrypter(@NotNull String digName) {
        this.digName = digName;
    }

    @Override
    @NotNull
    public String encrypt(@NotNull String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance(this.digName);
            byte[] hash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();

            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }

            return hexString.toString();
        }
        catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public boolean verify(@NotNull String password, @NotNull String hash) {
        return this.encrypt(password).equalsIgnoreCase(hash);
    }

}
