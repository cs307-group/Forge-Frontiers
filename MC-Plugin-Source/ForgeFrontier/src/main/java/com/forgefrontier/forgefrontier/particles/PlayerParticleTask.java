package com.forgefrontier.forgefrontier.particles;

public class PlayerParticleTask {

    int task;
    int particleID;



    public PlayerParticleTask(int task, int particleID) {
        this.task = task;
        this.particleID = particleID;
    }

    public int getTask() {
        return task;
    }

    public void setTask(int task) {
        this.task = task;
    }

    public int getParticleID() {
        return particleID;
    }

    public void setParticleID(int particleID) {
        this.particleID = particleID;
    }


}
