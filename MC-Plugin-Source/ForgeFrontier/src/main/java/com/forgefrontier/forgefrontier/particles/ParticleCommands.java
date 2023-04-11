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
            pds.createStaticPoints(amount, dist);
        } else pds.createStaticPoints();
        ArrayList<Vector> points = pds.getNext();
        SimpleRepeatParticleSpawner sps = new SimpleRepeatParticleSpawner(()->{
            for (Vector v : points) {
                p.spawnParticle(Particle.PORTAL, p.getEyeLocation().add(v).add(0,3,0), 1, 0, 0, 0);
            }
        },10,1000);
        sps.run();
    }



}
