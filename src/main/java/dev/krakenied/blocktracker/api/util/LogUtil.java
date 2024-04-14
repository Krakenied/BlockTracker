package dev.krakenied.blocktracker.api.util;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused")
public final class LogUtil {

    public static @NotNull String block2String(final @NotNull Block block) {
        final Location location = block.getLocation();
        return block.getType()
                + "/" + LogUtil.location2String(location);
    }

    public static @NotNull String location2String(final @NotNull Location location) {
        return location.getWorld().getName()
                + "/" + location.getBlockX()
                + "/" + location.getBlockY()
                + "/" + location.getBlockZ();
    }
}
