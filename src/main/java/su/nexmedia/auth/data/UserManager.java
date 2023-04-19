package su.nexmedia.auth.data;

import org.jetbrains.annotations.NotNull;
import su.nexmedia.auth.NexAuth;
import su.nexmedia.auth.data.impl.AuthUser;
import su.nexmedia.engine.api.data.AbstractUserManager;

import java.util.UUID;

public class UserManager extends AbstractUserManager<NexAuth, AuthUser> {
	
	public UserManager(@NotNull NexAuth plugin) {
		super(plugin, plugin);
	}

	@Override
	@NotNull
	protected AuthUser createData(@NotNull UUID uuid, @NotNull String name) {
		return new AuthUser(plugin, uuid, name);
	}
}
