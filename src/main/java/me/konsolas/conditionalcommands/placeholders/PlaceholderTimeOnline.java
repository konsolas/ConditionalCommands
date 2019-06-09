package me.konsolas.conditionalcommands.placeholders;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.Map;

public class PlaceholderTimeOnline extends AbstractStandardPlaceholder implements Listener {

    private final Map<Player, Long> loginTime = new HashMap<>();

    public PlaceholderTimeOnline() {
        super("time_online");
    }

    @Override
    public void init(Plugin plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);

        // For /reload compatibility
        for (Player player : plugin.getServer().getOnlinePlayers()) {
            loginTime.put(player, System.currentTimeMillis());
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        loginTime.put(event.getPlayer(), System.currentTimeMillis());
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        loginTime.remove(event.getPlayer());
    }

    @EventHandler
    public void onPlayerKick(PlayerKickEvent event) {
        loginTime.remove(event.getPlayer());
    }

    @Override
    public double getStat(Player player) {
        return System.currentTimeMillis() - loginTime.get(player);
    }

}