package su.nexmedia.auth.auth.impl;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nexmedia.auth.config.Config;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerSnapshot {

    private final Location                 location;
    private final ItemStack[]              inventory;
    private final ItemStack[]              armor;
    private final ItemStack[]              extras;
    private final Collection<PotionEffect> effects;
    private final GameMode                 gameMode;

    private static final Map<UUID, PlayerSnapshot> SNAPSHOTS = new HashMap<>();

    PlayerSnapshot(@NotNull Player player) {
        this.location = player.getLocation();
        this.inventory = player.getInventory().getContents();
        this.armor = player.getInventory().getArmorContents();
        this.extras = player.getInventory().getExtraContents();
        this.effects = player.getActivePotionEffects();
        this.gameMode = player.getGameMode();
    }

    public static void doSnapshot(@NotNull Player player) {
        SNAPSHOTS.put(player.getUniqueId(), new PlayerSnapshot(player));
    }

    public static void clear(@NotNull Player player) {
        player.setGameMode(GameMode.SURVIVAL);
        player.getInventory().clear();
        player.setAllowFlight(false);
        player.setFlying(false);
        player.setGliding(false);
        player.setSneaking(false);
        player.setSprinting(false);
        player.getActivePotionEffects().forEach(effect -> player.removePotionEffect(effect.getType()));
    }

    @Nullable
    public static PlayerSnapshot get(@NotNull Player player) {
        return SNAPSHOTS.get(player.getUniqueId());
    }

    public static void restore(@NotNull Player player) {
        PlayerSnapshot snapshot = SNAPSHOTS.remove(player.getUniqueId());
        if (snapshot == null) return;

        if (Config.LOGIN_LOCATION_ENABLED.get() && Config.LOGION_LOCATION_RESTORE.get()) {
            player.teleport(snapshot.getLocation());
        }
        player.setGameMode(snapshot.getGameMode());

        if (player.getGameMode() == GameMode.CREATIVE) {
            player.setAllowFlight(true);
            player.setFlying(true);
        }

        player.getActivePotionEffects().stream().map(PotionEffect::getType).forEach(player::removePotionEffect);
        player.addPotionEffects(snapshot.getPotionEffects());

        // Return player inventory before the game
        player.getInventory().setContents(snapshot.getInventory());
        player.getInventory().setArmorContents(snapshot.getArmor());
        player.getInventory().setExtraContents(snapshot.getExtras());
    }

    @NotNull
    public Location getLocation() {
        return this.location;
    }

    @NotNull
    public ItemStack[] getInventory() {
        return this.inventory;
    }

    public ItemStack[] getArmor() {
        return this.armor;
    }

    public ItemStack[] getExtras() {
        return extras;
    }

    @NotNull
    public Collection<PotionEffect> getPotionEffects() {
        return this.effects;
    }

    @NotNull
    public GameMode getGameMode() {
        return this.gameMode;
    }
}
