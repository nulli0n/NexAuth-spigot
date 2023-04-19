package su.nexmedia.auth.auth.listener;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.*;
import org.jetbrains.annotations.NotNull;
import su.nexmedia.auth.NexAuth;
import su.nexmedia.auth.auth.impl.AuthPlayer;
import su.nexmedia.auth.config.Config;
import su.nexmedia.engine.api.manager.AbstractListener;
import su.nexmedia.engine.utils.StringUtil;

public class RestrictionListener extends AbstractListener<NexAuth> {

    public RestrictionListener(@NotNull NexAuth plugin) {
        super(plugin);
    }

    private boolean cancelEvent(@NotNull PlayerEvent e) {
        return this.cancelEvent(e.getPlayer());
    }

    private boolean cancelEvent(@NotNull Player player) {
        AuthPlayer authPlayer = AuthPlayer.getOrCreate(player);
        return authPlayer.isInLogin() || authPlayer.isSecretManaging();
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onLoginBlockBreak(BlockBreakEvent e) {
        e.setCancelled(this.cancelEvent(e.getPlayer()));
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onLoginBlockPlace(BlockPlaceEvent e) {
        e.setCancelled(this.cancelEvent(e.getPlayer()));
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onLoginDisplayCommands(PlayerCommandSendEvent e) {
        if (!this.cancelEvent(e.getPlayer())) return;

        e.getCommands().removeIf(command -> {
            return !Config.GENERAL_LOGIN_COMMANDS.get().contains(command) && !Config.GENERAL_REGISTER_COMMANDS.get().contains(command);
        });
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onLoginCommand(PlayerCommandPreprocessEvent e) {
        Player player = e.getPlayer();

        AuthPlayer authPlayer = AuthPlayer.getOrCreate(player);
        if (authPlayer.isLogged()) return;

        String msg = e.getMessage();
        String inputCmd = StringUtil.extractCommandName(msg);

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
        e.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onLoginInteract(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        if (!this.cancelEvent(player)) return;

        e.setUseInteractedBlock(Event.Result.DENY);
        e.setUseItemInHand(Event.Result.DENY);
        e.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onLoginInteractEntity1(PlayerInteractEntityEvent e) {
        e.setCancelled(this.cancelEvent(e));
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onLoginInteractEntity2(PlayerInteractAtEntityEvent e) {
        e.setCancelled(this.cancelEvent(e));
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onLoginInventoryClick(InventoryClickEvent e) {
        e.setCancelled(this.cancelEvent((Player) e.getWhoClicked()));
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onLoginInventorySwap(PlayerSwapHandItemsEvent e) {
        e.setCancelled(this.cancelEvent(e));
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onLoginInventoryOpen(InventoryOpenEvent e) {
        Player player = (Player) e.getPlayer();
        e.setCancelled(this.cancelEvent(player));
        if (e.isCancelled()) player.closeInventory();
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onLoginItemDamage(PlayerItemDamageEvent e) {
        e.setCancelled(this.cancelEvent(e));
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onLoginItemPick(EntityPickupItemEvent e) {
        if (e.getEntity() instanceof Player player) {
            e.setCancelled(this.cancelEvent(player));
        }
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onLoginItemDrop(PlayerDropItemEvent e) {
        e.setCancelled(this.cancelEvent(e));
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onLoginDamage(EntityDamageEvent e) {
        if (e.getEntity() instanceof Player player) {
            e.setCancelled(this.cancelEvent(player));
        }
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onLoginMove(PlayerMoveEvent e) {
        Player player = e.getPlayer();
        if (!this.cancelEvent(player)) return;

        Location to = e.getTo();
        if (to == null) {
            e.setCancelled(true);
            return;
        }

        Location from = e.getFrom();
        if (from.getX() != to.getX() || from.getY() != to.getY() || from.getZ() != to.getZ()) {
            e.setCancelled(true);
        }
    }
}
