package su.nexmedia.auth.session;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nexmedia.auth.auth.AuthUtils;
import su.nexmedia.auth.auth.impl.AuthPlayer;
import su.nexmedia.auth.config.Config;

import java.util.UUID;

public class AuthSession {

    private final String ip;
    private final long   destroyDate;

    private int  failedAttempts;
    private UUID authorizedId;
    private long authorizationTimeoutDate;
    private long bannedUntil;

    public AuthSession(@NotNull String ip) {
        this.ip = ip;
        this.destroyDate = System.currentTimeMillis() + Config.SECURITY_SESSION_TIMEOUT.get() * 1000L;

        this.setFailedAttempts(0);
        this.unauthorize();
    }

    public boolean isAuthorized(@NotNull UUID id) {
        return !this.isAuthorizationExpired() && this.getAuthorizedId() != null && this.getAuthorizedId().equals(id);
    }

    public boolean isAuthorizationExpired() {
        if (this.mustBeDestroyed()) return true;

        return this.getAuthorizationTimeoutDate() >= 0 && System.currentTimeMillis() > this.getAuthorizationTimeoutDate();
    }

    public boolean mustBeDestroyed() {
        if (!AuthUtils.isPublicIP(this.getIP())) return true;
        if (this.isBanned()) return false;

        return System.currentTimeMillis() > this.destroyDate;
    }

    public boolean isLoginAttemptsWasted() {
        return Config.LOGIN_MAX_ATTEMPTS.get() > 0 && this.getFailedAttempts() >= Config.LOGIN_MAX_ATTEMPTS.get();
    }

    public boolean isBanned() {
        return this.getBannedUntil() < 0L || System.currentTimeMillis() <= this.getBannedUntil();
    }

    public void addFailedLoginAttempt() {
        this.setFailedAttempts(this.getFailedAttempts() + 1);
    }

    public void unauthorize() {
        this.setAuthorizedId(null);
        this.setAuthorizationTimeoutDate(0L);
    }

    public void ban(long seconds) {
        if (!AuthUtils.isPublicIP(this.getIP())) return;
        if (seconds == 0L) return;

        this.unauthorize();
        this.setBannedUntil(System.currentTimeMillis() + seconds * 1000L);
        this.setFailedAttempts(0);
    }

    public void authorize(@NotNull AuthPlayer authPlayer) {
        if (!Config.SECURITY_SESSION_ENABLED.get() || Config.SECURITY_SESSION_AUTH_TIMEOUT.get() <= 0) return;
        if (!authPlayer.isLogged()) return;
        if (!AuthUtils.isPublicIP(this.getIP())) return;

        this.setAuthorizedId(authPlayer.getData().getId());
        this.setAuthorizationTimeoutDate(System.currentTimeMillis() + Config.SECURITY_SESSION_AUTH_TIMEOUT.get() * 1000L);
        this.setFailedAttempts(0);
    }

    @NotNull
    public String getIP() {
        return ip;
    }

    public int getFailedAttempts() {
        return failedAttempts;
    }

    public void setFailedAttempts(int failedAttempts) {
        this.failedAttempts = Math.max(0, failedAttempts);
    }

    @Nullable
    public UUID getAuthorizedId() {
        return authorizedId;
    }

    public void setAuthorizedId(@Nullable UUID authorizedId) {
        this.authorizedId = authorizedId;
    }

    public long getAuthorizationTimeoutDate() {
        return authorizationTimeoutDate;
    }

    public void setAuthorizationTimeoutDate(long authorizationTimeoutDate) {
        this.authorizationTimeoutDate = authorizationTimeoutDate;
    }

    public long getBannedUntil() {
        return bannedUntil;
    }

    public void setBannedUntil(long bannedUntil) {
        this.bannedUntil = bannedUntil;
    }
}
