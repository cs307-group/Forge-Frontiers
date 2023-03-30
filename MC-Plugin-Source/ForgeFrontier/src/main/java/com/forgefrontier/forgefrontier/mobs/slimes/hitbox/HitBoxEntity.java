package com.forgefrontier.forgefrontier.mobs.slimes.hitbox;

import com.forgefrontier.forgefrontier.items.CustomItemManager;
import com.forgefrontier.forgefrontier.mobs.slimes.CustomSlimeEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.level.Level;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;

public class HitBoxEntity extends CustomSlimeEntity {

    public Location loc;
    public HashMap<String, Integer> customDropTable;
    public ArrayList<String> customDropKeys;
    public HashMap<Material, Integer> dropTable;
    public ArrayList<Material> dropKeys;
    public World world;

    public HitBoxEntity(EntityType<? extends Slime> entityTypes, Level world) {
        super(entityTypes, world);

        this.dropTable = new HashMap<>();
        this.dropKeys = new ArrayList<>();
        this.customDropTable = new HashMap<>();
        this.customDropKeys = new ArrayList<>();
        this.loc = null;
        this.world = null;
        this.setNoGravity(true);
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

    public void setLoc(Location loc) {
        this.loc = loc;
    }

    public void dropItems() {
        for (Material dropKey : dropKeys) {
            int rand = (int) (Math.random() * 100) + 1;
            if (rand <= dropTable.get(dropKey)) {
                ItemStack item = new ItemStack(dropKey);
                world.dropItem(loc, item);
            }
        }

        for (String customDropKey : customDropKeys) {
            int rand = (int) (Math.random() * 100) + 1;
            if (rand <= customDropTable.get(customDropKey)) {
                ItemStack item = new ItemStack(CustomItemManager.getCustomItem(customDropKey).asInstance(null).asItemStack());
                world.dropItem(loc, item);
            }
        }
    }

    public void registerDropItem(String item, int chance) {
        customDropKeys.add(item);
        customDropTable.put(item, chance);
    }

    public void registerDropItem(Material item, int chance) {
        dropKeys.add(item);
        dropTable.put(item, chance);
    }

    public void setNamePlate(String name) {
    }
}
