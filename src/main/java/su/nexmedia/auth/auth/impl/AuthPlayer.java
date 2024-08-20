package su.nexmedia.auth.auth.impl;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nexmedia.auth.config.Config;
import su.nexmedia.auth.data.impl.AuthUser;
import su.nexmedia.auth.session.AuthSession;

public final class AuthPlayer {

    private final Player   player;
    private final String   inetAddress;
    private final AuthUser data;
    private final AuthSession session;

    private PlayerState state;
    private long        loginExpireDate;

    private String tempPasswordHash;
    private String tempSecretInput;

    public AuthPlayer(@NotNull Player player, @NotNull String inetAddress, @NotNull AuthUser data, @NotNull AuthSession session) {
        this.player = player;
        this.inetAddress = inetAddress;
        this.data = data;
        this.session = session;

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
    public String getInetAddress() {
        return inetAddress;
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
