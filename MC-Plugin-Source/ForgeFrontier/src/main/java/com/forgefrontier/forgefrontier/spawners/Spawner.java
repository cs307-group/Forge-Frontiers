package com.forgefrontier.forgefrontier.spawners;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class Spawner {

    public static HashMap<String, Material> MATERIALS = new HashMap<>();

    static {
        Material[] materials = Material.values();
        for (int i = 0; i < materials.length; i++) {
            if (materials[i].isBlock()) {
                MATERIALS.put(materials[i].toString(), materials[i]);
            }
        }
    }

    String id;
    String friendlyName;
    Material materialRepresentation;
    String entityCode;

    public Spawner(String entityCode) {
        this.id = UUID.randomUUID().toString();
        this.friendlyName = id;
        this.materialRepresentation = Material.BARRIER;

        // TODO: Get the code for the entity (TBD)
        this.entityCode = entityCode;
    }

    //TODO: setup config constructor
    public Spawner(ConfigurationSection config) {

    }

    public Spawner(String spawnerId, String friendlyname, Material blockMaterial, String entityCode) {
        this.id = spawnerId;
        this.friendlyName = friendlyname;
        this.materialRepresentation = blockMaterial;

        this.entityCode = entityCode;
    }

    public String getId() {
        return id;
    }

    public String getFriendlyName() {
        return friendlyName;
    }

    public Material getMaterialRepresentation() {
        return materialRepresentation;
    }

    public String getEntityCode() {
        return entityCode;
    }

    public void setFriendlyName(String friendlyName) {
        this.friendlyName = friendlyName;
    }

    public void setBlockMaterial(Material blockMaterial) {
        this.materialRepresentation = blockMaterial;
    }
}
