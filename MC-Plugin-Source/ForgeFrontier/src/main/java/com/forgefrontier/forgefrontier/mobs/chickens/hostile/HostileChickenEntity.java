package com.forgefrontier.forgefrontier.mobs.chickens.hostile;

import com.forgefrontier.forgefrontier.ForgeFrontier;
import com.forgefrontier.forgefrontier.mobs.chickens.CustomChickenEntity;
import com.forgefrontier.forgefrontier.particles.ParticleManager;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Chicken;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

public class HostileChickenEntity extends CustomChickenEntity {

    private float idleSpeed;
    private float aggroSpeed;
    public long lastDamageTime = 0;
    private double damage;
    private Player nearestPlayer;

    public HostileChickenEntity(EntityType<? extends Chicken> entityTypes, Level world) {
        super(entityTypes, world);
        idleSpeed = 1.0f;
        aggroSpeed = 1.2f;
        damage = 10;
    }

    @Override
    public void customTick() {
        super.customTick();
        doHostileBehavior();
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
            // sets speed to faster when attacking a player
            this.setSpeed(aggroSpeed);
            // follow the player
            this.getNavigation().moveTo(nearestPlayer, this.getSpeed());
            this.lookAt(nearestPlayer, 0, 0);

            // check if enough time has passed for the entity to attack, and if so damage the player
            if (spigotPlayer != null && System.currentTimeMillis() - lastDamageTime > 1000 &&
                    nearestPlayer.distanceTo(this) <= 1.5) {
                attack(spigotPlayer);
            }
        } else {
            // return to normal speed when not attacking
            this.setSpeed(idleSpeed);
            this.getNavigation().moveTo(this, this.getSpeed());
        }
    }

    public void attack(org.bukkit.entity.Player player) {
        EntityDamageByEntityEvent event = new EntityDamageByEntityEvent(this.getBukkitEntity(),
                player, EntityDamageEvent.DamageCause.ENTITY_ATTACK, this.damage);
        Bukkit.getServer().getPluginManager().callEvent(event);
        if (!event.isCancelled()) {
            // ATTAK PARTICLE
            Location l = this.getBukkitEntity().getLocation();
            player.getWorld().spawnParticle(Particle.SWEEP_ATTACK, l.clone().add(l.getDirection()), 2);
            player.playSound(this.getBukkitEntity(), Sound.ENTITY_PLAYER_ATTACK_CRIT,0.7F,1);
            double eventDamage = event.getDamage();
            player.damage(eventDamage);

        }
        lastDamageTime = System.currentTimeMillis();
    }

    public float getAggroSpeed() {
        return aggroSpeed;
    }

    public float getIdleSpeed() {
        return idleSpeed;
    }

    public void setAggroSpeed(float speed) {
        this.aggroSpeed = speed;
    }

    public void setIdleSpeed(float speed) {
        this.idleSpeed = speed;
    }

    public double getDamage() {
        return this.damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }
}
