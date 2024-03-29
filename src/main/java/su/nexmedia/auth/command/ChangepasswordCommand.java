package su.nexmedia.auth.command;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import su.nexmedia.auth.NexAuth;
import su.nexmedia.auth.Perms;
import su.nexmedia.auth.config.Lang;
import su.nexmedia.engine.api.command.CommandResult;
import su.nexmedia.engine.api.command.GeneralCommand;

public class ChangepasswordCommand extends GeneralCommand<NexAuth> {

    public ChangepasswordCommand(@NotNull NexAuth plugin) {
        super(plugin, new String[]{"changepassword", "changepw"}, Perms.COMMAND_CHANGEPASSWORD);
        this.setDescription(plugin.getMessage(Lang.COMMAND_CHANGEPASSWORD_DESC));
        this.setUsage(plugin.getMessage(Lang.COMMAND_CHANGEPASSWORD_USAGE));
        this.setPlayerOnly(true);
    }

    @Override
    public void onExecute(@NotNull CommandSender sender, @NotNull CommandResult result) {
        if (result.length() < 2) {
            this.printUsage(sender);
            return;
        }

        Player player = (Player) sender;
        String oldPass = result.getArg(0);
        String newPass = result.getArg(1);
        plugin.getAuthManager().changepassword(player, oldPass, newPass);
    }
}
