package su.nexmedia.auth.command.admin;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import su.nexmedia.auth.NexAuth;
import su.nexmedia.auth.Perms;
import su.nexmedia.auth.config.Lang;
import su.nexmedia.engine.api.command.AbstractCommand;
import su.nexmedia.engine.api.command.CommandResult;
import su.nexmedia.engine.utils.CollectionsUtil;

import java.util.List;

public class UnregisterCommand extends AbstractCommand<NexAuth> {

    public UnregisterCommand(@NotNull NexAuth plugin) {
        super(plugin, new String[]{"unregister"}, Perms.COMMAND_ADMIN);
        this.setDescription(plugin.getMessage(Lang.COMMAND_ADMIN_UNREGISTER_DESC));
        this.setUsage(plugin.getMessage(Lang.COMMAND_ADMIN_UNREGISTER_USAGE));
    }

    @Override
    @NotNull
    public List<String> getTab(@NotNull Player player, int i, @NotNull String[] args) {
        if (i == 1) {
            return CollectionsUtil.playerNames(player);
        }
        return super.getTab(player, i, args);
    }

    @Override
    public void onExecute(@NotNull CommandSender sender, @NotNull CommandResult result) {
        if (result.length() != 2) {
            this.printUsage(sender);
            return;
        }

        this.plugin.getUserManager().getUserDataAsync(result.getArg(1)).thenAccept(user -> {
            if (user == null) {
                this.errorPlayer(sender);
                return;
            }

            // Synchronize
            this.plugin.runTask(task -> {
                Player player = user.getPlayer();
                if (player != null) {
                    player.kickPlayer(plugin.getMessage(Lang.COMMAND_ADMIN_UNREGISTER_KICK).getLocalized());
                }

                plugin.getUserManager().getUsersLoadedMap().remove(user.getId());
                plugin.getData().deleteUser(user.getId());

                plugin.getMessage(Lang.COMMAND_ADMIN_UNREGISTER_DONE)
                    .replace("%player%", user.getName())
                    .send(sender);
            });
        });
    }
}
