package dev.krakenied.blocktracker.bukkit;

import dev.krakenied.blocktracker.api.config.AbstractBlockTrackerConfig;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

public final class BukkitBlockTrackerConfig extends AbstractBlockTrackerConfig<YamlConfiguration, Material> {

    private final BukkitBlockTrackerPlugin plugin;

    public BukkitBlockTrackerConfig(final @NotNull BukkitBlockTrackerPlugin plugin) {
        this.plugin = plugin;
    }

    // Plugin-related getters

    @Override
    public @NotNull File getPluginFolder() {
        return this.plugin.getDataFolder();
    }

    @Override
    public @NotNull String getPluginName() {
        return this.plugin.getName();
    }

    @SuppressWarnings("deprecation")
    @Override
    public @NotNull String getPluginVersion() {
        return this.plugin.getDescription().getVersion();
    }

    @Override
    public @NotNull Logger getLogger() {
        return this.plugin.getLogger();
    }

    // Config-related methods

    @Override
    public void initConfig() {
        this.config = new YamlConfiguration();
    }

    @Override
    public void load() throws Throwable {
        this.config.load(this.configFile);
    }

    @Override
    public void save() throws IOException {
        this.config.save(this.configFile);
    }

    @Override
    public void setHeader(final @NotNull List<String> header) {
        this.config.options().setHeader(header);
    }

    @Override
    public void width(final int width) {
        this.config.options().width(width);
    }

    @Override
    public boolean getBoolean(final @NotNull String path, final boolean def, final @NotNull List<String> comments) {
        this.setDefault(path, def, comments);
        return this.config.getBoolean(path);
    }

    @Override
    public @NotNull List<String> getStringList(final @NotNull String path, final @NotNull List<String> def, final @NotNull List<String> comments) {
        this.setDefault(path, def, comments);

        final List<?> list = this.config.getList(path, def);
        final int expectedSize = list.size();

        final ObjectList<String> stringList = new ObjectArrayList<>(expectedSize);
        for (final Object object : list) {
            if (object instanceof final String string) {
                stringList.add(string);
            } else {
                this.plugin.getLogger().severe(object + " is not a string type value, skipping!");
            }
        }

        return stringList;
    }

    private <T> void setDefault(final @NotNull String path, final T def, final @NotNull List<String> comments) {
        final boolean contains = this.config.contains(path, true);
        if (!contains) {
            this.config.set(path, def);
        }
        this.config.setComments(path, comments);
    }

    // Other weird stuff needed for specific platform compatibility

    @Override
    public @NotNull Class<Material> getMaterialClass() {
        return Material.class;
    }
}
