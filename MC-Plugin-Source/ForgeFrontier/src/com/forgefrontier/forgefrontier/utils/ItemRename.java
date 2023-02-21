package com.forgefrontier.forgefrontier.utils;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Method;




/**
 * THIS IS CODE FROM A ONLINE FORUM FOR UTILITY PURPOSES
 * SOURCE: https://bukkit.org/threads/reflection-friendly-item-names.339467/
 * @author Forge_User_62502025
 */
public class ItemRename {
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

    public static String simpleRename(Material material) {
        String[] strs = material.toString().toLowerCase().split("_");
        StringBuilder out = new StringBuilder();
        for (String s : strs) {
            out.append(Character.toUpperCase(s.charAt(0))).append(s.substring(1)).append(" ");
        }
        out.deleteCharAt(out.length()-1);
        return out.toString();
    }
    public static String getFriendlyName(Material material) {
        return material == null ? "Air" : getFriendlyName(new ItemStack(material), false);
    }

    private static Class localeClass = null;
    private static Class craftItemStackClass = null, nmsItemStackClass = null, nmsItemClass = null;
    private static String OBC_PREFIX = Bukkit.getServer().getClass().getPackage().getName();
    private static String NMS_PREFIX = OBC_PREFIX.replace("org.bukkit.craftbukkit", "net.minecraft.server");

    public static String getFriendlyName(ItemStack itemStack, boolean checkDisplayName) {
        if (itemStack == null || itemStack.getType() == Material.AIR) return "Air";
        try {
            if (craftItemStackClass == null)
                craftItemStackClass = Class.forName(OBC_PREFIX + ".inventory.CraftItemStack");
            Method nmsCopyMethod = craftItemStackClass.getMethod("asNMSCopy", ItemStack.class);

            if (nmsItemStackClass == null) nmsItemStackClass = Class.forName(NMS_PREFIX + ".ItemStack");
            Object nmsItemStack = nmsCopyMethod.invoke(null, itemStack);

            Object itemName = null;
            if (checkDisplayName) {
                Method getNameMethod = nmsItemStackClass.getMethod("getName");
                itemName = getNameMethod.invoke(nmsItemStack);
            } else {
                Method getItemMethod = nmsItemStackClass.getMethod("getItem");
                Object nmsItem = getItemMethod.invoke(nmsItemStack);

                if (nmsItemClass == null) nmsItemClass = Class.forName(NMS_PREFIX + ".Item");

                Method getNameMethod = nmsItemClass.getMethod("getName");
                Object localItemName = getNameMethod.invoke(nmsItem);

                if (localeClass == null) localeClass = Class.forName(NMS_PREFIX + ".LocaleI18n");
                Method getLocaleMethod = localeClass.getMethod("get", String.class);

                Object localeString = localItemName == null ? "" : getLocaleMethod.invoke(null, localItemName);
                itemName = ("" + getLocaleMethod.invoke(null, localeString.toString() + ".name")).trim();
            }
            return itemName != null ? itemName.toString() : capitalizeFully(itemStack.getType().name().replace("_", " ").toLowerCase());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return capitalizeFully(itemStack.getType().name().replace("_", " ").toLowerCase());
    }
}
