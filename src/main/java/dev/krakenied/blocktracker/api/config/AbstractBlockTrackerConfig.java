package dev.krakenied.blocktracker.api.config;

import dev.krakenied.blocktracker.api.util.UnsafeUtil;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.time.Year;
import java.util.EnumSet;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@SuppressWarnings("unused")
public abstract class AbstractBlockTrackerConfig<Y, M extends Enum<M>> {

    protected File configFile;
    protected Y config;

    public abstract @NotNull Class<M> getMaterialClass();

    public abstract @NotNull File getPluginFolder();

    public abstract @NotNull String getPluginName();

    public abstract @NotNull String getPluginVersion();

    public abstract void initConfig();

    public abstract void load() throws Throwable;

    public abstract void save() throws IOException;

    public abstract void setHeader(final @NotNull List<String> header);

    public abstract void width(final int width);

    public abstract @NotNull Logger getLogger();

    public abstract boolean getBoolean(final @NotNull String path, final boolean def, final @NotNull List<String> comments);

    public abstract @NotNull List<String> getStringList(final @NotNull String path, final @NotNull List<String> def, final @NotNull List<String> comments);

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
                "Specifies the list of source block materials to be untracked on block spread event."
        ));
    }
}
