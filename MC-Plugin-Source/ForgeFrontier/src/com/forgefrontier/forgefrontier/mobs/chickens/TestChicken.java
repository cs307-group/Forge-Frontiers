package com.forgefrontier.forgefrontier.mobs.chickens;

import com.forgefrontier.forgefrontier.ForgeFrontier;
import com.forgefrontier.forgefrontier.items.CustomItemManager;
import com.forgefrontier.forgefrontier.items.gear.instanceclasses.weapons.swords.WoodenSword;
import net.minecraft.world.entity.animal.EntityChicken;
import org.bukkit.*;
import org.bukkit.craftbukkit.v1_18_R2.CraftServer;
import org.bukkit.craftbukkit.v1_18_R2.entity.CraftChicken;
import org.bukkit.craftbukkit.v1_18_R2.entity.CraftEntity;
import org.bukkit.entity.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.loot.LootContext;
import org.bukkit.loot.LootTable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

/**
 * Example custom chicken class used for testing
 */
public class TestChicken extends CustomChicken {

    /** class representing the custom CraftEntity for the TestChicken */
    public static class CraftTestChicken extends CustomCraftChicken {
        /**
         * Constructor
         *
         * @param server the CraftServer instance
         */
        public CraftTestChicken(CraftServer server) {
            super(server, "TestChicken");

            // sets the health of the chicken
            this.setMaxHealth(100);
            this.setHealth(100);

            // sets the nameplate of the chicken
            this.setCustomName(ChatColor.WHITE + "TESTCHICKEN" + this.getHealth());
            this.setCustomNameVisible(true);

            this.setAI(true);

            defineLootTable();

            EntityChicken handle = this.getHandle();
        }

        private void defineLootTable() {
            this.setLootTable(new LootTable() {
                @NotNull
                @Override
                public Collection<ItemStack> populateLoot(@Nullable Random random, @NotNull LootContext context) {
                    Collection<ItemStack> loot = new ArrayList<ItemStack>();
                    loot.add(CustomItemManager.getCustomItem("WoodenSword").asInstance(null).asItemStack());
                    return loot;
                }

                @Override
                public void fillInventory(@NotNull Inventory inventory, @Nullable Random random, @NotNull LootContext context) {
                    inventory.addItem(CustomItemManager.getCustomItem("WoodenSword").asInstance(null).asItemStack());
                }

                @NotNull
                @Override
                public NamespacedKey getKey() {
                    NamespacedKey key = new NamespacedKey(ForgeFrontier.getInstance(), "TestChicken");
                    return key;
                }
            });
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

    /** defines the behaviour of the entity */
    @Override
    public void executeBehavior() {

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
}
