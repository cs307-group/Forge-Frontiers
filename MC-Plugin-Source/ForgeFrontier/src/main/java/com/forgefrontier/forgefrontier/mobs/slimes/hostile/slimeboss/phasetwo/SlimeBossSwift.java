package com.forgefrontier.forgefrontier.mobs.slimes.hostile.slimeboss.phasetwo;

import com.forgefrontier.forgefrontier.ForgeFrontier;
import com.forgefrontier.forgefrontier.mobs.slimes.hostile.HostileSlime;
import net.minecraft.world.entity.EntityType;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_18_R2.CraftServer;
import org.bukkit.craftbukkit.v1_18_R2.CraftWorld;
import org.bukkit.craftbukkit.v1_18_R2.entity.CraftEntity;
import org.bukkit.metadata.FixedMetadataValue;

public class SlimeBossSwift extends HostileSlime {

    public static String CODE = "SlimeBossSwift";
    public static class CraftSlimeBossSwift extends CraftHostileSlime {

        /**
         * Constructor
         *
         * @param server    the CraftServer instance
         * @param world     the world instance
         */
        public CraftSlimeBossSwift(CraftServer server, CraftWorld world) {
            super(server, "SlimeBossSwift", 200, 5, new SlimeBossSwiftEntity(EntityType.SLIME, world.getHandle()));
            saveMetaData();
        }

        /** Stores the necessary data in metadata to be accessed later */
        @Override
        public void saveMetaData() {
            super.saveMetaData();
            this.setMetadata("code", new FixedMetadataValue(ForgeFrontier.getInstance(), "SlimeBossSwift"));
            this.setMetadata("name", new FixedMetadataValue(ForgeFrontier.getInstance(), "Swift Slime"));
        }
    }

    public SlimeBossSwift() {
        super("SlimeBossSwift");
    }

    /**
     * Constructor for a custom slime entity
     *
     * @param customName name of the custom slime
     */
    public SlimeBossSwift(String customName) {
        super(customName);
    }

    /**
     * Creates an entity instance of the TestChicken to spawn in the world
     *
     * @return the entity instance in the world
     */
    @Override
    public CraftEntity createCustomEntity(CraftWorld world) {
        return new CraftSlimeBossSwift((CraftServer) Bukkit.getServer(), world);
    }
}
