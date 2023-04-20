package com.forgefrontier.forgefrontier.particles.designs;

import com.forgefrontier.forgefrontier.utils.ParticleUtilMath;
import org.bukkit.util.Vector;

import java.util.ArrayList;

/**
 * CREDITS TO: Rosewood Development for the particle math
 * Received permission to utilize this particle design
 * https://github.com/Rosewood-Development/PlayerParticles/
 *
 * IMPORTANT: Use with CubeDesignInstance class to reuse points
 * This is a STEP BASED effect -- different sequences of particles will be played depending on the step.
 */
public class ParticleDesignCube extends ParticleDesign {
    ArrayList<ArrayList<Vector>> allPoints;
    private double edgeLength = 3;
    private double angularVelocityX = 0.1;
    private double angularVelocityY = 0.0;
    private double angularVelocityZ = 0.1;
    private int particlesPerEdge = 10;
    private int numSteps = 20;

    public ParticleDesignCube(int particlesPerEdge, float size) {
        this.edgeLength = size;
        this.particlesPerEdge = particlesPerEdge;
    }

    public ParticleDesignCube() { }



    public ArrayList<Vector> getNext(int step) {
        return allPoints.get(step);
    }

    public void createStaticPoints() {
        allPoints = new ArrayList<>();
        for (int step = 0; step < numSteps; step++) {
            ArrayList<Vector> stepPoints = new ArrayList<>();
            double xRotation = step * this.angularVelocityX;
            double yRotation = step * this.angularVelocityY;
            double zRotation = step * this.angularVelocityZ;
            double a = this.edgeLength / 2;
            double angleX, angleY;
            Vector v = new Vector();
            for (int i = 0; i < 4; i++) {
                angleY = i * Math.PI / 2;
                for (int j = 0; j < 2; j++) {
                    angleX = j * Math.PI;
                    for (int p = 0; p <= this.particlesPerEdge; p++) {
                        v.setX(a).setY(a);
                        v.setZ(this.edgeLength * p / this.particlesPerEdge - a);
                        ParticleUtilMath.rotateAroundAxisX(v, angleX);
                        ParticleUtilMath.rotateAroundAxisY(v, angleY);
                        ParticleUtilMath.rotateVector(v, xRotation, yRotation, zRotation);
                        stepPoints.add(v.clone());
                    }
                }
                for (int p = 0; p <= this.particlesPerEdge; p++) {
                    v.setX(a).setZ(a);
                    v.setY(this.edgeLength * p / this.particlesPerEdge - a);
                    ParticleUtilMath.rotateAroundAxisY(v, angleY);
                    ParticleUtilMath.rotateVector(v, xRotation, yRotation, zRotation);
                    stepPoints.add(v.clone());
                }
            }
            allPoints.add(stepPoints);
        }
    }


    public double getEdgeLength() {
        return edgeLength;
    }

    public void setEdgeLength(double edgeLength) {
        this.edgeLength = edgeLength;
    }

    public double getAngularVelocityX() {
        return angularVelocityX;
    }

    public void setAngularVelocityX(double angularVelocityX) {
        this.angularVelocityX = angularVelocityX;
    }

    public double getAngularVelocityY() {
        return angularVelocityY;
    }

    public void setAngularVelocityY(double angularVelocityY) {
        this.angularVelocityY = angularVelocityY;
    }

    public double getAngularVelocityZ() {
        return angularVelocityZ;
    }

    public void setAngularVelocityZ(double angularVelocityZ) {
        this.angularVelocityZ = angularVelocityZ;
    }

    public int getParticlesPerEdge() {
        return particlesPerEdge;
    }

    public void setParticlesPerEdge(int particlesPerEdge) {
        this.particlesPerEdge = particlesPerEdge;
    }

    public int getNumSteps() {
        return numSteps;
    }

    public void setNumSteps(int numSteps) {
        this.numSteps = numSteps;
    }
}
