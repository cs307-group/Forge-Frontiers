package com.forgefrontier.forgefrontier.items.customskull;

import com.forgefrontier.forgefrontier.ForgeFrontier;
import com.forgefrontier.forgefrontier.utils.ItemUtil;
import com.forgefrontier.forgefrontier.utils.Manager;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

public class CustomSkullManager extends Manager {

    HashMap<String, ItemStack> skullMappings;
    HashMap<String, String> links;
    public CustomSkullManager(ForgeFrontier plugin) {
        super(plugin);
        skullMappings = new HashMap<>();
        links = new HashMap<>();
    }

    @Override
    public void init() {
        links.put("AirOrb", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZjhhNDI4NzZhYmYwZDM3OWNhYmVhNGZhMTg1ZTAwMjFkMDRkZjBmNjhlODg5ZGZkM2MwYWNjN2Y4NTAyMzAzMiJ9fX0=");
        links.put("ZephyrHelmetSkull", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMTc2YTkwMzM0MThjNzk2ZDM5NDE3OTYyOTUzODMyODU0MzUyYTcyNzlkZGVjMzFlZTA5NDMwYzI1OGEwYWFiNSJ9fX0=");
        for (String s : links.keySet()) {
            skullMappings.put(s, ItemUtil.getHead(links.get(s)));
        }
    }


    public ItemStack getSkullItem(String itemName) {
        if (skullMappings.containsKey(itemName)) {
            return skullMappings.get(itemName).clone();
        } else return null;
    }
    @Override
    public void disable() {

    }
}
