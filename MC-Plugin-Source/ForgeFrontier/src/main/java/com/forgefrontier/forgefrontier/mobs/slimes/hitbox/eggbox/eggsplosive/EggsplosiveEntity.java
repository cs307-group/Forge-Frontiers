package com.forgefrontier.forgefrontier.mobs.slimes.hitbox.eggbox.eggsplosive;

import com.forgefrontier.forgefrontier.ForgeFrontier;
import com.forgefrontier.forgefrontier.mobs.chickens.hostile.chickenboss.ChickBossEntity;
import com.forgefrontier.forgefrontier.mobs.slimes.hitbox.eggbox.EggBoxEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.level.Level;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class EggsplosiveEntity extends EggBoxEntity {

    ChickBossEntity owner;
    long lastTimeStamp;
    long totalTimeAlive;
    String name;
    int timeToExplode = 15;
    public int particleTask = -1;

    /**
     * For when not spawned by boss
     * @param entityTypes the type of entity (slime)
     * @param world the world
     */
    public EggsplosiveEntity(EntityType<? extends Slime> entityTypes, Level world) {
        super(entityTypes, world);
        this.owner = null;
        lastTimeStamp = 0;
        totalTimeAlive = 0;
    }

    @Override
    public void whileAlive() {
        super.whileAlive();
        if (this.getBukkitEntity().getCustomName() != null) {
            this.name = this.getBukkitEntity().getCustomName();
        }

        // initializes lastTimeStamp
        if (lastTimeStamp == 0) {
            lastTimeStamp = System.currentTimeMillis();
        }

        if (totalTimeAlive >= (timeToExplode * 1000)) { // explode on failure
            explode(40);
            eggEntity.setCustomName("");
            eggEntity.setCustomNameVisible(false);
            eggEntity.remove();
            this.remove(RemovalReason.KILLED);
        } else {
            totalTimeAlive += System.currentTimeMillis() - lastTimeStamp;
            lastTimeStamp = System.currentTimeMillis();
            setNamePlate(name);
        }
    }

    @Override
    public void dropItems() {
        super.dropItems();
        explode(20);
        if (owner != null) {
            owner.removeEgg(this);
        }
    }

    @Override
    public void setNamePlate(String name) {
        if (existed) {
            this.name = name;
            eggEntity.setCustomName(name + " | " + (timeToExplode - (totalTimeAlive / 1000)));
            eggEntity.setCustomNameVisible(true);
        }
    }

    public void explode(int damage) {
        world.spawnParticle(Particle.EXPLOSION_HUGE, loc, 2);
        world.playSound(loc, Sound.ENTITY_GENERIC_EXPLODE, 1.0f, 1.0f);

        ArrayList<Player> players = new ArrayList<Player>();
        List<Entity> nearbyEntities = this.getBukkitEntity().getNearbyEntities(6, 6, 6);
        for (int i = 0; i < nearbyEntities.size(); i++) {
            if (nearbyEntities.get(i) instanceof Player player) {
                player.damage(damage);
            }
        }
        if (particleTask != -1) {
            ForgeFrontier.getInstance().getServer().getScheduler().cancelTask(particleTask);
            particleTask = -1;
        }
    }

    public void setOwner(ChickBossEntity owner) {
        this.owner = owner;
    }
}
