package su.nexmedia.auth.auth.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.jetbrains.annotations.NotNull;
import su.nexmedia.auth.AuthPlugin;
import su.nexmedia.auth.Placeholders;
import su.nexmedia.auth.auth.AuthManager;
import su.nexmedia.auth.auth.AuthUtils;
import su.nexmedia.auth.auth.impl.AuthPlayer;
import su.nexmedia.auth.auth.impl.PlayerSnapshot;
import su.nexmedia.auth.config.Config;
import su.nexmedia.auth.config.Lang;
import su.nexmedia.auth.session.AuthSession;
import su.nightexpress.nightcore.manager.AbstractListener;
import su.nightexpress.nightcore.util.TimeUtil;

public class LoginListener extends AbstractListener<AuthPlugin> {

    private final AuthManager authManager;

    public LoginListener(@NotNull AuthPlugin plugin, @NotNull AuthManager authManager) {
        super(plugin);
        this.authManager = authManager;
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onAuthPreLogin(AsyncPlayerPreLoginEvent event) {
        String address = AuthUtils.getRawAddress(event.getAddress());
        AuthSession session = plugin.getSessionManager().getSession(address);
        if (session != null && session.isBanned()) {
            String time = TimeUtil.formatDuration(session.getBannedUntil());
            event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_BANNED, String.join("\n", Config.GENERAL_BAN_MESSAGE.get()).replace(Placeholders.GENERIC_TIME, time));
            return;
        }

        if (this.authManager.isBadCountry(event.getAddress())) {
            event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, Lang.JOIN_ERROR_BAD_COUNTRY.getLegacy());
            return;
        }

        String userName = event.getName();

        if (userName.length() < Config.SECURITY_NAME_MIN_LENGTH.get()) {
            String msg = Lang.JOIN_ERROR_NAME_SHORT.getLegacy().replace(Placeholders.GENERIC_AMOUNT, String.valueOf(Config.SECURITY_NAME_MIN_LENGTH.get()));
            event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, msg);
            return;
        }

        if (userName.length() > Config.SECURITY_NAME_MAX_LENGTH.get()) {
            String msg = Lang.JOIN_ERROR_NAME_LONG.getLegacy().replace(Placeholders.GENERIC_AMOUNT, String.valueOf(Config.SECURITY_NAME_MAX_LENGTH.get()));
            event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, msg);
            return;
        }

        if (!userName.matches(Config.SECURITY_NAME_REGEX.get())) {
            String msg = Lang.JOIN_ERROR_NAME_INVALID_CHARS.getLegacy().replace(Placeholders.GENERIC_PATTERN, Config.SECURITY_NAME_REGEX.get());
            event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, msg);
            return;
        }

        if (Config.SECURITY_CHECK_NAME_CASE.get()) {
            String realName = plugin.getData().getRealName(userName);
            if (realName != null && !realName.equals(userName)) {
                String msg = Lang.JOIN_ERROR_NAME_INVALID_CASE.getLegacy().replace(Placeholders.GENERIC_NAME, realName);
                event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, msg);
                return;
            }
        }

        Player duplicate = plugin.getServer().getPlayerExact(userName);
        if (duplicate != null) {
            String msg = Lang.JOIN_ERROR_ALREADY_LOGGED.getLegacy();
            event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, msg);
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onAuthJoin(PlayerJoinEvent event) {
        this.authManager.onLogin(event.getPlayer());
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onAuthQuit(PlayerQuitEvent event) {
        PlayerSnapshot.restore(event.getPlayer());
        this.authManager.removePlayer(event.getPlayer());
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onLoginChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        AuthPlayer authPlayer = this.authManager.getPlayer(player);
        if (!authPlayer.isInLogin() && !authPlayer.isSecretManaging()) return;

        String input = event.getMessage();
        if (input.isEmpty()) return;

        event.getRecipients().clear();
        event.setCancelled(true);
        event.setMessage("");

        if (!Config.GENERAL_USE_CHAT.get()) return;

        this.plugin.runTask(task -> {
            if (authPlayer.isInLogin()) {
                if (authPlayer.isRegistered()) {
                    this.authManager.tryLogIn(player, input);
                }
                else {
                    this.authManager.tryRegister(player, input);
                }
            }
            else if (authPlayer.isSecretManaging()) {
                this.authManager.changeSecretKey(player, input);
            }
        });
    }
}
