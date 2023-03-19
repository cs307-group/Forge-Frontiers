package com.forgefrontier.forgefrontier.mining;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

public class MiningArea {

    Vector pos1;
    Vector pos2;
    String areaName;
    Material replacementMaterial;

    List<MiningResource> resources;

    public MiningArea(Vector pos1, Vector pos2, String areaName, Material replacementMaterial) {
        this.pos1 = Vector.getMinimum(pos1, pos2);
        this.pos2 = Vector.getMaximum(pos1, pos2);
        this.areaName = areaName;
        this.replacementMaterial = replacementMaterial;
        this.resources = new ArrayList<>();
    }

    public void setPos1(Vector pos1) {
        this.pos1 = pos1;
    }

    public void setPos2(Vector pos2) {
        this.pos2 = pos2;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public void setReplacementMaterial(Material replacementMaterial) {
        this.replacementMaterial = replacementMaterial;
    }

    public Vector getPos1() {
        return pos1;
    }

    public Vector getPos2() {
        return pos2;
    }

    public String getAreaName() {
        return areaName;
    }

    public Material getReplacementMaterial() {
        return replacementMaterial;
    }

    public void addResource(MiningResource resource) {
        this.resources.add(resource);
    }

    public List<MiningResource> getResources() {
        return this.resources;
    }

    public boolean contains(Vector v) {
        if(pos1.getBlockX() > v.getBlockX())
            return false;
        if(pos1.getBlockY() > v.getBlockY())
            return false;
        if(pos1.getBlockZ() > v.getBlockZ())
            return false;
        if(pos2.getBlockX() < v.getBlockX())
            return false;
        if(pos2.getBlockY() < v.getBlockY())
            return false;
        if(pos2.getBlockZ() < v.getBlockZ())
            return false;
        return true;
    }
}
