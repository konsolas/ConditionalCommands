package me.konsolas.conditionalcommands.placeholders;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.bukkit.entity.Player;

import java.util.concurrent.TimeUnit;

public class PlaceholderCooldown extends AbstractParameteredPlaceholder {
    private final Cache<String, Long> cooldowns = CacheBuilder.newBuilder()
            .expireAfterWrite(12, TimeUnit.HOURS)
            .concurrencyLevel(1)
            .build();

    public PlaceholderCooldown() {
        super("cooldown");
    }

    @Override
    protected String getSub(Player player, String param) {
        String[] split = param.split("#");
        if (split.length != 2) {
            throw new RuntimeException("cooldown parameter '" + param + "' has invalid formatting - exactly one '#' needed");
        }
        String key = split[0];
        long seconds = 0;
        try {
            seconds = Long.parseLong(split[1]);
        } catch (NumberFormatException e) {
            throw new RuntimeException("cooldown parameter '" + param + "' has invalid formatting - number after '#' needed, but got: '" + split[1] + "'");
        }
        long currMillis = System.currentTimeMillis();
        Long millis = cooldowns.getIfPresent(key);
        if (millis == null) {
            return "0";
        } else {
            long diff = TimeUnit.MILLISECONDS.toSeconds(currMillis - millis);
            if (diff < 1) {
                cooldowns.invalidate(key);
                return "0";
            } else {
                return Long.toString(seconds - diff);
            }
        }
    }
    
    public void putOnCooldown(String key) {
        cooldowns.put(key, System.currentTimeMillis());
    }
}
