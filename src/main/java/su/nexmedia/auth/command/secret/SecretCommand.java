package su.nexmedia.auth.command.secret;

import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import su.nexmedia.auth.NexAuth;
import su.nexmedia.auth.Perms;
import su.nexmedia.auth.config.Lang;
import su.nexmedia.engine.api.command.CommandResult;
import su.nexmedia.engine.api.command.GeneralCommand;
import su.nexmedia.engine.command.list.HelpSubCommand;

public class SecretCommand extends GeneralCommand<NexAuth> {

    public SecretCommand(@NotNull NexAuth plugin) {
        super(plugin, new String[]{"secret"}, Perms.SECRET);
        this.setDescription(plugin.getMessage(Lang.COMMAND_SECRET_DESC));
        this.setUsage(plugin.getMessage(Lang.COMMAND_SECRET_USAGE));

        this.addDefaultCommand(new HelpSubCommand<>(plugin));
        this.addChildren(new SecretAddCommand(plugin));
        this.addChildren(new SecretRemoveCommand(plugin));
    }

    @Override
    public void onExecute(@NotNull CommandSender sender, @NotNull CommandResult result) {

    }
}
