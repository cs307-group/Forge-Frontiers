package com.forgefrontier.forgefrontier.particles.gameparticles;

import com.forgefrontier.forgefrontier.ForgeFrontier;
import com.forgefrontier.forgefrontier.particles.FFParticle;
import com.forgefrontier.forgefrontier.particles.ParticleManager;
import com.forgefrontier.forgefrontier.particles.designs.ParticleDesign;
import com.forgefrontier.forgefrontier.particles.designs.ParticleDesignSphere;
import org.bukkit.Particle;

/**
 * Prefix: SKL
 */
public class SkillParticles {
    public static FFParticle GROUNDSMASHPARTICLE;
    public static FFParticle DUSTSETTLE;
    public static void init() {
        ParticleManager particleManager = ForgeFrontier.getInstance().getParticleManager();
        ParticleDesignSphere pds = new ParticleDesignSphere();
        pds.createStaticPoints();
        GROUNDSMASHPARTICLE = new FFParticle(pds);
        GROUNDSMASHPARTICLE.setParticle(Particle.CAMPFIRE_COSY_SMOKE);
        GROUNDSMASHPARTICLE.id = "SKLGrndSmsh";
        particleManager.registerParticle(GROUNDSMASHPARTICLE);
        DUSTSETTLE = new FFParticle(pds);
        DUSTSETTLE.setParticle(Particle.WHITE_ASH);
        DUSTSETTLE.id = "SKLDustSettle";
        DUSTSETTLE.setDensity(5);
        particleManager.registerParticle(DUSTSETTLE);
    }
}
