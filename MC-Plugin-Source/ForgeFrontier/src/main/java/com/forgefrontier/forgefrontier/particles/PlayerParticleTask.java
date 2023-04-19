package com.forgefrontier.forgefrontier.particles;

public class PlayerParticleTask {

    int task;
    int particleID;
    int slotID;
    String provider;

    public PlayerParticleTask(int task, int slotID) {
        this.task = task;
        this.particleID = particleID;
        this.provider = "";
        this.slotID = slotID;
    }

    public PlayerParticleTask(int task, int slotID, int particleID) {
        this.task = task;
        this.particleID = particleID;
        this.provider = "";
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

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public int getSlotID() {
        return slotID;
    }

    public void setSlotID(int slotID) {
        this.slotID = slotID;
    }
}
