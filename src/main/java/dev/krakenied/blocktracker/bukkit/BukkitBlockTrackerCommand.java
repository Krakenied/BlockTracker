package dev.krakenied.blocktracker.bukkit;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public final class BukkitBlockTrackerCommand extends Command {

    private final BukkitBlockTrackerPlugin plugin;

    public BukkitBlockTrackerCommand(final @NotNull BukkitBlockTrackerPlugin plugin) {
        super("blocktracker");
        this.setPermission("blocktracker.command");
        this.plugin = plugin;
    }

    @Override
    public boolean execute(final @NotNull CommandSender sender, final @NotNull String label, final @NotNull String[] args) {
        this.plugin.reloadConfig();
        sender.sendMessage(Component.text("BlockTracker configuration reloaded!", TextColor.color(0x4CBB17)));
        return true;
    }
}
