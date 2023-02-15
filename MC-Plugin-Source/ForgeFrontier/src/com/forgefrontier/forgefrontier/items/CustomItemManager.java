package com.forgefrontier.forgefrontier.items;

import com.forgefrontier.forgefrontier.ForgeFrontier;
import com.forgefrontier.forgefrontier.generators.PlaceGeneratorItem;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_18_R2.inventory.CraftItemStack;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomItemManager implements Listener {

    ForgeFrontier plugin;

    Map<String, CustomItem> items;

    public CustomItemManager(ForgeFrontier plugin) {
        this.plugin = plugin;
        this.items = new HashMap<>();
    }

    public void init() {

    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        ItemStack item = e.getItem();
        if(item == null)
            return;
        CustomItem customItem = asCustomItem(item);
        if(customItem == null)
            return;
        customItem.onInteract(e);
    }

    public static CustomItem getCustomItem(String code) {
        //code = code.replaceAll("[0-9a-fl-oA-FL-O]", "_");
        return ForgeFrontier.getInstance().getCustomItemManager().getItems().get(code);
    }

    public static CustomItem asCustomItem(ItemStack itemStack) {
        String code = extractCode(itemStack);
        if(code == null)
            return null;
        System.out.println(code);
        return ForgeFrontier.getInstance().getCustomItemManager().getItems().get(code);
    }

    public static ItemStack asItemStack(CustomItem customItem, int amt) {
        ItemStack item = customItem.asItemStack();
        String code = customItem.getCode();
        /*
        code = code.replaceAll("[0-9a-fl-oA-FL-O]", "_");
        String encodedCode = (code + "|").replaceAll("(.)", ChatColor.COLOR_CHAR + "$0");
        System.out.println(encodedCode);
        ItemMeta meta = item.getItemMeta();
        List<String> lore = meta.getLore();
        if(lore == null) lore = new ArrayList<>();
        lore.add(encodedCode);
        meta.setLore(lore);
        item.setItemMeta(meta);
         */

        net.minecraft.world.item.ItemStack item2 = CraftItemStack.asNMSCopy(item);

        System.out.println("Code: "+ code);
        item2.t().a("customitem-code", code);

        item = CraftItemStack.asBukkitCopy(item2);

        item.setAmount(amt);

        return item;
    }

    private static String extractCode(ItemStack itemStack) {

        net.minecraft.world.item.ItemStack item2 = CraftItemStack.asNMSCopy(itemStack);
        return item2.t().l("customitem-code");
        /*
        StringBuilder code = new StringBuilder();
        boolean sectionSeen = false;
        for(char c: itemStack.getItemMeta().getLore().get(itemStack.getItemMeta().getLore().size() - 1).toCharArray()) {
            if(c == ChatColor.COLOR_CHAR) {
                sectionSeen = true;
                continue;
            }
            if(!sectionSeen)
                return null;
            if(c == '|') {
                break;
            }
            code.append(c);
            sectionSeen = false;
        }
        return code.toString();
         */
    }

    public Map<String, CustomItem> getItems() {
        return this.items;
    }

    public void registerCustomItem(CustomItem customItem) {
        //customItem.code = customItem.getCode().replaceAll("[0-9a-fl-oA-FL-O]", "_");
        this.items.put(customItem.getCode(), customItem);
    }
}
