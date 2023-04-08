package com.forgefrontier.forgefrontier.mobs.chickens.dynamic;

import com.forgefrontier.forgefrontier.ForgeFrontier;
import com.forgefrontier.forgefrontier.mobs.chickens.hostile.HostileChicken;
import net.minecraft.world.entity.EntityType;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_18_R2.CraftServer;
import org.bukkit.craftbukkit.v1_18_R2.CraftWorld;
import org.bukkit.craftbukkit.v1_18_R2.entity.CraftEntity;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.HashMap;

public class DynamicChicken extends HostileChicken {

    /** class representing the custom CraftEntity for the TestChicken */
    public static class CraftDynamicChicken extends CraftHostileChicken {
        public CraftDynamicChicken(CraftServer server, CraftWorld world) {
            super(server, "PoisonChicken", 20,
                    new DynamicChickenEntity(EntityType.CHICKEN, world.getHandle()));
        }

        @Override
        public void defineDropTable() {
            registerDropItem(Material.SLIME_BALL, 100);
            registerDropItem(Material.POISONOUS_POTATO, 50);
            registerDropItem("SilverIngot", 100);
            registerDropItem("SilverIngot", 50);
            registerDropItem("SilverIngot", 50);
            registerDropItem("SilverIngot", 50);
        }

        @Override
        public void updateNamePlate(String name) {
            // sets the nameplate of the chicken
            // System.out.println("Updating nameplate");
            this.setCustomName(ChatColor.WHITE + "[☆] " + name + ": " + ((int) this.getHealth()) + "/" + ((int) this.getMaxHealth()));
            this.setCustomNameVisible(true);
        }

        @Override
        public void saveMetaData() {
            super.saveMetaData();
            this.setMetadata("code", new FixedMetadataValue(ForgeFrontier.getInstance(), "DynamicChicken"));
            HashMap<String, Double> values = new HashMap<>();
            values.put("incoming", 5.0); // damage to entity
            values.put("outgoing", 2.0); // damage to player
            this.setMetadata("dynamic", new FixedMetadataValue(ForgeFrontier.getInstance(), values));
        }
    }

    /** Constructor for TestChicken instance */
    public DynamicChicken() {
        super("DynamicChicken");
    }

    /**
     * Creates an entity instance of the TestChicken to spawn in the world
     *
     * @return the entity instance in the world
     */
    @Override
    public CraftEntity createCustomEntity(CraftWorld world) {
        return new DynamicChicken.CraftDynamicChicken((CraftServer) Bukkit.getServer(), world);
    }
}
