package com.forgefrontier.forgefrontier.items;

import com.forgefrontier.forgefrontier.ForgeFrontier;

import com.forgefrontier.forgefrontier.items.resources.SilverIngot;
import com.forgefrontier.forgefrontier.player.FFPlayer;
import com.forgefrontier.forgefrontier.utils.Manager;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_18_R2.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class CustomItemManager extends Manager implements Listener {

    /** Map of every registered custom item and their respective code. */
    Map<String, CustomItem> items;

    // Initialize fields in here. DO NOT ACCESS OTHER MODULES HERE.
    public CustomItemManager(ForgeFrontier plugin) {
        super(plugin);
        this.items = new HashMap<>();
    }

    @Override
    public void init() {

        this.registerCustomItem(new SilverIngot());
        
    }

    @Override
    public void disable() {

    }


    // Method to run to register a new CustomItem. If not run, the custom item will not be able to be identified.
    public void registerCustomItem(CustomItem customItem) {
        this.items.put(customItem.getCode(), customItem);
    }

    // Check when a player interacts with a custom item, if it's a custom item, run the interact event on it.
    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        ItemStack item = e.getItem();
        if(item == null)
            return;
        CustomItemInstance customItemInst = asCustomItemInstance(item);
        if(customItemInst == null)
            return;
        customItemInst.getBaseItem().onInteract(e, customItemInst);
    }

    // Prevent crafting using custom items.
    @EventHandler
    public void playerPrepareCraft(PrepareItemCraftEvent e) {
        for(ItemStack item: e.getInventory().getMatrix()) {
            if(item == null)
                continue;
            if(CustomItemManager.getCustomItem(item) != null) {
                e.getInventory().setResult(new ItemStack(Material.AIR));
                return;
            }
        }
    }

    @EventHandler
    public void playerCraft(CraftItemEvent e) {
        for(ItemStack item: e.getInventory().getMatrix()) {
            if(item == null)
                continue;
            if(CustomItemManager.getCustomItem(item) != null) {
                e.getInventory().setResult(new ItemStack(Material.AIR));
                e.setCancelled(true);
                return;
            }
        }
    }

    // Check when a player attacks with a custom item, if it's a custom item, run the attack event on it.
    @EventHandler
    public void onAttack(EntityDamageByEntityEvent e) {
        if(!(e.getDamager() instanceof Player)) {
            return;
        }
        Player p = (Player) e.getDamager();
        CustomItemInstance customItemInst = asCustomItemInstance(p.getInventory().getItem(EquipmentSlot.HAND));
        if(customItemInst == null)
            return;
        FFPlayer ffPlayer = plugin.getPlayerManager().getFFPlayerFromID(p.getUniqueId());
        customItemInst.getBaseItem().onAttack(e, customItemInst, ffPlayer);
    }

    // Get the custom item associated with the specified code.
    public static CustomItem getCustomItem(String code) {
        return ForgeFrontier.getInstance().getCustomItemManager().getItems().get(code);
    }

    // Convert an ItemStack into a CustomItemInstance of the appropriate custom item.
    public static CustomItem getCustomItem(ItemStack itemStack) {
        String code = extractCode(itemStack);
        if(code == null)
            return null;
        CustomItem customItem = ForgeFrontier.getInstance().getCustomItemManager().getItems().get(code);
        return customItem;
    }

    // Convert an ItemStack into a CustomItemInstance of the appropriate custom item.
    public static CustomItemInstance asCustomItemInstance(ItemStack itemStack) {
        String code = extractCode(itemStack);
        if(code == null)
            return null;
        CustomItem customItem = ForgeFrontier.getInstance().getCustomItemManager().getItems().get(code);
        if(customItem == null)
            return null;
        return customItem.asInstance(itemStack);
    }

    // Helper method to get the code from an ItemStack that identifies the custom item it is.
    private static String extractCode(ItemStack itemStack) {

        net.minecraft.world.item.ItemStack item2 = CraftItemStack.asNMSCopy(itemStack);
        if(item2.getTag() == null) { //TODO: (Isaac) check to make sure that the method is correct - item2.getTag()
            return null;
        }
        //TODO: (Isaac) check to make sure that the method is correct - item2.getTag()
        return item2.getTag().getString("base-code");
    }

    public Map<String, CustomItem> getItems() {
        return this.items;
    }

    public Set<String> getItemNames() {
        return items.keySet();
    }


}
