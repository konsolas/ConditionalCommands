package me.konsolas.conditionalcommands.placeholders;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class AbstractParameteredPlaceholder implements Placeholder {
    private final Pattern pattern;

    AbstractParameteredPlaceholder(String base) {
        this.pattern = Pattern.compile("-" + base + ":([A-Za-z0-9%._]*\\[[^\\]]*\\])?-");
    }

    @Override
    public boolean shouldApply(String test) {
        return pattern.matcher(test).find();
    }

    protected abstract String getSub(Player player, String param);

    @Override
    public String doSubstitution(String input, Player player) {
        Matcher matcher = pattern.matcher(input);

        while (matcher.find()) {
            input = input.replaceAll(Pattern.quote(matcher.group()), getSub(player, matcher.group(1)));
        }

        return input;
    }

    @Override
    public void init(Plugin plugin) {
    }
}
