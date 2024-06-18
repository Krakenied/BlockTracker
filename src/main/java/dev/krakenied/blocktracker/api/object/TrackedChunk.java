package dev.krakenied.blocktracker.api.object;

import dev.krakenied.blocktracker.api.data.PositionSet;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class TrackedChunk {

    private final PositionSet positionSet;

    public TrackedChunk(final int @Nullable [] data) {
        this.positionSet = data != null ? new PositionSet(data) : new PositionSet();
    }

    public boolean isTracked(final int x, final int y, final int z) {
        return this.positionSet.contains(x, y, z);
    }

    public boolean track(final int x, final int y, final int z) {
        return this.positionSet.add(x, y, z);
    }

    public boolean untrack(final int x, final int y, final int z) {
        return this.positionSet.remove(x, y, z);
    }

    public boolean isEmpty() {
        return this.positionSet.isEmpty();
    }

    public int @NotNull [] toIntArray() {
        return this.positionSet.toIntArray();
    }
}
