package me.konsolas.conditionalcommands.placeholders;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class PlaceholderPlayerCount extends AbstractPlaceholder {
    public PlaceholderPlayerCount() {
        super("player_count");
    }

    @Override
    public double getStat(Player player) {
        return Bukkit.getOnlinePlayers().size();
    }
}
