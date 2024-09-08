package dev.krakenied.blocktracker.api.util;

import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.EnumMap;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

@SuppressWarnings("unused")
public final class UnsafeUtil {

    @SuppressWarnings("unchecked")
    public static <T> @NotNull T cast(final @NotNull Object obj) {
        return (T) obj;
    }

    public static <E extends Enum<E>> @NotNull EnumSet<E> stringList2EnumSet(final @NotNull Class<E> enumClass, final @Nullable Logger logger, final @NotNull List<String> stringList) {
        final EnumSet<E> ret = EnumSet.noneOf(enumClass);

        for (final String string : stringList) {
            final E enumValue = UnsafeUtil.enumValueOf(enumClass, logger, string);

            if (enumValue != null) {
                ret.add(enumValue);
            }
        }

        return ret;
    }

    public static <E extends Enum<E>> @NotNull List<String> enumSet2StringList(final @NotNull EnumSet<E> enumSet) {
        final ObjectList<String> ret = new ObjectArrayList<>();

        for (final E enumValue : enumSet) {
            final String enumName = enumValue.name();

            ret.add(enumName);
        }

        return ret;
    }

    public static <E extends Enum<E>> @NotNull EnumMap<E, EnumSet<E>> stringToObjectMap2EnumToEnumSetMap(final @NotNull Class<E> enumClass, final @Nullable Logger logger, final @NotNull Map<String, Object> string2ObjectMap) {
        final EnumMap<E, EnumSet<E>> ret = new EnumMap<>(enumClass);

        for (final Map.Entry<String, Object> string2ObjectEntry : string2ObjectMap.entrySet()) {
            final E retKey = UnsafeUtil.enumValueOf(enumClass, logger, string2ObjectEntry.getKey());

            if (retKey == null) {
                continue;
            }

            final Object entryValue = string2ObjectEntry.getValue();

            if (entryValue instanceof final List<?> entryValueList) {
                final int expectedSize = entryValueList.size();
                final ObjectList<String> stringList = new ObjectArrayList<>(expectedSize);

                for (final Object object : entryValueList) {
                    if (object instanceof final String string) {
                        stringList.add(string);
                    } else if (logger != null) {
                        logger.severe(object + " is not a string type value, skipping!");
                    }
                }

                final EnumSet<E> retValue = UnsafeUtil.stringList2EnumSet(enumClass, logger, stringList);
                ret.put(retKey, retValue);

                continue;
            }

            if (logger != null) {
                final String message = entryValue + " is not a list type value, skipping!";
                logger.severe(message);
            }
        }

        return ret;
    }

    public static <E extends Enum<E>> @NotNull Map<String, Object> enumToEnumSetMap2StringToObjectMap(final @NotNull EnumMap<E, EnumSet<E>> enumMap) {
        final Object2ObjectMap<String, Object> string2ObjectMap = new Object2ObjectOpenHashMap<>();

        for (final var e : enumMap.entrySet()) {
            final E keyEnumValue = e.getKey();
            final String keyName = keyEnumValue.name();

            final EnumSet<E> valueEnumSet = e.getValue();
            final List<String> valueNameList = UnsafeUtil.enumSet2StringList(valueEnumSet);

            string2ObjectMap.put(keyName, valueNameList);
        }

        return string2ObjectMap;
    }

    private static <E extends Enum<E>> @Nullable E enumValueOf(final @NotNull Class<E> enumClass, final @Nullable Logger logger, final @NotNull String string) {
        try {
            return Enum.valueOf(enumClass, string);
        } catch (final IllegalArgumentException e) {
            if (logger != null) {
                final String message = e.getMessage();
                logger.severe(message);
            }
            return null;
        }
    }
}
