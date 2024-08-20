package su.nexmedia.auth;

import org.jetbrains.annotations.NotNull;
import su.nexmedia.auth.auth.AuthManager;
import su.nexmedia.auth.data.UserManager;
import su.nexmedia.auth.session.SessionManager;

public class AuthAPI {

    private static AuthPlugin plugin;

    static void load(@NotNull AuthPlugin plugin) {
        AuthAPI.plugin = plugin;
    }

    @NotNull
    public static AuthManager getAuthManager() {
        return plugin.getAuthManager();
    }

    @NotNull
    public static SessionManager getSessionManager() {
        return plugin.getSessionManager();
    }

    @NotNull
    public static UserManager getUserManager() {
        return plugin.getUserManager();
    }
}
