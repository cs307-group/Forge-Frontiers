package com.forgefrontier.forgefrontier.particles;

import com.forgefrontier.forgefrontier.particles.designs.ParticleDesign;
import com.forgefrontier.forgefrontier.particles.designs.StaticParticleDesign;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.block.data.BlockData;
import org.bukkit.util.Vector;

import java.util.ArrayList;

public class FFParticle {
    public enum SpawnParticleType {
        NORMAL, DUST, TRANSITION, BLOCK
    }
    protected final ParticleDesign pd;
    protected ArrayList<Vector> staticParticles;
    protected Particle particle = Particle.REDSTONE;
    protected Particle.DustOptions dustOptions = null;
    protected Particle.DustTransition dustTransition = null;
    protected BlockData blockType = null;
    protected SpawnParticleType spawnType = null;
    protected float offX = 0;
    protected float offY = 0;
    protected float offZ = 0;
    protected int density = 1;
    public int id = -1;
    public int delay = 10;
    public int duration = 20;
    public String name;



    public FFParticle(ParticleDesign pd) {
        this.pd = pd;

        if (this.pd instanceof StaticParticleDesign) {
            this.staticParticles = pd.getNext();
        }
        spawnType = SpawnParticleType.NORMAL;
    }

    public void playParticleAtLocation(World w, Location loc) {

        if (!(this.pd instanceof StaticParticleDesign)) {
            staticParticles = pd.getNext();
        }
        for (Vector v : staticParticles) {
            spawnOneParticle(w, loc.clone().add(v));
        }
    }

    public void spawnOneParticle(World w, Location loc) {
        if (this.particle == null) return;
        if (this.spawnType == SpawnParticleType.NORMAL) {
            w.spawnParticle(particle, loc, density, offX, offY, offZ);
        } else if (this.spawnType == SpawnParticleType.TRANSITION) {
            w.spawnParticle(Particle.DUST_COLOR_TRANSITION, loc, density, offX, offY, offZ, dustTransition);
        } else if (this.spawnType == SpawnParticleType.DUST) {
            w.spawnParticle(Particle.REDSTONE, loc, density, offX, offY, offZ, dustOptions);
        } else if (this.spawnType == SpawnParticleType.BLOCK) {
            w.spawnParticle(Particle.FALLING_DUST, loc, density, offX, offY, offZ, blockType);
        }
    }

    public Particle getParticle() {
        return particle;
    }

    public void setParticle(Particle p) {
        this.particle = p;
    }

    public Particle.DustOptions getDustOptions() {
        return dustOptions;
    }

    public void setDustOptions(Particle.DustOptions dustOptions) {
        this.dustOptions = dustOptions;
        spawnType = SpawnParticleType.DUST;
    }

    public Particle.DustTransition getDustTransition() {
        return dustTransition;
    }

    public void setDustTransition(Particle.DustTransition dustTransition) {
        spawnType = SpawnParticleType.TRANSITION;
        this.dustTransition = dustTransition;
    }

    public void setBlockType(BlockData blockType) {
        spawnType = SpawnParticleType.BLOCK;
        this.blockType = blockType;
    }

    public int getDensity() {
        return density;
    }

    public void setDensity(int density) {
        this.density = density;
    }

    public void setOffsets(float x, float y, float z) {
        this.offX = x;
        this.offY = y;
        this.offZ = z;
    }
}
