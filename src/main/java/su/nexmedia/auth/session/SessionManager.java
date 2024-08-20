package su.nexmedia.auth.session;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nexmedia.auth.AuthPlugin;
import su.nightexpress.nightcore.manager.AbstractManager;

import java.util.HashMap;
import java.util.Map;

public class SessionManager extends AbstractManager<AuthPlugin> {

    private final Map<String, AuthSession> sessionMap;

    public SessionManager(@NotNull AuthPlugin plugin) {
        super(plugin);
        this.sessionMap = new HashMap<>();
    }

    @Override
    protected void onLoad() {

    }

    @Override
    protected void onShutdown() {
        this.sessionMap.clear();
    }

    @NotNull
    public Map<String, AuthSession> getSessionMap() {
        this.sessionMap.values().removeIf(AuthSession::mustBeDestroyed);
        return sessionMap;
    }

    /*@Nullable
    public AuthSession getSession(@NotNull Player player) {
        return this.getSession(AuthPlayer.getOrCreate(player).getIP());
    }*/

    @Nullable
    public AuthSession getSession(@NotNull String ip) {
        return this.getSessionMap().get(ip);
    }

    @NotNull
    public AuthSession getOrCreateSession(@NotNull String ip) {
        AuthSession session = this.getSession(ip);
        if (session == null) {
            session = new AuthSession(ip);
            this.getSessionMap().put(session.getAddress(), session);
        }
        return session;
    }
}
