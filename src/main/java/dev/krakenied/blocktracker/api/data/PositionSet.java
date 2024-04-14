package dev.krakenied.blocktracker.api.data;

import it.unimi.dsi.fastutil.ints.IntOpenHashSet;

public final class PositionSet extends IntOpenHashSet {

    public PositionSet(final int[] data) {
        super(data);
    }

    public PositionSet() {
        super();
    }

    public boolean add(final int x, final int y, final int z) {
        return this.add(PositionSet.blockKey(x, y, z));
    }

    public boolean remove(final int x, final int y, final int z) {
        return this.remove(PositionSet.blockKey(x, y, z));
    }

    public boolean contains(final int x, final int y, final int z) {
        return this.contains(PositionSet.blockKey(x, y, z));
    }

    /**
     * Only encodes y block ranges from -8388608 to 8388607
     */
    public static int blockKey(final int x, final int y, final int z) {
        return (x & 0xF) | ((z & 0xF) << 4) | (y << 8);
    }
}
