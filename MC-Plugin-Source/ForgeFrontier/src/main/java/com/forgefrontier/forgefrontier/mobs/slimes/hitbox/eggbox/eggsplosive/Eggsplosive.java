package com.forgefrontier.forgefrontier.mobs.slimes.hitbox.eggbox.eggsplosive;

import com.forgefrontier.forgefrontier.ForgeFrontier;
import com.forgefrontier.forgefrontier.mobs.chickens.hostile.chickenboss.ChickBossEntity;
import com.forgefrontier.forgefrontier.mobs.slimes.hitbox.eggbox.EggBox;
import com.forgefrontier.forgefrontier.mobs.slimes.hitbox.eggbox.EggBoxEntity;
import net.minecraft.world.entity.EntityType;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_18_R2.CraftServer;
import org.bukkit.craftbukkit.v1_18_R2.CraftWorld;
import org.bukkit.craftbukkit.v1_18_R2.entity.CraftEntity;
import org.bukkit.metadata.FixedMetadataValue;

public class Eggsplosive extends EggBox {

    /** class representing the custom CraftEntity for the TestChicken */
    public static class CraftEggsplosive extends CraftEggBox {

        /**
         * Constructor
         *
         * @param server the CraftServer instance
         */
        public CraftEggsplosive(CraftServer server, CraftWorld world) {
            super(server, "Eggsplosive", 100, new EggsplosiveEntity(EntityType.SLIME, world.getHandle()), 4);
            this.updateNamePlate("Eggsplosive");
            saveMetaData();
        }

        @Override
        public void updateNamePlate(String name) {
            EggBoxEntity entity = (EggsplosiveEntity) handle;
            if (entity != null) {
                entity.setNamePlate(ChatColor.WHITE + name + ": " + ((int) this.getHealth()) + "/" + ((int) this.getMaxHealth()));
            }
        }

        /** Stores the necessary data in metadata to be accessed later */
        @Override
        public void saveMetaData() {
            super.saveMetaData();
            // Sets the metadata for the mob
            this.setMetadata("code", new FixedMetadataValue(ForgeFrontier.getInstance(), "Eggsplosive"));
            this.setMetadata("name", new FixedMetadataValue(ForgeFrontier.getInstance(), "Eggsplosive"));
        }
    }

    /** Constructor for TestChicken instance */
    public Eggsplosive() {
        super("Eggsplosive");
    }

    /** Constructor for inheritance */
    public Eggsplosive(String name) {
        super(name);
    }

    /**
     * Creates an entity instance of the TestChicken to spawn in the world
     *
     * @return the entity instance in the world
     */
    @Override
    public CraftEntity createCustomEntity(CraftWorld world) {
        return new Eggsplosive.CraftEggsplosive((CraftServer) Bukkit.getServer(), world);
    }
}
