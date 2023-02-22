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
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.PluginCommand;


import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.plugin.RegisteredServiceProvider;

import java.util.logging.Logger;

public class ForgeFrontier extends JavaPlugin {

    private static ForgeFrontier inst;

    public static String CHAT_PREFIX;
//
    private static Economy econ = null;
    private static Permission perms = null;
    private static Chat chat = null;
    private static final Logger log = Logger.getLogger("Minecraft");
    GeneratorManager generatorManager;
    CustomItemManager customItemManager;
    PlayerManager playerManager;
    Shop itemShop;

    @Override
    public void onEnable() {
        inst = this;
        if (!this.getDataFolder().exists()) {
            this.getDataFolder().mkdirs();
        }
        CHAT_PREFIX = ChatColor.GRAY + "[" + ChatColor.RED + ChatColor.BOLD + "Forge" + ChatColor.GOLD + ChatColor.BOLD + "Frontier" + ChatColor.GRAY + "] " + ChatColor.YELLOW;

        if (!setupEconomy() ) {
            log.severe(String.format("[%s] - Disabled due to no Vault dependency found!", getDescription().getName()));
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        setupPermissions();
        setupChat();

        // Managers
        this.generatorManager = new GeneratorManager(this);
        this.customItemManager = new CustomItemManager(this);
        this.playerManager = new PlayerManager(this);

        this.generatorManager.init();
        this.customItemManager.init();
        this.playerManager.init();

        this.itemShop = new Shop(econ);

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


    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = this.getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }

    private boolean setupPermissions() {
        RegisteredServiceProvider<Permission> rsp = getServer().getServicesManager().getRegistration(Permission.class);
        perms = rsp.getProvider();
        return perms != null;
    }

    private boolean setupChat() {
        RegisteredServiceProvider<Chat> rsp = getServer().getServicesManager().getRegistration(Chat.class);
        chat = rsp.getProvider();
        return chat != null;
    }



}
