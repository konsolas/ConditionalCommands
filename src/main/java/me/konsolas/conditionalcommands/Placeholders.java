package me.konsolas.conditionalcommands;

import me.konsolas.conditionalcommands.placeholders.*;

enum Placeholders {
    PING(new PlaceholderPing()),
    TPS(new PlaceholderTPS()),
    TIME_ONLINE(new PlaceholderTimeOnline()),
    PLAYER_COUNT(new PlaceholderPlayerCount()),
    UPTIME(new PlaceholderUptime());

    private AbstractPlaceholder placeholder;

    Placeholders(AbstractPlaceholder placeholder) {
        this.placeholder = placeholder;
    }

    public AbstractPlaceholder getPlaceholder() {
        return placeholder;
    }
}
