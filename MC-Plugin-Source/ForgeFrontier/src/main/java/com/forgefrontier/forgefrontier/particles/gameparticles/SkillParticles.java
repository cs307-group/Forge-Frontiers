package com.forgefrontier.forgefrontier.particles.gameparticles;

import com.forgefrontier.forgefrontier.ForgeFrontier;
import com.forgefrontier.forgefrontier.particles.FFParticle;
import com.forgefrontier.forgefrontier.particles.ParticleManager;
import com.forgefrontier.forgefrontier.particles.designs.ParticleDesign;
import com.forgefrontier.forgefrontier.particles.designs.ParticleDesignHalo;
import com.forgefrontier.forgefrontier.particles.designs.ParticleDesignPoint;
import com.forgefrontier.forgefrontier.particles.designs.ParticleDesignSphere;
import org.bukkit.Particle;

/**
 * Prefix: SKL
 */
public class SkillParticles {
    public static FFParticle GROUNDSMASHPARTICLE;
    public static FFParticle DASHCLOUD;
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

        ParticleDesignPoint singlePoint = new ParticleDesignPoint();
        singlePoint.createStaticPoints();
        DASHCLOUD = new FFParticle(singlePoint);
        DASHCLOUD.setParticle(Particle.CLOUD);
        DASHCLOUD.setDensity(2);
        DASHCLOUD.setOffsets(1,0,1);
        DASHCLOUD.id = "SKLDashCloud";
        particleManager.registerParticle(DASHCLOUD);





    }
}
