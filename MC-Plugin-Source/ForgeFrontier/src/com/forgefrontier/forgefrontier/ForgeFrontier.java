package com.forgefrontier.forgefrontier;

import com.forgefrontier.forgefrontier.commands.*;
import com.forgefrontier.forgefrontier.connections.DBConnection;
import com.forgefrontier.forgefrontier.generators.GeneratorCommandExecutor;
import com.forgefrontier.forgefrontier.generators.GeneratorManager;
import com.forgefrontier.forgefrontier.generators.GeneratorShopCommandExecutor;
import com.forgefrontier.forgefrontier.gui.GuiListener;
import com.forgefrontier.forgefrontier.items.CustomItemManager;
import com.forgefrontier.forgefrontier.items.ItemCommandExecutor;
import com.forgefrontier.forgefrontier.items.gear.GearItemManager;
import com.forgefrontier.forgefrontier.items.gear.instanceclasses.armor.chestpiece.LeatherChestplate;
import com.forgefrontier.forgefrontier.items.gear.instanceclasses.armor.helmet.*;
import com.forgefrontier.forgefrontier.items.gear.instanceclasses.weapons.bows.*;
import com.forgefrontier.forgefrontier.items.gear.instanceclasses.weapons.swords.*;
import com.forgefrontier.forgefrontier.items.gear.upgradegems.*;
import com.forgefrontier.forgefrontier.player.InspectCommandExecutor;
import com.forgefrontier.forgefrontier.player.PlayerManager;
import com.forgefrontier.forgefrontier.shop.Shop;

import org.apache.commons.lang.SystemUtils;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.PluginCommand;

import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.plugin.RegisteredServiceProvider;
import world.bentobox.bentobox.BentoBox;

import java.io.*;
import java.sql.*;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ForgeFrontier extends JavaPlugin {

    private static ForgeFrontier inst;

    public static String CHAT_PREFIX;
    private static Economy econ = null;
    private static Permission perms = null;
    private static Chat chat = null;
    private static final Logger log = Logger.getLogger("Minecraft");
    private static DBConnection postGresConnection = null;

    GeneratorManager generatorManager;
    CustomItemManager customItemManager;
    PlayerManager playerManager;
    GearItemManager gearItemManager;
    Shop itemShop;

    @Override
    public void onEnable() {
        inst = this;
        CHAT_PREFIX = ChatColor.GRAY + "[" + ChatColor.RED + ChatColor.BOLD + "Forge" + ChatColor.GOLD + ChatColor.BOLD + "Frontier" + ChatColor.GRAY + "] " + ChatColor.YELLOW;

        if (!this.getDataFolder().exists()) {
            this.getDataFolder().mkdirs();
        }
        this.saveDefaultConfig();
        //setupDatabaseConnection();
        if (!setupEconomy() ) {
            log.severe(String.format("[%s] - Disabled due to no Vault dependency found!", getDescription().getName()));
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        setupPermissions();
        setupChat();
        setupDBConnection();
        // Managers
        this.generatorManager = new GeneratorManager(this);
        this.customItemManager = new CustomItemManager(this);
        this.playerManager = new PlayerManager(this);
        this.gearItemManager = new GearItemManager(this);

        this.customItemManager.init();
        this.generatorManager.init();
        this.playerManager.init();
        this.gearItemManager.init();

        this.setupPlayerShop();
        this.getCustomItemManager().registerCustomItem(new UpgradeGem());
        this.getCustomItemManager().registerCustomItem(new WoodenSword());
        this.getCustomItemManager().registerCustomItem(new WoodenBow());
        this.getCustomItemManager().registerCustomItem(new LeatherHelmet());
        this.getCustomItemManager().registerCustomItem(new LeatherChestplate());

        // Manager Listeners
        Bukkit.getServer().getPluginManager().registerEvents(this.generatorManager, this);
        Bukkit.getServer().getPluginManager().registerEvents(this.customItemManager, this);
        Bukkit.getServer().getPluginManager().registerEvents(this.playerManager, this);
        Bukkit.getServer().getPluginManager().registerEvents(this.gearItemManager, this);

        // General Listeners
        Bukkit.getServer().getPluginManager().registerEvents(new GuiListener(), this);

        // Commands
        PluginCommand genCmd = Bukkit.getPluginCommand("gen");
        if(genCmd != null)
            genCmd.setExecutor(new GeneratorCommandExecutor());
        PluginCommand genshopCmd = Bukkit.getPluginCommand("genshop");
        if(genshopCmd != null)
            genshopCmd.setExecutor(new GeneratorShopCommandExecutor());
        PluginCommand gearshopCmd = Bukkit.getPluginCommand("gearshop");
        if(gearshopCmd != null)
            gearshopCmd.setExecutor(new GearShopCommandExecutor());
        PluginCommand shopCmd = Bukkit.getPluginCommand("shop");
        if (shopCmd != null)
            shopCmd.setExecutor(itemShop.getCommandExecutor());
        PluginCommand customItemCmd = Bukkit.getPluginCommand("customgive");
        if (customItemCmd != null)
            customItemCmd.setExecutor(new ItemCommandExecutor());
        PluginCommand inspectCmd = Bukkit.getPluginCommand("inspect");
        if (inspectCmd != null)
            inspectCmd.setExecutor(new InspectCommandExecutor(playerManager));
        PluginCommand islandCmd = Bukkit.getPluginCommand("island");
        if (islandCmd != null)
            islandCmd.setExecutor(new IslandCommandExecutor());
        PluginCommand linkCmd = Bukkit.getPluginCommand("link");
        if (linkCmd != null)
            linkCmd.setExecutor(new LinkCommandExecutor());
        PluginCommand rerollCmd = Bukkit.getPluginCommand("reroll");
        if (rerollCmd != null)
            rerollCmd.setExecutor(new RerollCommandExecutor());
        PluginCommand forgefrontierCmd = Bukkit.getPluginCommand("forgefrontier");
        if (forgefrontierCmd != null)
            forgefrontierCmd.setExecutor(new ForgeFrontierCommandExecutor());
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

    public static Economy getEconomy() {
        return econ;
    }

    private void setupDBConnection() {
        postGresConnection = new DBConnection();
        boolean result = postGresConnection.setupDatabaseConnection();
        if (result)
            postGresConnection.testConnection();
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

    public DBConnection getDBConnection() {
        return postGresConnection;
    }

    public Shop getPlayerShop() {
        return this.itemShop;
    }

    public void setupPlayerShop() {
        this.itemShop = new Shop();
        postGresConnection.loadListings();
    }
}
