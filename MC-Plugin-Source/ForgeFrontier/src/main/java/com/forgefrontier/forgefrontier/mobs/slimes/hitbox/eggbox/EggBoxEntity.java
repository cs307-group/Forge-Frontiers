package com.forgefrontier.forgefrontier.mobs.slimes.hitbox.eggbox;

import com.forgefrontier.forgefrontier.mobs.slimes.hitbox.HitBoxEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.level.Level;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public class EggBoxEntity extends HitBoxEntity {

    public boolean existed;
    public Item eggEntity;

    public EggBoxEntity(EntityType<? extends Slime> entityTypes, Level world) {
        super(entityTypes, world);
        existed = false;
    }

    @Override
    public void customTick() {
        super.customTick();
    }

    @Override
    public void whileAlive() {
        super.whileAlive();
        // runs once on spawn
        if (this.loc != null && !existed) {
            existed = true;
            ItemStack egg = new ItemStack(Material.EGG);
            Location eggLoc = new Location(loc.getWorld(), loc.getX(), loc.getY() + 0.5, loc.getZ());
            eggEntity = loc.getWorld().dropItem(eggLoc, egg);
            eggEntity.setPickupDelay(Integer.MAX_VALUE);
            eggEntity.setVelocity(new Vector(0, 0, 0));
            eggEntity.setGravity(false);
            eggEntity.setInvulnerable(true);
            eggEntity.setRotation(0, 0);
            setNamePlate(ChatColor.WHITE + "Egg" + ": " + ((int) this.getHealth()) + "/" + ((int) this.getMaxHealth()));
        }
        if (this.getCustomName() != null) {
            eggEntity.setCustomName(this.getBukkitEntity().getCustomName());
        }
    }

    @Override
    public void dropItems() {
        super.dropItems();
        eggEntity.setCustomName("");
        eggEntity.setCustomNameVisible(false);
        eggEntity.setPickupDelay(1);
        eggEntity.setGravity(true);
        eggEntity.setInvulnerable(false);
    }

    @Override
    public void setNamePlate(String name) {
        if (existed) {
            eggEntity.setCustomName(name);
            eggEntity.setCustomNameVisible(true);
        }
    }
}
