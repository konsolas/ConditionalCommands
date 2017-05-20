package me.konsolas.conditionalcommands;

import me.konsolas.conditionalcommands.placeholders.*;

enum Placeholders {
    PING(new PlaceholderPing()),
    TPS(new PlaceholderTPS()),
    TIME_ONLINE(new PlaceholderTimeOnline()),
    PLAYER_COUNT(new PlaceholderPlayerCount()),
    UPTIME(new PlaceholderUptime()),
    PERM(new PlaceholderPerm()),
    AACVL(new PlaceholderAACVL());

    private Placeholder placeholder;

    Placeholders(Placeholder placeholder) {
        this.placeholder = placeholder;
    }

    public Placeholder getPlaceholder() {
        return placeholder;
    }
}
