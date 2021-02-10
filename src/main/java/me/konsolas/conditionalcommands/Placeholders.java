package me.konsolas.conditionalcommands;

import me.konsolas.conditionalcommands.placeholders.*;

enum Placeholders {
    PING(new PlaceholderPing()),
    TPS(new PlaceholderTPS()),
    TIME_ONLINE(new PlaceholderTimeOnline()),
    PLAYER_COUNT(new PlaceholderPlayerCount()),
    UPTIME(new PlaceholderUptime()),
    PERM(new PlaceholderPerm()),
    PERM_COUNT(new PlaceholderPermCount()),
    AACVL(new PlaceholderAACVL()),
    CHANCE(new PlaceholderChance());

    private Placeholder placeholder;

    Placeholders(Placeholder placeholder) {
        this.placeholder = placeholder;
    }

    public Placeholder getPlaceholder() {
        return placeholder;
    }
}
