package me.konsolas.conditionalcommands;

import me.konsolas.conditionalcommands.placeholders.*;

enum Placeholder {
    PING(new PlaceholderPing()),
    TPS(new PlaceholderTPS()),
    TIME_ONLINE(new PlaceholderTimeOnline()),
    PLAYER_COUNT(new PlaceholderPlayerCount());

    private AbstractPlaceholder placeholder;

    Placeholder(AbstractPlaceholder placeholder) {
        this.placeholder = placeholder;
    }

    public AbstractPlaceholder getPlaceholder() {
        return placeholder;
    }
}
