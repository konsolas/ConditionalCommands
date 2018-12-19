package me.konsolas.conditionalcommands.placeholders;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class PlaceholderUptime extends AbstractStandardPlaceholder {
    private long startupTimeMS;

    public PlaceholderUptime() {
        super("uptime");
    }

    @Override
    public void init(Plugin plugin) {
        this.startupTimeMS = System.currentTimeMillis();
    }

    @Override
    public double getStat(Player player) {
        return (System.currentTimeMillis() - startupTimeMS) / 50;
    }
}