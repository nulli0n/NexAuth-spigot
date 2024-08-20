package su.nexmedia.auth;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;
import org.jetbrains.annotations.NotNull;
import su.nexmedia.auth.auth.AuthManager;
import su.nexmedia.auth.auth.RedirectManager;
import su.nexmedia.auth.command.impl.AdminCommands;
import su.nexmedia.auth.command.impl.SecretCommands;
import su.nexmedia.auth.command.impl.UserCommands;
import su.nexmedia.auth.config.Config;
import su.nexmedia.auth.config.Lang;
import su.nexmedia.auth.config.Perms;
import su.nexmedia.auth.data.DataHandler;
import su.nexmedia.auth.data.UserManager;
import su.nexmedia.auth.data.impl.AuthUser;
import su.nexmedia.auth.session.SessionManager;
import su.nightexpress.nightcore.NightDataPlugin;
import su.nightexpress.nightcore.command.experimental.ImprovedCommands;
import su.nightexpress.nightcore.config.PluginDetails;

public class AuthPlugin extends NightDataPlugin<AuthUser> implements ImprovedCommands {

    private DataHandler dataHandler;
    private UserManager userManager;

    private AuthManager    authManager;
    private RedirectManager redirectManager;
    private SessionManager sessionManager;

    private LogFilter logFilter;

    @Override
    @NotNull
    protected PluginDetails getDefaultDetails() {
        return PluginDetails.create("Auth", new String[]{"auth", "nexauth"})
            .setConfigClass(Config.class)
            .setLangClass(Lang.class)
            .setPermissionsClass(Perms.class)
            ;
    }

    @Override
    public void enable() {
        AuthAPI.load(this);

        this.loadCommands();

        this.dataHandler = new DataHandler(this);
        this.dataHandler.setup();

        this.userManager = new UserManager(this);
        this.userManager.setup();

        this.authManager = new AuthManager(this);
        this.authManager.setup();

        if (Config.BUNGEE_TRANSFER_ENABLED.get()) {
            this.redirectManager = new RedirectManager(this);
            this.redirectManager.setup();
        }

        this.sessionManager = new SessionManager(this);
        this.sessionManager.setup();

        ((Logger) LogManager.getRootLogger()).addFilter(this.logFilter = new LogFilter());
    }

    @Override
    public void disable() {
        if (this.redirectManager != null) {
            this.redirectManager.shutdown();
            this.redirectManager = null;
        }

        if (this.authManager != null) {
            this.authManager.shutdown();
            this.authManager = null;
        }

        if (this.sessionManager != null) {
            this.sessionManager.shutdown();
            this.sessionManager = null;
        }

        this.logFilter.stop();
        this.logFilter = null;
    }

    private void loadCommands() {
        AdminCommands.load(this);
        UserCommands.load(this);
        SecretCommands.load(this);
    }

    @Override
    @NotNull
    public DataHandler getData() {
        return this.dataHandler;
    }

    @NotNull
    @Override
    public UserManager getUserManager() {
        return userManager;
    }

    @NotNull
    public AuthManager getAuthManager() {
        return this.authManager;
    }

    @NotNull
    public RedirectManager getRedirectManager() {
        return redirectManager;
    }

    @NotNull
    public SessionManager getSessionManager() {
        return sessionManager;
    }
}
