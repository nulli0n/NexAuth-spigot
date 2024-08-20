package su.nexmedia.auth.command.impl;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import su.nexmedia.auth.AuthPlugin;
import su.nexmedia.auth.Placeholders;
import su.nexmedia.auth.auth.AuthUtils;
import su.nexmedia.auth.command.CommandArguments;
import su.nexmedia.auth.config.Config;
import su.nexmedia.auth.config.Lang;
import su.nexmedia.auth.config.Perms;
import su.nightexpress.nightcore.command.experimental.CommandContext;
import su.nightexpress.nightcore.command.experimental.argument.ArgumentTypes;
import su.nightexpress.nightcore.command.experimental.argument.ParsedArguments;
import su.nightexpress.nightcore.command.experimental.impl.ReloadCommand;
import su.nightexpress.nightcore.command.experimental.node.ChainedNode;
import su.nightexpress.nightcore.command.experimental.node.DirectNode;

public class AdminCommands {

    public static void load(@NotNull AuthPlugin plugin) {
        ChainedNode root = plugin.getRootNode();

        ReloadCommand.inject(plugin, root, Perms.COMMAND_ADMIN);

        root.addChildren(DirectNode.builder(plugin, "changepassword")
            .description(Lang.COMMAND_ADMIN_CHANGEPASSWORD_DESC)
            .permission(Perms.COMMAND_ADMIN)
            .withArgument(ArgumentTypes.playerName(CommandArguments.PLAYER).required())
            .withArgument(ArgumentTypes.string(CommandArguments.NEW_PASSWORD).localized(Lang.COMMAND_ARGUMENT_NAME_NEW_PASSWORD).required())
            .executes((context, arguments) -> changePassword(plugin, context, arguments))
        );

        root.addChildren(DirectNode.builder(plugin, "resetsecret")
            .description(Lang.COMMAND_ADMIN_RESETSECRET_DESC)
            .permission(Perms.COMMAND_ADMIN)
            .withArgument(ArgumentTypes.playerName(CommandArguments.PLAYER).required())
            .executes((context, arguments) -> resetSecret(plugin, context, arguments))
        );

        if (Config.isLoginLocationEnabled()) {
            root.addChildren(DirectNode.builder(plugin, "setloginlocation")
                .description(Lang.COMMAND_ADMIN_LOGIN_LOCATION_DESC)
                .permission(Perms.COMMAND_ADMIN)
                .playerOnly()
                .executes((context, arguments) -> setLoginLocation(plugin, context, arguments))
            );
        }

        root.addChildren(DirectNode.builder(plugin, "unregister")
            .description(Lang.COMMAND_ADMIN_UNREGISTER_DESC)
            .permission(Perms.COMMAND_ADMIN)
            .withArgument(ArgumentTypes.playerName(CommandArguments.PLAYER).required())
            .executes((context, arguments) -> unregister(plugin, context, arguments))
        );
    }

    public static boolean changePassword(@NotNull AuthPlugin plugin, @NotNull CommandContext context, @NotNull ParsedArguments arguments) {
        plugin.getUserManager().manageUser(arguments.getStringArgument(CommandArguments.PLAYER), user -> {
            if (user == null) {
                context.errorBadPlayer();
                return;
            }

            String password = AuthUtils.finePassword(CommandArguments.NEW_PASSWORD);
            user.setPassword(password, user.getEncryptionType());
            plugin.getUserManager().scheduleSave(user);

            Lang.COMMAND_ADMIN_CHANGEPASSWORD_DONE.getMessage()
                .replace(Placeholders.PLAYER_NAME, user.getName())
                .send(context.getSender());
        });
        return true;
    }

    public static boolean resetSecret(@NotNull AuthPlugin plugin, @NotNull CommandContext context, @NotNull ParsedArguments arguments) {
        plugin.getUserManager().manageUser(arguments.getStringArgument(CommandArguments.PLAYER), user -> {
            if (user == null) {
                context.errorBadPlayer();
                return;
            }

            user.getSecretKey().reset();
            plugin.getUserManager().scheduleSave(user);

            Lang.COMMAND_ADMIN_RESETSECRET_DONE.getMessage()
                .replace(Placeholders.PLAYER_NAME, user.getName())
                .send(context.getSender());
        });
        return true;
    }

    public static boolean setLoginLocation(@NotNull AuthPlugin plugin, @NotNull CommandContext context, @NotNull ParsedArguments arguments) {
        Player player = context.getPlayerOrThrow();
        plugin.getAuthManager().setLoginLocation(player.getLocation());
        Lang.COMMAND_ADMIN_SETLOGINLOCATION_DONE.getMessage().send(context.getSender());
        return true;
    }

    public static boolean unregister(@NotNull AuthPlugin plugin, @NotNull CommandContext context, @NotNull ParsedArguments arguments) {
        plugin.getUserManager().manageUserSynchronized(arguments.getStringArgument(CommandArguments.PLAYER), user -> {
            if (user == null) {
                context.errorBadPlayer();
                return;
            }

            Player player = user.getPlayer();
            if (player != null) {
                player.kickPlayer(Lang.COMMAND_ADMIN_UNREGISTER_KICK.getLegacy());
            }

            plugin.getUserManager().saveScheduled(); // Save all user data to prevent save of deleted entries
            plugin.getUserManager().getLoadedByIdMap().remove(user.getId());
            plugin.getUserManager().getLoadedByNameMap().remove(user.getName().toLowerCase());
            plugin.getData().deleteUser(user.getId());

            Lang.COMMAND_ADMIN_UNREGISTER_DONE.getMessage()
                .replace(Placeholders.PLAYER_NAME, user.getName())
                .send(context.getSender());
        });
        return true;
    }
}
