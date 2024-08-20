package su.nexmedia.auth.config;

import su.nexmedia.auth.Placeholders;
import su.nightexpress.nightcore.util.wrapper.UniPermission;

public class Perms {

    private static final String PREFIX         = "nexauth.";
    private static final String PREFIX_COMMAND = PREFIX + "command.";

    public static final UniPermission PLUGIN  = new UniPermission(PREFIX + Placeholders.WILDCARD);
    public static final UniPermission COMMAND = new UniPermission(PREFIX_COMMAND + Placeholders.WILDCARD);
    public static final UniPermission SECRET  = new UniPermission(PREFIX + "secret");

    public static final UniPermission COMMAND_ADMIN          = new UniPermission(PREFIX_COMMAND + "admin");
    public static final UniPermission COMMAND_LOGIN          = new UniPermission(PREFIX_COMMAND + "login");
    public static final UniPermission COMMAND_REGISTER       = new UniPermission(PREFIX_COMMAND + "register");
    public static final UniPermission COMMAND_CHANGEPASSWORD = new UniPermission(PREFIX_COMMAND + "changepassword");

    static {
        PLUGIN.addChildren(COMMAND, SECRET);

        COMMAND.addChildren(COMMAND_ADMIN, COMMAND_LOGIN, COMMAND_REGISTER);
    }
}
