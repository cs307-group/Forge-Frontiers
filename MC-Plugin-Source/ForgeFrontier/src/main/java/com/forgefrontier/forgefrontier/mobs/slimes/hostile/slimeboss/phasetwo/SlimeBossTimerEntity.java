package com.forgefrontier.forgefrontier.mobs.slimes.hostile.slimeboss.phasetwo;

import com.forgefrontier.forgefrontier.ForgeFrontier;
import com.forgefrontier.forgefrontier.mobs.slimes.CustomSlimeEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.level.Level;
import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

public class SlimeBossTimerEntity extends CustomSlimeEntity {

    String name;
    int timeToExplode = 15;
    long lastTimeStamp;
    long totalTimeAlive;

    boolean existed;

    public SlimeBossTimerEntity(EntityType<? extends Slime> entityTypes, Level world) {
        super(entityTypes, world);
        name = null;
        lastTimeStamp = 0;
        totalTimeAlive = 0;
        this.setNoAi(true);
        existed = true;
    }

    @Override
    public void customTick() {
        super.customTick();
        if (this.getBukkitEntity().getCustomName() != null) {
            if (this.getBukkitEntity().getCustomName().contains("|")) {
                name = this.getBukkitEntity().getCustomName().substring(0, this.getBukkitEntity().getCustomName().indexOf('|') -1);
            } else {
                name = this.getBukkitEntity().getCustomName();
            }
        }

        // initializes lastTimeStamp
        if (lastTimeStamp == 0) {
            lastTimeStamp = System.currentTimeMillis();
        }

        if (totalTimeAlive >= (timeToExplode * 1000)) { // respawn king slime on failure
            respawnKing();
            this.getBukkitEntity().setCustomName("");
            this.getBukkitEntity().setCustomNameVisible(false);
            this.setNoAi(false);
            EntityDeathEvent event = new EntityDeathEvent((LivingEntity) this.getBukkitEntity(), new ArrayList<ItemStack>(), 0);
            Bukkit.getServer().getPluginManager().callEvent(event);
            this.getBukkitEntity().remove();
        } else {
            totalTimeAlive += System.currentTimeMillis() - lastTimeStamp;
            lastTimeStamp = System.currentTimeMillis();
            setNamePlate();
        }
    }

    public void setNamePlate() {
        this.getBukkitEntity().setCustomName(name + " | " + (timeToExplode - (totalTimeAlive / 1000)));
        setCustomNameVisible(true);
    }

    public void respawnKing() {
        if (existed) {
            ForgeFrontier.getInstance().getCustomEntityManager().spawnEntity("SlimeBoss", loc);
            existed = false;
        }
    }

    public void onDeath() {
        existed = false;
    }
}
