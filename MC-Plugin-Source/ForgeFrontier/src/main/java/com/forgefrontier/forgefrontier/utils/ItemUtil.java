package com.forgefrontier.forgefrontier.utils;

import com.forgefrontier.forgefrontier.ForgeFrontier;
import com.forgefrontier.forgefrontier.items.CustomItem;
import com.forgefrontier.forgefrontier.items.CustomItemInstance;
import com.forgefrontier.forgefrontier.items.CustomItemManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.lang.reflect.Method;
import java.util.List;
import java.util.logging.Level;


public class ItemUtil {

    public static String getCustomData(ItemStack itm) {
        String customData = "";
        CustomItemInstance ci = CustomItemManager.asCustomItemInstance(itm);
        if (ci != null) {
            customData = ci.getData().toString();
        }
        return customData;
    }

    public static String getStringLore(ItemStack itm) {
        ItemMeta meta = itm.getItemMeta();
        if (meta == null) return "";
        List<String> lore = meta.getLore();
        if (lore == null) return "";
        return String.join("\n", lore);
    }
    public static String itemName(ItemStack itm) {
        ItemMeta meta = itm.getItemMeta();
        if (meta == null) return simpleRename(itm.getType());
        if (meta.hasDisplayName()) return meta.getDisplayName();
        else return simpleRename(itm.getType());
    }

    /**
     * Get Material Renamed
     * @param material Material
     * @return Friendly Name
     */
    public static String simpleRename(Material material) {
        String[] strs = material.toString().toLowerCase().split("_");
        StringBuilder out = new StringBuilder();
        for (String s : strs) {
            out.append(Character.toUpperCase(s.charAt(0))).append(s.substring(1)).append(" ");
        }
        out.deleteCharAt(out.length()-1);
        return out.toString();
    }


    /**
     * THIS IS CODE FROM A ONLINE FORUM FOR UTILITY PURPOSES
     * SOURCE: https://bukkit.org/threads/reflection-friendly-item-names.339467/
     * @author Forge_User_62502025
     */
    public static String capitalizeFully(String name) {
        if (name != null) {
            if (name.length() > 1) {
                if (name.contains("_")) {
                    StringBuilder sbName = new StringBuilder();
                    for (String subName : name.split("_"))
                        sbName.append(subName.substring(0, 1).toUpperCase() + subName.substring(1).toLowerCase()).append(" ");
                    return sbName.toString().substring(0, sbName.length() - 1);
                } else {
                    return name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase();
                }
            } else {
                return name.toUpperCase();
            }
        } else {
            return "";
        }
    }

    /**
     *
     * @param p
     * @param item
     * @param amt
     * @return
     */
    public static boolean hasItem(Player p, ItemStack item, int amt) {
        int totalAmt = 0;
        int i = 0;
        ItemStack[] contents = p.getInventory().getContents();
        while(totalAmt < amt && i < 36) {
            if(contents[i] == null) {i++; continue;}
            if(customCompare(contents[i], item)) {
                totalAmt += contents[i].getAmount();
            }
            i += 1;
        }

        return totalAmt >= amt;
    }
    public static void take(Player p, ItemStack item, int amt) {
        int amtLeft = amt;
        int i = 0;
        ItemStack[] contents = p.getInventory().getContents();
        while(amtLeft > 0 && i < 36) {
            if(contents[i] == null) {i++; continue;}
            if(customCompare(contents[i], item)) {
                int amtToTake = Math.min(contents[i].getAmount(), amtLeft);
                contents[i].setAmount(contents[i].getAmount() - amtToTake);
                amtLeft -= amtToTake;
            }
            i += 1;
        }
    }


    public static boolean customCompare(ItemStack a, ItemStack b) {
        // TODO: FUNCTIONALITY TO COMPARE CUSTOM ITEM MATERIALS
        if (a.getType() != b.getType()) return false;
        ItemMeta aim = a.getItemMeta();
        ItemMeta bim = b.getItemMeta();
        if ((aim != null && bim == null) || (aim == null && bim != null)) return false;
        if (aim == null) return true;

        String dnA = aim.getDisplayName();
        String dnB = bim.getDisplayName();

        if (!dnA.equals(dnB)) return false;
        List<String> aLore = aim.getLore();
        List<String> bLore = bim.getLore();
        if ((aLore != null && bLore == null) || (aLore == null && bLore != null)) return false;

        if (aLore == null) return true;
        return String.join("\n", aLore).equals(String.join("\n", bLore));
    }


}
