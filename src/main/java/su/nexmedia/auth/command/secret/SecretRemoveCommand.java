package su.nexmedia.auth.command.secret;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import su.nexmedia.auth.NexAuth;
import su.nexmedia.auth.Perms;
import su.nexmedia.auth.Placeholders;
import su.nexmedia.auth.auth.impl.AuthPlayer;
import su.nexmedia.auth.auth.impl.PlayerState;
import su.nexmedia.auth.config.Lang;
import su.nexmedia.engine.api.command.AbstractCommand;

import java.util.Map;

public class SecretRemoveCommand extends AbstractCommand<NexAuth> {

    public SecretRemoveCommand(@NotNull NexAuth plugin) {
        super(plugin, new String[]{"remove"}, Perms.SECRET);
    }

    @Override
    @NotNull
    public String getUsage() {
        return plugin.getMessage(Lang.COMMAND_SECRET_REMOVE_USAGE).getLocalized();
    }

    @Override
    @NotNull
    public String getDescription() {
        return plugin.getMessage(Lang.COMMAND_SECRET_REMOVE_DESC).getLocalized();
    }

    @Override
    public boolean isPlayerOnly() {
        return true;
    }

    @Override
    protected void onExecute(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args, @NotNull Map<String, String> flags) {
        Player player = (Player) sender;
        AuthPlayer authPlayer = AuthPlayer.getOrCreate(player);
        if (!authPlayer.isRegistered() || !authPlayer.isLogged()) return;
        if (authPlayer.isSecretManaging()) return;

        if (!authPlayer.getData().hasSecretKey()) {
            plugin.getMessage(Lang.SECRET_ERROR_NOT_SET).send(sender);
            return;
        }

        authPlayer.setState(PlayerState.SECRET_REMOVE);
        plugin.getMessage(Lang.SECRET_ANSWER_PROMPT)
            .replace(Placeholders.GENERIC_QUESTION, authPlayer.getData().getSecretKey().getQuestion())
            .send(player);
    }
}
