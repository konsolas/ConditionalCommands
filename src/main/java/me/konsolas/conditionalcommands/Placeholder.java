package me.konsolas.conditionalcommands;

import me.konsolas.conditionalcommands.placeholders.AbstractPlaceholder;
import me.konsolas.conditionalcommands.placeholders.PlaceholderPing;
import me.konsolas.conditionalcommands.placeholders.PlaceholderTPS;
import me.konsolas.conditionalcommands.placeholders.PlaceholderTimeOnline;

public enum Placeholder {
    PING(new PlaceholderPing()),
    TPS(new PlaceholderTPS()),
    TIME_ONLINE(new PlaceholderTimeOnline()),;

    private AbstractPlaceholder placeholder;

    Placeholder(AbstractPlaceholder placeholder) {
        this.placeholder = placeholder;
    }

    public AbstractPlaceholder getPlaceholder() {
        return placeholder;
    }
}
