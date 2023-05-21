package com.forgefrontier.forgefrontier.custommobs;

import com.forgefrontier.forgefrontier.ForgeFrontier;
import com.forgefrontier.forgefrontier.custommobs.goals.LookAtPlayerGoal;
import com.forgefrontier.forgefrontier.custommobs.goals.MeleeAttackGoal;
import com.forgefrontier.forgefrontier.custommobs.goals.NearestAttackableTargetGoal;
import com.forgefrontier.forgefrontier.custommobs.passive.ChickenHandler;
import com.forgefrontier.forgefrontier.custommobs.passive.CustomGenericMob;
import com.forgefrontier.forgefrontier.utils.Manager;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_18_R2.CraftWorld;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.inventory.ItemStack;

public class EntityManager extends Manager {


    public EntityManager(ForgeFrontier plugin) {
        super(plugin);

    }

    @Override
    public void init() {

    }

    @Override
    public void disable() {

    }

    // TODO Debug Method
    public static void spawn(Location location) {
        ServerLevel worldServer = ((CraftWorld) location.getWorld()).getHandle();
        CustomGenericMob t = new CustomGenericMob(new ChickenHandler(), EntityType.CHICKEN, worldServer);
        worldServer.addFreshEntity(t.getHandle(), CreatureSpawnEvent.SpawnReason.CUSTOM);
        t.teleport(location);
    }

}
