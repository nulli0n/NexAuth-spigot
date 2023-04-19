package su.nexmedia.auth.auth.task;

import org.jetbrains.annotations.NotNull;
import su.nexmedia.auth.NexAuth;
import su.nexmedia.auth.auth.impl.AuthPlayer;
import su.nexmedia.auth.config.Config;
import su.nexmedia.engine.api.server.AbstractTask;

public class BungeeRedirectTask extends AbstractTask<NexAuth> {

    public BungeeRedirectTask(@NotNull NexAuth plugin) {
        super(plugin, 1, false);
    }

    @Override
    public void action() {
        AuthPlayer.getPlayers().stream().filter(AuthPlayer::isLogged).forEach(authPlayer -> {
            plugin.getAuthManager().connectToBungeeServer(authPlayer.getPlayer(), Config.BUNGEE_TRANSFER_SERVER.get());
        });
    }
}
