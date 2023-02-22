package com.forgefrontier.forgefrontier.items;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Zombie;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

// Example class to showcase a custom sword that does extra damage to Zombies.

// Extends UniqueCustomItem to include a unique code in it.
public final class ExampleZombieSword extends UniqueCustomItem {

    // The CustomItemInstance specifically for the ZombieSword, containing the specific data for the Zombie Sword.
    public static class ExampleZombieSwordInstance extends UniqueCustomItemInstance {
        double extraDamagePercent;

        public ExampleZombieSwordInstance(ItemStack itemStack) {
            super(itemStack);
        }
    }

    public ExampleZombieSword() {
        super("ZombieSword");

        // Register accumulator to set the base attributes of the CustomItemInstance
        // itemStack could be null, or could be an existing zombie sword you wish to get the instance of.
        this.registerInstanceAccumulator((__, itemStack) -> {

            // Because at the start the instance will be null, it must be instantiated here.
            ExampleZombieSwordInstance zombieSwordInstance = new ExampleZombieSwordInstance(itemStack);
            // Set the attributes relevant to it being specifically the zombie sword.
            if (itemStack == null) {
                // Set default value for a brand new Zombie Sword.
                zombieSwordInstance.data.put("extra-dmg", Math.random() * 2);
            }
            zombieSwordInstance.extraDamagePercent = (double) zombieSwordInstance.data.get("extra-dmg");

            // Return it to rise up to the accumulator for UniqueCustomItem (giving it a unique id)
            return zombieSwordInstance;
        });

        // Register an accumulator for creating the Zombie Sword item from the Zombie Sword Custom Item Instance.
        this.registerItemStackAccumulator((customItemInstance, itemStack) -> {
            // "customItemInstance" is only a CustomItemInstance, it must be casted before specific zombie sword attributes can be accessed.
            // Because this accumulator is being ran, it is guaranteed it is a safe cast, because all ZombieSword Custom Items will be of this instance (or a subclass).
            ExampleZombieSwordInstance zombieSwordInstance = (ExampleZombieSwordInstance) customItemInstance;
            // Create the actual ItemStack.
            ItemStack item = new ItemStackBuilder(Material.IRON_SWORD)
                .setDisplayName(ChatColor.WHITE + "Zombie Sword")
                .addLoreLine(ChatColor.GRAY + "Extra damage against Zombies:")
                .addLoreLine(ChatColor.GREEN + " + " + (((int) (zombieSwordInstance.extraDamagePercent * 1000)) / 10.0) + "%")
                .build();
            // Return the item to give it to the UniqueCustomItem's accumulator, which will add the unique id to the itemstack.
            return item;
        });
    }

    // Zombie Sword doesn't have any abilities, so doesn't matter. Can stay blank.
    @Override
    public void onInteract(PlayerInteractEvent e, CustomItemInstance itemInstance) {

    }

    // Upon attack with Zombie Sword, check if its hitting a zombie. If so, multiply by the multiplier of the sword.
    @Override
    public void onAttack(EntityDamageByEntityEvent e, CustomItemInstance customItemInstance) {
        ExampleZombieSwordInstance zombieSwordInstance = (ExampleZombieSwordInstance) customItemInstance;
        if(!(e.getEntity() instanceof Zombie))
            return;
        e.setDamage(e.getDamage() * (1.0 + zombieSwordInstance.extraDamagePercent));
    }


}
