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

    String entityCode;

    public Spawner(String entityCode) {
        // TODO: Get the code for the entity (TBD)
        this.entityCode = entityCode;
    }

    //TODO: setup config constructor
    public Spawner(ConfigurationSection config) {
        this.entityCode = config.getString("entity-code");
    }

    public void save(ConfigurationSection config) {
        config.set("entity-code", this.entityCode);
    }

    public Spawner(String friendlyname, Material blockMaterial, String entityCode) {
        this.entityCode = entityCode;
    }

    public String getEntityCode() {
        return entityCode;
    }

}
