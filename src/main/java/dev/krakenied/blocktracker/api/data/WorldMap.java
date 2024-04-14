package dev.krakenied.blocktracker.api.data;

import dev.krakenied.blocktracker.api.object.AbstractTrackedWorld;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;

import java.util.UUID;

public final class WorldMap<C, B, S> extends Object2ObjectOpenHashMap<UUID, AbstractTrackedWorld<C, B, S>> {

    public WorldMap() {
        super();
    }
}
