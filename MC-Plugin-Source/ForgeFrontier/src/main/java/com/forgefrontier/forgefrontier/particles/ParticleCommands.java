package com.forgefrontier.forgefrontier.particles;
import com.forgefrontier.forgefrontier.ForgeFrontier;
import com.forgefrontier.forgefrontier.particles.designs.*;
import com.forgefrontier.forgefrontier.particles.gameparticles.MobParticles;
import com.forgefrontier.forgefrontier.particles.particlespawner.SimpleRepeatParticleSpawner;
import org.bukkit.Particle;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.DefaultFor;
import revxrsal.commands.annotation.Subcommand;
import revxrsal.commands.annotation.*;

import java.util.ArrayList;
import java.util.Random;

@Command({"ffparticle"})
public class ParticleCommands {
    private final ForgeFrontier plugin;
    private final ParticleManager particleManager;
    public ParticleCommands(ForgeFrontier plugin) {
        this.plugin = plugin;
        this.particleManager = plugin.getParticleManager();
    }

    @DefaultFor({"ffparticle"})
    public void ffParticleDefault(CommandSender cs) {
        cs.sendMessage("Particle Commands");
    }

    @Subcommand({"play"})
    @AutoComplete("@idparticle")
    public void playByID(CommandSender cs, String id) {
        if (!(cs instanceof Player p)) {
            cs.sendMessage("Only players can use this command!");
            return;
        }
        FFParticle fp = particleManager.getParticleFromID(id);
        if (fp == null) {
            p.sendMessage("Cannot find specified particle!");
            return;
        }
        SimpleRepeatParticleSpawner sps;
        if (fp instanceof FFCosmeticParticle ffcp) {
             sps = new SimpleRepeatParticleSpawner(
                    () -> ffcp.playParticleAtLocation(p), ffcp.delay, 200);
        } else {
            sps = new SimpleRepeatParticleSpawner(() -> fp.playAtPlayer(p), 5, 100);
        }
        sps.run();
        particleManager.addPlayerParticle(p.getUniqueId(),
                new PlayerParticleTask(sps.getTask(), -1, "GENERIC"));

    }


    @Subcommand({"simple"})
    public void test1Particle(CommandSender cs, @Default("20") Integer amount, @Default("1") Float dist) {
        if (!(cs instanceof Player p)) {
            cs.sendMessage("Only players can use this command!");
            return;
        }
        ParticleDesignSphere pds = new ParticleDesignSphere();
        if (amount != null) {
            pds.setDistance(dist);
            pds.createStaticPoints(amount);
        } else pds.createStaticPoints();
        FFParticle ffParticle = new FFParticle(pds);
        ffParticle.setParticle(Particle.PORTAL);
        SimpleRepeatParticleSpawner sps = new SimpleRepeatParticleSpawner(
                () -> ffParticle.playParticleAtLocation(p.getWorld(), p.getEyeLocation()), 5, 1000);
        sps.run();
        particleManager.addPlayerParticle(p.getUniqueId(), new PlayerParticleTask(sps.getTask(), -1, "GENERIC"));
    }
    @Subcommand({"cube"})
    public void testCubeParticle(CommandSender cs, @Default("20") Integer edgeParticles, @Default("2") Integer size) {
        if (!(cs instanceof Player p)) {
            cs.sendMessage("Only players can use this command!");
            return;
        }
        ParticleDesignCube pdc = new ParticleDesignCube(edgeParticles, size);
        pdc.createStaticPoints();
        FFParticle ffParticle = new FFParticle(new CubeDesignInstance(pdc));
        Particle part = particleManager.getRandomParticle();
        p.sendMessage("Playing particle: " + part.toString());
        ffParticle.setParticle(part);
        SimpleRepeatParticleSpawner sps = new SimpleRepeatParticleSpawner(
                () -> ffParticle.playParticleAtLocation(p.getWorld(), p.getEyeLocation()), 5, -1);
        sps.run();
        particleManager.addPlayerParticle(p.getUniqueId(), new PlayerParticleTask(sps.getTask(), -1, "GENERIC"));
    }
    @Subcommand({"chain"})
    public void testChainParticle(CommandSender cs, @Default("20") Integer amount) {
        if (!(cs instanceof Player p)) {
            cs.sendMessage("Only players can use this command!");
            return;
        }
        ParticleDesignChain pdc = new ParticleDesignChain(30);


        if (amount != null) {
            pdc.setNum_points(amount);
        }
        pdc.createStaticPoints();

        FFParticle ffParticle = new FFParticle(pdc);
        Particle part = particleManager.getRandomParticle();
        p.sendMessage("Playing particle: " + part.toString());
        ffParticle.setParticle(part);
        SimpleRepeatParticleSpawner sps = new SimpleRepeatParticleSpawner(
                () -> ffParticle.playParticleAtLocation(p.getWorld(), p.getEyeLocation()), 5, -1);
        sps.run();
        particleManager.addPlayerParticle(p.getUniqueId(), new PlayerParticleTask(sps.getTask(), -1, "GENERIC"));
    }

    @Subcommand({"halo"})
    public void testHaloParticle(CommandSender cs, @Default("1") float radius, @Default("1") float offset) {
        if (!(cs instanceof Player p)) {
            cs.sendMessage("Only players can use this command!");
            return;
        }
        ParticleDesignHalo pdh = new ParticleDesignHalo(20, radius, offset);
        pdh.createStaticPoints();
        FFParticle ffParticle = new FFParticle(pdh);
        Particle part = particleManager.getRandomParticle();
        p.sendMessage("Playing particle: " + part.toString());
        ffParticle.setParticle(part);
        SimpleRepeatParticleSpawner sps = new SimpleRepeatParticleSpawner(
                () -> ffParticle.playParticleAtLocation(p.getWorld(), p.getEyeLocation()), 5, -1);
        sps.run();
        particleManager.addPlayerParticle(p.getUniqueId(), new PlayerParticleTask(sps.getTask(), -1, "GENERIC"));
    }

    @Subcommand({"addrand"})
    public void addRandomParticle(CommandSender cs, @Default("2") Integer distance) {
        if (!(cs instanceof Player p)) {
            cs.sendMessage("Only players can use this command!");
            return;
        }
        Particle part = particleManager.getRandomParticle();
        p.sendMessage("Playing particle: " + part.toString());
        ParticleDesignSphere pds = new ParticleDesignSphere();
        pds.setDistance(distance);
        pds.createStaticPoints(100);
        FFParticle ffParticle = new FFParticle(pds);
        ffParticle.setParticle(part);
        SimpleRepeatParticleSpawner sps = new SimpleRepeatParticleSpawner(
                () -> ffParticle.playParticleAtLocation(p.getWorld(), p.getEyeLocation()), 5, -1);
        sps.run();
        int task = sps.getTask();
        particleManager.addPlayerParticle(p.getUniqueId(), new PlayerParticleTask(task, -1, "GENERIC"));
    }
    @Subcommand({"clear"})
    public void clearParticle(CommandSender cs) {
        if (!(cs instanceof Player p)) {
            cs.sendMessage("Only players can use this command!");
            return;
        }
        particleManager.cancelAllPlayerParticles(p.getUniqueId());
    }
    @Subcommand({"cosmetic"})
    public void randCosmetic(CommandSender cs) {
        if (!(cs instanceof Player p)) {
            cs.sendMessage("Only players can use this command!");
            return;
        }
        FFCosmeticParticle randParticle = particleManager.randCosParticle();
        SimpleRepeatParticleSpawner srps = new SimpleRepeatParticleSpawner(
                () -> randParticle.playParticleAtLocation(p),
                10, 3000);
        particleManager.addPlayerParticle(p.getUniqueId(), new PlayerParticleTask(srps.run(), -1, "GENERIC"));

    }
    @Subcommand({"chickimmune"})
    public void chickImmune(CommandSender cs) {
        if (!(cs instanceof Player p)) {
            cs.sendMessage("Only players can use this command!");
            return;
        }
        SimpleRepeatParticleSpawner srps = new SimpleRepeatParticleSpawner(
                () -> MobParticles.CHICKBOSS_IMMUNE_PARTICLE.playParticleAtLocation(p.getWorld(), p.getLocation()),
                10, 5000);
        particleManager.addPlayerParticle(p.getUniqueId(), new PlayerParticleTask(srps.run(), -1, "GENERIC"));
    }

    @Subcommand({"chickegg"})
    public void chickEgg(CommandSender cs) {
        if (!(cs instanceof Player p)) {
            cs.sendMessage("Only players can use this command!");
            return;
        }
        SimpleRepeatParticleSpawner srps = new SimpleRepeatParticleSpawner(
                () -> MobParticles.CHICKBOSS_EGG_PARTICLE.playParticleAtLocation(p.getWorld(), p.getLocation().add(0,1,0)),
                10, 5000);
        particleManager.addPlayerParticle(p.getUniqueId(), new PlayerParticleTask(srps.run(), -1, "GENERIC"));
    }


}
