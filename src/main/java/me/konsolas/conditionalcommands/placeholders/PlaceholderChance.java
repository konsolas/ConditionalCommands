package me.konsolas.conditionalcommands.placeholders;

import org.bukkit.entity.Player;

import java.util.concurrent.ThreadLocalRandom;

public class PlaceholderChance extends AbstractParameteredPlaceholder {
    public PlaceholderChance() {
        super("chance");
    }

    @Override
    protected String getSub(Player player, String param) {
        if (!param.endsWith("%")) {
            throw new RuntimeException("Invalid chance parameter format, expecting <num>%");
        }

        double percent;
        try {
            percent = Double.parseDouble(param.substring(0, param.length() - 1));
        } catch (NumberFormatException e) {
            throw new RuntimeException("Invalid number format: " + param, e);
        }

        if (percent > 100 || percent < 0) {
            throw new RuntimeException("Expecting percentage between 0 and 100");
        }

        double random = ThreadLocalRandom.current().nextDouble() * 100;
        if (random < percent) {
            return "1.0";
        } else {
            return "0.0";
        }
    }
}
