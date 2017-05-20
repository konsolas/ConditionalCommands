package me.konsolas.conditionalcommands.placeholders;

import org.bukkit.entity.Player;

import java.util.regex.Pattern;

public class PlaceholderPerm extends AbstractParameteredPlaceholder {
    private static final Pattern PERM_PATTERN = Pattern.compile("-perm:([A-Za-z0-9.]*)-");

    public PlaceholderPerm() {
        super("perm");
    }

    @Override
    protected String getSub(Player player, String param) {
        return player.hasPermission(param) ? "1.0" : "0.0";
    }
}
