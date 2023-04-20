package com.forgefrontier.forgefrontier.particles.designs;

import org.bukkit.util.Vector;

import java.util.ArrayList;

public class ParticleDesignPoint extends StaticParticleDesign {
    private ArrayList<Vector> points;

    @Override
    public ArrayList<Vector> getNext() {
        return this.points;
    }
    @Override
    public void createStaticPoints() {
        points = new ArrayList<>();
        points.add(new Vector(0, 0, 0));
    }
    public void createStaticPoints(ArrayList<Vector> pts) {
        points = pts;
    }

}
