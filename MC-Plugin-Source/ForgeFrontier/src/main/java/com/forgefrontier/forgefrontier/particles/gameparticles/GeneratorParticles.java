package com.forgefrontier.forgefrontier.particles.gameparticles;

import com.forgefrontier.forgefrontier.particles.FFParticle;
import com.forgefrontier.forgefrontier.particles.designs.ParticleDesign;
import com.forgefrontier.forgefrontier.particles.designs.ParticleDesignSphere;
import org.bukkit.Particle;

public class GeneratorParticles {
    public static FFParticle SilverGenParticle;
    public static FFParticle CobaltGenParticle;


    public static void initGenParticles() {
        ParticleDesignSphere pds = new ParticleDesignSphere();
        pds.setDistance(1);
        pds.setNum_points(5);
        SilverGenParticle = new FFParticle(pds);
        SilverGenParticle.setParticle(Particle.WAX_OFF);

        pds = new ParticleDesignSphere();
        pds.setDistance(1);
        pds.setNum_points(5);
        CobaltGenParticle = new FFParticle(pds);
        CobaltGenParticle.setParticle(Particle.WATER_SPLASH);
    }


}
