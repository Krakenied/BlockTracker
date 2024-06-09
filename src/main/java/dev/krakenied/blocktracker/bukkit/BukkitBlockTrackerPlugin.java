package dev.krakenied.blocktracker.bukkit;

import dev.krakenied.blocktracker.api.BlockTrackerPlugin;
import dev.krakenied.blocktracker.api.config.AbstractBlockTrackerConfig;
import dev.krakenied.blocktracker.api.manager.AbstractTrackingManager;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public final class BukkitBlockTrackerPlugin extends JavaPlugin implements BlockTrackerPlugin<YamlConfiguration, World, Chunk, Block, BlockState, BlockFace, Material> {

    private final AbstractBlockTrackerConfig<YamlConfiguration, Material> blockTrackerConfig = new BukkitBlockTrackerConfig(this);
    private final AbstractTrackingManager<World, Chunk, Block, BlockState, BlockFace> trackingManager = new BukkitTrackingManager();

    @Override
    public void onEnable() {
        BukkitBlockTrackerAPI.setInstance(this);

        this.blockTrackerConfig.reloadConfig();
        this.trackingManager.initializeLoadedWorlds();
        this.registerListeners();
    }

    @Override
    public void onDisable() {
        this.unregisterListeners();
        this.trackingManager.terminateLoadedWorlds();
    }

    @Override
    public @NotNull AbstractBlockTrackerConfig<YamlConfiguration, Material> getBlockTrackerConfig() {
        return this.blockTrackerConfig;
    }

    @Override
    public @NotNull AbstractTrackingManager<World, Chunk, Block, BlockState, BlockFace> getTrackingManager() {
        return this.trackingManager;
    }

    private void registerListeners() {
        this.getServer().getPluginManager().registerEvents(new BukkitListener(this), this);
    }

    private void unregisterListeners() {
        HandlerList.unregisterAll(this);
    }

    @SuppressWarnings("unused")
    public static boolean isTracked(final @NotNull Block block) {
        return BukkitBlockTrackerAPI.isTracked(block);
    }
}
