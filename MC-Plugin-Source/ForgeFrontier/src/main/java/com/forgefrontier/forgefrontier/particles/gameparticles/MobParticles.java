package com.forgefrontier.forgefrontier.particles.gameparticles;

import com.forgefrontier.forgefrontier.particles.FFParticle;
import com.forgefrontier.forgefrontier.particles.designs.ParticleDesignChain;
import com.forgefrontier.forgefrontier.particles.designs.ParticleDesignSphere;
import org.bukkit.Particle;

public class MobParticles {
    public static FFParticle CHICKBOSS_EGG_PARTICLE;
    public static FFParticle CHICKBOSS_IMMUNE_PARTICLE;
    public static void init() {
        ParticleDesignChain pdc = new ParticleDesignChain();
        pdc.setNum_points(10);
        pdc.createStaticPoints();
        CHICKBOSS_EGG_PARTICLE = new FFParticle(pdc);
        CHICKBOSS_EGG_PARTICLE.setParticle(Particle.WAX_OFF);

        ParticleDesignSphere pds = new ParticleDesignSphere();
        pds.setDistance(1);
        pds.setNum_points(10);
        pds.createStaticPoints();
        CHICKBOSS_IMMUNE_PARTICLE = new FFParticle(pds);
        CHICKBOSS_IMMUNE_PARTICLE.setParticle(Particle.WAX_ON);

    }







}
