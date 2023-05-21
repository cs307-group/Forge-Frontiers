package com.forgefrontier.forgefrontier.custommobs;

import com.forgefrontier.forgefrontier.custommobs.goals.Goal;
import com.forgefrontier.forgefrontier.custommobs.passive.CustomGenericMob;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.BaseComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.goal.WrappedGoal;
import org.bukkit.attribute.Attribute;
import org.bukkit.inventory.ItemStack;
import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.List;

public abstract class CustomEntityHandler {

    // Fields
    ArrayList<Goal> goals;
    ArrayList<Goal> targetGoals;
    ArrayList<MobDrop> mobDrops;

    String mobName;
    int maxHealth;

    public CustomEntityHandler(String mobName, int maxHealth) {
        this.goals = new ArrayList<>();
        this.targetGoals = new ArrayList<>();
        this.mobDrops = new ArrayList<>();
        this.mobName = mobName;
        this.maxHealth = maxHealth;
    }

    // Methods ran by CustomGenericMob

    public void init(Mob mob) {
        mob.craftAttributes.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(this.maxHealth);
        mob.setHealth(this.maxHealth);
        mob.setCustomNameVisible(true);
        this.updateName(mob);
        this.onInit(mob);
    }

    public void runOnDeath(Mob mob) {
        // Spawn death particles
        // Add wrappers for other general death events that you don't want to implement everywhere.
        this.onDeath(mob);
    }

    public void dropLoot(Mob mob) {
        // TODO Drop loot from loot table.
    }

    public void hurt(Mob mob) {
        this.updateName(mob);
        this.onHurt(mob);
    }

    // Could be abstract or could be not abstract. Depends if you want an extra wrapper or not.
    public List<WrappedGoal> getNMSGoals(PathfinderMob mob) {
        return this.goals
            .stream()
            .map((goal) -> goal.asNMSWrappedGoal(mob))
            .toList();
    }

    public List<WrappedGoal> getNMSTargetGoals(PathfinderMob mob) {
        return this.targetGoals
                .stream()
                .map((goal) -> goal.asNMSWrappedGoal(mob))
                .toList();
    }

    public void tick(Mob mob) {
        // Wrap a stage system, event system, anything.
        this.onTick(mob);
    }

    // Private methods

    private void updateName(Mob mob) {
        mob.setCustomName(new TextComponent(this.mobName + " [" + (int) Math.ceil(mob.getHealth()) + "/" + (int) Math.ceil(mob.getMaxHealth()) + "]"));
    }

    // Registration methods for initialization.

    public void registerLoot(ItemStack itemStack, double chance) {
        this.mobDrops.add(new MobDrop(itemStack, chance));
    }

    public void registerGoal(Goal goal) {
        this.goals.add(goal);
    }
    public void registerTargetGoal(Goal goal) {
        this.targetGoals.add(goal);
    }

    // Abstract event methods

    public abstract void onInit(Mob mob);

    // Runs every time an entity is potentially hurt (not guaranteed to have been hurt)
    public abstract void onHurt(Mob mob);

    // Runs immediately after the entity has died.
    public abstract void onDeath(Mob mob);

    // Runs every tick. MINIMIZE USE PLEASE.
    public abstract void onTick(Mob mob);
}
