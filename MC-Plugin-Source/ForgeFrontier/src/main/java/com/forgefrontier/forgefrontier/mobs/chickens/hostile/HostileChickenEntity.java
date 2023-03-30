package com.forgefrontier.forgefrontier.mobs.chickens.hostile;

import com.forgefrontier.forgefrontier.mobs.chickens.CustomChickenEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Chicken;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.bukkit.Bukkit;

public class HostileChickenEntity extends CustomChickenEntity {

    private float idleSpeed;
    private float aggroSpeed;

    public HostileChickenEntity(EntityType<? extends Chicken> entityTypes, Level world) {
        super(entityTypes, world);
        idleSpeed = 1.0f;
        aggroSpeed = 1.2f;
    }

    @Override
    public void customTick() {
        super.customTick();

        // gets the nearest player as a target
        Player nearestPlayer = this.getLevel().getNearestPlayer(this, 20.0);

        // checks to see if a player was found within range
        if (nearestPlayer != null) {
            // converts NMS to Spigot object
            org.bukkit.entity.Player spigotPlayer = Bukkit.getPlayer(nearestPlayer.getUUID());
            // sets speed to faster when attacking a player
            this.setSpeed(aggroSpeed);
            // follow the player
            this.getNavigation().moveTo(nearestPlayer, this.getSpeed());

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
        player.damage(1);
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
}
