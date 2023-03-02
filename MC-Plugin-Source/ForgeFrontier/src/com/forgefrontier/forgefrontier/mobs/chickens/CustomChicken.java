package com.forgefrontier.forgefrontier.mobs.chickens;

import com.forgefrontier.forgefrontier.mobs.CustomEntity;
import com.forgefrontier.forgefrontier.mobs.CustomMob;
import net.minecraft.network.chat.ChatComponentText;
import org.bukkit.*;
import org.bukkit.attribute.Attributable;
import org.bukkit.entity.*;


/**
 * Superclass for all chicken entities in Forge Frontier
 */
public abstract class CustomChicken extends CustomMob implements Chicken {

    /**
     * Constructor for a custom chicken entity
     *
     * @param customName name of the custom chicken
     * @param maxHealth max health for the specified chicken
     */
    public CustomChicken(String customName, double maxHealth) {
        super(customName, EntityType.CHICKEN);

        setCustomName(new ChatComponentText("CHICKEN").toString() + ChatColor.WHITE + ChatColor.BOLD);
        setCustomNameVisible(true);
        setHealth(maxHealth);
    }


    /**
     * spawns an instance of the entity at the location
     *
     * @param loc the location the entity will be spawned
     */
    @Override
    public Entity spawnCustomEntity(Location loc) {
        return super.spawnCustomEntity(loc);
    }
}