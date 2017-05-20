package me.konsolas.conditionalcommands.placeholders;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayDeque;
import java.util.Collections;
import java.util.Deque;

public class PlaceholderTPS extends AbstractStandardPlaceholder {
    private TPS tps;

    public PlaceholderTPS() {
        super("tps");
    }

    @Override
    public void init(Plugin plugin) {
        tps = new TPS(plugin);
    }

    @Override
    public double getStat(Player player) {
        return tps.getCurrentTPS();
    }

    private static class TPS extends BukkitRunnable {
        private int resolution = 40;
        private long lastTick;
        private Deque<Long> tickIntervals;

        private TPS(Plugin plugin) {
            lastTick = System.currentTimeMillis();
            tickIntervals = new ArrayDeque<>(Collections.nCopies(resolution, 50L));
            this.runTaskTimer(plugin, 1, 1);
        }

        double getCurrentTPS() {
            try {
                double tps = 1000D / getDelta();
                return tps > 20D ? 20D : tps;
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
                base += delta;
            }
            return (double) base / (double) resolution;
        }
    }
}
