package su.nexmedia.auth.auth;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.util.Colorizer;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.function.Predicate;

public class AuthUtils {

    public static final  String IP_LOCAL = "127.0.0.1";
    private static final String IP_ZERO  = "0.0.0.0";

    @NotNull
    public static String finePassword(@NotNull String password) {
        return Colorizer.plain(password.split(" ")[0]).trim(); // Split at space because you can use spaces in chat, but not in command arguments.
    }

    public static boolean isPublicAddress(@NotNull String ip) {
        return !ip.equalsIgnoreCase(IP_LOCAL) && !ip.equalsIgnoreCase(IP_ZERO);
    }

    @NotNull
    public static String getRawAddress(@NotNull Player player) {
        InetSocketAddress address = player.getAddress();
        return address == null ? IP_LOCAL : getRawAddress(address.getAddress());
    }

    @NotNull
    public static String getRawAddress(@NotNull InetAddress address) {
        return address.getHostAddress();
    }

    public static boolean checkCharacters(@NotNull String password, int required, @NotNull Predicate<Character> predicate) {
        if (required <= 0) return true;

        int has = 0;
        for (char c : password.toCharArray()) {
            if (predicate.test(c)) has++;
        }

        return has >= required;
    }
}
