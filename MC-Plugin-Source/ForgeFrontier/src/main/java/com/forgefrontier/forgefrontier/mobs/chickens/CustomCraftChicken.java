package com.forgefrontier.forgefrontier.mobs.chickens;

import com.forgefrontier.forgefrontier.ForgeFrontier;
import com.forgefrontier.forgefrontier.mobs.CustomCraftEntity;
import com.forgefrontier.forgefrontier.spawners.SpawnerInstance;
import net.minecraft.world.entity.animal.Chicken;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.craftbukkit.v1_18_R2.CraftServer;
import org.bukkit.craftbukkit.v1_18_R2.entity.CraftChicken;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.*;
import java.util.logging.Level;

/**
 * Class to represent the actual chicken entity in game
 */
public abstract class CustomCraftChicken extends CraftChicken implements CustomCraftEntity {

    public String code;
    public String customCraftName;
    public Chicken handle;
    public HashMap<String, Integer> customDropTable;
    public HashMap<Material, Integer> dropTable;
    public ArrayList<String> customDropKeys;
    public ArrayList<Material> dropKeys;

    /**
     * Constructor
     * @param server the CraftServer instance
     */
    public CustomCraftChicken(CraftServer server, String chickenType, CustomChickenEntity entity) {
        super(server, entity);
        this.code = chickenType;
        this.handle = this.getHandle();
        initDropTable();
    }

    @Override
    public String getCode() {
        return this.code;
    }

    /**
     * Sets the health of the entity
     *
     * @param maxHealth
     */
    public void setDefaultHealth(int maxHealth) {
        // sets the health of the chicken
        this.setMaxHealth(maxHealth);
        this.setHealth(maxHealth);
    }

    /**
     * Initializes the nameplate of the mob
     *
     * @param name displays the name, current health, and custom health
     */
    public void updateNamePlate(String name) {
        // sets the nameplate of the chicken
        // System.out.println("Updating nameplate");
        this.setCustomName(ChatColor.WHITE + name + ": " + ((int) this.getHealth()) + "/" + ((int) this.getMaxHealth()));
        this.setCustomNameVisible(true);
    }

    /**
     * Initializes the bossbar for boss mobs
     *
     * @param name name displayed on the boss bar
     * @return the bossBar object
     */
    public BossBar initBossBar(String name) {
        BossBar bossBar;
        bossBar = Bukkit.createBossBar(name, BarColor.RED, BarStyle.SOLID);
        bossBar.setVisible(true);
        bossBar.setProgress(1);


        //TODO: Change this later (Currently Grabs every player on the server and displays the boss bar
        //      Need to change in order to only grab players nearby
        Map<UUID, Player> playerMap = ForgeFrontier.getInstance().getPlayerManager().getPlayersByUUID();
        Collection<Player> playerCollection = playerMap.values();
        Object[] players = playerCollection.toArray();
        for (Object player : players) {
            Player p = (Player) player;
            bossBar.addPlayer(p);
        }

        return bossBar;
    }

    /**
     * Initializes the drop hashmap
     */
    @Override
    public void initDropTable() {
        dropTable = new HashMap<>();
        customDropTable = new HashMap<>();
        customDropKeys = new ArrayList<>();
        dropKeys = new ArrayList<>();
    }

    /**
     * registers a custom item to be placed in the drop table
     *
     * @param item the item code
     * @param chance the drop chance (out of 100)
     */
    @Override
    public void registerDropItem(String item, int chance) {
        customDropKeys.add(item);
        customDropTable.put(item, chance);
    }

    /**
     * registers an item to be placed in the drop table
     *
     * @param item the item
     * @param chance the drop chance (out of 100)
     */
    @Override
    public void registerDropItem(Material item, int chance) {
        dropKeys.add(item);
        dropTable.put(item, chance);
    }

    /**
     * Defines the loot table for the TestChicken
     */
    @Override
    public void saveMetaData() {
        this.setMetadata("name", new FixedMetadataValue(ForgeFrontier.getInstance(), this.customCraftName));
        this.setMetadata("custom-drop-keys", new FixedMetadataValue(ForgeFrontier.getInstance(), this.customDropKeys));
        this.setMetadata("custom-drop-table", new FixedMetadataValue(ForgeFrontier.getInstance(), this.customDropTable));
        this.setMetadata("drop-keys", new FixedMetadataValue(ForgeFrontier.getInstance(), this.dropKeys));
        this.setMetadata("drop-table", new FixedMetadataValue(ForgeFrontier.getInstance(), this.dropTable));
    }

}
