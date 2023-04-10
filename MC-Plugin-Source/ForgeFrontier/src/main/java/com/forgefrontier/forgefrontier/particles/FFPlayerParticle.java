package com.forgefrontier.forgefrontier.particles;

import org.bukkit.Particle;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;


/*
* Particle Type - Generic (No extra modifications), Dust (Color & Transition mods), Block: Block identification
* Movement - How this particle should move
* Design - What this particle looks like (also controls movement to a certain degree)
* */
public class FFPlayerParticle {
    public enum ParticleType {
        GENERIC, DUST, BLOCK
    }
    Particle particle;
    Player player;

    public FFPlayerParticle(Player pl, Particle p) {
        this.particle = p;
        this.player = pl;
    }

    public void run() {

    }



}
