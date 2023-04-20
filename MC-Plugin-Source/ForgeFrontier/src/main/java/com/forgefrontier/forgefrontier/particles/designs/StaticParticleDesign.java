package com.forgefrontier.forgefrontier.particles.designs;

import org.bukkit.util.Vector;

import java.util.ArrayList;

public abstract class StaticParticleDesign extends ParticleDesign {
    /**
     * GetNext() returns next particle-set to be displayed of design
     * Static Designs: Returns the same thing every time
     * Dynamic Design: Returns next "step" of design. Ex. Spiral particle
     */
    public abstract ArrayList<Vector> getNext();
    public abstract void createStaticPoints();
}
