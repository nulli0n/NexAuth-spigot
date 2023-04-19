package su.nexmedia.auth.command;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import su.nexmedia.auth.NexAuth;
import su.nexmedia.auth.Perms;
import su.nexmedia.auth.config.Config;
import su.nexmedia.auth.config.Lang;
import su.nexmedia.engine.api.command.GeneralCommand;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class RegisterCommand extends GeneralCommand<NexAuth> {

	public RegisterCommand(@NotNull NexAuth plugin) {
		super(plugin, Config.GENERAL_REGISTER_COMMANDS.get().toArray(new String[0]), Perms.COMMAND_REGISTER);
	}

	@Override
	@NotNull
	public String getUsage() {
		return plugin.getMessage(Lang.COMMAND_REGISTER_USAGE).getLocalized();
	}

	@Override
	@NotNull
	public String getDescription() {
		return plugin.getMessage(Lang.COMMAND_REGISTER_DESC).getLocalized();
	}

	@Override
	public boolean isPlayerOnly() {
		return true;
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
	public void onExecute(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args, @NotNull Map<String, String> flags) {
		if (args.length < 1) {
			this.printUsage(sender);
			return;
		}

		Player player = (Player) sender;
		plugin.getAuthManager().tryRegister(player, args[0]);
	}
}
