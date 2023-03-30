package com.forgefrontier.forgefrontier;

import com.forgefrontier.forgefrontier.bazaarshop.BazaarCommand;
import com.forgefrontier.forgefrontier.bazaarshop.BazaarManager;
import com.forgefrontier.forgefrontier.commands.*;
import com.forgefrontier.forgefrontier.connections.DatabaseManager;
import com.forgefrontier.forgefrontier.generators.GeneratorCommandExecutor;
import com.forgefrontier.forgefrontier.generators.GeneratorManager;
import com.forgefrontier.forgefrontier.generators.GeneratorShopCommandExecutor;
import com.forgefrontier.forgefrontier.gui.GuiListener;
import com.forgefrontier.forgefrontier.items.CustomGiveCommand;
import com.forgefrontier.forgefrontier.items.CustomItemManager;
import com.forgefrontier.forgefrontier.items.gear.GearItemManager;
import com.forgefrontier.forgefrontier.items.gear.instanceclasses.armor.chestpiece.LeatherChestplate;
import com.forgefrontier.forgefrontier.items.gear.instanceclasses.armor.helmet.*;
import com.forgefrontier.forgefrontier.items.gear.instanceclasses.weapons.bows.*;
import com.forgefrontier.forgefrontier.items.gear.instanceclasses.weapons.swords.*;
import com.forgefrontier.forgefrontier.items.gear.upgradegems.*;
import com.forgefrontier.forgefrontier.mining.MiningCommandExecutor;
import com.forgefrontier.forgefrontier.mining.MiningManager;
import com.forgefrontier.forgefrontier.mobs.CustomEntityManager;
import com.forgefrontier.forgefrontier.mobs.EntityCommandExecutor;
import com.forgefrontier.forgefrontier.mobs.chickens.hostile.HostileChicken;
import com.forgefrontier.forgefrontier.mobs.chickens.hostile.chickenboss.ChickBoss;
import com.forgefrontier.forgefrontier.mobs.chickens.hostile.poison.PoisonChicken;
import com.forgefrontier.forgefrontier.mobs.slimes.hitbox.HitBox;
import com.forgefrontier.forgefrontier.mobs.slimes.hitbox.HitBoxEntity;
import com.forgefrontier.forgefrontier.mobs.slimes.hitbox.eggbox.EggBox;
import com.forgefrontier.forgefrontier.player.InspectCommandExecutor;
import com.forgefrontier.forgefrontier.player.PlayerManager;
import com.forgefrontier.forgefrontier.shop.Shop;

import com.forgefrontier.forgefrontier.stashes.StashManager;
import org.bukkit.command.CommandExecutor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.PluginCommand;

import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.plugin.RegisteredServiceProvider;
import revxrsal.commands.autocomplete.AutoCompleter;
import revxrsal.commands.bukkit.BukkitCommandHandler;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class ForgeFrontier extends JavaPlugin {

    private static ForgeFrontier inst;

    public static String CHAT_PREFIX;

    private Map<String, FileConfiguration> configFiles;

    private DatabaseManager databaseManager;
    private GeneratorManager generatorManager;
    private StashManager stashManager;
    private CustomItemManager customItemManager;
    private PlayerManager playerManager;
    private GearItemManager gearItemManager;
    private CustomEntityManager customEntityManager;
    private MiningManager miningManager;

    private Shop itemShop;
    private BazaarManager bazaarManager;
    private BukkitCommandHandler commandHandler;

    private Economy econ;
    private Permission perms;
    private Chat chat;

    @Override
    public void onEnable() {
        inst = this;
        CHAT_PREFIX = ChatColor.GRAY + "[" + ChatColor.RED + ChatColor.BOLD + "Forge" + ChatColor.GOLD + ChatColor.BOLD + "Frontier" + ChatColor.GRAY + "] " + ChatColor.YELLOW;
        this.configFiles = new HashMap<>();

        this.createConfig("config");
        this.createConfig("generators");
        this.createConfig("stashes");
        this.createConfig("mining");
        this.createConfig("gear-shop");
        this.createConfig("reroll");

        if (!setupEconomy() ) {
            getLogger().severe("Disabled due to no Vault dependency found!");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        setupPermissions();
        setupChat();

        // Managers
        this.databaseManager = new DatabaseManager(this);
        this.generatorManager = new GeneratorManager(this);
        this.stashManager = new StashManager(this);
        this.customItemManager = new CustomItemManager(this);
        this.playerManager = new PlayerManager(this);
        this.gearItemManager = new GearItemManager(this);
        this.customEntityManager = new CustomEntityManager(this);
        this.bazaarManager = new BazaarManager(this);
        this.miningManager = new MiningManager(this);

        this.databaseManager.init();
        this.customItemManager.init();
        this.generatorManager.init();
        this.stashManager.init();
        this.playerManager.init();
        this.gearItemManager.init();
        this.customEntityManager.init();
        this.bazaarManager.init();
        this.miningManager.init();

        // Player Shop
        this.setupPlayerShop();

        // Custom Items
        this.getCustomItemManager().registerCustomItem(new UpgradeGem());
        this.getCustomItemManager().registerCustomItem(new WoodenSword());
        this.getCustomItemManager().registerCustomItem(new WoodenBow());
        this.getCustomItemManager().registerCustomItem(new LeatherHelmet());
        this.getCustomItemManager().registerCustomItem(new LeatherChestplate());

        // Custom Mobs
        this.getCustomEntityManager().registerCustomEntity(new HostileChicken());
        this.getCustomEntityManager().registerCustomEntity(new PoisonChicken());
        this.getCustomEntityManager().registerCustomEntity(new ChickBoss());
        this.getCustomEntityManager().registerCustomEntity(new HitBox());
        this.getCustomEntityManager().registerCustomEntity(new EggBox());

        // Manager Listeners
        Bukkit.getServer().getPluginManager().registerEvents(this.generatorManager, this);
        Bukkit.getServer().getPluginManager().registerEvents(this.stashManager, this);
        Bukkit.getServer().getPluginManager().registerEvents(this.customItemManager, this);
        Bukkit.getServer().getPluginManager().registerEvents(this.playerManager, this);
        Bukkit.getServer().getPluginManager().registerEvents(this.gearItemManager, this);
        Bukkit.getServer().getPluginManager().registerEvents(this.customEntityManager, this);
        Bukkit.getServer().getPluginManager().registerEvents(this.miningManager, this);

        // General Listeners
        Bukkit.getServer().getPluginManager().registerEvents(new GuiListener(), this);
        this.registerCommands();
    }

    @Override
    public void onDisable() {
        this.databaseManager.disable();
        this.generatorManager.disable();
        this.stashManager.disable();
        this.customItemManager.disable();
        this.playerManager.disable();
        this.gearItemManager.disable();
        this.customEntityManager.disable();
        this.miningManager.disable();
    }

    private void createConfig(String name) {
        File customConfigFile = new File(getDataFolder(), name + ".yml");
        if (!customConfigFile.exists()) {
            customConfigFile.getParentFile().mkdirs();
            saveResource(name + ".yml", false);
        }

        configFiles.put(name, YamlConfiguration.loadConfiguration(customConfigFile));
    }

    private void registerCommand(String name, CommandExecutor executor) {
        PluginCommand cmd = Bukkit.getPluginCommand(name);
        if(cmd != null)
            cmd.setExecutor(executor);
    }

    /**
     * REQUIREMENT: COMMANDS MUST BE REGISTERED **AFTER** EVERYTHING IS DONE LOADING
     */
    private void registerCommands() {
        // Bukkit-Registered Commands
        registerCommand("gen"          , new GeneratorCommandExecutor());
        registerCommand("genshop"      , new GeneratorShopCommandExecutor());
        registerCommand("gearshop"     , new GearShopCommandExecutor());
        registerCommand("shop"         , itemShop.getCommandExecutor());
        registerCommand("inspect"      , new InspectCommandExecutor());
        registerCommand("island"       , new IslandCommandExecutor());
        registerCommand("link"         , new LinkCommandExecutor());
        registerCommand("reroll"       , new RerollCommandExecutor());
        registerCommand("forgefrontier", new ForgeFrontierCommandExecutor());
        registerCommand("customspawn"  , new EntityCommandExecutor());
        registerCommand("mining"       , new MiningCommandExecutor());

        // Wrapper-Registered Commands
        BukkitCommandHandler commandHandler = BukkitCommandHandler.create(this);
        commandHandler.register(new BazaarCommand(this));

        // Auto-Completer Registrations
        AutoCompleter autoCompleter = commandHandler.getAutoCompleter();
        autoCompleter.registerSuggestion("cgive", customItemManager.getItemNames());

        commandHandler.register(new CustomGiveCommand(this));

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

    private void setupPermissions() {
        RegisteredServiceProvider<Permission> rsp = getServer().getServicesManager().getRegistration(Permission.class);
        perms = rsp.getProvider();
    }

    private void setupChat() {
        RegisteredServiceProvider<Chat> rsp = getServer().getServicesManager().getRegistration(Chat.class);
        chat = rsp.getProvider();
    }

    public void setupPlayerShop() {
        this.itemShop = new Shop();
        this.getDatabaseManager().getShopDB().loadListings();
    }

    public FileConfiguration getConfig(String name) {
        return this.configFiles.get(name);
    }

    public DatabaseManager getDatabaseManager() {
        return this.databaseManager;
    }

    public CustomItemManager getCustomItemManager() {
        return this.customItemManager;
    }

    public GeneratorManager getGeneratorManager() {
        return this.generatorManager;
    }

    public StashManager getStashManager() {
        return this.stashManager;
    }

    public PlayerManager getPlayerManager() {
        return this.playerManager;
    }

    public GearItemManager getGearItemManager() {
        return this.gearItemManager;
    }

    public CustomEntityManager getCustomEntityManager() {
        return this.customEntityManager;
    }

    public BazaarManager getBazaarManager() {
        return this.bazaarManager;
    }

    public MiningManager getMiningManager() {
        return this.miningManager;
    }

    public Shop getPlayerShop() {
        return this.itemShop;
    }

    public Economy getEconomy() {
        return this.econ;
    }

    // Singleton Pattern
    public static ForgeFrontier getInstance() {
        return inst;
    }

}

