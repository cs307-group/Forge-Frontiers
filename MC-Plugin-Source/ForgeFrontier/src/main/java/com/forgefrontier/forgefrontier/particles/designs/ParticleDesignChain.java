package com.forgefrontier.forgefrontier.particles.designs;

import org.bukkit.util.Vector;

import java.util.ArrayList;

public class ParticleDesignChain extends StaticParticleDesign {


    int num_points = 40;
    ArrayList<Vector> staticPoints;
    public ParticleDesignChain() {
    }

    public ParticleDesignChain(int num_points) {
        this.num_points = num_points;

    }

    // Static Next
    public ArrayList<Vector> getNext() {
        return staticPoints;
    }


    /** Inspiration from Rosewood Development Particle Style */
    public void createStaticPoints(int amount) {
        staticPoints = new ArrayList<>();
        Vector origin = new Vector(0,0,0);
        for (double n = -0.2; n < 0.6; n += 0.8 / this.num_points) {
            staticPoints.add((origin.clone().add(new Vector(1 - n, n - 1.1, 1 - n))));
            staticPoints.add((origin.clone().add(new Vector(1 - n, n - 1.1, -1 + n))));
            staticPoints.add((origin.clone().add(new Vector(-1 + n, n - 1.1, 1 - n))));
            staticPoints.add((origin.clone().add(new Vector(-1 + n, n - 1.1, -1 + n))));
        }
    }
    public void createStaticPoints() {
        createStaticPoints(num_points);
    }

    public int getNum_points() {
        return num_points;
    }

    public void setNum_points(int num_points) {
        this.num_points = num_points;
    }



}
