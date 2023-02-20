package com.forgefrontier.forgefrontier;

import com.forgefrontier.forgefrontier.generators.GeneratorCommandExecutor;
import com.forgefrontier.forgefrontier.generators.GeneratorManager;
import com.forgefrontier.forgefrontier.gui.GuiListener;
import com.forgefrontier.forgefrontier.items.CustomItemManager;
import com.forgefrontier.forgefrontier.items.ExampleZombieSword;
import com.forgefrontier.forgefrontier.items.ItemCommandExecutor;
import com.forgefrontier.forgefrontier.items.gear.instanceclasses.weapons.bows.WoodenBow;
import com.forgefrontier.forgefrontier.items.gear.instanceclasses.weapons.swords.WoodenSword;
import com.forgefrontier.forgefrontier.items.gear.upgradegems.UpgradeGem;
import com.forgefrontier.forgefrontier.player.PlayerManager;
import com.forgefrontier.forgefrontier.shop.Shop;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;

public class ForgeFrontier extends JavaPlugin {

    private static ForgeFrontier inst;

    public static String CHAT_PREFIX;

    GeneratorManager generatorManager;
    CustomItemManager customItemManager;
    PlayerManager playerManager;
    Shop itemShop;

    @Override
    public void onEnable() {
        inst = this;
        CHAT_PREFIX = ChatColor.GRAY + "[" + ChatColor.RED + ChatColor.BOLD + "Forge" + ChatColor.GOLD + ChatColor.BOLD + "Frontier" + ChatColor.GRAY + "] " + ChatColor.YELLOW;

        // Managers
        this.generatorManager = new GeneratorManager(this);
        this.customItemManager = new CustomItemManager(this);
        this.playerManager = new PlayerManager(this);

        this.generatorManager.init();
        this.customItemManager.init();
        this.playerManager.init();

        this.itemShop = new Shop();

        // TODO: Remove testing code for the Zombie Sword example custom item.
        this.getCustomItemManager().registerCustomItem(new ExampleZombieSword());
        this.getCustomItemManager().registerCustomItem(new UpgradeGem());
        this.getCustomItemManager().registerCustomItem(new WoodenSword());
        this.getCustomItemManager().registerCustomItem(new WoodenBow());

        // Manager Listeners
        Bukkit.getServer().getPluginManager().registerEvents(this.generatorManager, this);
        Bukkit.getServer().getPluginManager().registerEvents(this.customItemManager, this);

        // General Listeners
        Bukkit.getServer().getPluginManager().registerEvents(new GuiListener(), this);

        // Commands
        PluginCommand genCmd = Bukkit.getPluginCommand("gen");
        if(genCmd != null)
            genCmd.setExecutor(new GeneratorCommandExecutor());
        PluginCommand shopCmd = Bukkit.getPluginCommand("shop");
        if (shopCmd != null)
            shopCmd.setExecutor(itemShop.getCommandExecutor());
        PluginCommand customItemCmd = Bukkit.getPluginCommand("customgive");
        if (customItemCmd != null)
            customItemCmd.setExecutor(new ItemCommandExecutor());
    }

    @Override
    public void onDisable() {
        this.generatorManager.disable();
        this.customItemManager.disable();
        this.playerManager.disable();
    }

    public CustomItemManager getCustomItemManager() {
        return this.customItemManager;
    }

    public GeneratorManager getGeneratorManager() {
        return this.generatorManager;
    }

    public PlayerManager getPlayerManager() {
        return this.playerManager;
    }

    // Singleton Pattern
    public static ForgeFrontier getInstance() {
        return inst;
    }
}
