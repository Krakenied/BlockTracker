package dev.krakenied.blocktracker.api;

import dev.krakenied.blocktracker.api.config.AbstractBlockTrackerConfig;
import dev.krakenied.blocktracker.api.manager.AbstractTrackingManager;
import org.jetbrains.annotations.NotNull;

public interface BlockTrackerPlugin<Y, W, C, B, S, F, M extends Enum<M>> {

    @NotNull AbstractBlockTrackerConfig<Y, M> getBlockTrackerConfig();

    @NotNull AbstractTrackingManager<W, C, B, S, F> getTrackingManager();
}
