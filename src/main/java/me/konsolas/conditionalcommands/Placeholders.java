package me.konsolas.conditionalcommands;

import me.konsolas.conditionalcommands.placeholders.*;

enum Placeholders {
    PING(new PlaceholderPing()),
    TPS(null),
    MSPT(new PlaceholderMSPT()),
    CPU(new PlaceholderCPU()),
    TIME_ONLINE(new PlaceholderTimeOnline()),
    PLAYER_COUNT(new PlaceholderPlayerCount()),
    UPTIME(new PlaceholderUptime()),
    PERM(new PlaceholderPerm()),
    PERM_COUNT(new PlaceholderPermCount()),
    AACVL(new PlaceholderAACVL()),
    CHANCE(new PlaceholderChance()),
    COOLDOWN(new PlaceholderCooldown());

    private Placeholder placeholder;

    Placeholders(Placeholder placeholder) {
        this.placeholder = placeholder;
    }

    public Placeholder getPlaceholder() {
        return placeholder;
    }

    public void setPlaceHolder(Placeholder placeholder) {
        this.placeholder = placeholder;
    }
}
