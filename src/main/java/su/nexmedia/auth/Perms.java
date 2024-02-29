package su.nexmedia.auth;

import su.nexmedia.engine.api.server.JPermission;

public class Perms {

    private static final String PREFIX         = "nexauth.";
    private static final String PREFIX_COMMAND = PREFIX + "command.";

    public static final JPermission PLUGIN  = new JPermission(PREFIX + Placeholders.WILDCARD, "Access to all plugin functions.");
    public static final JPermission COMMAND = new JPermission(PREFIX_COMMAND + Placeholders.WILDCARD, "Access to all plugin commands.");
    public static final JPermission SECRET  = new JPermission(PREFIX + "secret", "Allows to manage secret question for your account.");

    public static final JPermission COMMAND_ADMIN          = new JPermission(PREFIX_COMMAND + "admin", "Access to '/auth' admin commands.");
    public static final JPermission COMMAND_LOGIN          = new JPermission(PREFIX_COMMAND + "login", "Access to the login commands.");
    public static final JPermission COMMAND_REGISTER       = new JPermission(PREFIX_COMMAND + "register", "Access to the register commands.");
    public static final JPermission COMMAND_CHANGEPASSWORD = new JPermission(PREFIX_COMMAND + "changepassword", "Access to the change password command.");

    public static final JPermission BYPASS = new JPermission(PREFIX + "bypass-registration", "Bypass registration and login restrictions.");

    static {
        PLUGIN.addChildren(COMMAND, SECRET, BYPASS);

        COMMAND.addChildren(COMMAND_ADMIN, COMMAND_LOGIN, COMMAND_REGISTER);
    }
}
