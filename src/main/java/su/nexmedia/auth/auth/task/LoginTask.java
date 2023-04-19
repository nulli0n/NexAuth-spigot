package su.nexmedia.auth.auth.task;

import org.jetbrains.annotations.NotNull;
import su.nexmedia.auth.NexAuth;
import su.nexmedia.auth.auth.impl.AuthPlayer;
import su.nexmedia.auth.config.Config;
import su.nexmedia.auth.config.Lang;
import su.nexmedia.engine.api.server.AbstractTask;

public class LoginTask extends AbstractTask<NexAuth> {

    private int counter;

    public LoginTask(@NotNull NexAuth plugin) {
        super(plugin, 1, false);
        this.counter = 0;
    }

    @Override
    public void action() {
        AuthPlayer.getPlayers().stream().filter(AuthPlayer::isInLogin).forEach(authPlayer -> {
            if (authPlayer.isLoginExpired()) {
                authPlayer.getPlayer().kickPlayer(plugin.getMessage(Lang.LOGIN_ERROR_TIMEOUT).getLocalized());
                return;
            }

            int notifyInterval = Config.LOGIN_NOTIFY_INTERVAL.get();
            if (notifyInterval > 0 && this.counter % notifyInterval == 0) {
                if (authPlayer.isRegistered()) {
                    plugin.getMessage(Lang.LOGIN_NOTIFY_LOG_IN).send(authPlayer.getPlayer());
                }
                else {
                    plugin.getMessage(Lang.LOGIN_NOTIFY_REGISTER).send(authPlayer.getPlayer());
                }
            }
        });

        if (this.counter++ >= Config.LOGIN_NOTIFY_INTERVAL.get()) {
            this.counter = 0;
        }
    }

}
