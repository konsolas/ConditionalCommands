package me.konsolas.conditionalcommands.placeholders;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public abstract class AbstractPlaceholder implements Placeholder {
    private String matcher;

    AbstractPlaceholder(String matcher) {
        this.matcher = '-' + matcher + '-';
    }

    @Override
    public final boolean shouldApply(String test) {
        return test.contains(matcher);
    }

    @Override
    public final String doSubstitution(String input, Player player) {
        return input.replaceAll(matcher, Double.toString(getStat(player)));
    }

    @Override
    public void init(Plugin plugin) {
    }

    protected abstract double getStat(Player player);

    public class PlaceholderException extends RuntimeException {
        PlaceholderException(String desc, Throwable cause) {
            super(desc, cause);
        }
    }
}
