package com.forgefrontier.forgefrontier.mobs.slimes.hostile.slimeboss.phasetwo;

import com.forgefrontier.forgefrontier.ForgeFrontier;
import com.forgefrontier.forgefrontier.mobs.slimes.hostile.HostileSlime;
import com.forgefrontier.forgefrontier.mobs.slimes.hostile.slimeboss.SlimeBoss;
import com.forgefrontier.forgefrontier.mobs.slimes.hostile.slimeboss.SlimeBossEntity;
import net.minecraft.world.entity.EntityType;
import org.bukkit.Bukkit;
import org.bukkit.boss.BossBar;
import org.bukkit.craftbukkit.v1_18_R2.CraftServer;
import org.bukkit.craftbukkit.v1_18_R2.CraftWorld;
import org.bukkit.craftbukkit.v1_18_R2.entity.CraftEntity;
import org.bukkit.metadata.FixedMetadataValue;

public class SlimeBossSmall extends SlimeBoss {

    public static String CODE = "SlimeBossSmall";
    public static class CraftSlimeBossSmall extends SlimeBoss.CraftSlimeBoss {

        /**
         * Constructor
         *
         * @param server    the CraftServer instance
         * @param world     the world instance
         */
        public CraftSlimeBossSmall(CraftServer server, CraftWorld world) {
            super(server, "SlimeBossSmall", 200, 5, new SlimeBossSmallEntity(EntityType.SLIME, world.getHandle()));
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
            this.bossBar.setProgress(0);
            this.bossBar.removeAll();
            this.bossBar.setVisible(false);
        }

        @Override
        public void defineDropTable() {
            super.defineDropTable();
        }

        /** Stores the necessary data in metadata to be accessed later */
        @Override
        public void saveMetaData() {
            super.saveMetaData();
            this.setMetadata("code", new FixedMetadataValue(ForgeFrontier.getInstance(), "SlimeBossSmall"));
            this.setMetadata("bossbar", new FixedMetadataValue(ForgeFrontier.getInstance(), null));
            this.setMetadata("name", new FixedMetadataValue(ForgeFrontier.getInstance(), "Bounce Slime"));
        }
    }

    public SlimeBossSmall() {
        super("SlimeBossSmall");
    }

    /**
     * Constructor for a custom slime entity
     *
     * @param customName name of the custom slime
     */
    public SlimeBossSmall(String customName) {
        super(customName);
    }

    /**
     * Creates an entity instance of the TestChicken to spawn in the world
     *
     * @return the entity instance in the world
     */
    @Override
    public CraftEntity createCustomEntity(CraftWorld world) {
        return new CraftSlimeBossSmall((CraftServer) Bukkit.getServer(), world);
    }
}
