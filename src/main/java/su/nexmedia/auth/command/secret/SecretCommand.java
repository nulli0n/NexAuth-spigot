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
        this.addDefaultCommand(new HelpSubCommand<>(plugin));
        this.addChildren(new SecretAddCommand(plugin));
        this.addChildren(new SecretRemoveCommand(plugin));
    }

    @Override
    @NotNull
    public String getUsage() {
        return plugin.getMessage(Lang.COMMAND_SECRET_USAGE).getLocalized();
    }

    @Override
    @NotNull
    public String getDescription() {
        return plugin.getMessage(Lang.COMMAND_SECRET_DESC).getLocalized();
    }

    @Override
    public boolean isPlayerOnly() {
        return false;
    }

    @Override
    public void onExecute(@NotNull CommandSender sender, @NotNull CommandResult result) {

    }
}
