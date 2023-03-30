package com.forgefrontier.forgefrontier.mobs.slimes.hitbox.eggbox.eggsplosive;

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

    /**
     * for when spawned by boss
     * @param entityTypes the type of entity (slime)
     * @param world the world
     * @param owner the boss instance
     */
    public EggsplosiveEntity(EntityType<? extends Slime> entityTypes, Level world, ChickBossEntity owner) {
        super(entityTypes, world);
        this.owner = owner;
        lastTimeStamp = 0;
        totalTimeAlive = 0;
    }

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
        
        // initializes lastTimeStamp
        if (lastTimeStamp == 0) {
            lastTimeStamp = System.currentTimeMillis();
        }

        if (totalTimeAlive >= 10000) { // explode on failure
            explode(40);
            eggEntity.setCustomName("");
            eggEntity.setCustomNameVisible(false);
            eggEntity.remove();
            if (owner != null) {
                owner.removeEgg(this);
            }
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
    }

    @Override
    public void setNamePlate(String name) {
        if (existed) {
            this.name = name;
            eggEntity.setCustomName(name + " | " + (10 - (totalTimeAlive / 1000)));
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
    }
}
