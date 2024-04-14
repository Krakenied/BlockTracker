package dev.krakenied.blocktracker.bukkit;

import org.bukkit.block.Block;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused")
public final class BukkitBlockTrackerAPI {

    private static BukkitBlockTrackerPlugin instance;

    static void setInstance(final @NotNull BukkitBlockTrackerPlugin instance) {
        BukkitBlockTrackerAPI.instance = instance;
    }

    public static @NotNull BukkitBlockTrackerPlugin getInstance() {
        return BukkitBlockTrackerAPI.instance;
    }

    public static boolean isTracked(final @NotNull Block block) {
        return BukkitBlockTrackerAPI.instance.getTrackingManager().isTrackedByBlock(block);
    }
}
