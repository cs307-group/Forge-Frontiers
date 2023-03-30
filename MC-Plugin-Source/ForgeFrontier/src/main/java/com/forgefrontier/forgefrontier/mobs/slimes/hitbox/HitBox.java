package com.forgefrontier.forgefrontier.mobs.slimes.hitbox;

import com.forgefrontier.forgefrontier.ForgeFrontier;
import com.forgefrontier.forgefrontier.mobs.slimes.CustomCraftSlime;
import com.forgefrontier.forgefrontier.mobs.slimes.CustomSlime;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.craftbukkit.v1_18_R2.CraftServer;
import org.bukkit.craftbukkit.v1_18_R2.CraftWorld;
import org.bukkit.craftbukkit.v1_18_R2.entity.CraftEntity;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.metadata.FixedMetadataValue;

/**
 * Class which represents invisible hitboxes
 *
 * NOTE: When killed, you must spawn in any items you would like to drop in the tick function
 * in the corresponding entity class (i.e. HitBoxEntity). This is due to the necessity of teleporting
 * the slime entity to the void in order to avoid splitting.
 */
public class HitBox extends CustomSlime {

    /** class representing the custom CraftEntity for the TestChicken */
    public static class CraftHitBox extends CustomCraftSlime {

        // public BossBar bossBar;

        /**
         * Constructor
         *
         * @param server the CraftServer instance
         */
        public CraftHitBox(CraftServer server, CraftWorld world ) {
            super(server, "HitBox",
                    new HitBoxEntity(EntityType.SLIME, world.getHandle()));

            initHitBox("HitBox", 100, 4);
            saveMetaData();
        }

        /**
         * Constructor for inheritance
         *
         * @param server the CraftServer instance
         */
        public CraftHitBox(CraftServer server, String name, int health, HitBoxEntity entity, int size) {
            super(server, name, entity);
            initHitBox(name, health, size);
            saveMetaData();
        }

        public void initHitBox(String name, int health, int size) {
            this.setDefaultHealth(health);
            this.setAI(true);

            defineDropTable();
            this.setSize(size);
            this.setCollidable(false);
            this.setInvisible(true);

            AttributeInstance atk = this.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE);
            if (atk != null) {
                System.out.println("ATTRIBUTE SUCCESS!!!");
                atk.setBaseValue(0);
            }
        }

        @Override
        public void defineDropTable() {

        }

        /** Stores the necessary data in metadata to be accessed later */
        @Override
        public void saveMetaData() {
            super.saveMetaData();
            // Sets the metadata for the mob
            this.setMetadata("code", new FixedMetadataValue(ForgeFrontier.getInstance(), "HitBox"));
            // this.setMetadata("bossbar", new FixedMetadataValue(ForgeFrontier.getInstance(), bossBar));
        }
    }

    /** Constructor for TestChicken instance */
    public HitBox() {
        super("HitBox");
    }

    /** Constructor for inheritance */
    public HitBox(String name) {
        super(name);
    }

    /**
     * Creates an entity instance of the TestChicken to spawn in the world
     *
     * @return the entity instance in the world
     */
    @Override
    public CraftEntity createCustomEntity(CraftWorld world) {
        return new HitBox.CraftHitBox((CraftServer) Bukkit.getServer(), world);
    }

    @Override
    public CraftEntity spawnCustomEntity(Location loc, CraftEntity craftEntity) {
        loc.getWorld();
        ServerLevel worldServer = ((CraftWorld) loc.getWorld()).getHandle();
        worldServer.addFreshEntity(craftEntity.getHandle(), CreatureSpawnEvent.SpawnReason.CUSTOM);
        craftEntity.teleport(loc);
        HitBoxEntity entity = (HitBoxEntity) craftEntity.getHandle();
        entity.setLoc(loc);
        return craftEntity;
    }
}
