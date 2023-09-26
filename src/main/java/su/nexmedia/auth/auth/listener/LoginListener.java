package su.nexmedia.auth.auth.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.jetbrains.annotations.NotNull;
import su.nexmedia.auth.NexAuth;
import su.nexmedia.auth.Placeholders;
import su.nexmedia.auth.auth.AuthManager;
import su.nexmedia.auth.auth.AuthUtils;
import su.nexmedia.auth.auth.impl.AuthPlayer;
import su.nexmedia.auth.auth.impl.PlayerSnapshot;
import su.nexmedia.auth.config.Config;
import su.nexmedia.auth.config.Lang;
import su.nexmedia.auth.session.AuthSession;
import su.nexmedia.engine.api.manager.AbstractListener;
import su.nexmedia.engine.utils.TimeUtil;

public class LoginListener extends AbstractListener<NexAuth> {

    private final AuthManager authManager;

    public LoginListener(@NotNull AuthManager authManager) {
        super(authManager.plugin());
        this.authManager = authManager;
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onAuthPreLogin(AsyncPlayerPreLoginEvent e) {
        String ip = AuthUtils.getIp(e.getAddress());
        AuthSession session = plugin.getSessionManager().getSession(ip);
        if (session != null && session.isBanned()) {
            String time = TimeUtil.formatTimeLeft(session.getBannedUntil());
            e.disallow(AsyncPlayerPreLoginEvent.Result.KICK_BANNED, String.join("\n", Config.GENERAL_BAN_MESSAGE.get()).replace(Placeholders.GENERIC_TIME, time));
            return;
        }

        if (this.authManager.isBadCountry(e.getAddress())) {
            e.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, plugin.getMessage(Lang.JOIN_ERROR_BAD_COUNTRY).getLocalized());
            return;
        }

        String userName = e.getName();

        if (userName.length() < Config.SECURITY_NAME_MIN_LENGTH.get()) {
            String msg = plugin.getMessage(Lang.JOIN_ERROR_NAME_SHORT).replace(Placeholders.GENERIC_AMOUNT, Config.SECURITY_NAME_MIN_LENGTH.get()).getLocalized();
            e.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, msg);
            return;
        }

        if (userName.length() > Config.SECURITY_NAME_MAX_LENGTH.get()) {
            String msg = plugin.getMessage(Lang.JOIN_ERROR_NAME_LONG).replace(Placeholders.GENERIC_AMOUNT, Config.SECURITY_NAME_MAX_LENGTH.get()).getLocalized();
            e.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, msg);
            return;
        }

        if (!userName.matches(Config.SECURITY_NAME_REGEX.get())) {
            String msg = plugin.getMessage(Lang.JOIN_ERROR_NAME_INVALID_CHARS).replace(Placeholders.GENERIC_PATTERN, Config.SECURITY_NAME_REGEX.get()).getLocalized();
            e.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, msg);
            return;
        }

        if (Config.SECURITY_CHECK_NAME_CASE.get()) {
            String realName = plugin.getData().getRealName(userName);
            if (realName != null && !realName.equals(userName)) {
                String msg = plugin.getMessage(Lang.JOIN_ERROR_NAME_INVALID_CASE).replace(Placeholders.GENERIC_NAME, realName).getLocalized();
                e.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, msg);
                return;
            }
        }

        Player duplicate = plugin.getServer().getPlayer(userName);
        if (duplicate != null) {
            String msg = plugin.getMessage(Lang.JOIN_ERROR_ALREADY_LOGGED).getLocalized();
            e.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, msg);
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onAuthJoin(PlayerJoinEvent e) {
        this.authManager.onLogin(e.getPlayer());
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onAuthQuit(PlayerQuitEvent e) {
        PlayerSnapshot.restore(e.getPlayer());
        AuthPlayer.remove(e.getPlayer());
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onLoginChat(AsyncPlayerChatEvent e) {
        if (!Config.GENERAL_USE_CHAT.get()) return;

        Player player = e.getPlayer();
        AuthPlayer authPlayer = AuthPlayer.getOrCreate(player);
        if (!authPlayer.isInLogin() && !authPlayer.isSecretManaging()) return;

        String input = e.getMessage();
        if (input.isEmpty()) return;

        e.getRecipients().clear();
        e.setCancelled(true);
        e.setMessage("");

        this.plugin.getServer().getScheduler().runTask(plugin, () -> {
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
