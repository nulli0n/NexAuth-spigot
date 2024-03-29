package su.nexmedia.auth.command;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import su.nexmedia.auth.NexAuth;
import su.nexmedia.auth.Perms;
import su.nexmedia.auth.config.Config;
import su.nexmedia.auth.config.Lang;
import su.nexmedia.engine.api.command.CommandResult;
import su.nexmedia.engine.api.command.GeneralCommand;

import java.util.Collections;
import java.util.List;

public class RegisterCommand extends GeneralCommand<NexAuth> {

    public RegisterCommand(@NotNull NexAuth plugin) {
        super(plugin, Config.GENERAL_REGISTER_COMMANDS.get().toArray(new String[0]), Perms.COMMAND_REGISTER);
        this.setDescription(plugin.getMessage(Lang.COMMAND_REGISTER_DESC));
        this.setUsage(plugin.getMessage(Lang.COMMAND_REGISTER_USAGE));
        this.setPlayerOnly(true);
    }

    @Override
    @NotNull
    public List<String> getTab(@NotNull Player player, int arg, @NotNull String[] args) {
        if (arg == 1) {
            return Collections.singletonList(this.getUsage());
        }
        return super.getTab(player, arg, args);
    }

    @Override
    public void onExecute(@NotNull CommandSender sender, @NotNull CommandResult result) {
        if (result.length() < 1) {
            this.printUsage(sender);
            return;
        }

        Player player = (Player) sender;
        plugin.getAuthManager().tryRegister(player, result.getArg(0));
    }
}
