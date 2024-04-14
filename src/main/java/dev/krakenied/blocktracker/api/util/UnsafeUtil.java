package dev.krakenied.blocktracker.api.util;

import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused")
public final class UnsafeUtil {

    @SuppressWarnings("unchecked")
    public static <T> @NotNull T cast(final @NotNull Object obj) {
        return (T) obj;
    }
}
