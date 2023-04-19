package com.forgefrontier.forgefrontier.mining;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
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

    public MiningArea(ConfigurationSection config) {
        Vector pos1 = new Vector(
            config.getInt("pos1.x"),
            config.getInt("pos1.y"),
            config.getInt("pos1.z")
        );
        Vector pos2 = new Vector(
            config.getInt("pos2.x"),
            config.getInt("pos2.y"),
            config.getInt("pos2.z")
        );
        this.pos1 = Vector.getMinimum(pos1, pos2);
        this.pos2 = Vector.getMaximum(pos1, pos2);
        this.areaName = config.getString("name");
        this.replacementMaterial = Material.matchMaterial(config.getString("replace"));
        this.resources = new ArrayList<>();

        int resourceInd = 0;
        ConfigurationSection configSection;
        while((configSection = config.getConfigurationSection("resources." + resourceInd)) != null) {
            this.resources.add(new MiningResource(configSection));
            resourceInd += 1;
        }
    }

    public void save(ConfigurationSection section) {
        section.set("pos1.x", this.pos1.getBlockX());
        section.set("pos1.y", this.pos1.getBlockY());
        section.set("pos1.z", this.pos1.getBlockZ());
        section.set("pos2.x", this.pos2.getBlockX());
        section.set("pos2.y", this.pos2.getBlockY());
        section.set("pos2.z", this.pos2.getBlockZ());

        section.set("name", this.getAreaName());
        section.set("replace", this.getReplacementMaterial().toString());

        for(int i = 0; i < resources.size(); i++) {
            resources.get(i).save(section.createSection("resources." + i));
        }

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
        return pos2.getBlockZ() >= v.getBlockZ();
    }
}
