package me.konsolas.conditionalcommands.placeholders;

import org.bukkit.entity.Player;

public class PlaceholderPerm extends AbstractParameteredPlaceholder {

    public PlaceholderPerm() {
        super("perm");
    }

    @Override
    protected String getSub(Player player, String param) {
        return player.hasPermission(param) ? "1.0" : "0.0";
    }
}
