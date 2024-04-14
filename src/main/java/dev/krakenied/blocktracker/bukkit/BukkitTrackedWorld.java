package dev.krakenied.blocktracker.bukkit;

import dev.krakenied.blocktracker.api.object.AbstractTrackedWorld;
import dev.krakenied.blocktracker.api.object.TrackedChunk;
import org.bukkit.Chunk;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

public final class BukkitTrackedWorld extends AbstractTrackedWorld<Chunk, Block, BlockState> {

    @Override
    public int getBlockX(final @NotNull Block block) {
        return block.getX();
    }

    @Override
    public int getBlockY(final @NotNull Block block) {
        return block.getY();
    }

    @Override
    public int getBlockZ(final @NotNull Block block) {
        return block.getZ();
    }

    @Override
    public int getStateX(final @NotNull BlockState state) {
        return state.getX();
    }

    @Override
    public int getStateY(final @NotNull BlockState state) {
        return state.getY();
    }

    @Override
    public int getStateZ(final @NotNull BlockState state) {
        return state.getZ();
    }

    @Override
    public void initializeChunk(final @NotNull Chunk chunk) {
        final PersistentDataContainer pdc = chunk.getPersistentDataContainer();
        final int[] data = pdc.get(BukkitConstants.DATA_KEY, PersistentDataType.INTEGER_ARRAY);
        this.chunkMap.put(chunk.getX(), chunk.getZ(), new TrackedChunk(data));
    }

    @Override
    public void terminateChunk(final @NotNull Chunk chunk) {
        final TrackedChunk trackedChunk = this.chunkMap.get(chunk.getX(), chunk.getZ());
        if (trackedChunk == null) {
            return;
        }

        final PersistentDataContainer pdc = chunk.getPersistentDataContainer();
        if (trackedChunk.isEmpty()) {
            pdc.remove(BukkitConstants.DATA_KEY);
        } else {
            pdc.set(BukkitConstants.DATA_KEY, PersistentDataType.INTEGER_ARRAY, trackedChunk.toIntArray());
        }
    }
}
