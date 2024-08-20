package su.nexmedia.auth.config;

import su.nexmedia.auth.Placeholders;
import su.nexmedia.auth.api.encryption.EncryptionType;
import su.nightexpress.nightcore.config.ConfigValue;
import su.nightexpress.nightcore.util.Lists;
import su.nightexpress.nightcore.util.Plugins;
import su.nightexpress.nightcore.util.StringUtil;

import java.util.List;
import java.util.Set;

import static su.nightexpress.nightcore.util.text.tag.Tags.*;

public class Config {

    public static final String FILE_LOCATION = "location";

    public static final ConfigValue<EncryptionType> GENERAL_ENCRYPTION = ConfigValue.create("General.Encryption_Type",
        EncryptionType.class, EncryptionType.SHA512,
        "Sets password encryption method.",
        "[*] Works for new users only!",
        "Allowed values: " + StringUtil.inlineEnum(EncryptionType.class, ", ")
    );

    public static final ConfigValue<Boolean> GENERAL_USE_CHAT = ConfigValue.create("General.Use_Chat",
        true,
        "Sets whether or not players can type their password in chat to login/register.",
        "[*] No real message will be sent."
    );

    public static final ConfigValue<Integer> GENERAL_ACCOUNTS_PER_IP = ConfigValue.create("General.Accounts_Per_IP",
        2,
        "Sets how many accounts player can register for the same IP address."
    );

    public static final ConfigValue<Set<String>> GENERAL_LOGIN_COMMANDS = ConfigValue.create("General.Login_Commands",
        Lists.newSet("login", "l"),
        "Custom aliases for the login command.",
        "Set it to [] to disable login command completely (make sure that 'Use_Chat' is enabled)."
    );

    public static final ConfigValue<Set<String>> GENERAL_REGISTER_COMMANDS = ConfigValue.create("General.Register_Commands",
        Lists.newSet("register", "reg"),
        "Custom aliases for the register command.",
        "Set it to [] to disable register command completely (make sure that 'Use_Chat' is enabled)."
    );

    public static final ConfigValue<List<String>> GENERAL_BAN_MESSAGE = ConfigValue.create("General.BanMessage",
        Lists.newList(
            LIGHT_RED.enclose("You have been banned from this server!"),
            LIGHT_RED.enclose("Reason: " + ORANGE.enclose("Suspicious Activity")),
            "",
            LIGHT_GRAY.enclose("You can join again in: " + WHITE.enclose(Placeholders.GENERIC_TIME))
        ),
        "Sets the message to display for players banned for suspicious activity."
    );

    public static final ConfigValue<Boolean> GENERAL_LOG_ACTIONS = ConfigValue.create("General.Log_Actions",
        true,
        "Sets whether or not plugin should log user actions: login, register, secret question, etc."
    );

    public static final ConfigValue<Boolean> BUNGEE_TRANSFER_ENABLED = ConfigValue.create("BungeeCord.Redirect.Enabled",
        false,
        "Sets whether or not logged players will be auto moved to the specified server.",
        "[*] Works for BungeeCord (Waterfall) only."
    );

    public static final ConfigValue<String> BUNGEE_TRANSFER_SERVER = ConfigValue.create("BungeeCord.Redirect.Server",
        "hub",
        "Sets the server where logged players will be moved to."
    );

    public static final ConfigValue<Integer> LOGIN_TIMEOUT = ConfigValue.create("Login.Timeout",
        60,
        "Sets how many time (in seconds) player have to register/login before being kicked."
    );

    public static final ConfigValue<Integer> LOGIN_MAX_ATTEMPTS = ConfigValue.create("Login.Max_Attempts",
        5,
        "Sets how many times player can use incorrect password before being kicked."
    );

    public static final ConfigValue<Integer> LOGIN_NOTIFY_INTERVAL = ConfigValue.create("Login.Notify_Interval",
        2,
        "Sets how often (in seconds) send action bar notifications about registration/login.",
        "Set this to 0 to disable."
    );

    public static final ConfigValue<Boolean> LOGIN_LOCATION_ENABLED = ConfigValue.create("Login.Location.Enabled",
        false,
        "Sets whether or not players should be teleported to a certain location during login.",
        "Set location using the '/auth setloginlocation' command."
    );

    public static final ConfigValue<Boolean> LOGIN_LOCATION_RESTORE = ConfigValue.create("Login.Location.Restore",
        true,
        "When enabled, teleports players back to their previous location after log-in.",
        "[*] Effective only if login location is enabled."
    );

    public static final ConfigValue<List<String>> LOGIN_POST_COMMANDS = ConfigValue.create("Login.Post_Commands",
        Lists.newList(),
        "List of commands to execute when player logged-in.",
        "Use '" + Placeholders.PLAYER_NAME + "' for player name.",
        "You can use " + Plugins.PLACEHOLDER_API + " placeholders."
    );

    public static final ConfigValue<Boolean> SECURITY_VISUAL_CLEAN_PLAYER = ConfigValue.create("Security.Visual.CleanPlayer",
        true,
        "Sets whether or not player's inventory, potion effects, gamemode should reset during the login."
    );

    public static final ConfigValue<Boolean> SECURITY_VISUAL_BLINDNESS = ConfigValue.create("Security.Visual.Blindness",
        true,
        "Sets whether or not players will have Blindness effect in login."
    );

    public static final ConfigValue<Integer> SECURITY_SUSPICIOUS_BAN_TIME = ConfigValue.create("Security.Suspicious.BanTime",
        3600,
        "Sets for how long (in seconds) certain IP address will be banned for suspicious activity:",
        "- Max amount of failed login attempts.",
        "Set this to '-1' to ban IP until the server/plugin reload.",
        "Set this to '0' to disable."
    );

    public static final ConfigValue<Boolean> SECURITY_SESSION_ENABLED = ConfigValue.create("Security.Session.Enabled",
        false,
        "Enables/Disables session system.",
        "This will allow players to not enter their password when relogging in a short period of time with the same IP."
    );

    public static final ConfigValue<Integer> SECURITY_SESSION_TIMEOUT = ConfigValue.create("Security.Session.Timeout",
        21600,
        "Sets how soon (in seconds) global per-IP session will be destroyed."
    );

    public static final ConfigValue<Integer> SECURITY_SESSION_AUTH_TIMEOUT = ConfigValue.create("Security.Session.Authorization_Timeout",
        7200,
        "Sets session authorization timeout time (in seconds) when player will have to enter password again."
    );

    public static final ConfigValue<Boolean> SECURITY_CHECK_NAME_CASE = ConfigValue.create("Security.Check_Name_Case",
        true,
        "When 'true', prevents players from joining the sever if their name case does not matches name case in database.",
        "Example: If player registered with the name 'ThePlayer', all other variants like 'theplayer', 'THEPLAYER' will be disallowed.",
        "This option is useful when some of your plugins can't properly handle data for different name cases.",
        "If you're unsure, just leave this on 'true'."
    );

    public static final ConfigValue<Integer> SECURITY_NAME_MIN_LENGTH = ConfigValue.create("Security.Name.Min_Length",
        3,
        "Sets minimal length for player names. Players with shorter names will be disconnected."
    );

    public static final ConfigValue<Integer> SECURITY_NAME_MAX_LENGTH = ConfigValue.create("Security.Name.Max_Length",
        16,
        "Sets maximal length for player names. Players with longer names will be disconnected."
    );

    public static final ConfigValue<String> SECURITY_NAME_REGEX = ConfigValue.create("Security.Name.Regex",
        "[a-zA-Z0-9_]*",
        "Sets regex pattern for allowed names. Players with names that do no match this regex will be disconnected.",
        "By default it allows all english letters (A-Z), numbers (0-9), and underscore (_)."
    );

    public static final ConfigValue<Boolean> SECURITY_PASSWORD_CONFIRMATION = ConfigValue.create("Security.Password.Confirmation",
        true,
        "Sets whether or not players must confirm (repeat) their passwords to be registered."
    );

    public static final ConfigValue<Integer> SECURITY_PASSWORD_MIN_LENGTH = ConfigValue.create("Security.Password.Min_Length",
        5,
        "Sets minimal length for passwords. Shorter passwords will be denied."
    );

    public static final ConfigValue<Integer> SECURITY_PASSWORD_MAX_LENGTH = ConfigValue.create("Security.Password.Max_Length",
        32,
        "Sets maximal length for passwords. Longer passwords will be denied."
    );

    public static final ConfigValue<Integer> SECURITY_PASSWORD_MIN_UPPER_LETTERS = ConfigValue.create("Security.Password.Min_UpperCase_Letters",
        2,
        "Sets how many upper-case letters a password must have.",
        "[Example] 'aaaaaa' - NOT OK, 'AaAaAa' - OK."
    );

    public static final ConfigValue<Integer> SECURITY_PASSWORD_MIN_LOWER_LETTERS = ConfigValue.create("Security.Password.Min_LowerCase_Letters",
        1,
        "Sets how many lower-case letters a password must have.",
        "[Example] 'AAAAAA' - NOT OK, 'AaAaAa' - OK."
    );

    public static final ConfigValue<Integer> SECURITY_PASSWORD_MIN_UNIQUE_LETTERS = ConfigValue.create("Security.Password.Min_Unique_Letters",
        3,
        "Sets how many unqiue characters a password must have.",
        "[Example] 'aaaaaa' - NOT OK, 'aabbcc' - OK."
    );

    public static final ConfigValue<Integer> SECURITY_PASSWORD_MIN_DIGITS = ConfigValue.create("Security.Password.Min_Digits",
        1,
        "Sets how many digits a password must have.",
        "[Example] 'aaaaaa' - NOT OK, 'aaa123' - OK."
    );

    public static final ConfigValue<String> SECURITY_PASSWORD_REGEX = ConfigValue.create("Security.Password.Regex",
        "[a-zA-Z0-9_]*",
        "Sets regex pattern for allowed passwords. Passwords that do no match this regex will be denied.",
        "By default it allows all english letters (A-Z), numbers (0-9), and underscore (_)."
    );


    public static boolean isLoginLocationEnabled() {
        return LOGIN_LOCATION_ENABLED.get();
    }
}
