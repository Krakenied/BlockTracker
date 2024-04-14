package dev.krakenied.blocktracker.bukkit;

import dev.krakenied.blocktracker.api.manager.AbstractTrackingManager;
import dev.krakenied.blocktracker.api.object.AbstractTrackedWorld;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public final class BukkitTrackingManager extends AbstractTrackingManager<World, Chunk, Block, BlockState, BlockFace> {

    @Override
    public @NotNull UUID getUniqueIdByWorld(final @NotNull World world) {
        return world.getUID();
    }

    @Override
    public @NotNull World getWorldByChunk(final @NotNull Chunk chunk) {
        return chunk.getWorld();
    }

    @Override
    public @NotNull World getWorldByBlock(final @NotNull Block block) {
        return block.getWorld();
    }

    @Override
    public @NotNull World getWorldByState(final @NotNull BlockState state) {
        return state.getWorld();
    }

    @Override
    public @NotNull Block getRelative(final @NotNull Block block, final @NotNull BlockFace direction) {
        return block.getRelative(direction);
    }

    @Override
    public @NotNull AbstractTrackedWorld<Chunk, Block, BlockState> newTrackedWorld() {
        return new BukkitTrackedWorld();
    }

    @Override
    public @NotNull Chunk @NotNull [] getLoadedChunksByWorld(final @NotNull World world) {
        return world.getLoadedChunks();
    }

    @Override
    public @NotNull Iterable<World> getLoadedWorlds() {
        return Bukkit.getWorlds();
    }
}
