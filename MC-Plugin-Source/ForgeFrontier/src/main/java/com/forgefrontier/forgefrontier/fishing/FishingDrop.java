package com.forgefrontier.forgefrontier.fishing;

import com.forgefrontier.forgefrontier.ForgeFrontier;
import com.forgefrontier.forgefrontier.items.CustomItemInstance;
import com.forgefrontier.forgefrontier.items.CustomItemManager;
import com.forgefrontier.forgefrontier.items.ItemStackBuilder;
import com.forgefrontier.forgefrontier.utils.ItemUtil;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.logging.Level;

public class FishingDrop {
    private int rarity;
    private int minAmt;
    private int maxAmt;
    private final boolean isCustom;
    private final String custom_data;
    private ItemStack item;


    public FishingDrop(String material, String lore, String displayName, int rarity, int minAmt, int maxAmt) {

        item = new ItemStackBuilder(Material.matchMaterial(material))
                        .setDisplayName(displayName)
                        .setFullLore(lore).build();
        this.rarity = rarity;
        this.minAmt = minAmt;
        this.maxAmt = maxAmt;
        custom_data = "";
        isCustom = true;
    }

    public FishingDrop(String custom_data, int rarity, int minAmt, int maxAmt) {
        isCustom = true;
        this.rarity = rarity;
        this.minAmt = minAmt;
        this.maxAmt = maxAmt;
        this.custom_data = custom_data;
        CustomItemInstance cii = CustomItemManager.getInstanceFromData(custom_data);
        if (cii == null) {
            ForgeFrontier.getInstance().getLogger()
                    .log(Level.SEVERE,"Failure loading fishing drop: " + custom_data);
            return;
        }
        this.item = cii.asItemStack();
    }

    public int getRarity() {
        return rarity;
    }

    public void setRarity(int rarity) {
        this.rarity = rarity;
    }

    public int getMinAmt() {
        return minAmt;
    }

    public void setMinAmt(int minAmt) {
        this.minAmt = minAmt;
    }

    public int getMaxAmt() {
        return maxAmt;
    }

    public void setMaxAmt(int maxAmt) {
        this.maxAmt = maxAmt;
    }

    public String getCustom_data() {
        return custom_data;
    }

    public ItemStack getItem() {
        return item;
    }

    public void setItem(ItemStack item) {
        this.item = item;
    }
}
