package com.forgefrontier.forgefrontier.particles.designs;

import org.bukkit.util.Vector;

import java.util.ArrayList;

/**
 * Recommended you schedule this effect as it can be intensive.
 */
public class CubeDesignInstance extends ParticleDesign {
    ParticleDesignCube pdc;
    int step = 0;
    public CubeDesignInstance(ParticleDesignCube pdc) {
        this.pdc = pdc;
    }

    @Override
    public ArrayList<Vector> getNext() {
        ArrayList<Vector> pts = pdc.getNext(step);
        step = (step + 1) % pdc.getNumSteps();
        return pts;
    }
}
