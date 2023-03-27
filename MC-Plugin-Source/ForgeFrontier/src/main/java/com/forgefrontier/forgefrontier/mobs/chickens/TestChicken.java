package com.forgefrontier.forgefrontier.mobs.chickens;

import com.forgefrontier.forgefrontier.ForgeFrontier;
import com.forgefrontier.forgefrontier.items.CustomItemManager;
import net.minecraft.world.entity.ai.attributes.*;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.craftbukkit.v1_18_R2.CraftServer;
import org.bukkit.craftbukkit.v1_18_R2.attribute.CraftAttributeMap;
import org.bukkit.craftbukkit.v1_18_R2.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_18_R2.entity.CraftLivingEntity;
import org.bukkit.entity.*;
import org.bukkit.metadata.FixedMetadataValue;

import java.lang.reflect.Field;
import java.util.*;

/**
 * Example custom chicken class used for testing
 */
public class TestChicken extends CustomChicken {

    /** class representing the custom CraftEntity for the TestChicken */
    public static class CraftTestChicken extends CustomCraftChicken {

        private static Field attributeField;

        static {
            try {

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public void registerGenericAttribute(org.bukkit.entity.Entity entity, Attribute attribute) throws IllegalAccessException {

        }

        public BossBar bossBar;

        /**
         * Constructor
         *
         * @param server the CraftServer instance
         */
        public CraftTestChicken(CraftServer server) {
            super(server, "TestChicken");

            // sets the health of the chicken
            this.setMaxHealth(10);
            this.setHealth(10);

            // sets the nameplate of the chicken
            this.setCustomName(ChatColor.WHITE + "TESTCHICKEN" + this.getHealth());
            this.setCustomNameVisible(true);

            this.setAI(true);

            try {
                registerGenericAttribute(this, Attribute.GENERIC_ATTACK_DAMAGE);
            } catch (Exception e) {
                e.printStackTrace();
            }

            bossBar = Bukkit.createBossBar("BOSS CHICKEN", BarColor.RED, BarStyle.SEGMENTED_20);
            bossBar.setVisible(true);
            bossBar.setProgress(1);
            //TODO: Change this later
            Map<UUID, Player> playerMap = ForgeFrontier.getInstance().getPlayerManager().getPlayersByUUID();
            Collection<Player> playerCollection = playerMap.values();
            Object[] players = playerCollection.toArray();
            for (Object player : players) {
                Player p = (Player) player;
                bossBar.addPlayer(p);
            }

            this.setMetadata("code", new FixedMetadataValue(ForgeFrontier.getInstance(), "TestChicken"));
            this.setMetadata("bossbar", new FixedMetadataValue(ForgeFrontier.getInstance(), bossBar));

            defineLootTable();
        }

        /**
         * Defines the loot table for the TestChicken
         */
        private void defineLootTable() {
            // Clears all drops
            handle.drops.clear();

            // Drops a wooden sword 50% of the time
            if (((int) (Math.random() * 100)) + 1 < 100) {
                handle.drops.add(CustomItemManager.getCustomItem("WoodenSword").asInstance(null).asItemStack());
            }
        }


    }

    /** Constructor for TestChicken instance */
    public TestChicken() {
        super("TestChicken");
    }

    /**
     * spawns an instance of the entity at the location
     *
     * @param loc the location the entity will be spawned
     */
    @Override
    public CraftEntity spawnCustomEntity(Location loc, CraftEntity craftEntity) {
        CraftTestChicken chicken = (CraftTestChicken) super.spawnCustomEntity(loc, craftEntity);
        if (chicken != null) {
            System.out.println("INSTANCE OF TEST");
        }
        return chicken;
    }

    /**
     * Creates an entity instance of the TestChicken to spawn in the world
     *
     * @return the entity instance in the world
     */
    @Override
    public CraftEntity createCustomEntity() {
        return new CraftTestChicken((CraftServer) Bukkit.getServer());
    }

    /**
     * @return the corresponding CraftEntity wrapper class of this entity wrapper class
     */
    @Override
    public Class<? extends CraftEntity> getCorrespondingCraftEntity() {
        return CraftTestChicken.class;
    }
}
