package dev.krakenied.blocktracker.api.config;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.time.Year;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@SuppressWarnings("unused")
public abstract class AbstractBlockTrackerConfig<Y> {

    protected File configFile;
    protected Y config;

    public abstract @NotNull File getPluginFolder();

    public abstract @NotNull String getPluginName();

    public abstract @NotNull String getPluginVersion();

    public abstract void initConfig();

    public abstract void load() throws Throwable;

    public abstract void save() throws IOException;

    public abstract void setHeader(final @NotNull List<String> header);

    public abstract void copyDefaults(final boolean copyDefaults);

    public abstract @NotNull Logger getLogger();

    public abstract boolean getBoolean(final @NotNull String path, final boolean def);

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
                "Powered by " + this.getPluginName() + " version " + this.getPluginVersion()
        );

        // Load the config from file
        this.initConfig();
        try {
            this.load();
        } catch (final IOException ignored) {
        } catch (final Throwable t) {
            throw new RuntimeException(t);
        }

        // Set the config header and enable copying defaults
        this.setHeader(header);
        this.copyDefaults(true);

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

    public boolean trackPistonHeads = false; // disable by default (https://github.com/PaperMC/Paper/pull/9258/)
    public boolean disableBoneMealTracking = false;

    private void options() {
        this.trackPistonHeads = this.getBoolean("track-piston-heads", this.trackPistonHeads);
        this.disableBoneMealTracking = this.getBoolean("disable-bone-meal-tracking", this.disableBoneMealTracking);
    }
}
