package me.darkmun.blockcitytycoonfoundry;

import org.bukkit.Bukkit;

public class Task {
    private static final long MILLISECONDS_IN_TICK = 50;
    private boolean works = false;
    private boolean paused = false;
    private long remainingTimeToEnd = 0;
    private final long timeToEnd;
    private int taskID;
    private long runTimeMS;
    private final Runnable task;

    public Task(Runnable task, long timeToEnd) {
        this.task = task;
        this.timeToEnd = timeToEnd;
    }

    public void run() {
        works = true;

        remainingTimeToEnd = timeToEnd;
        taskID = Bukkit.getScheduler().runTaskLater(BlockCityTycoonFoundry.getPlugin(), task, remainingTimeToEnd).getTaskId();
        runTimeMS = System.currentTimeMillis();
    }

    public void pause() {
        if (works) {
            long stopTimeMS = System.currentTimeMillis();
            long timeFromRunningMS = stopTimeMS - runTimeMS;  // milliseconds
            long timeFromRunningInTicks = timeFromRunningMS/MILLISECONDS_IN_TICK;
            if ((remainingTimeToEnd - timeFromRunningInTicks) > 0) {
                Bukkit.getScheduler().cancelTask(taskID);
                remainingTimeToEnd -= timeFromRunningInTicks;
                paused = true;
            } else {
                works = false;
            }
        }
    }

    public void continueTask() {
        if (paused) {
            taskID = Bukkit.getScheduler().runTaskLater(BlockCityTycoonFoundry.getPlugin(), task, remainingTimeToEnd).getTaskId();
            runTimeMS = System.currentTimeMillis();
            paused = false;
        }
    }

    public void stop() {
        if (works && !paused) {
            Bukkit.getScheduler().cancelTask(taskID);
            remainingTimeToEnd = 0;
            works = false;
        }
    }
}
