package dev.olegsyrotyuk.spleef.util;

import dev.olegsyrotyuk.spleef.Spleef;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.scheduler.BukkitRunnable;

@Setter
@Getter
public class Timer {

    private int time;
    private Runnable everySecond;
    private Runnable onFinish;
    private BukkitRunnable task;

    public Timer(int time) {
        this.time = time;
    }

    public void start() {
        task = new BukkitRunnable() {
            @Override
            public void run() {
                if (time > 0) {
                    everySecond.run();
                    time--;
                } else {
                    onFinish.run();
                    cancel();
                }
            }
        };
        task.runTaskTimer(Spleef.getInstance(), 0L, 20L);
    }

    public void cancel() {
        if (task != null) {
            task.cancel();
        }
    }
}
