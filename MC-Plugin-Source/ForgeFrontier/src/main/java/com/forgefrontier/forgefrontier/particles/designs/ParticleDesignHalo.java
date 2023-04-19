package com.forgefrontier.forgefrontier.particles.designs;

import com.forgefrontier.forgefrontier.utils.ParticleUtilMath;
import org.bukkit.util.Vector;

import java.util.ArrayList;

public class ParticleDesignHalo extends ParticleDesign {

    int num_points = 20;
    float radius = 1;
    float playerOffset;
    ArrayList<Vector> staticPoints;
    public ParticleDesignHalo() {}
    public ParticleDesignHalo(int num_points, float radius, float playerOffset) {
        this.num_points = num_points;
        this.radius = radius;
        this.playerOffset = 0.5F;
    }

    // Static Next
    public ArrayList<Vector> getNext() {
        return staticPoints;
    }

    public void createStaticPoints(int amount) {
        this.num_points = amount;
        staticPoints = new ArrayList<>();
        double segment = 2 * Math.PI / this.num_points;
        for (int i = 0; i < this.num_points; i++) {
            double angle = segment * i;
            double dx = this.radius * Math.cos(angle);
            double dy = this.playerOffset;
            double dz = this.radius * ParticleUtilMath.sin(angle);
            staticPoints.add(new Vector(dx, dy, dz));
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

    public float getRadius() {
        return radius;
    }

    public void setRadius(float distance) {
        this.radius = distance;
    }

    public float getPlayerOffset() {
        return playerOffset;
    }

    public void setPlayerOffset(float playerOffset) {
        this.playerOffset = playerOffset;
    }
}
