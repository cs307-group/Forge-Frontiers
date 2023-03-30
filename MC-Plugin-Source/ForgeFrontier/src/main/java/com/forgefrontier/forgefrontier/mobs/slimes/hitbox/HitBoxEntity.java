package com.forgefrontier.forgefrontier.mobs.slimes.hitbox;

import com.forgefrontier.forgefrontier.mobs.slimes.CustomSlimeEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.level.Level;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.craftbukkit.v1_18_R2.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_18_R2.entity.CraftLivingEntity;
import org.bukkit.craftbukkit.v1_18_R2.entity.CraftSlime;
import org.bukkit.entity.LivingEntity;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class HitBoxEntity extends CustomSlimeEntity {

    public Location loc;

    public HitBoxEntity(EntityType<? extends Slime> entityTypes, Level world) {
        super(entityTypes, world);

        this.loc = null;
        this.setNoGravity(true);
    }

    @Override
    public void customTick() {
        super.customTick();

        if (loc != null) {
            this.teleportTo(loc.getX(), loc.getY(), loc.getZ());
            loc.getWorld().spawnParticle(Particle.SLIME, loc, 1);
        }
    }

    public void setLoc(Location loc) {
        this.loc = loc;
    }
}
