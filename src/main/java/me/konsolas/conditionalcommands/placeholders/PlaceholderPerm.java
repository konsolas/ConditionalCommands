package me.konsolas.conditionalcommands.placeholders;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PlaceholderPerm implements Placeholder {
    private static final Pattern PERM_PATTERN = Pattern.compile("-perm:([A-Za-z0-9.]*)-");

    @Override
    public boolean shouldApply(String test) {
        return PERM_PATTERN.matcher(test).find();
    }

    @Override
    public String doSubstitution(String input, Player player) {
        Matcher matcher = PERM_PATTERN.matcher(input);

        while (matcher.find()) {
            input = input.replaceAll(Pattern.quote(matcher.group()), player.hasPermission(matcher.group(1)) ? "1.0" : "0.0");
        }

        return input;
    }

    @Override
    public void init(Plugin plugin) {
    }
}
