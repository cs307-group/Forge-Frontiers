package com.forgefrontier.forgefrontier.mobs.slimes.hostile.slimeboss;

import com.forgefrontier.forgefrontier.ForgeFrontier;
import com.forgefrontier.forgefrontier.mobs.slimes.hostile.HostileSlime;
import net.minecraft.world.entity.EntityType;
import org.bukkit.Bukkit;
import org.bukkit.boss.BossBar;
import org.bukkit.craftbukkit.v1_18_R2.CraftServer;
import org.bukkit.craftbukkit.v1_18_R2.CraftWorld;
import org.bukkit.craftbukkit.v1_18_R2.entity.CraftEntity;
import org.bukkit.metadata.FixedMetadataValue;

public class SlimeBoss extends HostileSlime {

    public static String CODE = "SlimeBoss";
    public static class CraftSlimeBoss extends HostileSlime.CraftHostileSlime {

        public BossBar bossBar;

        /**
         * Constructor
         *
         * @param server    the CraftServer instance
         * @param world     the world instance
         */
        public CraftSlimeBoss(CraftServer server, CraftWorld world) {
            super(server, "SlimeBoss", 400, 7, new SlimeBossEntity(EntityType.SLIME, world.getHandle()));
            saveMetaData();
        }
        public CraftSlimeBoss(CraftServer server, String name, int health, int scale, SlimeBossEntity entity) {
            super(server, name, health, scale, entity);
            saveMetaData();
        }

        /**
         * Sets initial values of slime
         *
         * @param name the name of the slime
         * @param health the max health of the slime
         */
        public void initCraftSlime(String name, int health, int scale) {
            super.initCraftSlime(name, health, scale);
            bossBar = initBossBar("King Slime");
        }

        @Override
        public void defineDropTable() {
            super.defineDropTable();
        }

        /** Stores the necessary data in metadata to be accessed later */
        @Override
        public void saveMetaData() {
            super.saveMetaData();
            this.setMetadata("bossbar", new FixedMetadataValue(ForgeFrontier.getInstance(), bossBar));
            this.setMetadata("name", new FixedMetadataValue(ForgeFrontier.getInstance(), "King Slime"));
            this.setMetadata("code", new FixedMetadataValue(ForgeFrontier.getInstance(), "SlimeBoss"));
            // this.setMetadata("tier", new FixedMetadataValue(ForgeFrontier.getInstance(), 2));
        }
    }

    public SlimeBoss() {
        super("SlimeBoss");
    }

    /**
     * Constructor for a custom slime entity
     *
     * @param customName name of the custom slime
     */
    public SlimeBoss(String customName) {
        super(customName);
    }

    /**
     * Creates an entity instance of the TestChicken to spawn in the world
     *
     * @return the entity instance in the world
     */
    @Override
    public CraftEntity createCustomEntity(CraftWorld world) {
        return new CraftSlimeBoss((CraftServer) Bukkit.getServer(), world);
    }
}
