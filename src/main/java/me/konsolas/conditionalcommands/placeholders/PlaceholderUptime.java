package me.konsolas.conditionalcommands.placeholders;

import org.bukkit.entity.Player;

public class PlaceholderUptime extends AbstractPlaceholder {
    private long startupTimeMS;

    public PlaceholderUptime() {
        super("uptime");

        this.startupTimeMS = System.currentTimeMillis();
    }

    @Override
    public double getStat(Player player) {
        return (System.currentTimeMillis() - startupTimeMS) / 50;
    }
}
