package com.forgefrontier.forgefrontier.mobs.slimes.hostile.slimeboss.phasetwo;

import com.forgefrontier.forgefrontier.mobs.slimes.hostile.HostileSlimeEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.level.Level;

public class SlimeBossSwiftEntity extends HostileSlimeEntity {

    SlimeBossTimerEntity owner;

    public SlimeBossSwiftEntity(EntityType<? extends Slime> entityTypes, Level world) {
        super(entityTypes, world);
        setDamageValue(20);
        setAggroSpeed(2);
    }

    public void onDeath() {
        owner.removeSubEntity(this);
    }

    public void setOwner(SlimeBossTimerEntity entity) {
        this.owner = entity;
    }

}
