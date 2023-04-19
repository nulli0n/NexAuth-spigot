package su.nexmedia.auth;

import org.jetbrains.annotations.NotNull;
import su.nexmedia.auth.auth.AuthManager;
import su.nexmedia.auth.data.UserManager;
import su.nexmedia.auth.session.SessionManager;

public class NexAuthAPI {

    public static final NexAuth PLUGIN = NexAuth.getPlugin(NexAuth.class);

    @NotNull
    public static AuthManager getAuthManager() {
        return PLUGIN.getAuthManager();
    }

    @NotNull
    public static SessionManager getSessionManager() {
        return PLUGIN.getSessionManager();
    }

    @NotNull
    public static UserManager getUserManager() {
        return PLUGIN.getUserManager();
    }
}
