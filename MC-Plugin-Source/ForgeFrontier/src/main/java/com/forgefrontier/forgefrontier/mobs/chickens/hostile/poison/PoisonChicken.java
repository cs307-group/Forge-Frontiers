package com.forgefrontier.forgefrontier.mobs.chickens.hostile.poison;

import com.forgefrontier.forgefrontier.mobs.chickens.CustomCraftChicken;
import com.forgefrontier.forgefrontier.mobs.chickens.hostile.HostileChicken;
import net.minecraft.world.entity.EntityType;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_18_R2.CraftServer;
import org.bukkit.craftbukkit.v1_18_R2.CraftWorld;
import org.bukkit.craftbukkit.v1_18_R2.entity.CraftEntity;

public class PoisonChicken extends HostileChicken {

    /** class representing the custom CraftEntity for the TestChicken */
    public static class CraftPoisonChicken extends CraftHostileChicken {
        public CraftPoisonChicken(CraftServer server) {
            super(server, "PoisonChicken", 15,
                    new PoisonChickenEntity(EntityType.CHICKEN, ((CraftWorld)server.getWorld("world")).getHandle()));
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
    }

    /** Constructor for TestChicken instance */
    public PoisonChicken() {
        super("PoisonChicken");
    }


    /**
     * Creates an entity instance of the TestChicken to spawn in the world
     *
     * @return the entity instance in the world
     */
    @Override
    public CraftEntity createCustomEntity() {
        return new CraftPoisonChicken((CraftServer) Bukkit.getServer());
    }
}
