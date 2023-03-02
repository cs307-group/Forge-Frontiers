package com.forgefrontier.forgefrontier.mobs.chickens;

import org.bukkit.*;
import org.bukkit.craftbukkit.v1_18_R2.CraftServer;
import org.bukkit.craftbukkit.v1_18_R2.entity.CraftChicken;
import org.bukkit.craftbukkit.v1_18_R2.entity.CraftEntity;
import org.bukkit.entity.*;

/**
 * Example custom chicken class used for testing
 */
public class TestChicken extends CustomChicken {

    /** class representing the custom CraftEntity for the TestChicken */
    public static class CraftTestChicken extends CustomCraftChicken {
        /**
         * Constructor
         *
         * @param server the CraftServer instance
         * @param chickenType the code of the chicken
         */
        public CraftTestChicken(CraftServer server, String chickenType) {
            super(server, chickenType);
        }
    }

    public TestChicken() {
        super("TestChicken", new CraftTestChicken((CraftServer) Bukkit.getServer(), "TestChicken"));
    }

    /**
     * spawns an instance of the entity at the location
     *
     * @param loc the location the entity will be spawned
     */
    @Override
    public Entity spawnCustomEntity(Location loc) {
        CraftChicken chicken = (CraftChicken) super.spawnCustomEntity(loc);

        initHealthValues(1000, 1000, chicken);
        chicken.setCustomName(ChatColor.WHITE + "TESTCHICKEN" + chicken.getHealth());
        chicken.setCustomNameVisible(true);

        return chicken;
    }

    /** defines the behaviour of the entity */
    @Override
    public void executeBehavior() {

    }

    /** initializes health values for the entity */
    @Override
    public void initHealthValues(double maxHealth, double health, CraftEntity entity) {
        CraftChicken chicken = (CraftChicken) entity;
        if (entity == null) {
            return;
        }

        chicken.setMaxHealth(1000);
        chicken.setHealth(1000);
    }
}
