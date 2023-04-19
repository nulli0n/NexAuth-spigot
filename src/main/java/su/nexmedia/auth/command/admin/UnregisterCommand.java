package su.nexmedia.auth.command.admin;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import su.nexmedia.auth.NexAuth;
import su.nexmedia.auth.Perms;
import su.nexmedia.auth.data.impl.AuthUser;
import su.nexmedia.auth.config.Lang;
import su.nexmedia.engine.api.command.AbstractCommand;
import su.nexmedia.engine.utils.CollectionsUtil;

import java.util.List;
import java.util.Map;

public class UnregisterCommand extends AbstractCommand<NexAuth> {

	public UnregisterCommand(@NotNull NexAuth plugin) {
		super(plugin, new String[] {"unregister"}, Perms.COMMAND_ADMIN);
	}

	@Override
	@NotNull
	public String getUsage() {
		return plugin.getMessage(Lang.COMMAND_ADMIN_UNREGISTER_USAGE).getLocalized();
	}

	@Override
	@NotNull
	public String getDescription() {
		return plugin.getMessage(Lang.COMMAND_ADMIN_UNREGISTER_DESC).getLocalized();
	}

	@Override
	public boolean isPlayerOnly() {
		return false;
	}
	
	@Override
	@NotNull
	public List<String> getTab(@NotNull Player player, int i, @NotNull String[] args) {
		if (i == 1) {
			return CollectionsUtil.playerNames(player);
		}
		return super.getTab(player, i, args);
	}

	@Override
	protected void onExecute(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args, @NotNull Map<String, String> flags) {
		if (args.length != 2) {
			this.printUsage(sender);
			return;
		}
		
		String pName = args[1];
		AuthUser user = plugin.getUserManager().getUserData(pName);
		if (user == null) {
			this.errorPlayer(sender);
			return;
		}
		
		Player player = user.getPlayer();
		if (player != null) {
			player.kickPlayer(plugin.getMessage(Lang.COMMAND_ADMIN_UNREGISTER_KICK).getLocalized());
		}
		
		plugin.getUserManager().unloadUser(user.getId());
		plugin.getData().onSave();
		plugin.getData().deleteUser(user.getId());
		
		plugin.getMessage(Lang.COMMAND_ADMIN_UNREGISTER_DONE)
			.replace("%player%", user.getName())
			.send(sender);
	}
}
