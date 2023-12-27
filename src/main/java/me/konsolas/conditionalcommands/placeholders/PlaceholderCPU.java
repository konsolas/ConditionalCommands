package me.konsolas.conditionalcommands.placeholders;

import me.konsolas.conditionalcommands.providers.CPUProviderSpark;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class PlaceholderCPU extends AbstractParameteredPlaceholder {

    private CPUProviderSpark cpu;

    public PlaceholderCPU() {
        super("cpu");
    }

    @Override
    protected String getSub(Player player, String param) {
        if (cpu == null) {
            throw new RuntimeException("Cannot do cpu placeholder because spark is not installed");
        }

        return cpu.getValue(param).toString();
    }

    @Override
    public void init(Plugin plugin) {
        if (Bukkit.getPluginManager().isPluginEnabled("spark")) {
            cpu = new CPUProviderSpark();
        } else {
            Bukkit.getLogger().warning("Spark is not installed!");
        }
    }
}
