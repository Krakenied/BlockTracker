package dev.krakenied.blocktracker.api.util;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused")
public final class LogUtil {

    public static @NotNull String block2String(final @NotNull Block block) {
        return LogUtil.state2String(block.getState());
    }

    public static @NotNull String state2String(final @NotNull BlockState state) {
        final Location location = state.getLocation();
        return state.getType()
                + "/" + LogUtil.location2String(location);
    }

    public static @NotNull String location2String(final @NotNull Location location) {
        return location.getWorld().getName()
                + "/" + location.getBlockX()
                + "/" + location.getBlockY()
                + "/" + location.getBlockZ();
    }
}
