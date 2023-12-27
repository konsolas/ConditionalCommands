package me.konsolas.conditionalcommands.providers;

import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayDeque;
import java.util.Collections;
import java.util.Deque;

public class TPSProviderBukkit extends BukkitRunnable implements Provider<Double> {
    private final int resolution = 40;
    private final Deque<Long> tickIntervals;
    private long lastTick;

    public TPSProviderBukkit(Plugin plugin) {
        lastTick = System.currentTimeMillis();
        tickIntervals = new ArrayDeque<>(Collections.nCopies(resolution, 50L));
        this.runTaskTimer(plugin, 1, 1);
    }

    public Double getValue() {
        try {
            double tps = 1000D / getDelta();
            return Math.min(tps, 20D);
        } catch (Exception e) {
            return 20D;
        }
    }

    @Override
    public void run() {
        long curr = System.currentTimeMillis();
        long delta = curr - lastTick;
        lastTick = curr;
        tickIntervals.removeFirst();
        tickIntervals.addLast(delta);
    }

    private double getDelta() {
        int base = 0;
        for (long delta : tickIntervals) {
            base += (int) delta;
        }
        return (double) base / (double) resolution;
    }
}
