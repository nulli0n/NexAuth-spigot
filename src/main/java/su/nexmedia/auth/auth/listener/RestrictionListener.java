package su.nexmedia.auth.auth.listener;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.*;
import org.jetbrains.annotations.NotNull;
import su.nexmedia.auth.AuthPlugin;
import su.nexmedia.auth.auth.AuthManager;
import su.nexmedia.auth.auth.impl.AuthPlayer;
import su.nexmedia.auth.config.Config;
import su.nightexpress.nightcore.manager.AbstractListener;
import su.nightexpress.nightcore.util.CommandUtil;

public class RestrictionListener extends AbstractListener<AuthPlugin> {

    private final AuthManager authManager;

    public RestrictionListener(@NotNull AuthPlugin plugin, @NotNull AuthManager authManager) {
        super(plugin);
        this.authManager = authManager;
    }

    private boolean cancelEvent(@NotNull PlayerEvent event) {
        return this.cancelEvent(event.getPlayer());
    }

    private boolean cancelEvent(@NotNull Player player) {
        AuthPlayer authPlayer = this.authManager.getPlayer(player);
        return authPlayer.isInLogin() || authPlayer.isSecretManaging();
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onLoginBlockBreak(BlockBreakEvent event) {
        event.setCancelled(this.cancelEvent(event.getPlayer()));
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onLoginBlockPlace(BlockPlaceEvent event) {
        event.setCancelled(this.cancelEvent(event.getPlayer()));
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onLoginDisplayCommands(PlayerCommandSendEvent event) {
        if (!this.cancelEvent(event.getPlayer())) return;

        event.getCommands().removeIf(command -> {
            return !Config.GENERAL_LOGIN_COMMANDS.get().contains(command) && !Config.GENERAL_REGISTER_COMMANDS.get().contains(command);
        });
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onLoginCommand(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();

        AuthPlayer authPlayer = this.authManager.getPlayer(player);
        if (authPlayer.isLogged()) return;

        String msg = event.getMessage();
        String inputCmd = CommandUtil.getCommandName(msg);

        if (authPlayer.isRegistered()) {
            if (Config.GENERAL_LOGIN_COMMANDS.get().contains(inputCmd)) {
                return;
            }
        }
        else {
            if (Config.GENERAL_REGISTER_COMMANDS.get().contains(inputCmd)) {
                return;
            }
        }
        event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onLoginInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (!this.cancelEvent(player)) return;

        event.setUseInteractedBlock(Event.Result.DENY);
        event.setUseItemInHand(Event.Result.DENY);
        event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onLoginInteractEntity1(PlayerInteractEntityEvent event) {
        event.setCancelled(this.cancelEvent(event));
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onLoginInteractEntity2(PlayerInteractAtEntityEvent event) {
        event.setCancelled(this.cancelEvent(event));
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onLoginInventoryClick(InventoryClickEvent event) {
        event.setCancelled(this.cancelEvent((Player) event.getWhoClicked()));
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onLoginInventorySwap(PlayerSwapHandItemsEvent event) {
        event.setCancelled(this.cancelEvent(event));
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onLoginInventoryOpen(InventoryOpenEvent event) {
        Player player = (Player) event.getPlayer();
        event.setCancelled(this.cancelEvent(player));
        if (event.isCancelled()) player.closeInventory();
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onLoginItemDamage(PlayerItemDamageEvent event) {
        event.setCancelled(this.cancelEvent(event));
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onLoginItemPick(EntityPickupItemEvent event) {
        if (event.getEntity() instanceof Player player) {
            event.setCancelled(this.cancelEvent(player));
        }
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onLoginItemDrop(PlayerDropItemEvent event) {
        event.setCancelled(this.cancelEvent(event));
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onLoginDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player player) {
            event.setCancelled(this.cancelEvent(player));
        }
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onLoginDamage(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player player) {
            event.setCancelled(this.cancelEvent(player));
        }
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onLoginMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if (!this.cancelEvent(player)) return;

        Location to = event.getTo();
        if (to == null) {
            event.setCancelled(true);
            return;
        }

        Location from = event.getFrom();
        if (from.getX() != to.getX() || from.getY() != to.getY() || from.getZ() != to.getZ()) {
            event.setCancelled(true);
        }
    }
}
