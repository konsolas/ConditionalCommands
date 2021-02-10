package me.konsolas.conditionalcommands.placeholders;

import org.bukkit.entity.Player;

public class PlaceholderPermCount extends AbstractParameteredPlaceholder {

    public PlaceholderPermCount() {
        super("perm_count");
    }

    @Override
    protected String getSub(Player player, String param) {
        int count = 0;
        for (Player plr : player.getServer().getOnlinePlayers()) {
            if (plr.hasPermission(param)) {
                count += 1;
            }
        }
        return Long.toString(count);
    }
}
