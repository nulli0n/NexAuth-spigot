package su.nexmedia.auth.config;

import org.bukkit.Sound;
import su.nexmedia.auth.Placeholders;
import su.nexmedia.engine.api.lang.LangKey;

import static su.nexmedia.engine.utils.Colors.*;

public class Lang {

    public static final LangKey COMMAND_LOGIN_USAGE = LangKey.of("Command.Login.Usage", "<password>");
    public static final LangKey COMMAND_LOGIN_DESC  = LangKey.of("Command.Login.Desc", "Login into the server.");

    public static final LangKey COMMAND_REGISTER_USAGE = LangKey.of("Command.Register.Usage", "<password>");
    public static final LangKey COMMAND_REGISTER_DESC  = LangKey.of("Command.Register.Desc", "Register on the server.");

    public static final LangKey COMMAND_CHANGEPASSWORD_USAGE = LangKey.of("Command.Changepassword.Usage", "<old password> <new password>");
    public static final LangKey COMMAND_CHANGEPASSWORD_DESC  = LangKey.of("Command.Changepassword.Desc", "Change your current password");

    public static final LangKey COMMAND_SECRET_USAGE = LangKey.of("Command.Secret.Usage", "[help]");
    public static final LangKey COMMAND_SECRET_DESC  = LangKey.of("Command.Secret.Desc", "Manage account's secret question.");

    public static final LangKey COMMAND_SECRET_ADD_USAGE = LangKey.of("Command.Secret.Add.Usage", "");
    public static final LangKey COMMAND_SECRET_ADD_DESC  = LangKey.of("Command.Secret.Add.Desc", "Add secret question to the account.");

    public static final LangKey COMMAND_SECRET_REMOVE_USAGE = LangKey.of("Command.Secret.Remove.Usage", "");
    public static final LangKey COMMAND_SECRET_REMOVE_DESC  = LangKey.of("Command.Secret.Remove.Desc", "Remove secret question from the account.");

    public static final LangKey COMMAND_ADMIN_CHANGEPASSWORD_USAGE = LangKey.of("Command.Admin.Changepassword.Usage", "<player> <password>");
    public static final LangKey COMMAND_ADMIN_CHANGEPASSWORD_DESC  = LangKey.of("Command.Admin.Changepassword.Desc", "Change player's current password.");
    public static final LangKey COMMAND_ADMIN_CHANGEPASSWORD_DONE  = LangKey.of("Command.Admin.Changepassword.Done", "Changed " + GREEN + Placeholders.PLAYER_NAME + GRAY + "'s password.");

    public static final LangKey COMMAND_ADMIN_UNREGISTER_USAGE = LangKey.of("Command.Admin.Unregister.Usage", "<user>");
    public static final LangKey COMMAND_ADMIN_UNREGISTER_DESC  = LangKey.of("Command.Admin.Unregister.Desc", "Unregister specified player.");
    public static final LangKey COMMAND_ADMIN_UNREGISTER_DONE  = LangKey.of("Command.Admin.Unregister.Done", "&a%player%&7 unregistered.");
    public static final LangKey COMMAND_ADMIN_UNREGISTER_KICK  = LangKey.of("Command.Admin.Unregister.Kick", "&eYour account has been unregistered.");

    public static final LangKey COMMAND_ADMIN_RESETSECRET_USAGE = LangKey.of("Command.Admin.ResetSecret.Usage", "<player>");
    public static final LangKey COMMAND_ADMIN_RESETSECRET_DESC  = LangKey.of("Command.Admin.ResetSecret.Desc", "Reset player's secret question.");
    public static final LangKey COMMAND_ADMIN_RESETSECRET_DONE  = LangKey.of("Command.Admin.ResetSecret.Done", "Reset secret question for " + GREEN + Placeholders.PLAYER_NAME + GRAY + ".");

    public static final LangKey COMMAND_ADMIN_SETLOGINLOCATION_USAGE = LangKey.of("Command.Admin.SetLoginLocation.Usage", "");
    public static final LangKey COMMAND_ADMIN_SETLOGINLOCATIOH_DESC  = LangKey.of("Command.Admin.SetLoginLocation.Desc", "Set login location to your position.");
    public static final LangKey COMMAND_ADMIN_SETLOGINLOCATION_DONE  = LangKey.of("Command.Admin.SetLoginLocation.Done", "Login location set!");

    public static final LangKey JOIN_ERROR_NAME_SHORT         = LangKey.of("Join.Error.Name.Short", RED + "Your name is too short! It must contains at least " + ORANGE + Placeholders.GENERIC_AMOUNT + RED + " characters!");
    public static final LangKey JOIN_ERROR_NAME_LONG          = LangKey.of("Join.Error.Name.Long", RED + "Your name is too long! It must be no longer than " + ORANGE + Placeholders.GENERIC_AMOUNT + RED + " characters!");
    public static final LangKey JOIN_ERROR_NAME_INVALID_CHARS = LangKey.of("Join.Error.Name.InvalidChars", RED + "Your name contains disallowed characters! Valid character range: " + ORANGE + Placeholders.GENERIC_PATTERN);
    public static final LangKey JOIN_ERROR_NAME_INVALID_CASE  = LangKey.of("Join.Error.Name.InvalidCase", RED + "Your nickname is in wrong case! Must be: " + ORANGE + Placeholders.GENERIC_NAME);
    public static final LangKey JOIN_ERROR_BAD_COUNTRY        = LangKey.of("Join.Error.BadCountry", RED + "Your country is banned from this server!");
    public static final LangKey JOIN_ERROR_ALREADY_LOGGED     = LangKey.of("Join.Error.AlreadyLogged", RED + "Player with such name is already logged on.");

    public static final LangKey LOGIN_NOTIFY_LOG_IN = LangKey.of("Login.Notify.LogIn",
        "<! type:\"action_bar\" !>" +
            RED + "You're not logged in. Log in with " + ORANGE + "/login <password>");

    public static final LangKey LOGIN_NOTIFY_REGISTER = LangKey.of("Login.Notify.Register",
        "<! type:\"action_bar\" !>" +
            RED + "You're not registered. Register with " + ORANGE + "/register <password>");

    public static final LangKey LOGIN_ERROR_TOO_MANY_ATTEMPTS = LangKey.of("Login.Error.TooManyAttempts", RED + "You have reached the limit of login attempts.");
    public static final LangKey LOGIN_ERROR_TIMEOUT           = LangKey.of("Login.Error.Timeout", RED + "Login timeout!");

    public static final LangKey REGISTER_REMIND_PASSWORD = LangKey.of("Register.RemindPassword",
        "<! prefix:\"false\" !>" +
            "\n&7" +
            "\n" + GREEN + "&lRegistration Completed!" +
            "\n" + GRAY + "Your passowrd is: <? show_text:\"" + ORANGE + Placeholders.GENERIC_PASSWORD + "\" ?>" + GREEN + "[Spoiler]</>" +
            "\n&7" +
            "\n" + RED + "&lNote: " + GRAY + "Never share your password with anyone! Server staff will " + RED + "never" + GRAY + " ask you to share it with them." +
            "\n&7");

    public static final LangKey REGISTER_ERROR_PASSWORD_SHORT = LangKey.of("Register.Error.Password.Short",
        "<! type:\"titles:20:-1:20\" sound:\"" + Sound.ENTITY_VILLAGER_NO.name() + "\" !>" +
            "\n" + RED + "&lPassword Too Short!" +
            "\n" + GRAY + "Password must have at least " + RED + Placeholders.GENERIC_AMOUNT + GRAY + " characters!");

    public static final LangKey REGISTER_ERROR_PASSWORD_LONG = LangKey.of("Register.Error.Password.Long",
        "<! type:\"titles:20:-1:20\" sound:\"" + Sound.ENTITY_VILLAGER_NO.name() + "\" !>" +
            "\n" + RED + "&lPassword Too Long!" +
            "\n" + GRAY + "Password can be no longer than " + RED + Placeholders.GENERIC_AMOUNT + GRAY + " characters!");

    public static final LangKey REGISTER_ERROR_PASSWORD_INVALID_CHARS = LangKey.of("Register.Error.Password.InvalidChars",
        "<! type:\"titles:20:-1:20\" sound:\"" + Sound.ENTITY_VILLAGER_NO.name() + "\" !>" +
            "\n" + RED + "&lUnsafe Password!" +
            "\n" + GRAY + "Your password contains unacceptable characters!");

    public static final LangKey REGISTER_ERROR_PASSWORD_NOT_ENOUGH_LOWER_CHARS = LangKey.of("Register.Error.Password.NotEnoughLowerChars",
        "<! type:\"titles:20:-1:20\" sound:\"" + Sound.ENTITY_VILLAGER_NO.name() + "\" !>" +
            "\n" + RED + "&lUnsafe Password!" +
            "\n" + GRAY + "Password must have at least " + RED + Placeholders.GENERIC_AMOUNT + GRAY + " lower-case character(s)!");

    public static final LangKey REGISTER_ERROR_PASSWORD_NOT_ENOUGH_UPPER_CHARS = LangKey.of("Register.Error.Password.NotEnoughUpperChars",
        "<! type:\"titles:20:-1:20\" sound:\"" + Sound.ENTITY_VILLAGER_NO.name() + "\" !>" +
            "\n" + RED + "&lUnsafe Password!" +
            "\n" + GRAY + "Password must have at least " + RED + Placeholders.GENERIC_AMOUNT + GRAY + " upper-case character(s)!");

    public static final LangKey REGISTER_ERROR_PASSWORD_NOT_ENOUGH_DIGITS = LangKey.of("Register.Error.Password.NotEnoughDigits",
        "<! type:\"titles:20:-1:20\" sound:\"" + Sound.ENTITY_VILLAGER_NO.name() + "\" !>" +
            "\n" + RED + "&lUnsafe Password!" +
            "\n" + GRAY + "Password must have at least " + RED + Placeholders.GENERIC_AMOUNT + GRAY + " digit(s)!");

    public static final LangKey REGISTER_ERROR_PASSWORD_NOT_ENOUGH_UNIQUES = LangKey.of("Register.Error.Password.NotEnoughUniques",
        "<! type:\"titles:20:-1:20\" sound:\"" + Sound.ENTITY_VILLAGER_NO.name() + "\" !>" +
            "\n" + RED + "&lUnsafe Password!" +
            "\n" + GRAY + "Password must have at least " + RED + Placeholders.GENERIC_AMOUNT + GRAY + " unique characters!");

    public static final LangKey REGISTER_ERROR_IP_LIMIT = LangKey.of("Register.Error.IPLimit",
        "<! type:\"titles:20:-1:20\" sound:\"" + Sound.ENTITY_VILLAGER_NO.name() + "\" !>" +
            "\n" + RED + "&lToo Many Accounts!" +
            "\n" + GRAY + "You have reached the limit of accounts for your IP.");

    public static final LangKey CHANGEPASSWORD_ERROR_WRONG_OLD = LangKey.of("Changepassword.Error.WrongOld", "Old password is incorrect!");
    public static final LangKey CHANGEPASSWORD_NOTIFY_CHANGED  = LangKey.of("Changepassword.Notify.Changed", "You changed your password!");

    public static final LangKey LOGIN_PROMPT_PASSWORD = LangKey.of("Login.Prompt.Password",
        "<! type:\"titles:20:-1:20\" !>" +
            "\n" + YELLOW + "&lPlease Log-In" +
            "\n" + GRAY + "Type your password in " + YELLOW + "chat" + GRAY + ".");

    public static final LangKey LOGIN_PROMPT_REGISTER = LangKey.of("Login.Prompt.Register",
        "<! type:\"titles:20:-1:20\" !>" +
            "\n" + RED + "&lPlease Register" +
            "\n" + GRAY + "Type your password in " + RED + "chat" + GRAY + ".");

    public static final LangKey LOGIN_PROMPT_CONFIRM_PASSWORD = LangKey.of("Login.Prompt.ConfirmPassword",
        "<! type:\"titles:20:-1:20\" sound:\"" + Sound.BLOCK_LAVA_POP.name() + "\" !>" +
            "\n" + ORANGE + "&lConfirm Password" +
            "\n" + GRAY + "Type your password in " + ORANGE + "chat" + GRAY + " again.");

    public static final LangKey LOGIN_REGISTER_SUCCESS = LangKey.of("Login.Register.Success",
        "<! type:\"titles:20:60:20\" sound:\"" + Sound.ENTITY_PLAYER_LEVELUP.name() + "\" !>" +
            "\n" + GREEN + "&lSuccessful Registration!" +
            "\n" + GRAY + "You're registered. You can play now.");

    public static final LangKey LOGIN_SUCCESS = LangKey.of("Login.Success",
        "<! type:\"titles:20:60:20\" sound:\"" + Sound.BLOCK_IRON_DOOR_OPEN.name() + "\" !>" +
            "\n" + GREEN + "&lSuccessful Login!" +
            "\n" + GRAY + "You're logged in. You can play now.");

    public static final LangKey LOGIN_ERROR_WRONG_PASSWORD = LangKey.of("Login.Error.WrongPassword",
        "<! type:\"titles:20:-1:20\" sound:\"" + Sound.ENTITY_VILLAGER_NO.name() + "\" !>" +
            "\n" + RED + "&lWrong Password!" +
            "\n" + GRAY + "Please, try again. " + DARK_GRAY + "(" + Placeholders.GENERIC_AMOUNT + " attempt(s) left)");

    public static final LangKey LOGIN_ERROR_WRONG_CONFIRM = LangKey.of("Login.Error.WrongConfirm",
        "<! type:\"titles:20:-1:20\" sound:\"" + Sound.ENTITY_VILLAGER_NO.name() + "\" !>" +
            "\n" + RED + "&lPasswords Not Matches" +
            "\n" + GRAY + "Password confirmation failed. Enter " + RED + "new" + GRAY + " password.");

    public static final LangKey SECRET_ERROR_NOT_SET     = LangKey.of("Secret.Error.NotSet", RED + "You don't have a secret question!");
    public static final LangKey SECRET_ERROR_ALREADY_SET = LangKey.of("Secret.Error.AlreadySet", RED + "You already have a secret question! You need to remove it first.");

    public static final LangKey SECRET_ADD_NOTIFY = LangKey.of("Secret.Add.Notify",
        "<! prefix:\"false\" !>" +
            "\n&7" +
            "\n" + YELLOW + "&l>" + GRAY + " Extra protect your account by adding " + YELLOW + "Secret Question" + GRAY + "!" +
            "\n" + YELLOW + "&l>" + GRAY + " Type " + YELLOW + "/secret add" + GRAY + " add a secret question." +
            "\n&7");

    public static final LangKey SECRET_ANSWER_PROMPT = LangKey.of("Secret.Answer.Prompt",
        "<! type:\"titles:20:-1:20\" sound:\"" + Sound.BLOCK_LAVA_POP.name() + "\" !>" +
            "\n" + ORANGE + "&l" + Placeholders.GENERIC_QUESTION +
            "\n" + GRAY + "Enter secret answer in " + ORANGE + "chat" + GRAY + ".");

    public static final LangKey SECRET_ANSWER_WRONG = LangKey.of("Secret.Answer.Wrong",
        "<! type:\"titles:20:-1:20\" sound:\"" + Sound.ENTITY_VILLAGER_NO.name() + "\" !>" +
            "\n" + RED + "&l" + Placeholders.GENERIC_QUESTION +
            "\n" + GRAY + "Wrong secret answer! Try " + RED + "again" + GRAY + ".");

    public static final LangKey SECRET_ADD_QUESTION = LangKey.of("Secret.Add.Question",
        "<! type:\"titles:20:-1:20\" sound:\"" + Sound.BLOCK_LAVA_POP.name() + "\" !>" +
            "\n" + YELLOW + "&lSecret Question" +
            "\n" + GRAY + "Create secret question in " + YELLOW + "chat" + GRAY + ".");

    public static final LangKey SECRET_ADD_ANSWER = LangKey.of("Secret.Add.Answer",
        "<! type:\"titles:20:-1:20\" sound:\"" + Sound.BLOCK_LAVA_POP.name() + "\" !>" +
            "\n" + YELLOW + "&l" + Placeholders.GENERIC_QUESTION +
            "\n" + GRAY + "Create secret answer in " + YELLOW + "chat" + GRAY + ".");

    public static final LangKey SECRET_ADD_DONE = LangKey.of("Secret.Add.Done",
        "<! type:\"titles:20:60:20\" sound:\"" + Sound.BLOCK_ENCHANTMENT_TABLE_USE.name() + "\" !>" +
            "\n" + GREEN + "&lSecret Added!" +
            "\n" + GRAY + "You created secret question for your account.");

    public static final LangKey SECRET_REMOVE_DONE = LangKey.of("Secret.Remove.Done",
        "<! type:\"titles:20:60:20\" sound:\"" + Sound.BLOCK_GRINDSTONE_USE.name() + "\" !>" +
            "\n" + GREEN + "&lSecret Removed!" +
            "\n" + GRAY + "You removed secret question from your account.");
}
