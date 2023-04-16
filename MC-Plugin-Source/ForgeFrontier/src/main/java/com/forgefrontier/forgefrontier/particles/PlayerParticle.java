package com.forgefrontier.forgefrontier.particles;

import com.forgefrontier.forgefrontier.ForgeFrontier;
import org.bukkit.Particle;

public class PlayerParticle {
    int slotID;
    int particleID;
    public PlayerParticle(int slotID, Particle p) {
        this.slotID = slotID;
        particleID = ForgeFrontier.getInstance().getParticleManager().getParticleID(p);
    }



}
