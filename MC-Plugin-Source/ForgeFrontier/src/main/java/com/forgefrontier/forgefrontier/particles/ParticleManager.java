package com.forgefrontier.forgefrontier.particles;

import com.forgefrontier.forgefrontier.ForgeFrontier;
import com.forgefrontier.forgefrontier.particles.gameparticles.CosmeticParticles;
import com.forgefrontier.forgefrontier.particles.gameparticles.GeneratorParticles;
import com.forgefrontier.forgefrontier.particles.gameparticles.MobParticles;
import com.forgefrontier.forgefrontier.particles.particlespawner.SimpleRepeatParticleSpawner;
import com.forgefrontier.forgefrontier.utils.Manager;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.*;
import java.util.logging.Level;

public class ParticleManager extends Manager {

    public static final List<Particle> allVanillaParticles =
            Collections.unmodifiableList(Arrays.asList(Particle.values()));
    private static final int NUM_VANILLA_PARTICLES = allVanillaParticles.size();
    private static final Random RANDOM = new Random();

    HashMap<UUID, HashMap<Integer, PlayerParticleTask>> playerParticles;
    HashMap<Particle, Integer> rawParticleToID;

    HashMap<Integer, FFParticle> gameParticleMap;
    HashMap<Integer, FFCosmeticParticle> cosmeticParticleMap;
    BukkitScheduler scheduler;


    public ParticleManager(ForgeFrontier plugin) {
        super(plugin);
        playerParticles = new HashMap<>();
        this.scheduler = plugin.getServer().getScheduler();
        rawParticleToID = new HashMap<>();
        for (int i = 0; i < allVanillaParticles.size(); i++) {
            rawParticleToID.put(allVanillaParticles.get(i), i);
        }
        gameParticleMap = new HashMap<>();
        cosmeticParticleMap = new HashMap<>();
    }

    @Override
    public void init() {
        // Pre-load particles
        GeneratorParticles.initGenParticles();
        MobParticles.init();
        CosmeticParticles.init();
    }

    @Override
    public void disable() {
        // Unload particles
        for (HashMap<Integer, PlayerParticleTask> pp : playerParticles.values()) {
            for (PlayerParticleTask ppt : pp.values()) {
                if (scheduler.isCurrentlyRunning(ppt.getTask())) {
                    scheduler.cancelTask(ppt.getTask());
                }
            }
        }
        playerParticles = null;

    }

    public void reload() {
    }

    public void addPlayerGeneratorParticle(UUID p, int task, String generatorProvider) {
        HashMap<Integer, PlayerParticleTask> ppts = playerParticles.get(p);
        if (ppts == null) {
            playerParticles.put(p,new HashMap<>());
            return;
        }
        int genNum = 0;
        for (PlayerParticleTask ppt : ppts.values()) {
            if (ppt.particleID >= 500 && ppt.particleID < 600) {
                genNum = Math.max(genNum, ppt.particleID);
            }
        }
        genNum++;
        PlayerParticleTask nppt = new PlayerParticleTask(task,genNum);
        nppt.setProvider(generatorProvider);
        ppts.put(-1, nppt);
    }


    public void addPlayerCosmetic(Player p, FFCosmeticParticle ffcp, int slot) {
        if (ffcp == null) {
            removePlayerParticleSlot(p.getUniqueId(),slot);
            return;
        }
        SimpleRepeatParticleSpawner srps = new SimpleRepeatParticleSpawner(
                () -> ffcp.playParticleAtLocation(p), ffcp.delay, -1);
        PlayerParticleTask ppt = new PlayerParticleTask(srps.run(),slot);
        addPlayerParticle(p.getUniqueId(),ppt);
    }

    /* Adds player particle to a specific slot */
    public void addPlayerParticle(UUID p, PlayerParticleTask task) {
        if (!playerParticles.containsKey(p)) {
            playerParticles.put(p,new HashMap<>());
        }
        HashMap<Integer, PlayerParticleTask> ppart = playerParticles.get(p);

        /* Cancel existing task in slot */
        if (ppart.containsKey(task.slotID) && ppart.get(task.slotID) != null) {
            int cancel = ppart.get(task.slotID).getTask();
            scheduler.cancelTask(ppart.get(task.slotID).getTask());
        }
        ppart.put(task.slotID, task);
    }

    public boolean removePlayerParticleSlot(UUID p, int slotID) {
        if (!playerParticles.containsKey(p)) { playerParticles.put(p,new HashMap<>()); return false;}
        HashMap<Integer, PlayerParticleTask> ppt = playerParticles.get(p);
        if (!ppt.containsKey(slotID) || ppt.get(slotID) == null) return false;
        PlayerParticleTask pt = ppt.get(slotID);
        if (pt != null) {

            scheduler.cancelTask(pt.getTask());
        }

        ppt.remove(slotID);
        return true;
    }


    public boolean removePlayerParticleTask(UUID p, int task) {
        if (!playerParticles.containsKey(p)) { playerParticles.put(p,new HashMap<>()); return false;}
        HashMap<Integer, PlayerParticleTask> tasks = playerParticles.get(p);

        return tasks.values().removeIf((e) -> e.task == task);
    }

    public void cancelAllPlayerParticles(UUID p) {
        HashMap<Integer, PlayerParticleTask> ppts = playerParticles.get(p);
        if (ppts == null) {
            playerParticles.put(p,new HashMap<>());
            return;
        }
        for (PlayerParticleTask ppt : ppts.values()) {
            if (ppt == null) continue;
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
        return rawParticleToID.get(p);
    }


    public void registerParticle(FFParticle particle) {
        this.gameParticleMap.put(particle.id, particle);
    }

    public void registerCosmeticParticle(FFCosmeticParticle particle) {
        this.cosmeticParticleMap.put(particle.id, particle);
        this.gameParticleMap.put(particle.id, particle);
    }

    public FFCosmeticParticle randCosParticle() {
        int cosParticle = randCosParticleID();
        return cosmeticParticleMap.get(cosParticle);
    }

    public int randCosParticleID() {
        Set<Integer> ids = cosmeticParticleMap.keySet();
        int r = RANDOM.nextInt(ids.size());
        int i = 0;
        for(int obj : ids)
        {
            if (i == r)
                return obj;
            i++;
        }
        return 0;
    }

    public FFParticle getParticleFromID(String sid) {
        int id = 0;
        try {
            id = Integer.parseInt(sid);
        } catch (NumberFormatException ignore) {}
        if (id == 0) return null;
        return gameParticleMap.get(id);
    }
    public FFParticle getParticleFromID(int id) {
        return gameParticleMap.get(id);
    }

    public FFCosmeticParticle getCosmeticParticle(String sid) {
        int id = 0;
        try {
            id = Integer.parseInt(sid);
        } catch (NumberFormatException ignore) {}
        if (id == 0) return null;
        return cosmeticParticleMap.get(id);
    }


}
