package su.nexmedia.auth.command.impl;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import su.nexmedia.auth.AuthPlugin;
import su.nexmedia.auth.command.CommandArguments;
import su.nexmedia.auth.config.Config;
import su.nexmedia.auth.config.Lang;
import su.nexmedia.auth.config.Perms;
import su.nightexpress.nightcore.command.experimental.CommandContext;
import su.nightexpress.nightcore.command.experimental.RootCommand;
import su.nightexpress.nightcore.command.experimental.ServerCommand;
import su.nightexpress.nightcore.command.experimental.argument.ArgumentTypes;
import su.nightexpress.nightcore.command.experimental.argument.ParsedArguments;
import su.nightexpress.nightcore.command.experimental.node.ChainedNode;

public class UserCommands {

    private static ServerCommand changeCommand;
    private static ServerCommand loginCommand;
    private static ServerCommand registerCommand;

    public static void load(@NotNull AuthPlugin plugin) {
        ChainedNode root = plugin.getRootNode();

        changeCommand = RootCommand.direct(plugin, new String[]{"changepassword", "changepw"}, builder -> builder
            .description(Lang.COMMAND_CHANGEPASSWORD_DESC)
            .permission(Perms.COMMAND_CHANGEPASSWORD)
            .playerOnly()
            .withArgument(ArgumentTypes.string(CommandArguments.OLD_PASSWORD).localized(Lang.COMMAND_ARGUMENT_NAME_OLD_PASSWORD).required())
            .withArgument(ArgumentTypes.string(CommandArguments.NEW_PASSWORD).localized(Lang.COMMAND_ARGUMENT_NAME_NEW_PASSWORD).required())
            .executes((context, arguments) -> changePassword(plugin, context, arguments))
        );
        plugin.getCommandManager().registerCommand(changeCommand);

        String[] loginAliases = Config.GENERAL_LOGIN_COMMANDS.get().toArray(new String[0]);
        if (loginAliases.length != 0) {
            loginCommand = RootCommand.direct(plugin, loginAliases, builder -> builder
                .description(Lang.COMMAND_LOGIN_DESC)
                .permission(Perms.COMMAND_LOGIN)
                .playerOnly()
                .withArgument(ArgumentTypes.string(CommandArguments.PASSWORD).localized(Lang.COMMAND_ARGUMENT_NAME_PASSWORD).required())
                .executes((context, arguments) -> login(plugin, context, arguments))
            );
            plugin.getCommandManager().registerCommand(loginCommand);
        }

        String[] registerAliases = Config.GENERAL_REGISTER_COMMANDS.get().toArray(new String[0]);
        if (registerAliases.length != 0) {
            registerCommand = RootCommand.direct(plugin, registerAliases, builder -> builder
                .description(Lang.COMMAND_REGISTER_DESC)
                .permission(Perms.COMMAND_REGISTER)
                .playerOnly()
                .withArgument(ArgumentTypes.string(CommandArguments.PASSWORD).localized(Lang.COMMAND_ARGUMENT_NAME_PASSWORD).required())
                .executes((context, arguments) -> register(plugin, context, arguments))
            );
            plugin.getCommandManager().registerCommand(registerCommand);
        }
    }

    public static void unload(@NotNull AuthPlugin plugin) {
        plugin.getCommandManager().unregisterCommand(changeCommand);
        if (loginCommand != null) {
            plugin.getCommandManager().unregisterCommand(loginCommand);
        }
        if (registerCommand != null) {
            plugin.getCommandManager().unregisterCommand(registerCommand);
        }
    }

    public static boolean changePassword(@NotNull AuthPlugin plugin, @NotNull CommandContext context, @NotNull ParsedArguments arguments) {
        Player player = context.getPlayerOrThrow();
        String oldPass = arguments.getStringArgument(CommandArguments.OLD_PASSWORD);
        String newPass = arguments.getStringArgument(CommandArguments.NEW_PASSWORD);
        plugin.getAuthManager().changepassword(player, oldPass, newPass);
        return true;
    }

    public static boolean login(@NotNull AuthPlugin plugin, @NotNull CommandContext context, @NotNull ParsedArguments arguments) {
        Player player = context.getPlayerOrThrow();
        plugin.getAuthManager().tryLogIn(player, arguments.getStringArgument(CommandArguments.PASSWORD));
        return true;
    }

    public static boolean register(@NotNull AuthPlugin plugin, @NotNull CommandContext context, @NotNull ParsedArguments arguments) {
        Player player = context.getPlayerOrThrow();
        plugin.getAuthManager().tryRegister(player, arguments.getStringArgument(CommandArguments.PASSWORD));
        return true;
    }
}
