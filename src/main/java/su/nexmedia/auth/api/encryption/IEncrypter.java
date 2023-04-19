package su.nexmedia.auth.api.encryption;

import org.jetbrains.annotations.NotNull;

public interface IEncrypter {

    @NotNull String encrypt(@NotNull String password);

    boolean verify(@NotNull String password, @NotNull String hash);
}
