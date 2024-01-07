package me.konsolas.conditionalcommands.providers;

import me.lucko.spark.api.Spark;
import me.lucko.spark.api.statistic.StatisticWindow;
import me.lucko.spark.api.statistic.types.DoubleStatistic;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;

public class TPSProviderSpark implements
        ParameteredProvider<Double> {
    private final DoubleStatistic<StatisticWindow.TicksPerSecond> tpsSpark;

    public TPSProviderSpark() {
        RegisteredServiceProvider<Spark> provider = Bukkit.getServicesManager().getRegistration(Spark.class);
        tpsSpark = provider.getProvider().tps();
    }

    @Override
    public Double getValue(String param) {
        StatisticWindow.TicksPerSecond window = parseWindow(param);
        return tpsSpark.poll(window);
    }

    private StatisticWindow.TicksPerSecond parseWindow(String param) {
        if (param == null || param.isEmpty()) {
            return StatisticWindow.TicksPerSecond.SECONDS_5; // Default value
        }

        int duration;
        String timeUnit;

        if (param.matches("\\d+[sm]")) { // Matches patterns like "5s", "10m", etc.
            duration = Integer.parseInt(param.substring(0, param.length() - 1));
            timeUnit = param.substring(param.length() - 1);
        } else if (param.matches("\\d+")) { // Matches patterns like "5", "10", etc.
            duration = Integer.parseInt(param);
            timeUnit = "s"; // Default to seconds
        } else if (param.equals("m")) {
            duration = 1;
            timeUnit = "m";
        } else {
            return StatisticWindow.TicksPerSecond.SECONDS_5; // Default value for unrecognized patterns
        }

        switch (timeUnit) {
            case "s":
                switch (duration) {
                    case 5:
                        return StatisticWindow.TicksPerSecond.SECONDS_5;
                    case 10:
                        return StatisticWindow.TicksPerSecond.SECONDS_10;
                    default:
                        return StatisticWindow.TicksPerSecond.SECONDS_5;
                }
            case "m":
                switch (duration) {
                    case 1:
                        return StatisticWindow.TicksPerSecond.MINUTES_1;
                    case 15:
                        return StatisticWindow.TicksPerSecond.MINUTES_15;
                    default:
                        return StatisticWindow.TicksPerSecond.MINUTES_1;
                }
            default:
                return StatisticWindow.TicksPerSecond.SECONDS_5; // Default value for unrecognized time unit
        }
    }
}
