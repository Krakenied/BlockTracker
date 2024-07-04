package dev.krakenied.blocktracker.bukkit;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class BukkitConstants {

    public static final @NotNull NamespacedKey DATA_KEY = new NamespacedKey("block_tracker", "data");
    public static final @Nullable Material SNIFFER_EGG_MATERIAL = Material.getMaterial("SNIFFER_EGG");
}
