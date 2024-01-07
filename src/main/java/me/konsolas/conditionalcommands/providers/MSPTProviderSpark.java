package me.konsolas.conditionalcommands.providers;

import me.lucko.spark.api.Spark;
import me.lucko.spark.api.statistic.StatisticWindow;
import me.lucko.spark.api.statistic.misc.DoubleAverageInfo;
import me.lucko.spark.api.statistic.types.GenericStatistic;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MSPTProviderSpark implements ParameteredProvider<Double> {

    private GenericStatistic<DoubleAverageInfo, StatisticWindow.MillisPerTick> msptSpark;

    public MSPTProviderSpark() {
        RegisteredServiceProvider<Spark> provider = Bukkit.getServicesManager().getRegistration(Spark.class);
        if (provider != null) {
            msptSpark = provider.getProvider().mspt();
        }
    }

    public Double getValue(String param) {
        Command command = parseCommand(param);
        if (command == null) {
            return msptSpark.poll(StatisticWindow.MillisPerTick.SECONDS_10).max();
        }

        switch (command.function) {
            case "max":
                return msptSpark.poll(command.window).max();
            case "mean":
                return msptSpark.poll(command.window).mean();
            case "min":
                return msptSpark.poll(command.window).min();
            case "median":
                return msptSpark.poll(command.window).median();
            case "percentile":
                return msptSpark.poll(command.window).percentile(command.percentile);
            default:
                throw new RuntimeException("Could not parse MSPT command for input: \"" + param + "\"");
        }
    }

    private Command parseCommand(String param) {
        Pattern pattern = Pattern.compile("(\\w+)\\[(\\d+)([sm])?\\]");
        Matcher matcher = pattern.matcher(param);

        if (matcher.matches()) {
            String function = matcher.group(1);
            String arg = matcher.group(2);
            String timeUnit = matcher.group(3);
            StatisticWindow.MillisPerTick window = parseWindow(arg, timeUnit);
            int percentile = function.equals("percentile") ? Integer.parseInt(arg) : 95;
            return new Command(function, window, percentile);
        }
        return null;
    }

    private StatisticWindow.MillisPerTick parseWindow(String arg, String timeUnit) {
        if (arg == null || timeUnit == null) {
            return StatisticWindow.MillisPerTick.SECONDS_10;
        }

        int duration = Integer.parseInt(arg);
        switch (timeUnit) {
            case "s":
                return duration == 10 ? StatisticWindow.MillisPerTick.SECONDS_10 : null;
            case "m":
                return duration == 1 ? StatisticWindow.MillisPerTick.MINUTES_1 : null;
            default:
                return StatisticWindow.MillisPerTick.SECONDS_10;
        }
    }

    private static class Command {
        String function;
        StatisticWindow.MillisPerTick window;
        int percentile;

        Command(String function, StatisticWindow.MillisPerTick window, int percentile) {
            this.function = function;
            this.window = window;
            this.percentile = percentile;
        }
    }
}
