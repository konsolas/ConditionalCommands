package me.konsolas.conditionalcommands.placeholders;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public interface Placeholder {

    /**
     * Checks whether the given string contains a pattern that matches this placeholder
     *
     * @param test input string
     * @return true if this placeholder should be applied to the string
     */
    boolean shouldApply(String test);

    /**
     * Replaces all instances of this placeholder in the input string with an appropriate statistic for the player
     *
     * @param input  string to replace
     * @param player player to use
     * @return a new string
     */
    String doSubstitution(String input, Player player);

    /**
     * Called when the server starts, allowing the placeholder to initialize tasks or counters.
     *
     * @param plugin instance of a bukkit plugin
     */
    void init(Plugin plugin);
}
