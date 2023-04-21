package com.forgefrontier.forgefrontier.mobs.slimes;

import com.forgefrontier.forgefrontier.items.CustomItemManager;
import com.forgefrontier.forgefrontier.mobs.CustomEntity;
import com.forgefrontier.forgefrontier.spawners.SpawnerInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.level.Level;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;

public abstract class CustomSlimeEntity extends Slime implements CustomEntity {

    private final int expensiveTickVal = 40;
    private int expensiveTickCurrVal = 0;

    public Location loc;
    public HashMap<String, Integer> customDropTable;
    public ArrayList<String> customDropKeys;
    public HashMap<Material, Integer> dropTable;
    public ArrayList<Material> dropKeys;
    public World world;
    public SpawnerInstance spawner;

    public CustomSlimeEntity(EntityType<? extends Slime> entityTypes, Level world) {
        super(entityTypes, world);

        this.dropTable = new HashMap<>();
        this.dropKeys = new ArrayList<>();
        this.customDropTable = new HashMap<>();
        this.customDropKeys = new ArrayList<>();
        this.loc = null;
        this.world = world.getWorld();
    }

    /**
     * Called once a tick to update the entity
     */
    @Override
    public void tick() {
        super.tick();
        setLoc(this.getBukkitEntity().getLocation());
        this.customTick();
    }

    /**
     * Function to define AI of mob in
     */
    @Override
    public void customTick() {
    }

    public boolean determineExpensive() {
        if (expensiveTickCurrVal == expensiveTickVal) {
            expensiveTickCurrVal = 0;
            return true;
        }
        expensiveTickCurrVal++;
        return false;
    }

    public void registerDropItem(String item, int chance) {
        customDropKeys.add(item);
        customDropTable.put(item, chance);
        //TODO: Get Config from code
    }

    public void registerDropItem(Material item, int chance) {
        dropKeys.add(item);
        dropTable.put(item, chance);
        //TODO: Get Config from code
    }

    public void setLoc(Location loc) {
        this.loc = loc;
    }

    public void dropItems() {
        System.out.println(loc.toString() + "dropkeys: " + dropKeys.size() + " | customDropKeys: " + customDropKeys.size());

        for (Material dropKey : dropKeys) {
            int rand = (int) (Math.random() * 100) + 1;
            System.out.println("Rand: " + rand + " | Chance: " + dropTable.get(dropKey));
            if (rand <= dropTable.get(dropKey)) {
                ItemStack item = new ItemStack(dropKey);
                System.out.println("DROP: " + item);
                world.dropItem(loc, item);
            }
        }

        for (String customDropKey : customDropKeys) {
            int rand = (int) (Math.random() * 100) + 1;
            System.out.println("Rand: " + rand + " | Chance: " + customDropTable.get(customDropKey));
            if (rand <= customDropTable.get(customDropKey)) {
                ItemStack item = new ItemStack(CustomItemManager.getCustomItem(customDropKey).asInstance(null).asItemStack());
                System.out.println("DROP: " + customDropKey);
                world.dropItem(loc, item);
            }
        }
    }

    @Override
    public void registerSpawner(SpawnerInstance spawner) {
        this.spawner = spawner;
    }

    @Override
    public void updateSpawnerOnDeath() {
        if (this.spawner != null) {
            this.spawner.entityDeath();
        }
    }
}
