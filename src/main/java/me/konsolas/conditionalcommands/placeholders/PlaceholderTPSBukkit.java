package me.konsolas.conditionalcommands.placeholders;

import me.konsolas.conditionalcommands.providers.Provider;
import me.konsolas.conditionalcommands.providers.TPSProviderBukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class PlaceholderTPSBukkit extends AbstractStandardPlaceholder {
    private Provider<Double> tps;

    public PlaceholderTPSBukkit() {
        super("tps");
    }

    @Override
    protected double getStat(Player player) {
        return tps.getValue();
    }

    @Override
    public void init(Plugin plugin) {
        tps = new TPSProviderBukkit(plugin);
    }
}
