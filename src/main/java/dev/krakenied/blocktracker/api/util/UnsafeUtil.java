package dev.krakenied.blocktracker.api.util;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;
import java.util.List;
import java.util.logging.Logger;

@SuppressWarnings("unused")
public final class UnsafeUtil {

    @SuppressWarnings("unchecked")
    public static <T> @NotNull T cast(final @NotNull Object obj) {
        return (T) obj;
    }

    public static <E extends Enum<E>> @NotNull EnumSet<E> stringList2EnumSet(final @NotNull Class<E> enumClass, final @Nullable Logger logger, final @NotNull List<String> stringList) {
        final EnumSet<E> enumSet = EnumSet.noneOf(enumClass);
        for (final String string : stringList) {
            final E enumValue;

            try {
                enumValue = Enum.valueOf(enumClass, string);
            } catch (final IllegalArgumentException e) {
                if (logger != null) {
                    final String message = e.getMessage();
                    logger.severe(message);
                }
                continue;
            }

            enumSet.add(enumValue);
        }
        return enumSet;
    }

    public static <E extends Enum<E>> @NotNull List<String> enumSet2StringList(final @NotNull EnumSet<E> enumSet) {
        final ObjectList<String> stringList = new ObjectArrayList<>();
        for (final E enumValue : enumSet) {
            final String enumName = enumValue.name();
            stringList.add(enumName);
        }
        return stringList;
    }
}
