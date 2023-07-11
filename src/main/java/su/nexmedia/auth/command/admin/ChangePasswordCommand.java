package su.nexmedia.auth.command.admin;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import su.nexmedia.auth.NexAuth;
import su.nexmedia.auth.Perms;
import su.nexmedia.auth.Placeholders;
import su.nexmedia.auth.auth.AuthUtils;
import su.nexmedia.auth.config.Lang;
import su.nexmedia.auth.data.impl.AuthUser;
import su.nexmedia.engine.api.command.AbstractCommand;
import su.nexmedia.engine.api.command.CommandResult;
import su.nexmedia.engine.utils.CollectionsUtil;

import java.util.List;

public class ChangePasswordCommand extends AbstractCommand<NexAuth> {

    public ChangePasswordCommand(@NotNull NexAuth plugin) {
        super(plugin, new String[]{"changepassword"}, Perms.COMMAND_ADMIN);
    }

    @Override
    @NotNull
    public String getUsage() {
        return plugin.getMessage(Lang.COMMAND_ADMIN_CHANGEPASSWORD_USAGE).getLocalized();
    }

    @Override
    @NotNull
    public String getDescription() {
        return plugin.getMessage(Lang.COMMAND_ADMIN_CHANGEPASSWORD_DESC).getLocalized();
    }

    @Override
    public boolean isPlayerOnly() {
        return false;
    }

    @Override
    @NotNull
    public List<String> getTab(@NotNull Player player, int arg, @NotNull String[] args) {
        if (arg == 1) {
            return CollectionsUtil.playerNames(player);
        }
        return super.getTab(player, arg, args);
    }

    @Override
    public void onExecute(@NotNull CommandSender sender, @NotNull CommandResult result) {
        if (result.length() < 3) {
            this.printUsage(sender);
            return;
        }

        AuthUser user = plugin.getUserManager().getUserData(result.getArg(1));
        if (user == null) {
            this.errorPlayer(sender);
            return;
        }

        String password = AuthUtils.finePassword(result.getArg(2));
        user.setPassword(password, user.getEncryptionType());

        plugin.getMessage(Lang.COMMAND_ADMIN_CHANGEPASSWORD_DONE)
            .replace(Placeholders.PLAYER_NAME, user.getName())
            .send(sender);
    }
}
