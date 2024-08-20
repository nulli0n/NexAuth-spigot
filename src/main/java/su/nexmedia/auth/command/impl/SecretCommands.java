package su.nexmedia.auth.command.impl;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import su.nexmedia.auth.AuthPlugin;
import su.nexmedia.auth.Placeholders;
import su.nexmedia.auth.auth.impl.AuthPlayer;
import su.nexmedia.auth.auth.impl.PlayerState;
import su.nexmedia.auth.config.Lang;
import su.nexmedia.auth.config.Perms;
import su.nightexpress.nightcore.command.experimental.CommandContext;
import su.nightexpress.nightcore.command.experimental.RootCommand;
import su.nightexpress.nightcore.command.experimental.argument.ParsedArguments;
import su.nightexpress.nightcore.command.experimental.node.ChainedNode;

public class SecretCommands {

    public static void load(@NotNull AuthPlugin plugin) {
        //ChainedNode root = plugin.getRootNode();

        RootCommand<AuthPlugin, ChainedNode> root = RootCommand.chained(plugin, new String[]{"secret"}, builder -> builder
            .description(Lang.COMMAND_SECRET_DESC)
            .permission(Perms.SECRET)
            .addDirect("add", children -> children
                .description(Lang.COMMAND_SECRET_ADD_DESC)
                .permission(Perms.SECRET)
                .playerOnly()
                .executes((context, arguments) -> add(plugin, context, arguments))
            )
            .addDirect("remove", children -> children
                .description(Lang.COMMAND_SECRET_REMOVE_DESC)
                .permission(Perms.SECRET)
                .playerOnly()
                .executes((context, arguments) -> remove(plugin, context, arguments))
            )
        );

//        root.addChildren(DirectNode.builder(plugin, "add")
//            .description(Lang.COMMAND_SECRET_ADD_DESC)
//            .permission(Perms.SECRET)
//            .playerOnly()
//            .executes((context, arguments) -> add(plugin, context, arguments))
//        );
//
//        root.addChildren(DirectNode.builder(plugin, "remove")
//            .description(Lang.COMMAND_SECRET_REMOVE_DESC)
//            .permission(Perms.SECRET)
//            .playerOnly()
//            .executes((context, arguments) -> remove(plugin, context, arguments))
//        );
    }

    public static boolean add(@NotNull AuthPlugin plugin, @NotNull CommandContext context, @NotNull ParsedArguments arguments) {
        Player player = context.getPlayerOrThrow();

        AuthPlayer authPlayer = plugin.getAuthManager().getPlayer(player);
        if (!authPlayer.isRegistered() || !authPlayer.isLogged()) return false;
        if (authPlayer.isSecretManaging()) return false;

        if (authPlayer.getData().hasSecretKey()) {
            Lang.SECRET_ERROR_ALREADY_SET.getMessage().send(context.getSender());
            return false;
        }

        authPlayer.setState(PlayerState.SECRET_ADD);
        Lang.SECRET_ADD_QUESTION.getMessage().send(player);
        return true;
    }

    public static boolean remove(@NotNull AuthPlugin plugin, @NotNull CommandContext context, @NotNull ParsedArguments arguments) {
        Player player = context.getPlayerOrThrow();

        AuthPlayer authPlayer = plugin.getAuthManager().getPlayer(player);
        if (!authPlayer.isRegistered() || !authPlayer.isLogged()) return false;
        if (authPlayer.isSecretManaging()) return false;

        if (!authPlayer.getData().hasSecretKey()) {
            Lang.SECRET_ERROR_NOT_SET.getMessage().send(context.getSender());
            return false;
        }

        authPlayer.setState(PlayerState.SECRET_REMOVE);
        Lang.SECRET_ANSWER_PROMPT.getMessage()
            .replace(Placeholders.GENERIC_QUESTION, authPlayer.getData().getSecretKey().getQuestion())
            .send(player);
        return true;
    }
}
