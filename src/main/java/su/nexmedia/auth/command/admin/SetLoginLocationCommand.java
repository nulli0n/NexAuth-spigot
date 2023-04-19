package su.nexmedia.auth.command.admin;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import su.nexmedia.auth.NexAuth;
import su.nexmedia.auth.Perms;
import su.nexmedia.auth.config.Lang;
import su.nexmedia.engine.api.command.AbstractCommand;

import java.util.Map;

public class SetLoginLocationCommand extends AbstractCommand<NexAuth> {

    public SetLoginLocationCommand(@NotNull NexAuth plugin) {
        super(plugin, new String[] {"setloginlocation", "setloginloc"}, Perms.COMMAND_ADMIN);
    }

    @Override
    @NotNull
    public String getUsage() {
        return plugin.getMessage(Lang.COMMAND_ADMIN_SETLOGINLOCATION_USAGE).getLocalized();
    }

    @Override
    @NotNull
    public String getDescription() {
        return plugin.getMessage(Lang.COMMAND_ADMIN_SETLOGINLOCATIOH_DESC).getLocalized();
    }

    @Override
    public boolean isPlayerOnly() {
        return true;
    }

    @Override
    protected void onExecute(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args, @NotNull Map<String, String> flags) {
        Player player = (Player) sender;
        this.plugin.getAuthManager().setLoginLocation(player.getLocation());
        this.plugin.getMessage(Lang.COMMAND_ADMIN_SETLOGINLOCATION_DONE).send(sender);
    }
}
