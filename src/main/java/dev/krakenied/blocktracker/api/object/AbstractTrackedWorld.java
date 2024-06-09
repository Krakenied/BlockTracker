package dev.krakenied.blocktracker.api.object;

import dev.krakenied.blocktracker.api.data.ChunkMap;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractTrackedWorld<C, B, S> {

    protected final ChunkMap chunkMap;

    public AbstractTrackedWorld() {
        this.chunkMap = new ChunkMap();
    }

    public abstract int getBlockX(final @NotNull B block);

    public abstract int getBlockY(final @NotNull B block);

    public abstract int getBlockZ(final @NotNull B block);

    public abstract int getStateX(final @NotNull S state);

    public abstract int getStateY(final @NotNull S state);

    public abstract int getStateZ(final @NotNull S state);

    public abstract void initializeChunk(final @NotNull C chunk);

    public abstract void terminateChunk(final @NotNull C chunk);

    public boolean isTrackedByBlock(final @NotNull B block) {
        return this.getTrackedChunkByBlock(block).isTracked(
                this.getBlockX(block),
                this.getBlockY(block),
                this.getBlockZ(block)
        );
    }

    public boolean trackByBlock(final @NotNull B block) {
        return this.getTrackedChunkByBlock(block).track(
                this.getBlockX(block),
                this.getBlockY(block),
                this.getBlockZ(block)
        );
    }

    public boolean trackByState(final @NotNull S state) {
        return this.getTrackedChunkByState(state).track(
                this.getStateX(state),
                this.getStateY(state),
                this.getStateZ(state)
        );
    }

    public boolean untrackByBlock(final @NotNull B block) {
        return this.getTrackedChunkByBlock(block).untrack(
                this.getBlockX(block),
                this.getBlockY(block),
                this.getBlockZ(block)
        );
    }

    public boolean untrackByState(final @NotNull S state) {
        return this.getTrackedChunkByState(state).untrack(
                this.getStateX(state),
                this.getStateY(state),
                this.getStateZ(state)
        );
    }

    @SuppressWarnings("DataFlowIssue")
    protected @NotNull TrackedChunk getTrackedChunkByBlock(final @NotNull B block) {
        return this.chunkMap.getByBlock(this.getBlockX(block), this.getBlockZ(block));
    }

    @SuppressWarnings("DataFlowIssue")
    protected @NotNull TrackedChunk getTrackedChunkByState(final @NotNull S state) {
        return this.chunkMap.getByBlock(this.getStateX(state), this.getStateZ(state));
    }
}
