package su.nexmedia.auth.config;

import org.bukkit.Sound;
import su.nightexpress.nightcore.language.entry.LangString;
import su.nightexpress.nightcore.language.entry.LangText;
import su.nightexpress.nightcore.language.message.OutputType;

import static su.nexmedia.auth.Placeholders.*;
import static su.nightexpress.nightcore.language.tag.MessageTags.*;
import static su.nightexpress.nightcore.util.text.tag.Tags.*;

public class Lang {

    public static final LangString COMMAND_ARGUMENT_NAME_OLD_PASSWORD = LangString.of("Command.Argument.Name.OldPassword", "old password");
    public static final LangString COMMAND_ARGUMENT_NAME_NEW_PASSWORD = LangString.of("Command.Argument.Name.NewPassword", "new password");
    public static final LangString COMMAND_ARGUMENT_NAME_PASSWORD     = LangString.of("Command.Argument.Name.Password", "password");

    public static final LangString COMMAND_LOGIN_DESC                = LangString.of("Command.Login.Desc", "Login into the server.");
    public static final LangString COMMAND_REGISTER_DESC             = LangString.of("Command.Register.Desc", "Register on the server.");
    public static final LangString COMMAND_CHANGEPASSWORD_DESC       = LangString.of("Command.Changepassword.Desc", "Change your password");
    public static final LangString COMMAND_SECRET_DESC               = LangString.of("Command.Secret.Desc", "Secret question commands.");
    public static final LangString COMMAND_SECRET_ADD_DESC           = LangString.of("Command.Secret.Add.Desc", "Add secret question.");
    public static final LangString COMMAND_SECRET_REMOVE_DESC        = LangString.of("Command.Secret.Remove.Desc", "Remove secret question.");
    public static final LangString COMMAND_ADMIN_CHANGEPASSWORD_DESC = LangString.of("Command.Admin.Changepassword.Desc", "Change player's password.");
    public static final LangString COMMAND_ADMIN_UNREGISTER_DESC     = LangString.of("Command.Admin.Unregister.Desc", "Unregister a player.");
    public static final LangString COMMAND_ADMIN_RESETSECRET_DESC    = LangString.of("Command.Admin.ResetSecret.Desc", "Reset player's secret question.");
    public static final LangString COMMAND_ADMIN_LOGIN_LOCATION_DESC = LangString.of("Command.Admin.SetLoginLocation.Desc", "Set login location to your position.");

    public static final LangText COMMAND_ADMIN_CHANGEPASSWORD_DONE = LangText.of("Command.Admin.Changepassword.Done",
        LIGHT_GRAY.enclose("Changed " + LIGHT_YELLOW.enclose(PLAYER_NAME) + "'s password.")
    );

    public static final LangText COMMAND_ADMIN_UNREGISTER_DONE = LangText.of("Command.Admin.Unregister.Done",
        LIGHT_GRAY.enclose(LIGHT_YELLOW.enclose(PLAYER_NAME) + " unregistered.")
    );

    public static final LangString COMMAND_ADMIN_UNREGISTER_KICK = LangString.of("Command.Admin.Unregister.Kick",
        LIGHT_RED.enclose("Your account has been unregistered.")
    );

    public static final LangText COMMAND_ADMIN_RESETSECRET_DONE = LangText.of("Command.Admin.ResetSecret.Done",
        LIGHT_GRAY.enclose("Reset secret question for " + LIGHT_YELLOW.enclose(PLAYER_NAME) + ".")
    );

    public static final LangText COMMAND_ADMIN_SETLOGINLOCATION_DONE = LangText.of("Command.Admin.SetLoginLocation.Done",
        LIGHT_GRAY.enclose("Login location set!")
    );

    public static final LangString JOIN_ERROR_NAME_SHORT = LangString.of("Join.Error.Name.Short",
        LIGHT_RED.enclose("Your name is too short! It must contains at least " + ORANGE + GENERIC_AMOUNT + RED + " characters!")
    );

    public static final LangString JOIN_ERROR_NAME_LONG = LangString.of("Join.Error.Name.Long",
        LIGHT_RED.enclose("Your name is too long! It must be no longer than " + ORANGE + GENERIC_AMOUNT + RED + " characters!")
    );

    public static final LangString JOIN_ERROR_NAME_INVALID_CHARS = LangString.of("Join.Error.Name.InvalidChars",
        LIGHT_RED.enclose("Your name contains disallowed characters! Valid character range: " + ORANGE + GENERIC_PATTERN)
    );

    public static final LangString JOIN_ERROR_NAME_INVALID_CASE = LangString.of("Join.Error.Name.InvalidCase",
        LIGHT_RED.enclose("Your nickname is in wrong case! Must be: " + ORANGE + GENERIC_NAME)
    );

    public static final LangString JOIN_ERROR_BAD_COUNTRY = LangString.of("Join.Error.BadCountry",
        LIGHT_RED.enclose("Your country is banned from this server!")
    );

    public static final LangString JOIN_ERROR_ALREADY_LOGGED = LangString.of("Join.Error.AlreadyLogged",
        LIGHT_RED.enclose("Player with such name is already logged on.")
    );

    public static final LangText LOGIN_NOTIFY_LOG_IN = LangText.of("Login.Notify.LogIn",
        OUTPUT.enclose(OutputType.ACTION_BAR),
        LIGHT_RED.enclose("You're not logged in. Log in with " + ORANGE.enclose("/login <password>"))
    );

    public static final LangText LOGIN_NOTIFY_REGISTER = LangText.of("Login.Notify.Register",
        OUTPUT.enclose(OutputType.ACTION_BAR),
        LIGHT_RED.enclose("You're not registered. Register with " + ORANGE.enclose("/register <password>"))
    );

    public static final LangString LOGIN_ERROR_TOO_MANY_ATTEMPTS = LangString.of("Login.Error.TooManyAttempts",
        LIGHT_RED.enclose("You have reached the limit of login attempts.")
    );

    public static final LangString LOGIN_ERROR_TIMEOUT = LangString.of("Login.Error.Timeout",
        LIGHT_RED.enclose("Login timeout!")
    );

    public static final LangText REGISTER_REMIND_PASSWORD = LangText.of("Register.RemindPassword",
        TAG_NO_PREFIX,
        " ",
        LIGHT_GREEN.enclose(BOLD.enclose("Registration Completed!")),
        LIGHT_GRAY.enclose("Your passowrd is: " + HOVER.encloseHint(LIGHT_GREEN.enclose("[Spoiler]"), ORANGE.enclose(GENERIC_PASSWORD))),
        " ",
        LIGHT_RED.enclose(BOLD.enclose("Note: ")) + LIGHT_GRAY.enclose("Never share your password with anyone! Server staff will " + RED.enclose("never") + " ask you to share it with them."),
        " "
    );

    public static final LangText REGISTER_ERROR_PASSWORD_SHORT = LangText.of("Register.Error.Password.Short",
        OUTPUT.enclose(20, -1) + SOUND.enclose(Sound.ENTITY_VILLAGER_NO),
        LIGHT_RED.enclose(BOLD.enclose("Password Too Short!")),
        LIGHT_GRAY.enclose("Password must have at least " + LIGHT_RED.enclose(GENERIC_AMOUNT) + " characters!")
    );

    public static final LangText REGISTER_ERROR_PASSWORD_LONG = LangText.of("Register.Error.Password.Long",
        OUTPUT.enclose(20, -1) + SOUND.enclose(Sound.ENTITY_VILLAGER_NO),
        LIGHT_RED.enclose(BOLD.enclose("Password Too Long!")),
        LIGHT_GRAY.enclose("Password can be no longer than " + LIGHT_RED.enclose(GENERIC_AMOUNT) + " characters!")
    );

    public static final LangText REGISTER_ERROR_PASSWORD_INVALID_CHARS = LangText.of("Register.Error.Password.InvalidChars",
        OUTPUT.enclose(20, -1) + SOUND.enclose(Sound.ENTITY_VILLAGER_NO),
        LIGHT_RED.enclose(BOLD.enclose("Unsafe Password!")),
        LIGHT_GRAY.enclose("Your password contains forbidden characters!")
    );

    public static final LangText REGISTER_ERROR_PASSWORD_NOT_ENOUGH_LOWER_CHARS = LangText.of("Register.Error.Password.NotEnoughLowerChars",
        OUTPUT.enclose(20, -1) + SOUND.enclose(Sound.ENTITY_VILLAGER_NO),
        LIGHT_RED.enclose(BOLD.enclose("Unsafe Password!")),
        LIGHT_GRAY.enclose("Password must have at least " + LIGHT_RED.enclose(GENERIC_AMOUNT) + " lower-case character(s)!")
    );

    public static final LangText REGISTER_ERROR_PASSWORD_NOT_ENOUGH_UPPER_CHARS = LangText.of("Register.Error.Password.NotEnoughUpperChars",
        OUTPUT.enclose(20, -1) + SOUND.enclose(Sound.ENTITY_VILLAGER_NO),
        LIGHT_RED.enclose(BOLD.enclose("Unsafe Password!")),
        LIGHT_GRAY.enclose("Password must have at least " + LIGHT_RED.enclose(GENERIC_AMOUNT) + " upper-case character(s)!")
    );

    public static final LangText REGISTER_ERROR_PASSWORD_NOT_ENOUGH_DIGITS = LangText.of("Register.Error.Password.NotEnoughDigits",
        OUTPUT.enclose(20, -1) + SOUND.enclose(Sound.ENTITY_VILLAGER_NO),
        LIGHT_RED.enclose(BOLD.enclose("Unsafe Password!")),
        LIGHT_GRAY.enclose("Password must have at least " + LIGHT_RED.enclose(GENERIC_AMOUNT) + " digit(s)!")
    );

    public static final LangText REGISTER_ERROR_PASSWORD_NOT_ENOUGH_UNIQUES = LangText.of("Register.Error.Password.NotEnoughUniques",
        OUTPUT.enclose(20, -1) + SOUND.enclose(Sound.ENTITY_VILLAGER_NO),
        LIGHT_RED.enclose(BOLD.enclose("Unsafe Password!")),
        LIGHT_GRAY.enclose("Password must have at least " + LIGHT_RED.enclose(GENERIC_AMOUNT) + " unique characters!")
    );

    public static final LangText REGISTER_ERROR_IP_LIMIT = LangText.of("Register.Error.IPLimit",
        OUTPUT.enclose(20, -1) + SOUND.enclose(Sound.ENTITY_VILLAGER_NO),
        LIGHT_RED.enclose(BOLD.enclose("Too Many Accounts!")),
        LIGHT_GRAY.enclose("You have reached the limit of accounts for your IP.")
    );

    public static final LangText CHANGEPASSWORD_ERROR_WRONG_OLD = LangText.of("Changepassword.Error.WrongOld",
        LIGHT_GRAY.enclose("Old password is incorrect!")
    );

    public static final LangText CHANGEPASSWORD_NOTIFY_CHANGED = LangText.of("Changepassword.Notify.Changed",
        LIGHT_GRAY.enclose("You changed your password!")
    );

    public static final LangText LOGIN_PROMPT_PASSWORD = LangText.of("Login.Prompt.Password",
        OUTPUT.enclose(20, -1),
        LIGHT_YELLOW.enclose(BOLD.enclose("Please Log-In")),
        LIGHT_GRAY.enclose("Type your password in " + LIGHT_YELLOW.enclose("chat") + ".")
    );

    public static final LangText LOGIN_PROMPT_REGISTER = LangText.of("Login.Prompt.Register",
        OUTPUT.enclose(20, -1),
        LIGHT_RED.enclose(BOLD.enclose("Please Register")),
        LIGHT_GRAY.enclose("Type your password in " + LIGHT_RED.enclose("chat") + ".")
    );

    public static final LangText LOGIN_PROMPT_CONFIRM_PASSWORD = LangText.of("Login.Prompt.ConfirmPassword",
        OUTPUT.enclose(20, -1) + SOUND.enclose(Sound.BLOCK_LAVA_POP),
        ORANGE.enclose(BOLD.enclose("Confirm Password")),
        LIGHT_GRAY.enclose("Type your password in " + ORANGE.enclose("chat") + " again.")
    );

    public static final LangText LOGIN_REGISTER_SUCCESS = LangText.of("Login.Register.Success",
        OUTPUT.enclose(20, 60) + SOUND.enclose(Sound.ENTITY_PLAYER_LEVELUP),
        LIGHT_GREEN.enclose(BOLD.enclose("Successful Registration!")),
        LIGHT_GRAY.enclose("You're registered. You can play now.")
    );

    public static final LangText LOGIN_SUCCESS = LangText.of("Login.Success",
        OUTPUT.enclose(20, 60) + SOUND.enclose(Sound.BLOCK_IRON_DOOR_OPEN),
        LIGHT_GREEN.enclose(BOLD.enclose("Successful Login!")),
        LIGHT_GRAY.enclose("You're logged in. You can play now.")
    );

    public static final LangText LOGIN_ERROR_WRONG_PASSWORD = LangText.of("Login.Error.WrongPassword",
        OUTPUT.enclose(20, -1) + SOUND.enclose(Sound.ENTITY_VILLAGER_NO),
        LIGHT_RED.enclose(BOLD.enclose("Wrong Password!")),
        LIGHT_GRAY.enclose("Please, try again. " + DARK_GRAY.enclose("(" + GENERIC_AMOUNT + " attempt(s) left)"))
    );

    public static final LangText LOGIN_ERROR_WRONG_CONFIRM = LangText.of("Login.Error.WrongConfirm",
        OUTPUT.enclose(20, -1) + SOUND.enclose(Sound.ENTITY_VILLAGER_NO),
        LIGHT_RED.enclose(BOLD.enclose("Passwords Not Matches")),
        LIGHT_GRAY.enclose("Password confirmation failed. Enter " + LIGHT_RED.enclose("new") + " password.")
    );

    public static final LangText SECRET_ERROR_NOT_SET = LangText.of("Secret.Error.NotSet",
        LIGHT_RED.enclose("You don't have a secret question!")
    );

    public static final LangText SECRET_ERROR_ALREADY_SET = LangText.of("Secret.Error.AlreadySet",
        LIGHT_RED.enclose("You already have a secret question! You need to remove it first.")
    );

    public static final LangText SECRET_ADD_NOTIFY = LangText.of("Secret.Add.Notify",
        TAG_NO_PREFIX,
        " ",
        LIGHT_YELLOW.enclose(BOLD.enclose(">")) + LIGHT_GRAY.enclose(" Extra protect your account by adding " + LIGHT_YELLOW.enclose("Secret Question") + "!"),
        LIGHT_YELLOW.enclose(BOLD.enclose(">")) + LIGHT_GRAY.enclose(" Type " + LIGHT_YELLOW.enclose("/secret add") + " add a secret question."),
        " "
    );

    public static final LangText SECRET_ANSWER_PROMPT = LangText.of("Secret.Answer.Prompt",
        OUTPUT.enclose(20, -1) + SOUND.enclose(Sound.BLOCK_LAVA_POP),
        LIGHT_ORANGE.enclose(BOLD.enclose(GENERIC_QUESTION)),
        LIGHT_GRAY.enclose("Enter secret answer in " + LIGHT_ORANGE.enclose("chat") + ".")
    );

    public static final LangText SECRET_ANSWER_WRONG = LangText.of("Secret.Answer.Wrong",
        OUTPUT.enclose(20, -1) + SOUND.enclose(Sound.ENTITY_VILLAGER_NO),
        LIGHT_RED.enclose(BOLD.enclose(GENERIC_QUESTION)),
        LIGHT_GRAY.enclose("Wrong secret answer! Try " + LIGHT_RED.enclose("again") + ".")
    );

    public static final LangText SECRET_ADD_QUESTION = LangText.of("Secret.Add.Question",
        OUTPUT.enclose(20, -1) + SOUND.enclose(Sound.BLOCK_LAVA_POP),
        LIGHT_YELLOW.enclose(BOLD.enclose("Secret Question")),
        LIGHT_GRAY.enclose("Create secret question in " + LIGHT_YELLOW.enclose("chat") + ".")
    );

    public static final LangText SECRET_ADD_ANSWER = LangText.of("Secret.Add.Answer",
        OUTPUT.enclose(20, -1) + SOUND.enclose(Sound.BLOCK_LAVA_POP),
        LIGHT_YELLOW.enclose(BOLD.enclose(GENERIC_QUESTION)),
        LIGHT_GRAY.enclose("Create secret answer in " + LIGHT_YELLOW.enclose("chat") + ".")
    );

    public static final LangText SECRET_ADD_DONE = LangText.of("Secret.Add.Done",
        OUTPUT.enclose(20, 60) + SOUND.enclose(Sound.BLOCK_ENCHANTMENT_TABLE_USE),
        LIGHT_GREEN.enclose(BOLD.enclose("Secret Added!")),
        LIGHT_GRAY.enclose("You created secret question for your account.")
    );

    public static final LangText SECRET_REMOVE_DONE = LangText.of("Secret.Remove.Done",
        OUTPUT.enclose(20, 60) + SOUND.enclose(Sound.BLOCK_GRINDSTONE_USE),
        LIGHT_GREEN.enclose(BOLD.enclose("Secret Removed!")),
        LIGHT_GRAY.enclose("You removed secret question from your account.")
    );
}
