package su.nexmedia.auth.auth.impl;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nexmedia.auth.NexAuthAPI;
import su.nexmedia.auth.auth.AuthUtils;
import su.nexmedia.auth.config.Config;
import su.nexmedia.auth.data.impl.AuthUser;
import su.nexmedia.auth.session.AuthSession;

import java.util.*;

public final class AuthPlayer {

    private static final Map<Player, AuthPlayer> PLAYER_MAP = new WeakHashMap<>();

    private final Player player;
    private final String ip;
    private final AuthUser data;
    private final AuthSession session;

    private PlayerState state;
    private long loginExpireDate;

    private String tempPasswordHash;
    private String tempSecretInput;

    @NotNull
    public static AuthPlayer getOrCreate(@NotNull Player player) {
        AuthPlayer authPlayer = PLAYER_MAP.get(player);
        if (authPlayer == null) {
            authPlayer = new AuthPlayer(player, NexAuthAPI.getUserManager().getUserData(player));
            PLAYER_MAP.put(player, authPlayer);
        }
        return authPlayer;
    }

    @NotNull
    public static Set<AuthPlayer> getPlayers() {
        return new HashSet<>(PLAYER_MAP.values());
    }

    public static void remove(@NotNull Player player) {
        PLAYER_MAP.remove(player);
    }

    private AuthPlayer(@NotNull Player player, @NotNull AuthUser data) {
        this.player = player;
        this.ip = AuthUtils.getIp(player);
        this.data = data;
        this.session = NexAuthAPI.getSessionManager().getOrCreateSession(this.getIP());

        this.loginExpireDate = -1;
        this.setState(PlayerState.NONE);
    }

    @NotNull
    public Player getPlayer() {
        return player;
    }

    @NotNull
    public AuthUser getData() {
        return data;
    }

    @NotNull
    public AuthSession getSession() {
        return session;
    }

    @NotNull
    public String getIP() {
        return ip;
    }

    @NotNull
    public PlayerState getState() {
        return this.state;
    }

    public void setState(@NotNull PlayerState state) {
        this.state = state;
    }

    public boolean isRegistered() {
        return this.getData().isRegistered();
    }

    public boolean isLogged() {
        return this.getState() == PlayerState.LOGGED_IN;
    }

    public boolean isInLogin() {
        return this.getState() == PlayerState.IN_LOGIN;
    }

    public boolean isSecretManaging() {
        return this.getState() == PlayerState.SECRET_ADD || this.getState() == PlayerState.SECRET_REMOVE;
    }

    public long getLoginExpireTime() {
        return this.loginExpireDate;
    }

    public void updateLoginExpireTime() {
        if (Config.LOGIN_TIMEOUT.get() <= 0 || this.isLogged()) {
            this.loginExpireDate = -1L;
            return;
        }
        this.loginExpireDate = System.currentTimeMillis() + (Config.LOGIN_TIMEOUT.get() * 1000L);
    }

    public boolean isLoginExpired() {
        if (this.isLogged()) return false;

        return this.getLoginExpireTime() > 0 && System.currentTimeMillis() >= this.getLoginExpireTime();
    }

    public boolean needPasswordConfirm() {
        return !this.isRegistered() && this.isInLogin() && this.getTempPasswordHash() == null;
    }

    public boolean canPasswordConfirm(@NotNull String confirm) {
        return this.getTempPasswordHash() != null && this.getTempPasswordHash().equals(confirm);
    }

    @Nullable
    public String getTempPasswordHash() {
        return tempPasswordHash;
    }

    public void setTempPasswordHash(@Nullable String tempPasswordHash) {
        this.tempPasswordHash = tempPasswordHash;
    }

    @Nullable
    public String getTempSecretInput() {
        return tempSecretInput;
    }

    public void setTempSecretInput(@Nullable String tempSecretInput) {
        this.tempSecretInput = tempSecretInput;
    }
}
