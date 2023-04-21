package com.forgefrontier.forgefrontier.mobs.chickens.hostile.chickenboss;

import com.forgefrontier.forgefrontier.ForgeFrontier;
import com.forgefrontier.forgefrontier.mobs.chickens.hostile.HostileChicken;
import com.forgefrontier.forgefrontier.mobs.chickens.hostile.HostileChickenEntity;
import net.minecraft.world.entity.EntityType;
import org.bukkit.Bukkit;
import org.bukkit.boss.BossBar;
import org.bukkit.craftbukkit.v1_18_R2.CraftServer;
import org.bukkit.craftbukkit.v1_18_R2.CraftWorld;
import org.bukkit.craftbukkit.v1_18_R2.entity.CraftEntity;
import org.bukkit.metadata.FixedMetadataValue;

public class ChickBoss extends HostileChicken {

    public static class CraftChickBoss extends CraftHostileChicken {

        BossBar bossBar;

        /**
         * Constructor
         *
         * @param server the CraftServer instance
         */
        public CraftChickBoss(CraftServer server, CraftWorld world) {
            super(server, "ChickBoss", 200, new ChickBossEntity(EntityType.CHICKEN, world.getHandle()));
            saveMetaData();
        }

        @Override
        public void initCraftChicken(String name, int health) {
            super.initCraftChicken(name, health);
            bossBar = initBossBar("The Roosting Ravager");
        }

        @Override
        public void defineDropTable() {
            for (int i = 0; i < 32; i++) {
                registerDropItem("SilverIngot", 100);
            }
            registerDropItem("LeatherHelmet", 100);
            registerDropItem("ChickenWing", 100);
        }

        @Override
        public void saveMetaData() {
            super.saveMetaData();
            this.setMetadata("bossbar", new FixedMetadataValue(ForgeFrontier.getInstance(), bossBar));
            this.setMetadata("name", new FixedMetadataValue(ForgeFrontier.getInstance(), "Ravager"));
            this.setMetadata("tier", new FixedMetadataValue(ForgeFrontier.getInstance(), 1));
        }
    }

    /** Constructor for TestChicken instance */
    public ChickBoss() {
        super("ChickBoss");
    }

    /** Constructor for inheritance */
    public ChickBoss(String name) {
        super(name);
    }

    /**
     * Creates an entity instance of the TestChicken to spawn in the world
     *
     * @return the entity instance in the world
     */
    @Override
    public CraftEntity createCustomEntity(CraftWorld world) {
        return new CraftChickBoss((CraftServer) Bukkit.getServer(), world);
    }
}
