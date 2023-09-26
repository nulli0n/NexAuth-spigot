package su.nexmedia.auth.config;

import su.nexmedia.auth.Placeholders;
import su.nexmedia.auth.api.encryption.EncryptionType;
import su.nexmedia.engine.api.config.JOption;
import su.nexmedia.engine.utils.CollectionsUtil;
import su.nexmedia.engine.utils.Colorizer;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class Config {

    public static final String FILE_LOCATION = "location";

    public static final JOption<EncryptionType> GENERAL_ENCRYPTION = JOption.create("General.Encryption_Type",
        EncryptionType.class, EncryptionType.SHA512,
        "Sets the password encryption type for new (!) users.",
        "Allowed values: " + String.join(", ", CollectionsUtil.getEnumsList(EncryptionType.class)));

    public static final JOption<Boolean> GENERAL_USE_CHAT = JOption.create("General.Use_Chat", true,
        "Sets whether players can type their password in chat without commands to login/register.",
        "No real chat messages will be sent!");

    public static final JOption<Integer> GENERAL_ACCOUNTS_PER_IP = JOption.create("General.Accounts_Per_IP", 2,
        "Sets how many accounts player can register for the same IP address.");

    public static final JOption<Set<String>> GENERAL_LOGIN_COMMANDS = JOption.create("General.Login_Commands",
        Set.of("login", "l"),
        "A list of command names that will be registered and used as login commands.",
        "You can set this to an empty list to disable login commands (make sure to allow chat usage then).");

    public static final JOption<Set<String>> GENERAL_REGISTER_COMMANDS = JOption.create("General.Register_Commands",
        Set.of("register", "reg"),
        "A list of command names that will be registered and used as register commands.",
        "You can set this to an empty list to disable register commands (make sure to allow chat usage then).");

    public static final JOption<List<String>> GENERAL_BAN_MESSAGE = JOption.create("General.BanMessage",
        Arrays.asList(
            "&cYou have been banned from this server!",
            "&cReason: &fSuspicious Activity",
            "",
            "&7You can join again in: &e" + Placeholders.GENERIC_TIME
        ),
        "Sets the message to display for players banned for suspicious activity."
    ).mapReader(Colorizer::apply);

    public static final JOption<Boolean> GENERAL_LOG_ACTIONS = JOption.create("General.Log_Actions", true,
        "Sets whether plugin should log user actions: login, register, secret question, etc.");

    public static final JOption<Boolean> BUNGEE_TRANSFER_ENABLED = JOption.create("BungeeCord.Redirect.Enabled", false,
        "Sets whether logged players will be auto moved to the specified server.");

    public static final JOption<String> BUNGEE_TRANSFER_SERVER = JOption.create("BungeeCord.Redirect.Server", "hub",
        "Sets the server where logged players will be moved to.");

    public static final JOption<Integer> LOGIN_TIMEOUT = JOption.create("Login.Timeout", 60,
        "Sets how many time (in seconds) player have to register/login before being kicked.");

    public static final JOption<Integer> LOGIN_MAX_ATTEMPTS = JOption.create("Login.Max_Attempts", 5,
        "Sets how many times player can enter incorrect password before being kicked.");

    public static final JOption<Integer> LOGIN_NOTIFY_INTERVAL = JOption.create("Login.Notify_Interval", 2,
        "Sets how often (in seconds) player will action bar notifications about registration/login.",
        "Set this to 0 to disable");

    public static final JOption<Boolean> LOGIN_LOCATION_ENABLED = JOption.create("Login.Location.Enabled", true,
        "Sets whether players should be teleported to a certain location during login.",
        "Set location using '/auth setloginlocation' command.");

    public static final JOption<Boolean> SECURITY_VISUAL_CLEAN_PLAYER = JOption.create("Security.Visual.CleanPlayer", true,
        "Sets whether player's inventory, potion effects, gamemode should reset during the login.");

    public static final JOption<Boolean> SECURITY_VISUAL_BLINDNESS = JOption.create("Security.Visual.Blindness", true,
        "Sets whether players will have Blindness effect while in login.");

    public static final JOption<Integer> SECURITY_SUSPICIOUS_BAN_TIME = JOption.create("Security.Suspicious.BanTime", 3600,
        "Sets for long (in seconds) certain IP address will be banned for suspicious activity:",
        "- Max amount of failed login attempts.",
        "Set this to '-1' to ban IP until the server/plugin reload.",
        "Set this to '0' to disable.");

    public static final JOption<Boolean> SECURITY_SESSION_ENABLED = JOption.create("Security.Session.Enabled", false,
        "Enables/Disables session system.",
        "This will allow players to not enter their password when relogging in a short period of time with the same IP.");

    public static final JOption<Integer> SECURITY_SESSION_TIMEOUT = JOption.create("Security.Session.Timeout", 21600,
        "Sets how soon (in seconds) global per-IP session will be destroyed.");

    public static final JOption<Integer> SECURITY_SESSION_AUTH_TIMEOUT = JOption.create("Security.Session.Authorization_Timeout", 7200,
        "Sets session authorization timeout time (in seconds) when player will have to enter password again.");

    public static final JOption<Boolean> SECURITY_CHECK_NAME_CASE = JOption.create("Security.Check_Name_Case", true,
        "When 'true', prevents players from joining the sever if their name case does not matches name case in database.",
        "Example: If player registered with the name 'ThePlayer', all other variants like 'theplayer', 'THEPLAYER' will be disallowed.",
        "This option is useful when some of your plugins can't properly handle data for different name cases.",
        "If you're unsure, just leave this on 'true'.");

    public static final JOption<Integer> SECURITY_NAME_MIN_LENGTH = JOption.create("Security.Name.Min_Length", 3,
        "Sets minimal length for player names. Players with shorter names will be disconnected.");

    public static final JOption<Integer> SECURITY_NAME_MAX_LENGTH = JOption.create("Security.Name.Max_Length", 16,
        "Sets maximal length for player names. Players with longer names will be disconnected.");
    public static final JOption<String>  SECURITY_NAME_REGEX      = JOption.create("Security.Name.Regex", "[a-zA-Z0-9_]*",
        "Sets regex pattern for allowed names. Players with names that do no match this regex will be disconnected.",
        "By default it allows all english letters (A-Z), numbers (0-9), and underscore (_) .");

    public static final JOption<Boolean> SECURITY_PASSWORD_CONFIRMATION = JOption.create("Security.Password.Confirmation", true,
        "Sets whether players must confirm (repeat) their passwords to be registered.");

    public static final JOption<Integer> SECURITY_PASSWORD_MIN_LENGTH = JOption.create("Security.Password.Min_Length", 5,
        "Sets minimal length for passwords. Shorter passwords will be denied.");

    public static final JOption<Integer> SECURITY_PASSWORD_MAX_LENGTH = JOption.create("Security.Password.Max_Length", 32,
        "Sets maximal length for passwords. Longer passwords will be denied.");

    public static final JOption<Integer> SECURITY_PASSWORD_MIN_UPPER_LETTERS = JOption.create("Security.Password.Min_UpperCase_Letters", 2,
        "Sets how many upper-case letters a password must have.",
        "[Example] 'aaaaaa' - NOT OK, 'AaAaAa' - OK.");

    public static final JOption<Integer> SECURITY_PASSWORD_MIN_LOWER_LETTERS = JOption.create("Security.Password.Min_LowerCase_Letters", 1,
        "Sets how many lower-case letters a password must have.",
        "[Example] 'AAAAAA' - NOT OK, 'AaAaAa' - OK.");

    public static final JOption<Integer> SECURITY_PASSWORD_MIN_UNIQUE_LETTERS = JOption.create("Security.Password.Min_Unique_Letters", 3,
        "Sets how many unqiue characters a password must have.",
        "[Example] 'aaaaaa' - NOT OK, 'aabbcc' - OK.");

    public static final JOption<Integer> SECURITY_PASSWORD_MIN_DIGITS = JOption.create("Security.Password.Min_Digits", 1,
        "Sets how many digits a password must have.",
        "[Example] 'aaaaaa' - NOT OK, 'aaa123' - OK.");

    public static final JOption<String> SECURITY_PASSWORD_REGEX = JOption.create("Security.Password.Regex", "[a-zA-Z0-9_]*",
        "Sets regex pattern for allowed passwords. Passwords that do no match this regex will be denied.",
        "By default it allows all english letters (A-Z), numbers (0-9), and underscore (_) .");
}
