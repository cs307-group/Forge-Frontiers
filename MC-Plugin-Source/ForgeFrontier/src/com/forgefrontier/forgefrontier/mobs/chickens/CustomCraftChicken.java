package com.forgefrontier.forgefrontier.mobs.chickens;

import com.forgefrontier.forgefrontier.mobs.CustomCraftEntity;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.animal.EntityChicken;
import org.bukkit.craftbukkit.v1_18_R2.CraftServer;
import org.bukkit.craftbukkit.v1_18_R2.CraftWorld;
import org.bukkit.craftbukkit.v1_18_R2.entity.CraftChicken;

/**
 * Class to represent the actual chicken entity in game
 */
public abstract class CustomCraftChicken extends CraftChicken implements CustomCraftEntity {

    String code;

    /**
     * Constructor
     * @param server the CraftServer instance
     */
    public CustomCraftChicken(CraftServer server, String chickenType) {
        super(server, new EntityChicken(EntityTypes.l, ((CraftWorld)server.getWorld("world")).getHandle()));
        this.code = chickenType;
    }

    @Override
    public String getCode() {
        return this.code;
    }
}
