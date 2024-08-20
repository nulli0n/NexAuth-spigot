package su.nexmedia.auth.auth;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;
import su.nexmedia.auth.AuthPlugin;
import su.nexmedia.auth.Placeholders;
import su.nexmedia.auth.api.event.AuthPlayerLoginEvent;
import su.nexmedia.auth.api.event.AuthPlayerRegisterEvent;
import su.nexmedia.auth.auth.impl.AuthPlayer;
import su.nexmedia.auth.auth.impl.PlayerSnapshot;
import su.nexmedia.auth.auth.impl.PlayerState;
import su.nexmedia.auth.auth.listener.LoginListener;
import su.nexmedia.auth.auth.listener.RestrictionListener;
import su.nexmedia.auth.config.Config;
import su.nexmedia.auth.config.Lang;
import su.nexmedia.auth.data.impl.AuthUser;
import su.nexmedia.auth.session.AuthSession;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.manager.AbstractManager;
import su.nightexpress.nightcore.util.Players;

import java.net.InetAddress;
import java.util.*;

public class AuthManager extends AbstractManager<AuthPlugin> {

    private final Map<UUID, AuthPlayer> playerMap;

    private Location loginLocation;

    public AuthManager(@NotNull AuthPlugin plugin) {
        super(plugin);
        this.playerMap = new HashMap<>();
    }

    @Override
    protected void onLoad() {
        this.addListener(new LoginListener(this.plugin, this));
        this.addListener(new RestrictionListener(this.plugin, this));

        this.getPlayers().forEach(authPlayer -> this.onLogin(authPlayer.getPlayer()));

        if (Config.isLoginLocationEnabled()) {
            this.plugin.runTask(task -> {
                FileConfig cfg = FileConfig.loadOrExtract(plugin, Config.FILE_LOCATION);
                this.loginLocation = cfg.getLocation("Login_Location");
            });
        }

        this.addTask(plugin.createTask(this::tickLogin).setSecondsInterval(1));
        this.addTask(plugin.createAsyncTask(this::notifyLogin).setSecondsInterval(Config.LOGIN_NOTIFY_INTERVAL.get()));

    }

    @Override
    protected void onShutdown() {
        this.plugin.getServer().getOnlinePlayers().forEach(PlayerSnapshot::restore);
    }

    private void tickLogin() {
        this.getPlayers().stream().filter(AuthPlayer::isInLogin).forEach(authPlayer -> {
            if (authPlayer.isLoginExpired()) {
                authPlayer.getPlayer().kickPlayer(Lang.LOGIN_ERROR_TIMEOUT.getLegacy());
            }
        });
    }

    private void notifyLogin() {
        this.getPlayers().stream().filter(AuthPlayer::isInLogin).forEach(authPlayer -> {
            if (authPlayer.isRegistered()) {
                Lang.LOGIN_NOTIFY_LOG_IN.getMessage().send(authPlayer.getPlayer());
            }
            else {
                Lang.LOGIN_NOTIFY_REGISTER.getMessage().send(authPlayer.getPlayer());
            }
        });
    }



    @NotNull
    public AuthPlayer getPlayer(@NotNull Player player) {
        UUID id = player.getUniqueId();
        AuthPlayer authPlayer = this.playerMap.get(id);

        if (authPlayer == null) {
            String address = AuthUtils.getRawAddress(player);
            AuthUser user = this.plugin.getUserManager().getUserData(player);
            AuthSession session = this.plugin.getSessionManager().getOrCreateSession(address);

            authPlayer = new AuthPlayer(player, address, user, session);
            this.playerMap.put(id, authPlayer);
        }

        return authPlayer;
    }

    @NotNull
    public Set<AuthPlayer> getPlayers() {
        return new HashSet<>(this.playerMap.values());
    }

    public void removePlayer(@NotNull Player player) {
        this.playerMap.remove(player.getUniqueId());
    }




    public void setLoginLocation(@NotNull Location loginLocation) {
        this.loginLocation = loginLocation;

        FileConfig cfg = FileConfig.loadOrExtract(plugin, Config.FILE_LOCATION);
        cfg.set("Login_Location", this.loginLocation);
        cfg.saveChanges();
    }

    public boolean validatePassword(@NotNull CommandSender player, @NotNull String password) {
        if (password.length() < Config.SECURITY_PASSWORD_MIN_LENGTH.get()) {
            Lang.REGISTER_ERROR_PASSWORD_SHORT.getMessage()
                .replace(Placeholders.GENERIC_AMOUNT, Config.SECURITY_PASSWORD_MIN_LENGTH.get())
                .send(player);
            return false;
        }

        if (password.length() > Config.SECURITY_PASSWORD_MAX_LENGTH.get()) {
            Lang.REGISTER_ERROR_PASSWORD_LONG.getMessage()
                .replace(Placeholders.GENERIC_AMOUNT, Config.SECURITY_PASSWORD_MAX_LENGTH.get())
                .send(player);
            return false;
        }

        if (!password.matches(Config.SECURITY_PASSWORD_REGEX.get())) {
            Lang.REGISTER_ERROR_PASSWORD_INVALID_CHARS.getMessage().send(player);
            return false;
        }

        if (!AuthUtils.checkCharacters(password, Config.SECURITY_PASSWORD_MIN_LOWER_LETTERS.get(), Character::isLowerCase)) {
            Lang.REGISTER_ERROR_PASSWORD_NOT_ENOUGH_LOWER_CHARS.getMessage()
                .replace(Placeholders.GENERIC_AMOUNT, Config.SECURITY_PASSWORD_MIN_LOWER_LETTERS.get())
                .send(player);
            return false;
        }

        if (!AuthUtils.checkCharacters(password, Config.SECURITY_PASSWORD_MIN_UPPER_LETTERS.get(), Character::isUpperCase)) {
            Lang.REGISTER_ERROR_PASSWORD_NOT_ENOUGH_UPPER_CHARS.getMessage()
                .replace(Placeholders.GENERIC_AMOUNT, Config.SECURITY_PASSWORD_MIN_UPPER_LETTERS.get())
                .send(player);
            return false;
        }

        if (!AuthUtils.checkCharacters(password, Config.SECURITY_PASSWORD_MIN_DIGITS.get(), Character::isDigit)) {
            Lang.REGISTER_ERROR_PASSWORD_NOT_ENOUGH_DIGITS.getMessage()
                .replace(Placeholders.GENERIC_AMOUNT, Config.SECURITY_PASSWORD_MIN_DIGITS.get())
                .send(player);
            return false;
        }

        if (Config.SECURITY_PASSWORD_MIN_UNIQUE_LETTERS.get() > 0) {
            Set<Character> chars = new HashSet<>();
            for (char c : password.toLowerCase().toCharArray()) {
                chars.add(c);
            }
            int diff = chars.size();
            if (diff < Config.SECURITY_PASSWORD_MIN_UNIQUE_LETTERS.get()) {
                Lang.REGISTER_ERROR_PASSWORD_NOT_ENOUGH_UNIQUES.getMessage()
                    .replace(Placeholders.GENERIC_AMOUNT, Config.SECURITY_PASSWORD_MIN_UNIQUE_LETTERS.get())
                    .send(player);
                return false;
            }
        }

        return true;
    }

    public boolean isBadCountry(@NotNull InetAddress address) {
        return false;
		/*
		if (!Config.loginCountryEnabled) return false;
		
		//String ip = ia.toString().replace("\\/", "").replace("/", "");
		String code = "";
		if (code.isEmpty() || code.equalsIgnoreCase("N/A")) return false;
		
		if (Config.loginCountryReverse) {
			return Config.loginCountryList.contains(code);
		}
		else {
			return !Config.loginCountryList.contains(code);
		}*/
    }

    public void onLogin(@NotNull Player player) {
        AuthPlayer authPlayer = this.getPlayer(player);
        authPlayer.setState(PlayerState.IN_LOGIN);
        authPlayer.updateLoginExpireTime();
        authPlayer.setTempPasswordHash(null);
        authPlayer.setTempSecretInput(null);

        if (!authPlayer.getData().isRegistered()) {
            Lang.LOGIN_PROMPT_REGISTER.getMessage().send(player);
        }
        else {
            if (authPlayer.getSession().isAuthorized(player.getUniqueId()) && this.login(player)) {
                return;
            }
            Lang.LOGIN_PROMPT_PASSWORD.getMessage().send(player);
        }

        if (Config.SECURITY_VISUAL_CLEAN_PLAYER.get()) {
            PlayerSnapshot.doSnapshot(player);
            PlayerSnapshot.clear(player);
        }

        if (Config.isLoginLocationEnabled() && this.loginLocation != null) {
            player.teleport(this.loginLocation);
        }
        this.blind(player, true);
    }

    public boolean tryRegister(@NotNull Player player, @NotNull String password) {
        AuthPlayer authPlayer = this.getPlayer(player);
        if (authPlayer.isRegistered() || authPlayer.isLogged()) return false;

        if (Config.GENERAL_ACCOUNTS_PER_IP.get() > 0) {
            String address = authPlayer.getInetAddress();
            if (AuthUtils.isPublicAddress(address)) {
                int count = plugin.getData().getUsersByIp(address);
                if (count >= Config.GENERAL_ACCOUNTS_PER_IP.get()) {
                    Lang.REGISTER_ERROR_IP_LIMIT.getMessage().send(player);
                    return false;
                }
            }
        }

        password = AuthUtils.finePassword(password);

        if (!this.validatePassword(player, password)) {
            return false;
        }

        if (Config.SECURITY_PASSWORD_CONFIRMATION.get()) {
            if (authPlayer.needPasswordConfirm()) {
                Lang.LOGIN_PROMPT_CONFIRM_PASSWORD.getMessage().send(player);
                authPlayer.setTempPasswordHash(password);
                return false;
            }

            if (!authPlayer.canPasswordConfirm(password)) {
                Lang.LOGIN_ERROR_WRONG_CONFIRM.getMessage().send(player);
                authPlayer.setTempPasswordHash(null);
                return false;
            }
        }

        return this.register(player, password);
    }

    public boolean register(@NotNull Player player, @NotNull String password) {
        AuthPlayer authPlayer = this.getPlayer(player);
        if (authPlayer.isRegistered()) return false;

        AuthUser user = authPlayer.getData();

        user.setRegistrationIP(authPlayer.getInetAddress());
        user.setPassword(password, user.getEncryptionType());
        authPlayer.setTempPasswordHash(null);
        authPlayer.setState(PlayerState.LOGGED_IN);
        authPlayer.getSession().authorize(authPlayer);
        player.updateCommands();
        PlayerSnapshot.restore(player);
        this.blind(player, false);

        plugin.getUserManager().scheduleSave(user);
        Lang.LOGIN_REGISTER_SUCCESS.getMessage().send(player);
        Lang.REGISTER_REMIND_PASSWORD.getMessage().replace(Placeholders.GENERIC_PASSWORD, password).send(player);
        if (!authPlayer.getData().hasSecretKey()) {
            Lang.SECRET_ADD_NOTIFY.getMessage().send(player);
        }
        if (Config.GENERAL_LOG_ACTIONS.get()) {
            plugin.info("Player " + player.getName() + " successfully registered!");
        }

        AuthPlayerRegisterEvent registerEvent = new AuthPlayerRegisterEvent(!Bukkit.isPrimaryThread(), player, user);
        plugin.getPluginManager().callEvent(registerEvent);

        return true;
    }

    public boolean tryLogIn(@NotNull Player player, @NotNull String password) {
        AuthPlayer authPlayer = this.getPlayer(player);
        if (!authPlayer.isRegistered() || !authPlayer.isInLogin()) return false;

        AuthUser user = authPlayer.getData();

        if (authPlayer.getTempPasswordHash() == null) {
            String hashed = user.getHashedPassword();
            password = AuthUtils.finePassword(password);

            if (!user.getEncryptionType().getEncrypter().verify(password, hashed)) {

                authPlayer.getSession().addFailedLoginAttempt();
                if (this.isOutOfAttempts(authPlayer)) return false;

                int leftAttempts = Config.LOGIN_MAX_ATTEMPTS.get() - authPlayer.getSession().getFailedAttempts();
                Lang.LOGIN_ERROR_WRONG_PASSWORD.getMessage()
                    .replace(Placeholders.GENERIC_AMOUNT, leftAttempts)
                    .send(player);

                if (Config.GENERAL_LOG_ACTIONS.get()) {
                    plugin.info("Player " + player.getName() + " used wrong password!");
                }
                return false;
            }

            authPlayer.setTempPasswordHash(password);

            if (user.hasSecretKey()) {
                Lang.SECRET_ANSWER_PROMPT.getMessage()
                    .replace(Placeholders.GENERIC_QUESTION, user.getSecretKey().getQuestion())
                    .send(player);
                return false;
            }
        }

        if (user.hasSecretKey()) {
            if (!user.getSecretKey().isAnswer(password)) {

                authPlayer.getSession().addFailedLoginAttempt();
                if (this.isOutOfAttempts(authPlayer)) return false;

                Lang.SECRET_ANSWER_WRONG.getMessage()
                    .replace(Placeholders.GENERIC_QUESTION, user.getSecretKey().getQuestion())
                    .send(player);

                if (Config.GENERAL_LOG_ACTIONS.get()) {
                    plugin.info("Player " + player.getName() + " used wrong secret answer.");
                }
                return false;
            }
        }

        return this.login(player);
    }

    public boolean login(@NotNull Player player) {
        AuthPlayer authPlayer = this.getPlayer(player);
        if (!authPlayer.isRegistered() || !authPlayer.isInLogin()) return false;

        authPlayer.setTempPasswordHash(null);
        authPlayer.setTempSecretInput(null);
        authPlayer.setState(PlayerState.LOGGED_IN);
        authPlayer.getSession().authorize(authPlayer);
        player.updateCommands();
        this.blind(player, false);
        PlayerSnapshot.restore(player);

        Config.LOGIN_POST_COMMANDS.get().forEach(command -> Players.dispatchCommand(player, command));

        Lang.LOGIN_SUCCESS.getMessage().send(player);
        if (!authPlayer.getData().hasSecretKey()) {
            Lang.SECRET_ADD_NOTIFY.getMessage().send(player);
        }
        if (Config.GENERAL_LOG_ACTIONS.get()) {
            plugin.info("Player " + player.getName() + " logged in!");
        }

        AuthPlayerLoginEvent loginEvent = new AuthPlayerLoginEvent(!Bukkit.isPrimaryThread(), player, authPlayer.getData());
        plugin.getPluginManager().callEvent(loginEvent);

        return true;
    }

    private boolean isOutOfAttempts(@NotNull AuthPlayer authPlayer) {
        if (authPlayer.getSession().isLoginAttemptsWasted()) {
            authPlayer.getPlayer().kickPlayer(Lang.LOGIN_ERROR_TOO_MANY_ATTEMPTS.getLegacy());
            if (Config.SECURITY_SUSPICIOUS_BAN_TIME.get() != 0) {
                authPlayer.getSession().ban(Config.SECURITY_SUSPICIOUS_BAN_TIME.get());
            }
            return true;
        }
        return false;
    }

    public boolean changepassword(@NotNull Player player, @NotNull String oldPass, @NotNull String newPass) {
        AuthPlayer authPlayer = this.getPlayer(player);
        if (!authPlayer.isRegistered() || !authPlayer.isLogged()) return false;

        AuthUser user = authPlayer.getData();
        String hashedOld = user.getHashedPassword();
        if (!user.getEncryptionType().getEncrypter().verify(oldPass, hashedOld)) {
            Lang.CHANGEPASSWORD_ERROR_WRONG_OLD.getMessage().send(player);
            return false;
        }

        if (!this.validatePassword(player, newPass)) {
            return false;
        }

        user.setPassword(newPass, user.getEncryptionType());
        plugin.getUserManager().scheduleSave(user);
        Lang.CHANGEPASSWORD_NOTIFY_CHANGED.getMessage().send(player);
        if (Config.GENERAL_LOG_ACTIONS.get()) {
            plugin.info("Player " + player.getName() + " changed his password.");
        }
        return true;
    }

    public boolean changeSecretKey(@NotNull Player player, @NotNull String input) {
        AuthPlayer authPlayer = this.getPlayer(player);
        if (!authPlayer.isSecretManaging()) return false;

        AuthUser user = authPlayer.getData();
        if (authPlayer.getState() == PlayerState.SECRET_ADD) {
            if (authPlayer.getTempSecretInput() == null) {
                authPlayer.setTempSecretInput(input);
                Lang.SECRET_ADD_ANSWER.getMessage()
                    .replace(Placeholders.GENERIC_QUESTION, authPlayer.getTempSecretInput())
                    .send(player);
                return false;
            }
            user.getSecretKey().setQuestion(authPlayer.getTempSecretInput());
            user.getSecretKey().setAnswer(input);
            Lang.SECRET_ADD_DONE.getMessage()
                .replace(Placeholders.GENERIC_QUESTION, authPlayer.getTempSecretInput())
                .send(player);
            if (Config.GENERAL_LOG_ACTIONS.get()) {
                plugin.info("Player " + player.getName() + " added secret question.");
            }
        }
        else if (authPlayer.getState() == PlayerState.SECRET_REMOVE) {
            if (!user.getSecretKey().isAnswer(input)) {
                Lang.SECRET_ANSWER_WRONG.getMessage()
                    .replace(Placeholders.GENERIC_QUESTION, user.getSecretKey().getQuestion())
                    .send(player);
                return false;
            }
            Lang.SECRET_REMOVE_DONE.getMessage()
                .replace(Placeholders.GENERIC_QUESTION, user.getSecretKey().getQuestion())
                .send(player);
            user.getSecretKey().reset();
            if (Config.GENERAL_LOG_ACTIONS.get()) {
                plugin.info("Player " + player.getName() + " removed secret question.");
            }
        }

        authPlayer.setState(PlayerState.LOGGED_IN);
        this.plugin.getUserManager().scheduleSave(user);
        return true;
    }

    private void blind(@NotNull Player player, boolean isAdd) {
        if (!Config.SECURITY_VISUAL_BLINDNESS.get()) return;

        if (isAdd) {
            PotionEffect effect = new PotionEffect(PotionEffectType.BLINDNESS, Integer.MAX_VALUE, 10);
            player.addPotionEffect(effect);
        }
        else {
            player.removePotionEffect(PotionEffectType.BLINDNESS);
        }
    }
}
