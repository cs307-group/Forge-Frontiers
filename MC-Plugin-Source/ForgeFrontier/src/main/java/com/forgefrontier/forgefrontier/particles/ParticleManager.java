package com.forgefrontier.forgefrontier.particles;

import com.forgefrontier.forgefrontier.ForgeFrontier;
import com.forgefrontier.forgefrontier.utils.Manager;
import org.bukkit.Particle;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.*;
import java.util.logging.Level;

public class ParticleManager extends Manager {

    public static final List<Particle> allVanillaParticles =
            Collections.unmodifiableList(Arrays.asList(Particle.values()));
    private static final int NUM_VANILLA_PARTICLES = allVanillaParticles.size();
    private static final Random RANDOM = new Random();

    HashMap<UUID, ArrayList<PlayerParticleTask>> playerParticles;
    HashMap<Particle, Integer> particleToID;
    BukkitScheduler scheduler;


    public ParticleManager(ForgeFrontier plugin) {
        super(plugin);
        playerParticles = new HashMap<>();
        this.scheduler = plugin.getServer().getScheduler();
        for (int i = 0; i < allVanillaParticles.size(); i++) {
            particleToID.put(allVanillaParticles.get(i), i);
        }
    }

    @Override
    public void init() {
        // Load particles

    }

    @Override
    public void disable() {
        // Unload particles
        for (ArrayList<PlayerParticleTask> pp : playerParticles.values()) {
            for (PlayerParticleTask ppt : pp) {
                if (scheduler.isCurrentlyRunning(ppt.getTask())) {
                    scheduler.cancelTask(ppt.getTask());
                }
            }
        }
        playerParticles = null;

    }

    public void reload() {
    }

    public void addPlayerParticle(UUID p, PlayerParticleTask task) {
        if (!playerParticles.containsKey(p)) {
            playerParticles.put(p,new ArrayList<>());
        }
        playerParticles.get(p).add(task);
    }

    public boolean removePlayerParticleID(UUID p, int particleID) {
        if (!playerParticles.containsKey(p)) { playerParticles.put(p,new ArrayList<>()); return false;}
        ArrayList<PlayerParticleTask> tasks = playerParticles.get(p);
        return tasks.removeIf((e) -> e.particleID == particleID);
    }

    public boolean removePlayerParticleTask(UUID p, int task) {
        if (!playerParticles.containsKey(p)) { playerParticles.put(p,new ArrayList<>()); return false;}
        ArrayList<PlayerParticleTask> tasks = playerParticles.get(p);
        return tasks.removeIf((e) -> e.task == task);
    }

    public void cancelAllPlayerParticles(UUID p) {
        ArrayList<PlayerParticleTask> ppts = playerParticles.get(p);
        if (ppts == null) {
            playerParticles.put(p,new ArrayList<>());
            return;
        }
        for (PlayerParticleTask ppt : ppts) {
            plugin.getLogger().log(Level.INFO, "Clearing Particle Task: " + ppt.getTask());
            if (scheduler.isCurrentlyRunning(ppt.getTask()) || scheduler.isQueued(ppt.getTask())) {
                scheduler.cancelTask(ppt.getTask());
            }
        }
        ppts.clear();
    }

    public Particle getRandomParticle() {
        return allVanillaParticles.get(RANDOM.nextInt(NUM_VANILLA_PARTICLES));
    }

    public int getParticleID(Particle p) {
        if (p == null) return -1;
        return particleToID.get(p);
    }


}
