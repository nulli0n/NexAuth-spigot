package su.nexmedia.auth.auth;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.Messenger;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;
import su.nexmedia.auth.NexAuth;
import su.nexmedia.auth.Placeholders;
import su.nexmedia.auth.api.event.AuthPlayerLoginEvent;
import su.nexmedia.auth.api.event.AuthPlayerRegisterEvent;
import su.nexmedia.auth.auth.impl.AuthPlayer;
import su.nexmedia.auth.auth.impl.PlayerSnapshot;
import su.nexmedia.auth.auth.impl.PlayerState;
import su.nexmedia.auth.auth.listener.LoginListener;
import su.nexmedia.auth.auth.listener.RestrictionListener;
import su.nexmedia.auth.auth.task.BungeeRedirectTask;
import su.nexmedia.auth.auth.task.LoginTask;
import su.nexmedia.auth.config.Config;
import su.nexmedia.auth.config.Lang;
import su.nexmedia.auth.data.impl.AuthUser;
import su.nexmedia.engine.api.config.JYML;
import su.nexmedia.engine.api.manager.AbstractManager;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.net.InetAddress;
import java.util.HashSet;
import java.util.Set;

public class AuthManager extends AbstractManager<NexAuth> {

    private Location loginLocation;

    private LoginTask          loginTask;
    private BungeeRedirectTask redirectTask;

    private static final Messenger MESSENGER = Bukkit.getServer().getMessenger();
    private static final String    CHANNEL   = "BungeeCord";

    public AuthManager(@NotNull NexAuth plugin) {
        super(plugin);
    }

    @Override
    public void onLoad() {
        this.addListener(new LoginListener(this));
        this.addListener(new RestrictionListener(this.plugin));

        AuthPlayer.getPlayers().forEach(authPlayer -> this.onLogin(authPlayer.getPlayer()));

        if (Config.BUNGEE_TRANSFER_ENABLED.get()) {
            MESSENGER.registerOutgoingPluginChannel(this.plugin, CHANNEL);
            this.redirectTask = new BungeeRedirectTask(this.plugin);
            this.redirectTask.start();
        }

        (this.loginTask = new LoginTask(this.plugin)).start();

        if (Config.LOGIN_LOCATION_ENABLED.get()) {
            this.plugin.runTask(task -> {
                JYML cfg = JYML.loadOrExtract(plugin, Config.FILE_LOCATION);
                this.loginLocation = cfg.getLocation("Login_Location");
            });
        }
    }

    @Override
    public void onShutdown() {
        if (this.redirectTask != null) {
            this.redirectTask.stop();
            this.redirectTask = null;
        }
        if (this.loginTask != null) {
            this.loginTask.stop();
            this.loginTask = null;
        }

        if (Config.BUNGEE_TRANSFER_ENABLED.get()) {
            MESSENGER.unregisterOutgoingPluginChannel(this.plugin, CHANNEL);
        }

        this.plugin.getServer().getOnlinePlayers().forEach(PlayerSnapshot::restore);
    }

    public void setLoginLocation(@NotNull Location loginLocation) {
        this.loginLocation = loginLocation;

        JYML cfg = JYML.loadOrExtract(plugin, Config.FILE_LOCATION);
        cfg.set("Login_Location", this.loginLocation);
        cfg.saveChanges();
    }

    public boolean validatePassword(@NotNull CommandSender player, @NotNull String password) {
        if (password.length() < Config.SECURITY_PASSWORD_MIN_LENGTH.get()) {
            plugin.getMessage(Lang.REGISTER_ERROR_PASSWORD_SHORT)
                .replace(Placeholders.GENERIC_AMOUNT, Config.SECURITY_PASSWORD_MIN_LENGTH.get())
                .send(player);
            return false;
        }

        if (password.length() > Config.SECURITY_PASSWORD_MAX_LENGTH.get()) {
            plugin.getMessage(Lang.REGISTER_ERROR_PASSWORD_LONG)
                .replace(Placeholders.GENERIC_AMOUNT, Config.SECURITY_PASSWORD_MAX_LENGTH.get())
                .send(player);
            return false;
        }

        if (!password.matches(Config.SECURITY_PASSWORD_REGEX.get())) {
            plugin.getMessage(Lang.REGISTER_ERROR_PASSWORD_INVALID_CHARS).send(player);
            return false;
        }

        if (!AuthUtils.checkCharacters(password, Config.SECURITY_PASSWORD_MIN_LOWER_LETTERS.get(), Character::isLowerCase)) {
            plugin.getMessage(Lang.REGISTER_ERROR_PASSWORD_NOT_ENOUGH_LOWER_CHARS)
                .replace(Placeholders.GENERIC_AMOUNT, Config.SECURITY_PASSWORD_MIN_LOWER_LETTERS.get())
                .send(player);
            return false;
        }

        if (!AuthUtils.checkCharacters(password, Config.SECURITY_PASSWORD_MIN_UPPER_LETTERS.get(), Character::isUpperCase)) {
            plugin.getMessage(Lang.REGISTER_ERROR_PASSWORD_NOT_ENOUGH_UPPER_CHARS)
                .replace(Placeholders.GENERIC_AMOUNT, Config.SECURITY_PASSWORD_MIN_UPPER_LETTERS.get())
                .send(player);
            return false;
        }

        if (!AuthUtils.checkCharacters(password, Config.SECURITY_PASSWORD_MIN_DIGITS.get(), Character::isDigit)) {
            plugin.getMessage(Lang.REGISTER_ERROR_PASSWORD_NOT_ENOUGH_DIGITS)
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
                plugin.getMessage(Lang.REGISTER_ERROR_PASSWORD_NOT_ENOUGH_UNIQUES)
                    .replace(Placeholders.GENERIC_AMOUNT, Config.SECURITY_PASSWORD_MIN_UNIQUE_LETTERS.get())
                    .send(player);
                return false;
            }
        }

        return true;
    }

    @Deprecated
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
        AuthPlayer authPlayer = AuthPlayer.getOrCreate(player);
        authPlayer.setState(PlayerState.IN_LOGIN);
        authPlayer.updateLoginExpireTime();

        if (!authPlayer.getData().isRegistered()) {
            plugin.getMessage(Lang.LOGIN_PROMPT_REGISTER).send(player);
        }
        else {
            if (authPlayer.getSession().isAuthorized(player.getUniqueId()) && this.login(player)) {
                return;
            }
            plugin.getMessage(Lang.LOGIN_PROMPT_PASSWORD).send(player);
        }

        if (Config.SECURITY_VISUAL_CLEAN_PLAYER.get()) {
            PlayerSnapshot.doSnapshot(player);
            PlayerSnapshot.clear(player);
        }

        if (Config.LOGIN_LOCATION_ENABLED.get() && this.loginLocation != null) {
            player.teleport(this.loginLocation);
        }
        this.blind(player, true);
    }

    public boolean tryRegister(@NotNull Player player, @NotNull String password) {
        AuthPlayer authPlayer = AuthPlayer.getOrCreate(player);
        if (authPlayer.isRegistered() || authPlayer.isLogged()) return false;

        if (Config.GENERAL_ACCOUNTS_PER_IP.get() > 0) {
            String ip = authPlayer.getIP();
            if (AuthUtils.isPublicIP(ip)) {
                int count = plugin.getData().getUsersByIp(ip);
                if (count >= Config.GENERAL_ACCOUNTS_PER_IP.get()) {
                    plugin.getMessage(Lang.REGISTER_ERROR_IP_LIMIT).send(player);
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
                plugin.getMessage(Lang.LOGIN_PROMPT_CONFIRM_PASSWORD).send(player);
                authPlayer.setTempPasswordHash(password);
                return false;
            }

            if (!authPlayer.canPasswordConfirm(password)) {
                plugin.getMessage(Lang.LOGIN_ERROR_WRONG_CONFIRM).send(player);
                authPlayer.setTempPasswordHash(null);
                return false;
            }
        }

        return this.register(player, password);
    }

    public boolean register(@NotNull Player player, @NotNull String password) {
        AuthPlayer authPlayer = AuthPlayer.getOrCreate(player);
        if (authPlayer.isRegistered()) return false;

        AuthUser user = authPlayer.getData();

        user.setRegistrationIP(authPlayer.getIP());
        user.setPassword(password, user.getEncryptionType());
        authPlayer.setTempPasswordHash(null);
        authPlayer.setState(PlayerState.LOGGED_IN);
        authPlayer.getSession().authorize(authPlayer);
        player.updateCommands();
        PlayerSnapshot.restore(player);
        this.blind(player, false);

        plugin.getMessage(Lang.LOGIN_REGISTER_SUCCESS).send(player);
        plugin.getMessage(Lang.REGISTER_REMIND_PASSWORD).replace(Placeholders.GENERIC_PASSWORD, password).send(player);
        if (!authPlayer.getData().hasSecretKey()) {
            plugin.getMessage(Lang.SECRET_ADD_NOTIFY).send(player);
        }
        if (Config.GENERAL_LOG_ACTIONS.get()) {
            plugin.info("Player " + player.getName() + " successfully registered!");
        }

        AuthPlayerRegisterEvent registerEvent = new AuthPlayerRegisterEvent(!Bukkit.isPrimaryThread(), player, user);
        plugin.getPluginManager().callEvent(registerEvent);

        return true;
    }

    public boolean tryLogIn(@NotNull Player player, @NotNull String password) {
        AuthPlayer authPlayer = AuthPlayer.getOrCreate(player);
        if (!authPlayer.isRegistered() || !authPlayer.isInLogin()) return false;

        AuthUser user = authPlayer.getData();

        if (authPlayer.getTempPasswordHash() == null) {
            String hashed = user.getHashedPassword();
            password = AuthUtils.finePassword(password);

            if (!user.getEncryptionType().getEncrypter().verify(password, hashed)) {

                authPlayer.getSession().addFailedLoginAttempt();
                if (this.isOutOfAttempts(authPlayer)) return false;

                int leftAttempts = Config.LOGIN_MAX_ATTEMPTS.get() - authPlayer.getSession().getFailedAttempts();
                plugin.getMessage(Lang.LOGIN_ERROR_WRONG_PASSWORD)
                    .replace(Placeholders.GENERIC_AMOUNT, leftAttempts)
                    .send(player);

                if (Config.GENERAL_LOG_ACTIONS.get()) {
                    plugin.info("Player " + player.getName() + " used wrong password!");
                }
                return false;
            }

            authPlayer.setTempPasswordHash(password);

            if (user.hasSecretKey()) {
                plugin.getMessage(Lang.SECRET_ANSWER_PROMPT)
                    .replace(Placeholders.GENERIC_QUESTION, user.getSecretKey().getQuestion())
                    .send(player);
                return false;
            }
        }

        if (user.hasSecretKey()) {
            if (!user.getSecretKey().isAnswer(password)) {

                authPlayer.getSession().addFailedLoginAttempt();
                if (this.isOutOfAttempts(authPlayer)) return false;

                plugin.getMessage(Lang.SECRET_ANSWER_WRONG)
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
        AuthPlayer authPlayer = AuthPlayer.getOrCreate(player);
        if (!authPlayer.isRegistered() || !authPlayer.isInLogin()) return false;

        authPlayer.setTempPasswordHash(null);
        authPlayer.setTempSecretInput(null);
        authPlayer.setState(PlayerState.LOGGED_IN);
        authPlayer.getSession().authorize(authPlayer);
        player.updateCommands();
        this.blind(player, false);
        PlayerSnapshot.restore(player);

        plugin.getMessage(Lang.LOGIN_SUCCESS).send(player);
        if (!authPlayer.getData().hasSecretKey()) {
            plugin.getMessage(Lang.SECRET_ADD_NOTIFY).send(player);
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
            authPlayer.getPlayer().kickPlayer(plugin.getMessage(Lang.LOGIN_ERROR_TOO_MANY_ATTEMPTS).getLocalized());
            if (Config.SECURITY_SUSPICIOUS_BAN_TIME.get() != 0) {
                authPlayer.getSession().ban(Config.SECURITY_SUSPICIOUS_BAN_TIME.get());
            }
            return true;
        }
        return false;
    }

    public boolean changepassword(@NotNull Player player, @NotNull String oldPass, @NotNull String newPass) {
        AuthPlayer authPlayer = AuthPlayer.getOrCreate(player);
        if (!authPlayer.isRegistered() || !authPlayer.isLogged()) return false;

        AuthUser user = authPlayer.getData();
        String hashedOld = user.getHashedPassword();
        if (!user.getEncryptionType().getEncrypter().verify(oldPass, hashedOld)) {
            plugin.getMessage(Lang.CHANGEPASSWORD_ERROR_WRONG_OLD).send(player);
            return false;
        }

        if (!this.validatePassword(player, newPass)) {
            return false;
        }

        user.setPassword(newPass, user.getEncryptionType());
        plugin.getMessage(Lang.CHANGEPASSWORD_NOTIFY_CHANGED).send(player);
        if (Config.GENERAL_LOG_ACTIONS.get()) {
            plugin.info("Player " + player.getName() + " changed his password.");
        }
        return true;
    }

    public boolean changeSecretKey(@NotNull Player player, @NotNull String input) {
        AuthPlayer authPlayer = AuthPlayer.getOrCreate(player);
        if (!authPlayer.isSecretManaging()) return false;

        AuthUser user = authPlayer.getData();
        if (authPlayer.getState() == PlayerState.SECRET_ADD) {
            if (authPlayer.getTempSecretInput() == null) {
                authPlayer.setTempSecretInput(input);
                plugin.getMessage(Lang.SECRET_ADD_ANSWER)
                    .replace(Placeholders.GENERIC_QUESTION, authPlayer.getTempSecretInput())
                    .send(player);
                return false;
            }
            user.getSecretKey().setQuestion(authPlayer.getTempSecretInput());
            user.getSecretKey().setAnswer(input);
            plugin.getMessage(Lang.SECRET_ADD_DONE)
                .replace(Placeholders.GENERIC_QUESTION, authPlayer.getTempSecretInput())
                .send(player);
            if (Config.GENERAL_LOG_ACTIONS.get()) {
                plugin.info("Player " + player.getName() + " added secret question.");
            }
        }
        else if (authPlayer.getState() == PlayerState.SECRET_REMOVE) {
            if (!user.getSecretKey().isAnswer(input)) {
                plugin.getMessage(Lang.SECRET_ANSWER_WRONG)
                    .replace(Placeholders.GENERIC_QUESTION, user.getSecretKey().getQuestion())
                    .send(player);
                return false;
            }
            plugin.getMessage(Lang.SECRET_REMOVE_DONE)
                .replace(Placeholders.GENERIC_QUESTION, user.getSecretKey().getQuestion())
                .send(player);
            user.getSecretKey().reset();
            if (Config.GENERAL_LOG_ACTIONS.get()) {
                plugin.info("Player " + player.getName() + " removed secret question.");
            }
        }

        authPlayer.setState(PlayerState.LOGGED_IN);
        user.saveData(this.plugin);
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

    public boolean connectToBungeeServer(@NotNull Player player, @NotNull String server) {
        try {
            if (server.length() == 0) {
                plugin.info("Could not redirect player to the 'null' server.");
                return false;
            }
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);
            dataOutputStream.writeUTF("Connect");
            dataOutputStream.writeUTF(server);
            player.sendPluginMessage(plugin, CHANNEL, byteArrayOutputStream.toByteArray());
            byteArrayOutputStream.close();
            dataOutputStream.close();
        }
        catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
        return true;
    }
}
