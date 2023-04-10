package com.forgefrontier.forgefrontier.particles.designs;


import org.bukkit.util.Vector;

import java.util.ArrayList;

/**
 * Design is the base structure for a particle
 * Either a "feed-forward" design, or a static design
 * See: getNext()
 */
public abstract class ParticleDesign {


    public ParticleDesign() {

    }

    /**
     * GetNext() returns next particle-set to be displayed of design
     * Static Designs: Returns the same thing every time
     * Dynamic Design: Returns next "step" of design. Ex. Spiral particle
     */
    public ArrayList<Vector> getNext() {
        return null;
    }
    public void createStaticPoints() {}

}
