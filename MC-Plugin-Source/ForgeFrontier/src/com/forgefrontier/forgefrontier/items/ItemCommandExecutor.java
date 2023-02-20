package com.forgefrontier.forgefrontier.items;

import com.forgefrontier.forgefrontier.ForgeFrontier;
import com.forgefrontier.forgefrontier.items.gear.upgradegems.*;
import com.forgefrontier.forgefrontier.items.gear.instanceclasses.weapons.swords.*;
import com.forgefrontier.forgefrontier.items.gear.instanceclasses.weapons.bows.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * ItemCommandExecutor
 *
 * Executes the commands from in-game which spawn custom items into a player's inventory
 */
public class ItemCommandExecutor implements CommandExecutor {

    /** HashMap containing all the different custom item classes */
    private final Map<String, Class<? extends UniqueCustomItem>> itemTypes;

    /**
     * Constructor which initializes the values of the hashmap
     */
    public ItemCommandExecutor() {
        this.itemTypes = new HashMap<>();
        //Add all the different item types to the hashmap
        itemTypes.put("WoodenSword", WoodenSword.class);
        itemTypes.put("WoodenBow", WoodenBow.class);
        itemTypes.put("UpgradeGem", UpgradeGem.class);
    }

    /**
     * Iterate through a package and add all classes inside that package to the itemTypes hashmap
     *
     * @param packageName the reference to the package
     */
    public void packageLoader(String packageName) {
        //TODO: debug and get working
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        String packagePath = packageName.replace('.', '/');
        try {
            Enumeration<URL> resources = classLoader.getResources(packagePath);
            while (resources.hasMoreElements()) {
                URL resource = resources.nextElement();
                if (resource.getProtocol().equals("file")) {
                    File directory = new File(resource.toURI());
                    for (File file : directory.listFiles()) {
                        if (file.getName().endsWith(".class") && !file.getName().endsWith("Instance.class")) {
                            String itemName = packageName + '.' + file.getName().replace(".class", "");
                            Class<? extends UniqueCustomItem> itemType = (Class<? extends UniqueCustomItem>) Class.forName(itemName);
                            itemTypes.put(itemName, itemType);
                        }
                    }
                }
            }
        } catch (IOException | ClassNotFoundException | URISyntaxException e) {
            e.printStackTrace();
        }
    }

    /**
     * Handles the command sent via in-game or the terminal
     *
     * @param sender represents the sender of the command
     * @param command represents the command sent
     * @param label the command label that the sender typed
     * @param args the different arguments included after the command label
     * @return a boolean which indicates whether the command was successful or not?
     */
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Class<? extends UniqueCustomItem> itemType;

        if (args.length == 0) {
            sender.sendMessage("Invalid usage: /customgive <item type> or /customgive <player name> <item type>" +
                    " or /customgive <player name> <item type> <number of items>");
            return true;
        }

        System.out.println(sender instanceof Player);
        if (sender instanceof Player) {
            if (args.length == 1) {
                Player player = (Player) sender;
                itemType = getItemType(args[0]);

                if (itemType == null) {
                    sender.sendMessage("Invalid item type: " + args[0]);
                    return true;
                }

                return addItemToInv(args[0], player, sender);
            } else if (args.length == 2) {

            } else if (args.length == 3) {

            }
        }
        return true;
    }

    /**
     * Attempts to add the itemType specified to the player's inventory
     *
     * @param itemType the class of the item to be added
     * @param player the player whose inventory the item is to be added
     * @param sender the sender of the command, used to send back error/success messages
     */
    private boolean addItemToInv(String itemType, Player player, CommandSender sender) {
        ItemStack item = CustomItemManager.getCustomItem(itemType).asInstance(null).asItemStack();
        player.getInventory().addItem(item);
        sender.sendMessage(ForgeFrontier.CHAT_PREFIX + itemType + " item added to " +
                player.getDisplayName() + "'s inventory");
        return true;
    }

    /**
     * Gets the item type specified in the command
     *
     * @param typeArg the string representation of the item specified by the command
     * @return the class type of the item specified in the command
     */
    private Class<? extends UniqueCustomItem> getItemType(String typeArg) {
        return itemTypes.get(typeArg);
    }
}
