package com.forgefrontier.forgefrontier.mobs.slimes.hitbox;

import com.forgefrontier.forgefrontier.mobs.slimes.CustomSlimeEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.level.Level;

public class HitBoxEntity extends CustomSlimeEntity {

    public HitBoxEntity(EntityType<? extends Slime> entityTypes, Level world) {
        super(entityTypes, world);
        this.setNoGravity(true);
    }

    /**
     * Overwrites basic Slime AI
     */
    @Override
    protected void registerGoals() {
        // removes all previous AI
    }

    @Override
    public void customTick() {
        super.customTick();

        if (loc != null) {
            whileAlive();
        }
    }

    public void whileAlive() {
        if (this.world == null) {
            this.world = loc.getWorld();
        }
        this.teleportTo(loc.getX(), loc.getY(), loc.getZ());
        // world.spawnParticle(Particle.EXPLOSION_HUGE, loc, 2);
    }

    public void setNamePlate(String name) {
    }
}
