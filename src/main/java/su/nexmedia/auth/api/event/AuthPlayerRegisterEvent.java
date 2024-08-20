package su.nexmedia.auth.api.event;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import su.nexmedia.auth.data.impl.AuthUser;

public class AuthPlayerRegisterEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    private final Player   player;
    private final AuthUser user;

    public AuthPlayerRegisterEvent(boolean async, @NotNull Player player, @NotNull AuthUser user) {
        super(async);
        this.player = player;
        this.user = user;
    }

    @NotNull
    public Player getPlayer() {
        return player;
    }

    @NotNull
    public AuthUser getUser() {
        return user;
    }

    @Override
    @NotNull
    public final HandlerList getHandlers() {
        return handlers;
    }

    @NotNull
    public static HandlerList getHandlerList() {
        return handlers;
    }
}
