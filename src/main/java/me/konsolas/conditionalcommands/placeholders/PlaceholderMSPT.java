package me.konsolas.conditionalcommands.placeholders;

import me.konsolas.conditionalcommands.providers.MSPTProviderSpark;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class PlaceholderMSPT extends AbstractParameteredPlaceholder {

    private MSPTProviderSpark mspt;

    public PlaceholderMSPT() {
        super("mspt");
    }

    @Override
    protected String getSub(Player player, String param) {
        if (mspt == null) {
            throw new RuntimeException("Cannot do mspt placeholder because spark is not installed");
        }

        return mspt.getValue(param).toString();
    }

    @Override
    public void init(Plugin plugin) {
        if (Bukkit.getPluginManager().isPluginEnabled("spark")) {
            mspt = new MSPTProviderSpark();
        } else {
            Bukkit.getLogger().warning("Spark is not installed!");
        }
    }
}
