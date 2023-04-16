package com.forgefrontier.forgefrontier.particles.designs;

import org.bukkit.Particle;
import org.bukkit.util.Vector;

import java.util.ArrayList;

public class ParticleDesignSphere extends StaticParticleDesign {

    int num_points = 20;
    float distance = 1;
    ArrayList<Vector> staticPoints;
    public ParticleDesignSphere() {
    }

    // Static Next
    public ArrayList<Vector> getNext() {
        return staticPoints;
    }


    public void createStaticPoints(int amount) {
        /* https://stackoverflow.com/questions/9600801/evenly-distributing-n-points-on-a-sphere */
        this.num_points = amount;
        staticPoints = new ArrayList<>();
        float phi = (float) (Math.PI * (Math.sqrt(5.) - 1.0));
        for (int i = 0; i < num_points; i++) {
            float y = 1.0F - (i / (float)(num_points - 1)) * 2.0F;
            float radius = (float) Math.sqrt(1 - y * y);
            float theta = phi * i;
            float x = (float) Math.cos(theta) * radius;
            float z = (float) Math.sin(theta) * radius;
            x *= distance;
            y *= distance;
            z *= distance;
            staticPoints.add(new Vector(x,y,z));
        }
    }
    public void createStaticPoints() {
        createStaticPoints(20);
    }
    public int getNum_points() {
        return num_points;
    }

    public void setNum_points(int num_points) {
        this.num_points = num_points;
    }

    public float getDistance() {
        return distance;
    }

    public void setDistance(float distance) {
        this.distance = distance;
    }
}
