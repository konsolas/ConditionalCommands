package me.konsolas.conditionalcommands.placeholders;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public abstract class AbstractPlaceholder {
    private String matcher;

    public AbstractPlaceholder(String matcher) {
        this.matcher = '-' + matcher + '-';
    }

    public final boolean shouldApply(String test) {
        return test.contains(matcher);
    }

    public final String doSubstitution(String input, Player player) {
        return input.replaceAll(matcher, Double.toString(getStat(player)));
    }

    public abstract double getStat(Player player);

    public void init(Plugin plugin) {
    }

    public class PlaceholderException extends RuntimeException {
        public PlaceholderException(String desc, Throwable cause) {
            super(desc, cause);
        }
    }
}
