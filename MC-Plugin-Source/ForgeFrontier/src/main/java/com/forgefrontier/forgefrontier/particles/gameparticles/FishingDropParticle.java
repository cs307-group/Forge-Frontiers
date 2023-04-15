package com.forgefrontier.forgefrontier.particles.gameparticles;

import com.forgefrontier.forgefrontier.particles.particlespawner.SimpleRepeatParticleSpawner;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;

/**
 * Color changes with rarity
 * Intensity changes with rarity
 */
public class FishingDropParticle {

    Location loc;
    int rarity;
    World w;
    int duration;
    int delay = 2;
    public FishingDropParticle(int rarity, World w, Location loc, int duration) {
        this.rarity = rarity;
        this.loc = loc;
        this.w = w;
        this.duration = duration;
    }
    public FishingDropParticle(int rarity, World w, Location loc, int delay, int duration) {
        this.rarity = rarity;
        this.loc = loc;
        this.w = w;
        this.duration = duration;
        this.delay = delay;
    }
    public void spawnParticle() {
        new SimpleRepeatParticleSpawner(this::spawnOneParticle, delay, duration).run();
    }
    public void spawnOneParticle() {
        if (rarity == 0) {
            w.spawnParticle(Particle.SPELL_MOB_AMBIENT, loc, 10);
        } else if (rarity == 1) {
            Particle.DustOptions dustOptions = new Particle.DustOptions(
                    Color.fromRGB(1, 140, 17), 2.0F);
            w.spawnParticle(Particle.REDSTONE, loc, 30, 0.5,0.5,0.5, dustOptions);
        } else if (rarity == 2) {
            Particle.DustOptions dustOptions = new Particle.DustOptions(
                    Color.fromRGB(42, 166, 255), 2.0F);
            w.spawnParticle(Particle.REDSTONE, loc, 40, 1.0,1.0,1.0, dustOptions);
            w.spawnParticle(Particle.SOUL_FIRE_FLAME,loc,10,3,3,3);
        } else if (rarity == 3) {
            w.spawnParticle(Particle.REVERSE_PORTAL,loc,100);
        } else if (rarity == 4) {
            w.spawnParticle(Particle.CRIMSON_SPORE,loc,100,0.5,0.5,0.5);
        } else {
            w.spawnParticle(Particle.EXPLOSION_LARGE,loc,5,0.5,0.5,0.5);
            w.spawnParticle(Particle.LAVA,loc,100,0.5,0.5,0.5);
            w.spawnParticle(Particle.SOUL_FIRE_FLAME,loc,50,5,5,5);
        }
    }




}
