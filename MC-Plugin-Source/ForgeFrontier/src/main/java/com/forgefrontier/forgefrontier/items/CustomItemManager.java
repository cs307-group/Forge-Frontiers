package com.forgefrontier.forgefrontier.items;

import com.forgefrontier.forgefrontier.ForgeFrontier;

import com.forgefrontier.forgefrontier.items.resources.SilverIngot;
import com.forgefrontier.forgefrontier.player.FFPlayer;
import com.forgefrontier.forgefrontier.utils.Manager;
import net.minecraft.nbt.CompoundTag;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_18_R2.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;

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

        // Base Null item that will always exist for placeholder purposes.
        this.registerCustomItem(new GeneralCustomItem("Null", "BARRIER", "&c&lNull", "&7If you got this, then that means we messed up. Whoops!"));

        // Silver ingots for debug purposes.
        this.registerCustomItem(new GeneralCustomItem("SilverIngot", "IRON_INGOT", "&7Silver Ingot", "&8Ingots of silver from deep beneath the earth."));

        ForgeFrontier.getInstance().getDatabaseManager().getConfigDB().loadItems((items) -> {
            for(GeneralCustomItem generalCustomItem: items) {
                this.registerCustomItem(generalCustomItem);
            }
        });

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

    // Check when a player clicks a custom item or with a custom item. If it's a custom item, run the click event on it.
    @EventHandler
    public void onInventoryApply(InventoryClickEvent e) {
        if(e.getCurrentItem() == null || e.getCurrentItem().getType() == Material.AIR)
            return;
        ItemStack item = e.getCursor();
        if(item == null)
            return;
        CustomItemInstance customItemInst = asCustomItemInstance(item);
        if(customItemInst == null)
            return;
        customItemInst.getBaseItem().onApply(e, customItemInst, e.getClickedInventory().getItem(e.getSlot()));
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

    @EventHandler
    public void onPlaceBlock(BlockPlaceEvent e) {
        CustomItemInstance inst = asCustomItemInstance(e.getItemInHand());
        if(inst == null)
            return;
        e.setCancelled(true);
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

    private static final JSONParser parser;

    static {
        parser = new JSONParser();
    }

    public static CustomItemInstance getInstanceFromData(String dataString) {
        JSONObject data = null;
        try {
            data  = (JSONObject) parser.parse(dataString);
        } catch (ParseException e) {
            ForgeFrontier.getInstance().getLogger().log(Level.SEVERE, "Unable to parse JSON data: " + dataString);
            return null;
        }

        String code = (String) data.get("base-code");
        CustomItem customItem = CustomItemManager.getCustomItem(code);
        ItemStack itemStack = new ItemStack(Material.COBBLESTONE);
        net.minecraft.world.item.ItemStack nmsItem = CraftItemStack.asNMSCopy(itemStack);

        nmsItem.setTag(new CompoundTag());

        nmsItem.getTag().putString("base-code", code);
        nmsItem.getTag().putString("custom-data", dataString);

        itemStack = CraftItemStack.asBukkitCopy(nmsItem);
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
