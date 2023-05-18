package com.forgefrontier.forgefrontier;

import com.forgefrontier.forgefrontier.bazaarshop.BazaarCommand;
import com.forgefrontier.forgefrontier.bazaarshop.BazaarManager;
import com.forgefrontier.forgefrontier.commands.*;
import com.forgefrontier.forgefrontier.connections.DatabaseManager;
import com.forgefrontier.forgefrontier.crafting.CraftingManager;
import com.forgefrontier.forgefrontier.fishing.FishingManager;
//import com.forgefrontier.forgefrontier.generators.GeneratorCommandExecutor;
import com.forgefrontier.forgefrontier.generators.GeneratorManager;
import com.forgefrontier.forgefrontier.generators.GeneratorShopCommandExecutor;
import com.forgefrontier.forgefrontier.gui.GuiListener;
import com.forgefrontier.forgefrontier.commands.CustomGiveCommand;
import com.forgefrontier.forgefrontier.items.CustomItemManager;
import com.forgefrontier.forgefrontier.items.customskull.CustomSkullManager;
import com.forgefrontier.forgefrontier.items.gear.GearItemManager;
import com.forgefrontier.forgefrontier.items.gear.instanceclasses.armor.chestpiece.LeatherChestplate;
import com.forgefrontier.forgefrontier.items.gear.instanceclasses.armor.helmet.*;
import com.forgefrontier.forgefrontier.items.gear.instanceclasses.weapons.bows.*;
import com.forgefrontier.forgefrontier.items.gear.instanceclasses.weapons.swords.*;
import com.forgefrontier.forgefrontier.items.gear.upgradegems.*;
import com.forgefrontier.forgefrontier.listeners.BentoBoxListener;
import com.forgefrontier.forgefrontier.listeners.ChatManager;
import com.forgefrontier.forgefrontier.mining.MiningCommandExecutor;
import com.forgefrontier.forgefrontier.mining.MiningManager;
import com.forgefrontier.forgefrontier.mobs.CustomEntityManager;
import com.forgefrontier.forgefrontier.mobs.EntityCommandExecutor;
import com.forgefrontier.forgefrontier.mobs.chickens.dynamic.DynamicChicken;
import com.forgefrontier.forgefrontier.mobs.chickens.hostile.HostileChicken;
import com.forgefrontier.forgefrontier.mobs.chickens.hostile.chickenboss.ChickBoss;
import com.forgefrontier.forgefrontier.mobs.chickens.hostile.poison.PoisonChicken;
import com.forgefrontier.forgefrontier.mobs.slimes.hitbox.HitBox;
import com.forgefrontier.forgefrontier.mobs.slimes.hitbox.eggbox.EggBox;
import com.forgefrontier.forgefrontier.mobs.slimes.hitbox.eggbox.eggsplosive.Eggsplosive;
import com.forgefrontier.forgefrontier.mobs.slimes.hostile.HostileSlime;
import com.forgefrontier.forgefrontier.mobs.slimes.hostile.slimeboss.SlimeBoss;
import com.forgefrontier.forgefrontier.mobs.slimes.hostile.slimeboss.phasetwo.SlimeBossSmall;
import com.forgefrontier.forgefrontier.mobs.slimes.hostile.slimeboss.phasetwo.SlimeBossSwift;
import com.forgefrontier.forgefrontier.mobs.slimes.hostile.slimeboss.phasetwo.SlimeBossTimer;
import com.forgefrontier.forgefrontier.particles.ParticleCommands;
import com.forgefrontier.forgefrontier.particles.ParticleManager;
import com.forgefrontier.forgefrontier.player.InspectCommandExecutor;
import com.forgefrontier.forgefrontier.player.PlayerManager;
import com.forgefrontier.forgefrontier.shop.Shop;

import com.forgefrontier.forgefrontier.spawners.Spawner;
import com.forgefrontier.forgefrontier.spawners.SpawnerManager;
import com.forgefrontier.forgefrontier.stashes.StashManager;
import com.forgefrontier.forgefrontier.tutorial.TutorialManager;
import org.bukkit.Particle;
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
import java.util.Set;

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
    private FishingManager fishingManager;
    private ParticleManager particleManager;
    private CraftingManager craftingManager;
    private CustomSkullManager customSkullManager;
    private TutorialManager tutorialManager;
    private ChatManager chatManager;
    private GameManager gameManager;
    private SpawnerManager spawnerManager;

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
        this.createConfig("tutorial");
        this.createConfig("generators");
        this.createConfig("stashes");
        this.createConfig("mining");
        this.createConfig("gear-shop");
        this.createConfig("reroll");
        this.createConfig("fishing");
        this.createConfig("entity-drops");
        this.createConfig("spawners");

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
        this.fishingManager = new FishingManager(this);
        this.particleManager = new ParticleManager(this);
        this.craftingManager = new CraftingManager(this);
        this.customSkullManager = new CustomSkullManager(this);
        this.tutorialManager = new TutorialManager(this);
        this.chatManager = new ChatManager(this);
        this.gameManager = new GameManager(this);

        this.spawnerManager = new SpawnerManager(this);

        this.databaseManager.init();
        this.customSkullManager.init();
        this.customItemManager.init();
        this.generatorManager.init();
        this.stashManager.init();
        this.playerManager.init();
        this.gearItemManager.init();
        this.customEntityManager.init();
        this.bazaarManager.init();
        this.miningManager.init();
        this.fishingManager.init();
        this.particleManager.init();
        this.craftingManager.init();
        this.tutorialManager.init();
        this.chatManager.init();
        this.gameManager.init();
        this.spawnerManager.init();

        // Player Shop
        this.setupPlayerShop();

        // Custom Items
        /*
        this.getCustomItemManager().registerCustomItem(new UpgradeGem());
        this.getCustomItemManager().registerCustomItem(new WoodenSword());
        this.getCustomItemManager().registerCustomItem(new WoodenBow());
        this.getCustomItemManager().registerCustomItem(new LeatherHelmet());
        this.getCustomItemManager().registerCustomItem(new LeatherChestplate());
        */
        // Custom Mobs
        this.getCustomEntityManager().registerCustomEntity(new HostileChicken());
        this.getCustomEntityManager().registerCustomEntity(new PoisonChicken());
        this.getCustomEntityManager().registerCustomEntity(new ChickBoss());
        this.getCustomEntityManager().registerCustomEntity(new HitBox());
        this.getCustomEntityManager().registerCustomEntity(new EggBox());
        this.getCustomEntityManager().registerCustomEntity(new Eggsplosive());
        this.getCustomEntityManager().registerCustomEntity(new DynamicChicken());
        this.getCustomEntityManager().registerCustomEntity(new HostileSlime());
        this.getCustomEntityManager().registerCustomEntity(new SlimeBoss());
        this.getCustomEntityManager().registerCustomEntity(new SlimeBossTimer());
        this.getCustomEntityManager().registerCustomEntity(new SlimeBossSmall());
        this.getCustomEntityManager().registerCustomEntity(new SlimeBossSwift());

        // Manager Listeners
        Bukkit.getServer().getPluginManager().registerEvents(this.generatorManager, this);
        Bukkit.getServer().getPluginManager().registerEvents(this.stashManager, this);
        Bukkit.getServer().getPluginManager().registerEvents(this.customItemManager, this);
        Bukkit.getServer().getPluginManager().registerEvents(this.playerManager, this);
        Bukkit.getServer().getPluginManager().registerEvents(this.gearItemManager, this);
        Bukkit.getServer().getPluginManager().registerEvents(this.customEntityManager, this);
        Bukkit.getServer().getPluginManager().registerEvents(this.miningManager, this);
        Bukkit.getServer().getPluginManager().registerEvents(this.fishingManager, this);
        Bukkit.getServer().getPluginManager().registerEvents(this.craftingManager, this);
        Bukkit.getServer().getPluginManager().registerEvents(this.tutorialManager, this);
        Bukkit.getServer().getPluginManager().registerEvents(this.chatManager, this);
        Bukkit.getServer().getPluginManager().registerEvents(this.spawnerManager, this);

        // General Listeners
        Bukkit.getServer().getPluginManager().registerEvents(new GuiListener(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new BentoBoxListener(this), this);

        // Register commands
        this.registerCommands();
    }

    @Override
    public void onDisable() {
        this.fishingManager.disable();
        this.generatorManager.disable();
        this.stashManager.disable();
        this.customItemManager.disable();
        this.playerManager.disable();
        this.gearItemManager.disable();
        this.customEntityManager.disable();
        this.miningManager.disable();
        this.databaseManager.disable();
        this.tutorialManager.disable();
        this.chatManager.disable();
        this.spawnerManager.disable();
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
        registerCommand("genshop"      , new GeneratorShopCommandExecutor());
        registerCommand("gearshop"     , new GearShopCommandExecutor());
        if (itemShop != null) registerCommand("pshop", itemShop.getCommandExecutor());
        registerCommand("inspect"      , new InspectCommandExecutor());
        registerCommand("island"       , new IslandCommandExecutor());
        registerCommand("link"         , new LinkCommandExecutor());
        registerCommand("reroll"       , new RerollCommandExecutor());
        registerCommand("forgefrontier", new ForgeFrontierCommandExecutor());
        registerCommand("customspawn"  , new EntityCommandExecutor());
        registerCommand("mining"       , new MiningCommandExecutor());
        registerCommand("tutorial"     , new TutorialCommandExecutor());
        registerCommand("rank"         , new RankCommandExecutor());

        // Wrapper-Registered Commands
        BukkitCommandHandler commandHandler = BukkitCommandHandler.create(this);
        commandHandler.register(new BazaarCommand(this));
        commandHandler.register(new FishingCommands(this));
        // Auto-Completer Registrations
        AutoCompleter autoCompleter = commandHandler.getAutoCompleter();
        autoCompleter.registerSuggestion("placeable_items", Spawner.MATERIALS.keySet());
        autoCompleter.registerSuggestion("cspawner", this.getSpawnerManager().getSpawners().keySet());
        autoCompleter.registerSuggestion("cgive", customItemManager.getItemNames());
        autoCompleter.registerSuggestion("cspawn", this.getCustomEntityManager().getEntities().keySet());
        autoCompleter.registerSuggestion("idparticle", this.getParticleManager().getParticleMap().keySet());
        commandHandler.register(new CustomGiveCommand(this));
        commandHandler.register(new ParticleCommands(this));
        commandHandler.register(new GeneratorCommand(this));
        commandHandler.register(new CustomSpawnCommand(this));
        commandHandler.register(new CraftingCommands(this));
        commandHandler.register(new SpawnerCommandExecutor(this));
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
        if (!ForgeFrontier.isDBConn()) return;
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

    public FishingManager getFishingManager() {
        return fishingManager;
    }

    public ParticleManager getParticleManager() { return particleManager; }

    public CraftingManager getCraftingManager() {
        return craftingManager;
    }

    public CustomSkullManager getCustomSkullManager() {
        return customSkullManager;
    }

    public TutorialManager getTutorialManager() {
        return tutorialManager;
    }

    public ChatManager getChatManager() {
        return chatManager;
    }

    public SpawnerManager getSpawnerManager() {
        return this.spawnerManager;
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

    public static boolean isDBConn() {
        if (ForgeFrontier.getInstance() == null) return false;
        if (ForgeFrontier.getInstance().getDatabaseManager() == null) return false;
        return (ForgeFrontier.getInstance().getDatabaseManager().connected);
    }

}

