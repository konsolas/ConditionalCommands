package me.konsolas.conditionalcommands.placeholders;

import me.konsolas.conditionalcommands.providers.ParameteredProvider;
import me.konsolas.conditionalcommands.providers.TPSProviderSpark;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class PlaceholderTPSSpark extends AbstractParameteredPlaceholder {
    private ParameteredProvider<Double> tps;

    public PlaceholderTPSSpark() {
        super("tps");
    }

    @Override
    protected String getSub(Player player, String param) {
        return tps.getValue(param).toString();
    }

    @Override
    public void init(Plugin plugin) {
        if (Bukkit.getPluginManager().isPluginEnabled("spark")) {
            tps = new TPSProviderSpark();
        }
    }
}
