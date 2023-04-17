package com.forgefrontier.forgefrontier.mobs.chickens.hostile.chickenboss;

import com.forgefrontier.forgefrontier.ForgeFrontier;
import com.forgefrontier.forgefrontier.mobs.CustomEntityManager;
import com.forgefrontier.forgefrontier.mobs.chickens.hostile.HostileChickenEntity;
import com.forgefrontier.forgefrontier.mobs.slimes.hitbox.eggbox.eggsplosive.EggsplosiveEntity;
import com.forgefrontier.forgefrontier.particles.gameparticles.MobParticles;
import com.forgefrontier.forgefrontier.particles.particlespawner.SimpleRepeatParticleSpawner;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Chicken;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_18_R2.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_18_R2.entity.CraftLivingEntity;

import java.util.ArrayList;

public class ChickBossEntity extends HostileChickenEntity {

    int totalInvulnTime = 15;
    int eggsToBeDestroyed = 4;

    boolean hasBeenInvuln;
    long invulnTime;
    int failCounter;
    long lastTickTime;
    ArrayList<EggsplosiveEntity> eggs;
    int invulParticleTask = -1;


    public ChickBossEntity(EntityType<? extends Chicken> entityTypes, Level world) {
        super(entityTypes, world);

        eggs = new ArrayList<>();

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

        if (hasBeenInvuln && failCounter < 3 && !eggs.isEmpty() && invulnTime < totalInvulnTime * 1000) {
            if (this.getHealth() < 100) {
                this.setHealth(100);
                CraftLivingEntity craftEntity = (CraftLivingEntity) Bukkit.getEntity(this.getUUID());
                craftEntity.setCustomName(ChatColor.WHITE + (String) craftEntity.getMetadata("name").get(0).value() +
                        ": " + craftEntity.getHealth() + "/" + ((int) craftEntity.getMaxHealth()));
            }
            invulnTime += System.currentTimeMillis() - lastTickTime;
            lastTickTime = System.currentTimeMillis();
        } else if (hasBeenInvuln && eggs.isEmpty()) { // increased attack
            this.setInvulnerable(false);
            // Clear invulnerbility particles
            if (invulParticleTask != -1) {
                ForgeFrontier.getInstance().getServer().getScheduler().cancelTask(invulParticleTask);
            }
            this.setAggroSpeed(1.1f);
            this.setDamage(15);
            doHostileBehavior();
        } else if (failCounter == 3) { // enraged phase (you won't survive)
            this.setAggroSpeed(1.3f);
            this.setDamage(20);
            doHostileBehavior();
        } else if (this.getHealth() > 100) { // Phase 1
            doHostileBehavior();
        } else if (!hasBeenInvuln) { // Transition into Phase 2 (Destroy the Eggsplosives)
            this.setInvulnerable(true);
            SimpleRepeatParticleSpawner srps = new SimpleRepeatParticleSpawner(
                    () -> MobParticles.CHICKBOSS_IMMUNE_PARTICLE.playParticleAtLocation(this.getLevel().getWorld(),
                                    this.getBukkitEntity().getLocation().clone().add(0,1,0)),
                    10, 5000);
            invulParticleTask = srps.run();
            hasBeenInvuln = true;
            spawnEggs();
            lastTickTime = System.currentTimeMillis();
        } else if (invulnTime >= totalInvulnTime * 1000) { // If the player fails to destroy all the eggsplosives, re-enter phase 1
            this.setInvulnerable(false);
            hasBeenInvuln = false;
            this.setHealth(150);
            CraftLivingEntity craftEntity = (CraftLivingEntity) Bukkit.getEntity(this.getUUID());
            craftEntity.setCustomName(ChatColor.WHITE + (String) craftEntity.getMetadata("name").get(0).value() +
                    ": " + craftEntity.getHealth() + "/" + ((int) craftEntity.getMaxHealth()));
            invulnTime = 0;
            lastTickTime = 0;
            failCounter++;
            eggs.clear();
//            // Clear invulnerbility particles
            if (invulParticleTask != -1) {
                ForgeFrontier.getInstance().getServer().getScheduler().cancelTask(invulParticleTask);
            }
        }
    }

    public void removeEgg(EggsplosiveEntity egg) {
        this.eggs.remove(egg);
        eggsToBeDestroyed--;
        if (egg.particleTask != -1) {
            ForgeFrontier.getInstance().getServer().getScheduler().cancelTask(egg.particleTask);
            egg.particleTask = -1;
        }
    }

    public void spawnEggs() {
        for (int i = 0; i < eggsToBeDestroyed; i++) {
            double xMod = getRandMod();
            double zMod = getRandMod();
            Vec3 pos = this.getPosition(0f);
            Location loc = new Location(this.level.getWorld(), pos.x + xMod, pos.y, pos.z + zMod);
            CraftEntity entity = ForgeFrontier.getInstance().getCustomEntityManager().spawnEntity("Eggsplosive", loc);
            if (entity.getHandle() instanceof EggsplosiveEntity egg) {
                this.eggs.add(egg);
                egg.setOwner(this);

                SimpleRepeatParticleSpawner srps = new SimpleRepeatParticleSpawner(
                        () -> MobParticles.CHICKBOSS_EGG_PARTICLE.playParticleAtLocation(entity.getWorld(), loc.clone().add(0,1,0)),
                        10, 5000);
                srps.run();
                egg.particleTask = srps.getTask();

            }


        }
    }

    private double getRandMod() {
        if (Math.random() < 0.5) {
            return -2 - (6 * Math.random());
        } else {
            return 2 + (6 * Math.random());
        }
    }
}
