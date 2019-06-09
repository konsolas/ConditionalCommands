package me.konsolas.conditionalcommands.placeholders;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class PlaceholderAACVL extends AbstractParameteredPlaceholder {

    private Map<String, Object> hackTypes = new HashMap<>();
    private Method getAPI;
    private Method getViolationLevel;

    public PlaceholderAACVL() {
        super("aacvl");
    }

    @Override
    protected String getSub(Player player, String param) {
        try {
            Object obj = getAPI.invoke(null);
            int VL = (int) getViolationLevel.invoke(obj, player, hackTypes.get(param.toLowerCase()));
            return Integer.toString(VL);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException("Failed to handle AAC placeholder for " + param, e);
        }
    }

    @Override
    public void init(Plugin plugin) {
        if (plugin.getServer().getPluginManager().getPlugin("AAC") == null) {
            return; // AAC not installed
        }

        try {
            Class<?> hackTypeClass = Class.forName("me.konsolas.aac.api.HackType");

            for (Field field : hackTypeClass.getDeclaredFields()) {
                if (field.getType().equals(hackTypeClass)) {
                    hackTypes.put(field.getName().toLowerCase(), field.get(null));
                }
            }

            getAPI = Class.forName("me.konsolas.aac.api.AACAPIProvider")
                    .getDeclaredMethod("getAPI");
            getViolationLevel = Class.forName("me.konsolas.aac.api.AACAPI")
                    .getDeclaredMethod("getViolationLevel", Player.class, hackTypeClass);
        } catch (ClassNotFoundException | IllegalAccessException | NoSuchMethodException e) {
            throw new RuntimeException("Failed to initialise AAC placeholders", e);
        }
    }
}
