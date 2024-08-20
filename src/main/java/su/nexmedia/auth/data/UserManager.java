package su.nexmedia.auth.data;

import org.jetbrains.annotations.NotNull;
import su.nexmedia.auth.AuthPlugin;
import su.nexmedia.auth.data.impl.AuthUser;
import su.nightexpress.nightcore.database.AbstractUserManager;

import java.util.UUID;

public class UserManager extends AbstractUserManager<AuthPlugin, AuthUser> {

    public UserManager(@NotNull AuthPlugin plugin) {
        super(plugin);
    }

    @Override
    @NotNull
    public AuthUser createUserData(@NotNull UUID uuid, @NotNull String name) {
        return new AuthUser(plugin, uuid, name);
    }
}
