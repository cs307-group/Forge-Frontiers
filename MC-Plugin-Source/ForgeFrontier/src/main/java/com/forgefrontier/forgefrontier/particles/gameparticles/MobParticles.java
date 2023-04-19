package com.forgefrontier.forgefrontier.particles.gameparticles;

import com.forgefrontier.forgefrontier.ForgeFrontier;
import com.forgefrontier.forgefrontier.particles.FFParticle;
import com.forgefrontier.forgefrontier.particles.ParticleManager;
import com.forgefrontier.forgefrontier.particles.designs.ParticleDesignChain;
import com.forgefrontier.forgefrontier.particles.designs.ParticleDesignSphere;
import org.bukkit.Particle;

/**
 * Prefix: MB
 */
public class MobParticles {
    public static FFParticle CHICKBOSS_EGG_PARTICLE;
    public static FFParticle CHICKBOSS_IMMUNE_PARTICLE;
    public static FFParticle GENERIC_CUSTOM_DEATH_PARTICLE;
    public static FFParticle POISON_CHICKEN_DEATH_PARTICLE;

    public static void init() {
        ParticleManager mngr = ForgeFrontier.getInstance().getParticleManager();
        ParticleDesignChain pdc = new ParticleDesignChain();
        pdc.setNum_points(10);
        pdc.createStaticPoints();
        CHICKBOSS_EGG_PARTICLE = new FFParticle(pdc);
        CHICKBOSS_EGG_PARTICLE.setParticle(Particle.WAX_OFF);
        CHICKBOSS_EGG_PARTICLE.id = "MBChickEgg";
        mngr.registerParticle(CHICKBOSS_EGG_PARTICLE);

        ParticleDesignSphere pds = new ParticleDesignSphere();
        pds.setDistance(1);
        pds.setNum_points(10);
        pds.createStaticPoints();
        CHICKBOSS_IMMUNE_PARTICLE = new FFParticle(pds);
        CHICKBOSS_IMMUNE_PARTICLE.setParticle(Particle.WAX_ON);
        CHICKBOSS_IMMUNE_PARTICLE.id = "MBChickImmune";
        mngr.registerParticle(CHICKBOSS_IMMUNE_PARTICLE);

        GENERIC_CUSTOM_DEATH_PARTICLE = new FFParticle(pds);
        GENERIC_CUSTOM_DEATH_PARTICLE.setParticle(Particle.CRIMSON_SPORE);
        GENERIC_CUSTOM_DEATH_PARTICLE.setDensity(2);
        GENERIC_CUSTOM_DEATH_PARTICLE.id = "MBDeath";
        mngr.registerParticle(GENERIC_CUSTOM_DEATH_PARTICLE);

        POISON_CHICKEN_DEATH_PARTICLE = new FFParticle(pds);
        POISON_CHICKEN_DEATH_PARTICLE.setParticle(Particle.FALLING_SPORE_BLOSSOM);
        POISON_CHICKEN_DEATH_PARTICLE.setDensity(2);
        POISON_CHICKEN_DEATH_PARTICLE.id = "MBPChickDeath";
        mngr.registerParticle(POISON_CHICKEN_DEATH_PARTICLE);

    }







}
