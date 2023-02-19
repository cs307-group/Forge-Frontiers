package com.forgefrontier.forgefrontier.items;

import com.forgefrontier.forgefrontier.ForgeFrontier;
import com.forgefrontier.forgefrontier.generators.PlaceGeneratorItem;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_18_R2.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomItemManager implements Listener {

    ForgeFrontier plugin;

    // Map of every registered custom item and their respective code.
    Map<String, CustomItem> items;

    // Initialize fields in here. DO NOT ACCESS OTHER MODULES HERE.
    public CustomItemManager(ForgeFrontier plugin) {
        this.plugin = plugin;
        this.items = new HashMap<>();
    }

    // Ran whenever the plugin enabled the module. You can safely access other modules here, but no guarantee data will be in them.
    public void init() {

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
        customItemInst.getBaseItem().onAttack(e, customItemInst);
    }

    // Get the custom item associated with the specified code.
    public static CustomItem getCustomItem(String code) {
        return ForgeFrontier.getInstance().getCustomItemManager().getItems().get(code);
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
        if(item2.t() == null) {
            return null;
        }
        return item2.t().l("base-code");
    }

    public Map<String, CustomItem> getItems() {
        return this.items;
    }

}
