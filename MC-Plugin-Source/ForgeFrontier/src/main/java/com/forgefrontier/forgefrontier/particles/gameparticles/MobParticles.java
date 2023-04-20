package com.forgefrontier.forgefrontier.particles.gameparticles;

import com.forgefrontier.forgefrontier.ForgeFrontier;
import com.forgefrontier.forgefrontier.particles.FFParticle;
import com.forgefrontier.forgefrontier.particles.ParticleManager;
import com.forgefrontier.forgefrontier.particles.designs.ParticleDesignChain;
import com.forgefrontier.forgefrontier.particles.designs.ParticleDesignHalo;
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
    public static FFParticle SLIME_JUMP_CHARGE;
    public static FFParticle SLIME_JUMP_CHARGE2;
    public static FFParticle SLIME_JUMP_LAND;
    public static FFParticle HIT_FLASH;
    public static FFParticle SLIME_DEATH_PARTICLE;



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
        POISON_CHICKEN_DEATH_PARTICLE.setDensity(5);
        POISON_CHICKEN_DEATH_PARTICLE.setOffsets(2,2,2);
        POISON_CHICKEN_DEATH_PARTICLE.id = "MBPChickDeath";
        mngr.registerParticle(POISON_CHICKEN_DEATH_PARTICLE);


        ParticleDesignHalo largeBossRing = new ParticleDesignHalo();
        largeBossRing.setRadius(2);
        largeBossRing.setNum_points(20);
        largeBossRing.createStaticPoints();
        SLIME_JUMP_CHARGE = new FFParticle(largeBossRing);
        SLIME_JUMP_CHARGE.setParticle(Particle.COMPOSTER);
        SLIME_JUMP_CHARGE.setOffsets(1,1,1);
        SLIME_JUMP_CHARGE.id = "MBSlimeCharge";
        mngr.registerParticle(SLIME_JUMP_CHARGE);

        ParticleDesignSphere largeSphere = new ParticleDesignSphere();
        largeSphere.setDistance(2);
        largeSphere.setNum_points(20);
        largeSphere.createStaticPoints();
        SLIME_JUMP_CHARGE2 = new FFParticle(largeSphere);
        SLIME_JUMP_CHARGE2.setParticle(Particle.COMPOSTER);
        SLIME_JUMP_CHARGE2.setOffsets(0.5F,0.5F,0.5F);
        SLIME_JUMP_CHARGE2.id = "MBSlimeCharge2";
        mngr.registerParticle(SLIME_JUMP_CHARGE2);

        SLIME_JUMP_LAND = new FFParticle(largeBossRing);
        SLIME_JUMP_LAND.setDensity(2);
        SLIME_JUMP_LAND.setParticle(Particle.CAMPFIRE_COSY_SMOKE);
        SLIME_JUMP_LAND.setOffsets(2,2,2);
        SLIME_JUMP_LAND.id = "MBSlimeLand";
        mngr.registerParticle(SLIME_JUMP_LAND);


        HIT_FLASH = new FFParticle(pds);
        HIT_FLASH.setParticle(Particle.FLASH);
        HIT_FLASH.id = "MBHIT_FLASH";
        mngr.registerParticle(HIT_FLASH);

        SLIME_DEATH_PARTICLE = new FFParticle(largeSphere);
        SLIME_DEATH_PARTICLE.setParticle(Particle.SPORE_BLOSSOM_AIR);
        SLIME_DEATH_PARTICLE.setDensity(10);
        SLIME_DEATH_PARTICLE.setOffsets(2,2,2);
        SLIME_DEATH_PARTICLE.id = "MBSlimeBossDeath";
        mngr.registerParticle(SLIME_DEATH_PARTICLE);



    }







}
