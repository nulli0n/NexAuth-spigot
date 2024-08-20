package su.nexmedia.auth.data.impl;

import org.jetbrains.annotations.NotNull;
import su.nexmedia.auth.AuthPlugin;
import su.nexmedia.auth.auth.AuthUtils;
import su.nexmedia.auth.config.Config;
import su.nexmedia.auth.api.encryption.EncryptionType;
import su.nightexpress.nightcore.database.AbstractUser;

import java.util.UUID;

public class AuthUser extends AbstractUser<AuthPlugin> {

    private final SecretKey secretKey;

    private String         registrationIP;
    private String         hashedPassword;
    private EncryptionType encryptionType;

    public AuthUser(@NotNull AuthPlugin plugin, @NotNull UUID uuid, @NotNull String name) {
        this(plugin, uuid, name, System.currentTimeMillis(), System.currentTimeMillis(),
            AuthUtils.IP_LOCAL, "", Config.GENERAL_ENCRYPTION.get(), SecretKey.empty());
    }

    public AuthUser(
        @NotNull AuthPlugin plugin,
        @NotNull UUID uuid,
        @NotNull String name,
        long dateCreated,
        long lastOnline,
        @NotNull String registrationIP,
        @NotNull String hashedPassword,
        @NotNull EncryptionType encryptionType,
        @NotNull SecretKey secretKey
    ) {
        super(plugin, uuid, name, dateCreated, lastOnline);
        this.setRegistrationIP(registrationIP);
        this.hashedPassword = hashedPassword;
        this.encryptionType = encryptionType;
        this.secretKey = secretKey;
    }

    public boolean isRegistered() {
        return !this.getHashedPassword().isEmpty();
    }

    public boolean hasSecretKey() {
        return this.getSecretKey().isSet();
    }

    @NotNull
    public String getRegistrationIP() {
        return this.registrationIP;
    }

    public void setRegistrationIP(@NotNull String registrationIP) {
        this.registrationIP = registrationIP;
    }

    @NotNull
    public String getHashedPassword() {
        return this.hashedPassword;
    }

    public void setPassword(@NotNull String password, @NotNull EncryptionType type) {
        this.hashedPassword = password.isEmpty() ? password : type.getEncrypter().encrypt(password);
        this.encryptionType = type;
    }

    @NotNull
    public EncryptionType getEncryptionType() {
        return this.encryptionType;
    }

    @NotNull
    public SecretKey getSecretKey() {
        return secretKey;
    }


}
