package com.forgefrontier.forgefrontier.mobs.slimes.hostile;

import com.forgefrontier.forgefrontier.ForgeFrontier;
import com.forgefrontier.forgefrontier.mobs.slimes.CustomSlimeEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.Vec3;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

public class HostileSlimeEntity extends CustomSlimeEntity {

    // used to pause natural behavior in subclasses
    boolean doBehavior;

    private final float idleSpeed;
    private final float aggroSpeed;
    public long lastDamageTime = 0;
    private final double damage;
    private Player nearestPlayer;
    private int scale;

    public HostileSlimeEntity(EntityType<? extends Slime> entityTypes, Level world) {
        super(entityTypes, world);
        idleSpeed = 1.0f;
        aggroSpeed = 1.0f;
        damage = 5;

        doBehavior = true;
    }

    @Override
    public void customTick() {
        super.customTick();
        if (doBehavior) {
            doHostileBehavior();
        }
    }

    /**
     * Defines behavior similar to a zombie's default behavior
     */
    public void doHostileBehavior() {
        boolean expensive = determineExpensive();
        // gets the nearest player as a target
        if (expensive) {
            nearestPlayer = this.getLevel().getNearestPlayer(this, 20.0);
        }

        // checks to see if a player was found within range
        if (nearestPlayer != null) {
            // converts NMS to Spigot object
            org.bukkit.entity.Player spigotPlayer = Bukkit.getPlayer(nearestPlayer.getUUID());

            // check if enough time has passed for the entity to attack, and if so damage the player
            if (spigotPlayer != null && System.currentTimeMillis() - lastDamageTime > 1000 &&
                    nearestPlayer.distanceTo(this) <= (scale / 2.0) + 1.5) {
                attack(spigotPlayer);
            }
        }
    }

    public void attack(org.bukkit.entity.Player player) {
        if (player != null) {
            EntityDamageByEntityEvent event = new EntityDamageByEntityEvent(this.getBukkitEntity(),
                    player, EntityDamageEvent.DamageCause.ENTITY_ATTACK, this.damage);
            Bukkit.getServer().getPluginManager().callEvent(event);
            if (!event.isCancelled()) {
                double eventDamage = event.getDamage();
                player.damage(eventDamage, this.getBukkitEntity());
            }
            lastDamageTime = System.currentTimeMillis();
        }
    }


    public void attack(org.bukkit.entity.Player player, double damage) {
        if (player != null) {
            EntityDamageByEntityEvent event = new EntityDamageByEntityEvent(this.getBukkitEntity(),
                    player, EntityDamageEvent.DamageCause.ENTITY_ATTACK, damage);
            Bukkit.getServer().getPluginManager().callEvent(event);
            if (!event.isCancelled()) {
                double eventDamage = event.getDamage();
                player.damage(eventDamage, this.getBukkitEntity());
            }
            lastDamageTime = System.currentTimeMillis();
        }
    }

    public void setScale(int scale) {
        this.scale = scale;
    }

    public void setDoBehavior(boolean value) {
        doBehavior = value;
    }

    public boolean getDoBehavior() {
        return doBehavior;
    }

    public boolean hasNearestPlayer() {
        return nearestPlayer == null;
    }
}