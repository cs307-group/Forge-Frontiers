package com.forgefrontier.forgefrontier.mobs.slimes.hitbox.eggbox;

import com.forgefrontier.forgefrontier.ForgeFrontier;
import com.forgefrontier.forgefrontier.mobs.slimes.CustomCraftSlime;
import com.forgefrontier.forgefrontier.mobs.slimes.hitbox.HitBox;
import com.forgefrontier.forgefrontier.mobs.slimes.hitbox.HitBoxEntity;
import net.minecraft.world.entity.EntityType;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_18_R2.CraftServer;
import org.bukkit.craftbukkit.v1_18_R2.CraftWorld;
import org.bukkit.craftbukkit.v1_18_R2.entity.CraftEntity;
import org.bukkit.metadata.FixedMetadataValue;

public class EggBox extends HitBox {

    /** class representing the custom CraftEntity for the TestChicken */
    public static class CraftEggBox extends CraftHitBox {

        // public BossBar bossBar;

        /**
         * Constructor
         *
         * @param server the CraftServer instance
         */
        public CraftEggBox(CraftServer server, CraftWorld world ) {
            super(server, "EggBox", 100, new EggBoxEntity(EntityType.SLIME, world.getHandle()), 4);
            this.updateNamePlate("Egg");
            saveMetaData();
        }

        @Override
        public void defineDropTable() {
            for (int i = 0; i < 16; i++) {
                registerDropItem("SilverIngot", 100);
            }
            for (int i = 0; i < 16; i++) {
                registerDropItem("SilverIngot", 50);
            }
            for (int i = 0; i < 16; i++) {
                registerDropItem("SilverIngot", 20);
            }
        }

        @Override
        public void updateNamePlate(String name) {
            System.out.println("OOOO...    ");
            EggBoxEntity entity = (EggBoxEntity) handle;
            if (entity != null) {
                System.out.println("OUCH ||| " + name + ": " + ((int) this.getHealth()) + "/" + ((int) this.getMaxHealth()));
                entity.setNamePlate(ChatColor.WHITE + name + ": " + ((int) this.getHealth()) + "/" + ((int) this.getMaxHealth()));
            }
        }

        /** Stores the necessary data in metadata to be accessed later */
        @Override
        public void saveMetaData() {
            super.saveMetaData();
            // Sets the metadata for the mob
            this.setMetadata("code", new FixedMetadataValue(ForgeFrontier.getInstance(), "EggBox"));
            this.setMetadata("name", new FixedMetadataValue(ForgeFrontier.getInstance(), "Egg"));
            // this.setMetadata("bossbar", new FixedMetadataValue(ForgeFrontier.getInstance(), bossBar));
        }
    }

    /** Constructor for TestChicken instance */
    public EggBox() {
        super("EggBox");
    }

    /** Constructor for inheritance */
    public EggBox(String name) {
        super(name);
    }

    /**
     * Creates an entity instance of the TestChicken to spawn in the world
     *
     * @return the entity instance in the world
     */
    @Override
    public CraftEntity createCustomEntity(CraftWorld world) {
        return new EggBox.CraftEggBox((CraftServer) Bukkit.getServer(), world);
    }
}
