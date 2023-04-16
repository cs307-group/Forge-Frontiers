package com.forgefrontier.forgefrontier.particles;
import com.forgefrontier.forgefrontier.ForgeFrontier;
import com.forgefrontier.forgefrontier.particles.designs.ParticleDesignSphere;
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

    @Subcommand({"t1"})
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
        Random r = new Random();
        particleManager.addPlayerParticle(p.getUniqueId(), new PlayerParticleTask(task, r.nextInt()));
    }
    @Subcommand({"clear"})
    public void clearParticle(CommandSender cs) {
        if (!(cs instanceof Player p)) {
            cs.sendMessage("Only players can use this command!");
            return;
        }
        particleManager.cancelAllPlayerParticles(p.getUniqueId());
    }




}
