package com.forgefrontier.forgefrontier.utils;

import com.forgefrontier.forgefrontier.ForgeFrontier;
import com.forgefrontier.forgefrontier.items.CustomItem;
import com.forgefrontier.forgefrontier.items.CustomItemInstance;
import com.forgefrontier.forgefrontier.items.CustomItemManager;
import com.forgefrontier.forgefrontier.items.ItemStackBuilder;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
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

    public static String colorToStr(Color c) {
        if (c == null) return "NA";

        return "" + c.getRed() + "," + c.getGreen() + "," + c.getBlue();
    }

    public static Color strToColor(String s) {
        if (s == null) return null;
        Color c = Color.BLACK;
        String[] parts = s.split(",");
        if (parts.length != 3) return null;
        c = c.setRed(Integer.parseInt(parts[0]));
        c = c.setBlue(Integer.parseInt(parts[0]));
        c = c.setGreen(Integer.parseInt(parts[0]));
        return c;
    }


    /** https://www.spigotmc.org/threads/wishing-to-share-my-method-for-custom-textured-player-head-itemstacks.445763/#post-3861173 */
    private static byte[] longToBytes(long x) {
        ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);
        buffer.putLong(x);
        return buffer.array();
    }
    @SuppressWarnings("deprecation")
    public static ItemStack getHead(String texture) {
        UUID texUUID = UUID.nameUUIDFromBytes(texture.getBytes());
        byte[] bytesA = longToBytes(texUUID.getMostSignificantBits());
        byte[] bytesB = longToBytes(texUUID.getLeastSignificantBits());
        int[] intList = new int[] {
                new BigInteger(Arrays.copyOfRange(bytesA, 0, (bytesA.length/2))).intValue(),
                new BigInteger(Arrays.copyOfRange(bytesA, (bytesA.length/2), bytesA.length)).intValue(),
                new BigInteger(Arrays.copyOfRange(bytesB, 0, (bytesB.length/2))).intValue(),
                new BigInteger(Arrays.copyOfRange(bytesB, (bytesB.length/2), bytesB.length)).intValue()
        };
        return Bukkit.getUnsafe().modifyItemStack(
                new ItemStack(Material.PLAYER_HEAD),
                "{SkullOwner:{" + MessageFormat.format("Id:[I;{0},{1},{2},{3}]", Long.toString(intList[0]), Long.toString(intList[1]), Long.toString(intList[2]), Long.toString(intList[3])) + ",Properties:{textures:[{Value:\"" + texture + "\"}]}}}"
        );
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

    public static int getEnchantmentLevelInHand(Player p, Enchantment e) {
        ItemStack item = p.getInventory().getItemInMainHand();
        return item.getEnchantmentLevel(e);
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
                    return sbName.substring(0, sbName.length() - 1);
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
        ItemStack copyItem = new ItemStackBuilder(item).build();
        while(amtLeft > 0 && i < 36) {
            if(contents[i] == null) {i++; continue;}

            if(customCompare(contents[i], copyItem)) {
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
