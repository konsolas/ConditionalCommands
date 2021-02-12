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
        long currMillis = System.currentTimeMillis();
        Long millis = cooldowns.getIfPresent(param);
        if (millis == null) {
            return "43200";
        } else {
            long diff = TimeUnit.MILLISECONDS.toSeconds(currMillis - millis);
            if (diff < 1) {
                return "0";
            } else {
                return Long.toString(Math.min(diff, 43200));
            }
        }
    }
    
    public void putOnCooldown(String key) {
        cooldowns.put(key, System.currentTimeMillis());
    }
}
