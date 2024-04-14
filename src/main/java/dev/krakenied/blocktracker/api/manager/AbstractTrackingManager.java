package dev.krakenied.blocktracker.api.manager;

import dev.krakenied.blocktracker.api.data.WorldMap;
import dev.krakenied.blocktracker.api.object.AbstractTrackedWorld;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@SuppressWarnings("UnusedReturnValue")
public abstract class AbstractTrackingManager<W, C, B, S, F> {

    private final WorldMap<C, B, S> worldMap;

    public AbstractTrackingManager() {
        this.worldMap = new WorldMap<>();
    }

    public abstract @NotNull UUID getUniqueIdByWorld(final @NotNull W world);

    public abstract @NotNull W getWorldByChunk(final @NotNull C chunk);

    public abstract @NotNull W getWorldByBlock(final @NotNull B block);

    public abstract @NotNull W getWorldByState(final @NotNull S state);

    public abstract @NotNull B getRelative(final @NotNull B block, final @NotNull F direction);

    public abstract @NotNull AbstractTrackedWorld<C, B, S> newTrackedWorld();

    public abstract @NotNull C @NotNull [] getLoadedChunksByWorld(final @NotNull W world);

    public abstract @NotNull Iterable<W> getLoadedWorlds();

    public void initializeWorld(final @NotNull W world) {
        final AbstractTrackedWorld<C, B, S> trackedWorld = this.newTrackedWorld();

        final C[] chunks = this.getLoadedChunksByWorld(world);
        for (final C chunk : chunks) {
            trackedWorld.initializeChunk(chunk);
        }

        final UUID uniqueId = this.getUniqueIdByWorld(world);
        this.worldMap.put(uniqueId, trackedWorld);
    }

    public void terminateWorld(final @NotNull W world) {
        final UUID uniqueId = this.getUniqueIdByWorld(world);

        final AbstractTrackedWorld<C, B, S> trackedWorld = this.worldMap.remove(uniqueId);
        if (trackedWorld == null) {
            return;
        }

        final C[] chunks = this.getLoadedChunksByWorld(world);
        for (final C chunk : chunks) {
            trackedWorld.terminateChunk(chunk);
        }
    }

    public void initializeChunk(final @NotNull C chunk) {
        final AbstractTrackedWorld<C, B, S> trackedWorld = this.getTrackedWorldByChunk(chunk);
        if (trackedWorld != null) {
            trackedWorld.initializeChunk(chunk);
        }
    }

    public void terminateChunk(final @NotNull C chunk) {
        final AbstractTrackedWorld<C, B, S> trackedWorld = this.getTrackedWorldByChunk(chunk);
        if (trackedWorld != null) {
            trackedWorld.terminateChunk(chunk);
        }
    }

    public void initializeLoadedWorlds() {
        this.getLoadedWorlds().forEach(this::initializeWorld);
    }

    public void terminateLoadedWorlds() {
        this.getLoadedWorlds().forEach(this::terminateWorld);
    }

    public boolean isTrackedByBlock(final @NotNull B block) {
        final AbstractTrackedWorld<C, B, S> trackedWorld = this.getTrackedWorldByBlock(block);
        return trackedWorld != null && trackedWorld.isTrackedByBlock(block);
    }

    public boolean trackByBlock(final @NotNull B block) {
        final AbstractTrackedWorld<C, B, S> trackedWorld = this.getTrackedWorldByBlock(block);
        return trackedWorld != null && trackedWorld.trackByBlock(block);
    }

    public boolean trackByState(final @NotNull S state) {
        final AbstractTrackedWorld<C, B, S> trackedWorld = this.getTrackedWorldByState(state);
        return trackedWorld != null && trackedWorld.trackByState(state);
    }

    public boolean trackByStateIterable(final @NotNull Iterable<S> states) {
        boolean ret = false;
        for (final S state : states) {
            if (this.trackByState(state) && !ret) {
                ret = true;
            }
        }
        return ret;
    }

    public boolean untrackByBlock(final @NotNull B block) {
        final AbstractTrackedWorld<C, B, S> trackedWorld = this.getTrackedWorldByBlock(block);
        return trackedWorld != null && trackedWorld.untrackByBlock(block);
    }

    public boolean untrackByState(final @NotNull S state) {
        final AbstractTrackedWorld<C, B, S> trackedWorld = this.getTrackedWorldByState(state);
        return trackedWorld != null && trackedWorld.untrackByState(state);
    }

    public boolean untrackByBlockIterable(final @NotNull Iterable<B> blocks) {
        boolean ret = false;
        for (final B block : blocks) {
            if (this.untrackByBlock(block) && !ret) {
                ret = true;
            }
        }
        return ret;
    }

    public boolean untrackByStateIterable(final @NotNull Iterable<S> states) {
        boolean ret = false;
        for (final S state : states) {
            if (this.untrackByState(state) && !ret) {
                ret = true;
            }
        }
        return ret;
    }

    public void move(final @NotNull B from, final @NotNull B to) {
        if (this.untrackByBlock(from)) {
            this.trackByBlock(to);
        }
    }

    public void shiftByBlockList(final @NotNull List<B> blocks, final @NotNull F direction) {
        final List<B> blocksCopy = new ArrayList<>(blocks);

        final int size = blocksCopy.size();
        final boolean[] untracked = new boolean[size];

        for (int i = 0; i < size; i++) {
            final B block = blocksCopy.get(i);

            untracked[i] = this.untrackByBlock(block);
            blocksCopy.set(i, this.getRelative(block, direction));
        }

        for (int i = 0; i < size; i++) {
            final B block = blocksCopy.get(i);

            if (untracked[i]) {
                this.trackByBlock(block);
            } else {
                this.untrackByBlock(block);
            }
        }
    }

    protected @Nullable AbstractTrackedWorld<C, B, S> getTrackedWorldByWorld(final @NotNull W world) {
        final UUID uniqueId = this.getUniqueIdByWorld(world);
        return this.worldMap.get(uniqueId);
    }

    protected @Nullable AbstractTrackedWorld<C, B, S> getTrackedWorldByChunk(final @NotNull C chunk) {
        final W world = this.getWorldByChunk(chunk);
        return this.getTrackedWorldByWorld(world);
    }

    protected @Nullable AbstractTrackedWorld<C, B, S> getTrackedWorldByBlock(final @NotNull B block) {
        final W world = this.getWorldByBlock(block);
        return this.getTrackedWorldByWorld(world);
    }

    protected @Nullable AbstractTrackedWorld<C, B, S> getTrackedWorldByState(final @NotNull S state) {
        final W world = this.getWorldByState(state);
        return this.getTrackedWorldByWorld(world);
    }
}
