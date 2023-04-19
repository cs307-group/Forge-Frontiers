package com.forgefrontier.forgefrontier.mobs.slimes.hostile.slimeboss.phasetwo;

import com.forgefrontier.forgefrontier.ForgeFrontier;
import com.forgefrontier.forgefrontier.mobs.slimes.CustomCraftSlime;
import com.forgefrontier.forgefrontier.mobs.slimes.CustomSlime;
import com.forgefrontier.forgefrontier.mobs.slimes.CustomSlimeEntity;
import com.forgefrontier.forgefrontier.mobs.slimes.hostile.HostileSlimeEntity;
import net.minecraft.world.entity.EntityType;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.boss.BossBar;
import org.bukkit.craftbukkit.v1_18_R2.CraftServer;
import org.bukkit.craftbukkit.v1_18_R2.CraftWorld;
import org.bukkit.craftbukkit.v1_18_R2.entity.CraftEntity;
import org.bukkit.metadata.FixedMetadataValue;

public class SlimeBossTimer extends CustomSlime {

    public static String CODE = "SlimeBossTimer";
    public static class CraftSlimeBossTimer extends CustomCraftSlime {

        BossBar bossBar;

        /**
         * Constructor
         *
         * @param server    the CraftServer instance
         * @param world     the world instance
         */
        public CraftSlimeBossTimer(CraftServer server, CraftWorld world) {
            super(server, "SlimeBossTimer", new SlimeBossTimerEntity(EntityType.SLIME, world.getHandle()));
            initCraftSlime("King Slime's Core", 200, 5);
            saveMetaData();
        }

        /**
         * Sets initial values of slime
         *
         * @param name the name of the slime
         * @param health the max health of the slime
         */
        public void initCraftSlime(String name, int health, int scale) {
            this.customCraftName = name;

            setScale(scale);                // Note: Must set health after the size
            this.setDefaultHealth(health);  // Otherwise health will be = size^2

            AttributeInstance atk = this.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE);
            if (atk != null) {
                atk.setBaseValue(0);
            }

            this.updateNamePlate(customCraftName);
            bossBar = initBossBar("King Slime's Core");
        }

        @Override
        public void updateNamePlate(String name) {
            super.updateNamePlate(name);
            System.out.println("UPDATE NAME PLATE: " + this.getCustomName());
        }

        @Override
        public void defineDropTable() {
            CustomSlimeEntity entity = (CustomSlimeEntity) this.getHandle();
            entity.registerDropItem(Material.SLIME_BALL, 100);
            entity.registerDropItem(Material.SLIME_BALL, 100);
            for (int i = 0; i < 5; i++) {
                entity.registerDropItem(Material.SLIME_BALL, 50);
            }
        }

        /** Stores the necessary data in metadata to be accessed later */
        @Override
        public void saveMetaData() {
            super.saveMetaData();
            this.setMetadata("code", new FixedMetadataValue(ForgeFrontier.getInstance(), "SlimeBossTimer"));
            this.setMetadata("bossbar", new FixedMetadataValue(ForgeFrontier.getInstance(), bossBar));
            this.setMetadata("name", new FixedMetadataValue(ForgeFrontier.getInstance(), "King Slime's Core"));
            this.setMetadata("tier", new FixedMetadataValue(ForgeFrontier.getInstance(), 2));
        }

        public void removeBossBar() {
            System.out.println("REMOVE BAR");
            bossBar.setProgress(0);
            bossBar.removeAll();
        }
    }

    public SlimeBossTimer() {
        super("SlimeBossTimer");
    }

    /**
     * Constructor for a custom slime entity
     *
     * @param customName name of the custom slime
     */
    public SlimeBossTimer(String customName) {
        super(customName);
    }

    /**
     * Creates an entity instance of the TestChicken to spawn in the world
     *
     * @return the entity instance in the world
     */
    @Override
    public CraftEntity createCustomEntity(CraftWorld world) {
        return new CraftSlimeBossTimer((CraftServer) Bukkit.getServer(), world);
    }
}
