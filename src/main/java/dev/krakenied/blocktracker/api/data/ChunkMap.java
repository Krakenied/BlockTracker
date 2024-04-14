package dev.krakenied.blocktracker.api.data;

import dev.krakenied.blocktracker.api.object.TrackedChunk;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("UnusedReturnValue")
public final class ChunkMap extends Long2ObjectOpenHashMap<TrackedChunk> {

    public ChunkMap() {
        super();
    }

    public @Nullable TrackedChunk get(final int x, final int z) {
        return this.get(ChunkMap.chunkKey(x, z));
    }

    public @Nullable TrackedChunk getByBlock(final int x, final int z) {
        return this.get(ChunkMap.chunkKeyByBlock(x, z));
    }

    public @Nullable TrackedChunk put(final int x, final int z, final @Nullable TrackedChunk trackedChunk) {
        return this.put(ChunkMap.chunkKey(x, z), trackedChunk);
    }

    public static long chunkKey(final int x, final int z) {
        return ((long) x & 0xFFFFFFFFL) | (((long) z & 0xFFFFFFFFL) << 32);
    }

    public static long chunkKeyByBlock(final int x, final int z) {
        return ChunkMap.chunkKey(x >> 4, z >> 4);
    }
}
