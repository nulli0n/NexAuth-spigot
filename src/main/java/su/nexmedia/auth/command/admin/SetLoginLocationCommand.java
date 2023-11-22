package su.nexmedia.auth.command.admin;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import su.nexmedia.auth.NexAuth;
import su.nexmedia.auth.Perms;
import su.nexmedia.auth.config.Lang;
import su.nexmedia.engine.api.command.AbstractCommand;
import su.nexmedia.engine.api.command.CommandResult;

public class SetLoginLocationCommand extends AbstractCommand<NexAuth> {

    public SetLoginLocationCommand(@NotNull NexAuth plugin) {
        super(plugin, new String[]{"setloginlocation", "setloginloc"}, Perms.COMMAND_ADMIN);
        this.setDescription(plugin.getMessage(Lang.COMMAND_ADMIN_SETLOGINLOCATIOH_DESC));
        this.setUsage(plugin.getMessage(Lang.COMMAND_ADMIN_SETLOGINLOCATION_USAGE));
        this.setPlayerOnly(true);
    }

    @Override
    public void onExecute(@NotNull CommandSender sender, @NotNull CommandResult result) {
        Player player = (Player) sender;
        this.plugin.getAuthManager().setLoginLocation(player.getLocation());
        this.plugin.getMessage(Lang.COMMAND_ADMIN_SETLOGINLOCATION_DONE).send(sender);
    }
}
