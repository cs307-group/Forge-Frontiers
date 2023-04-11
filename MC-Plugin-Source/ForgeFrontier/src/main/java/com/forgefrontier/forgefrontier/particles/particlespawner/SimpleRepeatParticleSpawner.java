package com.forgefrontier.forgefrontier.particles.particlespawner;

import com.forgefrontier.forgefrontier.ForgeFrontier;
import com.forgefrontier.forgefrontier.particles.designs.ParticleDesign;
import org.bukkit.Particle;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;

public class SimpleRepeatParticleSpawner {

    int delay;
    int duration;
    Runnable spawnfunc;
    ForgeFrontier plugin;
    BukkitScheduler scheduler;
    private int task;

    public SimpleRepeatParticleSpawner(Runnable spawnfunc, int delay, int duration) {
        this.spawnfunc = spawnfunc;
        this.delay = delay;
        this.duration = duration;
        this.plugin = ForgeFrontier.getInstance();
        this.scheduler = plugin.getServer().getScheduler();
    }

    public void run() {
        task = scheduler.scheduleSyncRepeatingTask(plugin,spawnfunc,0,delay);
        scheduler.scheduleSyncDelayedTask(plugin,() -> scheduler.cancelTask(task), duration);
    }
}
