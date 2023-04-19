package su.nexmedia.auth;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;
import org.jetbrains.annotations.NotNull;
import su.nexmedia.auth.auth.AuthManager;
import su.nexmedia.auth.command.*;
import su.nexmedia.auth.command.admin.ChangePasswordCommand;
import su.nexmedia.auth.command.admin.ResetSecretCommand;
import su.nexmedia.auth.command.admin.SetLoginLocationCommand;
import su.nexmedia.auth.command.admin.UnregisterCommand;
import su.nexmedia.auth.command.secret.SecretCommand;
import su.nexmedia.auth.config.Config;
import su.nexmedia.auth.data.DataHandler;
import su.nexmedia.auth.data.UserManager;
import su.nexmedia.auth.data.impl.AuthUser;
import su.nexmedia.auth.config.Lang;
import su.nexmedia.auth.session.SessionManager;
import su.nexmedia.engine.NexPlugin;
import su.nexmedia.engine.api.command.GeneralCommand;
import su.nexmedia.engine.api.data.UserDataHolder;
import su.nexmedia.engine.command.list.ReloadSubCommand;

public class NexAuth extends NexPlugin<NexAuth> implements UserDataHolder<NexAuth, AuthUser> {

    private DataHandler dataHandler;
    private UserManager userManager;

    private AuthManager    authManager;
    private SessionManager sessionManager;

    private LogFilter logFilter;

    @Override
    @NotNull
    protected NexAuth getSelf() {
        return this;
    }

    @Override
    public void enable() {
        this.authManager = new AuthManager(this);
        this.authManager.setup();

        this.sessionManager = new SessionManager(this);
        this.sessionManager.setup();

        ((Logger) LogManager.getRootLogger()).addFilter(this.logFilter = new LogFilter());
    }

    @Override
    public void disable() {
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

    @Override
    public void loadConfig() {
        this.getConfig().initializeOptions(Config.class);
    }

    @Override
    public void loadLang() {
        this.getLangManager().loadMissing(Lang.class);
    }

    @Override
    public boolean setupDataHandlers() {
        this.dataHandler = DataHandler.getInstance(this);
        this.dataHandler.setup();

        this.userManager = new UserManager(this);
        this.userManager.setup();

        return true;
    }

    @Override
    public void registerHooks() {

    }

    @Override
    public void registerCommands(@NotNull GeneralCommand<NexAuth> mainCommand) {
        if (!Config.GENERAL_LOGIN_COMMANDS.get().isEmpty()) {
            this.getCommandManager().registerCommand(new LoginCommand(this));
        }
        if (!Config.GENERAL_REGISTER_COMMANDS.get().isEmpty()) {
            this.getCommandManager().registerCommand(new RegisterCommand(this));
        }
        this.getCommandManager().registerCommand(new ChangepasswordCommand(this));
        this.getCommandManager().registerCommand(new SecretCommand(this));

        mainCommand.addChildren(new ReloadSubCommand<>(this, Perms.COMMAND_ADMIN));
        if (Config.LOGIN_LOCATION_ENABLED.get()) {
            mainCommand.addChildren(new SetLoginLocationCommand(this));
        }
        mainCommand.addChildren(new ChangePasswordCommand(this));
        mainCommand.addChildren(new UnregisterCommand(this));
        mainCommand.addChildren(new ResetSecretCommand(this));
    }

    @Override
    public void registerPermissions() {
        this.registerPermissions(Perms.class);
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
    public SessionManager getSessionManager() {
        return sessionManager;
    }
}
