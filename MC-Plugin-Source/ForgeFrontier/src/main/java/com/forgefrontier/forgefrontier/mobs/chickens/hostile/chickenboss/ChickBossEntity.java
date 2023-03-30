package com.forgefrontier.forgefrontier.mobs.chickens.hostile.chickenboss;

import com.forgefrontier.forgefrontier.mobs.chickens.hostile.HostileChickenEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Chicken;
import net.minecraft.world.level.Level;

public class ChickBossEntity extends HostileChickenEntity {

    boolean hasBeenInvuln;
    int failCounter;
    long invulnTime;
    long lastTickTime;

    public ChickBossEntity(EntityType<? extends Chicken> entityTypes, Level world) {
        super(entityTypes, world);
        setIdleSpeed(1.0f);
        setAggroSpeed(1.0f);
        setDamage(10);
        failCounter = 0;
        hasBeenInvuln = false;
        invulnTime = 0;
        lastTickTime = 0;
    }

    @Override
    public void customTick() {

        if (hasBeenInvuln) {
            System.out.println("Still Invulnerable: " + invulnTime);
            invulnTime += System.currentTimeMillis() - lastTickTime;
            lastTickTime = System.currentTimeMillis();
        }

        if (failCounter == 3) { // enraged phase (you won't survive)
            System.out.println("ENRAGED");
            this.setAggroSpeed(2.0f);
            this.setDamage(30);
            doHostileBehavior();
        } else if (this.getHealth() > 100) { // Phase 1
            System.out.println("Phase 1");
            doHostileBehavior();
        } else if (!hasBeenInvuln) { // Transition into Phase 2 (Destroy the Eggsplosives)
            System.out.println("Invulnerable");
            this.setInvulnerable(true);
            hasBeenInvuln = true;
            lastTickTime = System.currentTimeMillis();
        } else if (invulnTime >= 10000) { // If the player fails to destroy all the eggsplosives, re-enter phase 1
            this.setInvulnerable(false);
            hasBeenInvuln = false;
            this.setHealth(150);
            invulnTime = 0;
            lastTickTime = 0;
            failCounter++;
            System.out.println("FAIL: END OF INVULNERABLE: " + failCounter);
        }
    }
}
