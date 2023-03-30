package com.forgefrontier.forgefrontier.mobs.chickens.hostile.poison;

import com.forgefrontier.forgefrontier.mobs.chickens.hostile.HostileChickenEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Chicken;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.level.Level;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class PoisonChickenEntity extends HostileChickenEntity {
    public PoisonChickenEntity(EntityType<? extends Chicken> entityTypes, Level world) {
        super(entityTypes, world);
    }

    @Override
    public void attack(org.bukkit.entity.Player player) {
        super.attack(player);
        player.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 200, 0, false, true, true));
    }
}
