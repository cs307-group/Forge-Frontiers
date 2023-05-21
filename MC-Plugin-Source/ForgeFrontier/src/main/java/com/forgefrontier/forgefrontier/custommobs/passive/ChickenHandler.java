package com.forgefrontier.forgefrontier.custommobs.passive;

import com.forgefrontier.forgefrontier.custommobs.CustomEntityHandler;
import com.forgefrontier.forgefrontier.custommobs.goals.LookAtPlayerGoal;
import net.minecraft.world.entity.Mob;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class ChickenHandler extends CustomEntityHandler {

    public ChickenHandler() {
        super("Chicken", 20);

        this.registerLoot(new ItemStack(Material.DIAMOND), 0.5);
        // Note: The goal is a wrapper, not the NMS version of the class.
        this.registerGoal(new LookAtPlayerGoal(1));

    }

    @Override
    public void onInit(Mob mob) {

    }

    @Override
    public void onHurt(Mob mob) {

    }

    @Override
    public void onDeath(Mob mob) {

    }

    @Override
    public void onTick(Mob mob) {

    }

}
