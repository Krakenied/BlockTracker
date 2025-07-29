package dev.krakenied.blocktracker.api.config;

import dev.krakenied.blocktracker.api.util.UnsafeUtil;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.time.Year;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;

@SuppressWarnings("unused")
public abstract class AbstractBlockTrackerConfig<Y, M extends Enum<M>> {

    protected File configFile;
    protected Y config;

    // Plugin-related getters

    public abstract @NotNull File getPluginFolder();

    public abstract @NotNull String getPluginName();

    public abstract @NotNull String getPluginVersion();

    public abstract @NotNull Logger getLogger();

    // Config-related methods

    public abstract void initConfig();

    public abstract void load() throws Throwable;

    public abstract void save() throws IOException;

    public abstract void setHeader(final @NotNull List<String> header);

    public abstract void width(final int width);

    public abstract boolean getBoolean(final @NotNull String path, final boolean def, final @NotNull List<String> comments);

    public abstract @NotNull List<String> getStringList(final @NotNull String path, final @NotNull List<String> def, final @NotNull List<String> comments);

    public abstract @NotNull Map<String, Object> getString2ObjectMap(final @NotNull String path, final @NotNull Map<String, Object> def, final @NotNull List<String> comments);

    public <E extends Enum<E>> @NotNull EnumSet<E> getEnumSet(final @NotNull Class<E> enumClass, final @NotNull String path, final @NotNull EnumSet<E> def, final @NotNull List<String> comments) {
        return UnsafeUtil.stringList2EnumSet(
                enumClass,
                this.getLogger(),
                this.getStringList(
                        path,
                        UnsafeUtil.enumSet2StringList(def),
                        comments
                )
        );
    }

    public <E extends Enum<E>> @NotNull EnumMap<E, EnumSet<E>> getEnum2EnumSetMap(final @NotNull Class<E> enumClass, final @NotNull String path, final @NotNull EnumMap<E, EnumSet<E>> def, final @NotNull List<String> comments) {
        return UnsafeUtil.stringToObjectMap2EnumToEnumSetMap(
                enumClass,
                this.getLogger(),
                this.getString2ObjectMap(
                        path,
                        UnsafeUtil.enumToEnumSetMap2StringToObjectMap(def),
                        comments
                )
        );
    }

    // Other weird stuff needed for specific platform compatibility

    public abstract @NotNull Class<M> getMaterialClass();

    // The actual config part

    public void reloadConfig() {
        // Set the config file
        if (this.configFile == null) {
            final File pluginFolder = this.getPluginFolder();
            this.configFile = new File(pluginFolder, "config.yml");
        }

        // Prepare the config header
        final int year = Year.now().getValue();
        final List<String> header = List.of(
                "(c) Krakenied " + year,
                "Powered by " + this.getPluginName() + " version " + this.getPluginVersion(),
                "",
                "Built-in permissions:",
                "blocktracker.command - to use the /blocktracker command, which reloads the plugin.",
                "blocktracker.debug - to use the debug wand, which is the Heart of the Sea.",
                "",
                "All the permissions are granted by default to server operators."
        );

        // Load the config from file
        this.initConfig();
        try {
            this.load();
        } catch (final IOException ignored) {
        } catch (final Throwable t) {
            throw new RuntimeException(t);
        }

        // Set the config header and width
        this.setHeader(header);
        this.width(Integer.MAX_VALUE);

        // Load the config and set its defaults
        final Method[] methods = AbstractBlockTrackerConfig.class.getDeclaredMethods();
        for (final Method method : methods) {
            final int modifiers = method.getModifiers();

            if (Modifier.isPrivate(modifiers) && !Modifier.isStatic(modifiers) && method.getReturnType() == Void.TYPE && method.getParameterCount() == 0) {
                try {
                    method.setAccessible(true);
                    method.invoke(this);
                } catch (final InvocationTargetException e) {
                    throw new RuntimeException(e);
                } catch (final IllegalAccessException e) {
                    this.getLogger().log(Level.SEVERE, "Could not invoke " + method, e);
                }
            }
        }

        // Save modified config to the file
        try {
            this.save();
        } catch (final IOException e) {
            this.getLogger().log(Level.SEVERE, "Could not save " + this.configFile, e);
        }
    }

    public boolean trackPistonHeads = false;
    public boolean disableBoneMealTracking = false;
    public boolean disableBlockSpreadTracking = true;
    public EnumSet<M> sourcesToUntrackOnBlockSpread = UnsafeUtil.stringList2EnumSet(this.getMaterialClass(), null, List.of(
            "CHORUS_FLOWER"
    ));
    public EnumSet<M> sourcesToIgnoreOnBlockSpread = EnumSet.noneOf(this.getMaterialClass());
    public EnumMap<M, EnumSet<M>> blocksToIgnoreOnBlockPlace = new EnumMap<>(this.getMaterialClass()) {{
        final Class<M> materialClass = AbstractBlockTrackerConfig.this.getMaterialClass();
        final Function<M, EnumSet<M>> emptySetFunction = k -> EnumSet.noneOf(materialClass);
        final M[] constants = materialClass.getEnumConstants();

        final String waxedPrefix = "WAXED_";
        final String oxidizedPrefix = "OXIDIZED_";

        // https://discord.com/channels/1233303624310849556/1233333369593528330/1325920425070559356
        for (final M waxedConstant : constants) {
            final String waxedConstantName = waxedConstant.name();

            if (waxedConstantName.startsWith(waxedPrefix)) {
                final String notWaxedConstantName = waxedConstantName.substring(waxedPrefix.length());
                final M notWaxedConstant;

                // shouldn't really throw, however we catch and log it just to ensure
                try {
                    notWaxedConstant = Enum.valueOf(materialClass, notWaxedConstantName);
                } catch (final IllegalArgumentException e) {
                    final String message = e.getMessage();
                    AbstractBlockTrackerConfig.this.getLogger().severe(message);
                    continue;
                }

                // put it in both directions
                this.computeIfAbsent(waxedConstant, emptySetFunction).add(notWaxedConstant);
                this.computeIfAbsent(notWaxedConstant, emptySetFunction).add(waxedConstant);

                if (notWaxedConstantName.startsWith(oxidizedPrefix)) {
                    final String regularConstantName = notWaxedConstantName.substring(oxidizedPrefix.length());
                    final M regularConstant, exposedConstant, weatheredConstant;

                    // shouldn't really throw, however we catch and log it just to ensure
                    try {
                        regularConstant = Enum.valueOf(materialClass, regularConstantName.equals("COPPER") ? "COPPER_BLOCK" : regularConstantName);
                        exposedConstant = Enum.valueOf(materialClass, "EXPOSED_" + regularConstantName);
                        weatheredConstant = Enum.valueOf(materialClass, "WEATHERED_" + regularConstantName);
                    } catch (final IllegalArgumentException e) {
                        final String message = e.getMessage();
                        AbstractBlockTrackerConfig.this.getLogger().severe(message);
                        continue;
                    }

                    // oxidized -> weathered -> exposed -> regular
                    this.computeIfAbsent(regularConstant, emptySetFunction).add(exposedConstant);
                    this.computeIfAbsent(exposedConstant, emptySetFunction).add(weatheredConstant);
                    this.computeIfAbsent(weatheredConstant, emptySetFunction).add(notWaxedConstant);
                }
            }
        }
    }};

    private void options() {
        this.trackPistonHeads = this.getBoolean("track-piston-heads", this.trackPistonHeads, List.of(
                "!!! CURRENTLY UNSUPPORTED !!!",
                "Whether piston heads should be tracked. Only toggle this option if you're absolutely certain of",
                "its consequences. This option is disabled by default due to improper functionality. The default",
                "value of this option will be changed once Paper merges the PR below, however, the plugin code",
                "change most probably will still be required (https://github.com/PaperMC/Paper/pull/9258)."
        ));
        this.disableBoneMealTracking = this.getBoolean("disable-bone-meal-tracking", this.disableBoneMealTracking, List.of(
                "Disables tracking of blocks grown with bone meal."
        ));
        this.disableBlockSpreadTracking = this.getBoolean("disable-block-spread-tracking", this.disableBlockSpreadTracking, List.of(
                "Disables tracking of blocks spread from tracked blocks."
        ));
        this.sourcesToUntrackOnBlockSpread = this.getEnumSet(this.getMaterialClass(), "sources-to-untrack-on-block-spread", this.sourcesToUntrackOnBlockSpread, List.of(
                "Specifies the list of source block materials that should be untracked when they cause",
                "block spread events. This means that when these source blocks spread to other blocks,",
                "the source blocks will be removed from tracking."
        ));
        this.sourcesToIgnoreOnBlockSpread = this.getEnumSet(this.getMaterialClass(), "sources-to-ignore-on-block-spread", this.sourcesToIgnoreOnBlockSpread, List.of(
                "Specifies the list of source block materials that should be ignored when they cause",
                "block spread events. This means that when these source blocks spread to other blocks,",
                "no action will be taken (neither tracking nor untracking)."
        ));
        this.blocksToIgnoreOnBlockPlace = this.getEnum2EnumSetMap(this.getMaterialClass(), "blocks-to-ignore-on-block-place", this.blocksToIgnoreOnBlockPlace, List.of(
                "Specifies the map of block materials to be ignored when placed, along with a list of blocks that",
                "were replaced during the placement event. For instance, setting WAXED_COPPER_BLOCK: [COPPER_BLOCK]",
                "ensures that waxing a block will not be tracked. Conversely, COPPER_BLOCK: [WAXED_COPPER_BLOCK]",
                "is needed to prevent tracking of unwaxing actions."
        ));
    }
}
