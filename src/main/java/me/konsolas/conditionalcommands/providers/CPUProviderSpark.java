package me.konsolas.conditionalcommands.providers;

import me.lucko.spark.api.Spark;
import me.lucko.spark.api.statistic.StatisticWindow;
import me.lucko.spark.api.statistic.types.DoubleStatistic;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CPUProviderSpark implements ParameteredProvider<Double> {

    private DoubleStatistic<StatisticWindow.CpuUsage> sys, proc;

    public CPUProviderSpark() {
        RegisteredServiceProvider<Spark> provider = Bukkit.getServicesManager().getRegistration(Spark.class);
        if (provider != null) {
            sys = provider.getProvider().cpuSystem();
            proc = provider.getProvider().cpuProcess();
        }
    }

    public Double getValue(String param) {
        Command command = parseCommand(param);
        if (command == null) {
            return proc.poll(StatisticWindow.CpuUsage.SECONDS_10);
        }

        switch (command.function) {
            case "sys":
                return sys.poll(command.window);
            case "proc":
                return proc.poll(command.window);
            default:
                throw new RuntimeException("Could not parse CPU command for input: \"" + param + "\"");
        }
    }

    private Command parseCommand(String param) {
        Pattern pattern = Pattern.compile("(\\w+)\\[(\\d+)([sm])?\\]");
        Matcher matcher = pattern.matcher(param);

        if (matcher.matches()) {
            String function = matcher.group(1);
            String arg = matcher.group(2);
            String timeUnit = matcher.group(3);
            StatisticWindow.CpuUsage window = parseWindow(arg + timeUnit);
            int percentile = function.equals("percentile") ? Integer.parseInt(arg) : 95;
            return new Command(function, window, percentile);
        }
        return null;
    }

    private StatisticWindow.CpuUsage parseWindow(String arg) {
        switch (arg) {
            case "10s":
                return StatisticWindow.CpuUsage.SECONDS_10;
            case "1m":
                return StatisticWindow.CpuUsage.MINUTES_1;
            case "15m":
                return StatisticWindow.CpuUsage.MINUTES_15;
            default:
                throw new IllegalArgumentException("Invalid time unit " + arg);
        }
    }

    private static class Command {
        String function;
        StatisticWindow.CpuUsage window;
        int percentile;

        Command(String function, StatisticWindow.CpuUsage window, int percentile) {
            this.function = function;
            this.window = window;
            this.percentile = percentile;
        }
    }
}
