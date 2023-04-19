package su.nexmedia.auth.command;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import su.nexmedia.auth.NexAuth;
import su.nexmedia.auth.Perms;
import su.nexmedia.auth.config.Lang;
import su.nexmedia.engine.api.command.GeneralCommand;

import java.util.Map;

public class ChangepasswordCommand extends GeneralCommand<NexAuth> {

    public ChangepasswordCommand(@NotNull NexAuth plugin) {
        super(plugin, new String[]{"changepassword", "changepw"}, Perms.COMMAND_CHANGEPASSWORD);
    }

    @Override
    @NotNull
    public String getUsage() {
        return plugin.getMessage(Lang.COMMAND_CHANGEPASSWORD_USAGE).getLocalized();
    }

    @Override
    @NotNull
    public String getDescription() {
        return plugin.getMessage(Lang.COMMAND_CHANGEPASSWORD_DESC).getLocalized();
    }

    @Override
    public boolean isPlayerOnly() {
        return true;
    }

    @Override
    protected void onExecute(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args, @NotNull Map<String, String> flags) {
        if (args.length != 2) {
            this.printUsage(sender);
            return;
        }

        Player player = (Player) sender;
        String oldPass = args[0];
        String newPass = args[1];
        plugin.getAuthManager().changepassword(player, oldPass, newPass);
    }
}
