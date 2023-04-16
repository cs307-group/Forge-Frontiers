package com.forgefrontier.forgefrontier.mobs.slimes.hostile;

import com.forgefrontier.forgefrontier.ForgeFrontier;
import com.forgefrontier.forgefrontier.mobs.chickens.hostile.HostileChicken;
import com.forgefrontier.forgefrontier.mobs.slimes.CustomCraftSlime;
import com.forgefrontier.forgefrontier.mobs.slimes.CustomSlime;
import com.forgefrontier.forgefrontier.mobs.slimes.CustomSlimeEntity;
import net.minecraft.world.entity.EntityType;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.craftbukkit.v1_18_R2.CraftServer;
import org.bukkit.craftbukkit.v1_18_R2.CraftWorld;
import org.bukkit.craftbukkit.v1_18_R2.entity.CraftEntity;
import org.bukkit.metadata.FixedMetadataValue;

public class HostileSlime extends CustomSlime {

    static class CraftHostileSlime extends CustomCraftSlime {

        /**
         * Constructor
         *
         * @param server    the CraftServer instance
         * @param world     the world instance
         */
        public CraftHostileSlime(CraftServer server, CraftWorld world) {
            super(server, "HostileSlime", new HostileSlimeEntity(EntityType.SLIME, world.getHandle()));
            initCraftSlime("HostileSlime", 20);
            saveMetaData();
        }

        /**
         * Sets initial values of slime
         *
         * @param name the name of the slime
         * @param health the max health of the slime
         */
        public void initCraftSlime(String name, int health) {
            this.setDefaultHealth(health);

            this.customCraftName = name;
            this.updateNamePlate(customCraftName);
            this.setSize(10);
            ((HostileSlimeEntity) this.getHandle()).setScale(5);

            this.setAI(true);

            AttributeInstance atk = this.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE);
            if (atk != null) {
                atk.setBaseValue(0);
            }

            defineDropTable();
        }

        @Override
        public void defineDropTable() {
            HostileSlimeEntity entity = (HostileSlimeEntity) this.getHandle();
            entity.registerDropItem(Material.SLIME_BALL, 100);
            entity.registerDropItem(Material.SLIME_BALL, 100);
            for (int i = 0; i < 5; i++) {
                entity.registerDropItem(Material.SLIME_BALL, 50);
            }
            System.out.println("DefineDropTable: " + dropKeys.size() + " | " + customDropKeys.size());
        }

        /** Stores the necessary data in metadata to be accessed later */
        @Override
        public void saveMetaData() {
            super.saveMetaData();
            // Sets the metadata for the mob
            this.setMetadata("code", new FixedMetadataValue(ForgeFrontier.getInstance(), "HostileSlime"));
        }
    }

    public HostileSlime() {
        super("HostileSlime");
    }

    /**
     * Constructor for a custom slime entity
     *
     * @param customName name of the custom slime
     */
    public HostileSlime(String customName) {
        super(customName);
    }

    /**
     * Creates an entity instance of the TestChicken to spawn in the world
     *
     * @return the entity instance in the world
     */
    @Override
    public CraftEntity createCustomEntity(CraftWorld world) {
        return new HostileSlime.CraftHostileSlime((CraftServer) Bukkit.getServer(), world);
    }
}
