package me.konsolas.conditionalcommands.placeholders;

import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class PlaceholderPing extends AbstractPlaceholder {
    private Method getHandleMethod;
    private Field pingField;

    public PlaceholderPing() {
        super("ping");
    }

    @Override
    public double getStat(Player player) {
        try {
            if (getHandleMethod == null) {
                getHandleMethod = player.getClass().getDeclaredMethod("getHandle");
                getHandleMethod.setAccessible(true);
            }
            Object entityPlayer = getHandleMethod.invoke(player);
            if (pingField == null) {
                pingField = entityPlayer.getClass().getDeclaredField("ping");
                pingField.setAccessible(true);
            }
            int ping = pingField.getInt(entityPlayer);

            return ping > 0 ? ping : 0;
        } catch (Exception e) {
            return 0;
        }
    }
}
