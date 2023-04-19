package com.forgefrontier.forgefrontier.mobs.slimes.hostile.slimeboss.phasetwo;

import com.forgefrontier.forgefrontier.mobs.slimes.hostile.slimeboss.SlimeBossEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.level.Level;

public class SlimeBossSmallEntity extends SlimeBossEntity {

    SlimeBossTimerEntity owner;

    public SlimeBossSmallEntity(EntityType<? extends Slime> entityTypes, Level world) {
        super(entityTypes, world);
    }

    @Override
    public void onDeath() {
        System.out.println("SMALL ON DEATH");
        owner.removeSubEntity(this);
    }

    public void setOwner(SlimeBossTimerEntity entity) {
        this.owner = entity;
    }
}
