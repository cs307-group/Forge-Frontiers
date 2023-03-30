package com.forgefrontier.forgefrontier.mobs.chickens.hostile;

import com.forgefrontier.forgefrontier.ForgeFrontier;
import com.forgefrontier.forgefrontier.mobs.chickens.CustomChicken;
import com.forgefrontier.forgefrontier.mobs.chickens.CustomCraftChicken;
import net.minecraft.world.entity.EntityType;
import org.bukkit.*;
import org.bukkit.boss.BossBar;
import org.bukkit.craftbukkit.v1_18_R2.CraftServer;
import org.bukkit.craftbukkit.v1_18_R2.CraftWorld;
import org.bukkit.craftbukkit.v1_18_R2.entity.CraftEntity;
import org.bukkit.metadata.FixedMetadataValue;

/**
 * Example custom chicken class used for testing
 */
public class HostileChicken extends CustomChicken {

    /** class representing the custom CraftEntity for the TestChicken */
    public static class CraftHostileChicken extends CustomCraftChicken {

        // public BossBar bossBar;

        /**
         * Constructor
         *
         * @param server the CraftServer instance
         */
        public CraftHostileChicken(CraftServer server) {
            super(server, "HostileChicken", new HostileChickenEntity(EntityType.CHICKEN, ((CraftWorld)server.getWorld("world")).getHandle()));
            initCraftChicken("HostileChicken", 10);
            saveMetaData();
        }


        /**
         * Constructor for inheritance
         *
         * @param server the CraftServer instance
         */
        public CraftHostileChicken(CraftServer server, String name, int health, HostileChickenEntity entity) {
            super(server, name, entity);
            initCraftChicken(name, health);
            saveMetaData();
        }

        /** handles setting initial values for the custom chicken*/
        public void initCraftChicken(String name, int health) {
            this.setDefaultHealth(health);

            this.customCraftName = name;
            this.updateNamePlate(customCraftName);

            this.setAI(true);

            // bossBar = initBossBar("Chicken Boss");
            defineDropTable();
        }

        @Override
        public void defineDropTable() {
            registerDropItem("WoodenSword", 100);
            registerDropItem("LeatherChestplate",50);
            registerDropItem("LeatherHelmet",25);
            registerDropItem(Material.DIAMOND, 100);
        }

        /** Stores the necessary data in metadata to be accessed later */
        @Override
        public void saveMetaData() {
            super.saveMetaData();
            // Sets the metadata for the mob
            this.setMetadata("code", new FixedMetadataValue(ForgeFrontier.getInstance(), "TestChicken"));
            // this.setMetadata("bossbar", new FixedMetadataValue(ForgeFrontier.getInstance(), bossBar));
        }
    }

    /** Constructor for TestChicken instance */
    public HostileChicken() {
        super("HostileChicken");
    }

    /** Constructor for inheritance */
    public HostileChicken(String name) {
        super(name);
    }

    /**
     * Creates an entity instance of the TestChicken to spawn in the world
     *
     * @return the entity instance in the world
     */
    @Override
    public CraftEntity createCustomEntity() {
        return new CraftHostileChicken((CraftServer) Bukkit.getServer());
    }
}
