package su.nexmedia.auth.auth;

import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.Messenger;
import org.jetbrains.annotations.NotNull;
import su.nexmedia.auth.AuthPlugin;
import su.nexmedia.auth.auth.impl.AuthPlayer;
import su.nexmedia.auth.config.Config;
import su.nightexpress.nightcore.manager.AbstractManager;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;

public class RedirectManager extends AbstractManager<AuthPlugin> {

    private static final String CHANNEL = "BungeeCord";

    private final Messenger messenger;

    public RedirectManager(@NotNull AuthPlugin plugin) {
        super(plugin);
        this.messenger = plugin.getServer().getMessenger();
    }

    @Override
    protected void onLoad() {
        this.messenger.registerOutgoingPluginChannel(this.plugin, CHANNEL);

        this.addTask(plugin.createTask(this::tickRedirect).setSecondsInterval(1));
    }

    @Override
    protected void onShutdown() {
        this.messenger.unregisterOutgoingPluginChannel(this.plugin, CHANNEL);
    }

    private void tickRedirect() {
        this.plugin.getAuthManager().getPlayers().stream().filter(AuthPlayer::isLogged).forEach(authPlayer -> {
            this.connectToBungeeServer(authPlayer.getPlayer(), Config.BUNGEE_TRANSFER_SERVER.get());
        });
    }

    public boolean connectToBungeeServer(@NotNull Player player, @NotNull String server) {
        try {
            if (server.isEmpty()) {
                plugin.info("Could not redirect player to the 'null' server.");
                return false;
            }

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);
            dataOutputStream.writeUTF("Connect");
            dataOutputStream.writeUTF(server);
            player.sendPluginMessage(plugin, CHANNEL, byteArrayOutputStream.toByteArray());
            byteArrayOutputStream.close();
            dataOutputStream.close();
        }
        catch (Exception exception) {
            exception.printStackTrace();
            return false;
        }

        return true;
    }
}
