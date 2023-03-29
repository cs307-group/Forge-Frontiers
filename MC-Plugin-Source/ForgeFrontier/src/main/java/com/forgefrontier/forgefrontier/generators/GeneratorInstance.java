package com.forgefrontier.forgefrontier.generators;

import com.forgefrontier.forgefrontier.ForgeFrontier;
import com.forgefrontier.forgefrontier.connections.GeneratorDB;
import com.forgefrontier.forgefrontier.utils.Locatable;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import world.bentobox.bentobox.BentoBox;
import world.bentobox.bentobox.database.objects.Island;

import java.util.Optional;

public class GeneratorInstance implements Locatable {

    Island island;
    String databaseId;
    Generator generator;
    Location location;
    int level;
    long lastCollectTime;

    public GeneratorInstance(Generator generator, Location location) {
        this.generator = generator;
        this.location = location;
        this.level = 0;
        this.lastCollectTime = System.currentTimeMillis();
    }

    public GeneratorInstance(Generator generator, Location location, int level, long lastCollectTime, String databaseId) {
        this.generator = generator;
        this.location = location;
        this.level = level;
        this.lastCollectTime = lastCollectTime;
        this.databaseId = databaseId;
    }

    public Inventory getInventory() {
        return new GeneratorInventoryHolder(this).getInventory();
    }

    public void collect(Player player) {
        ForgeFrontier.getInstance().getDatabaseManager().getGeneratorDB().updateGenerator(this, (status) -> {
            if(status == GeneratorDB.Status.ERROR) {
                ForgeFrontier.getInstance().getLogger().severe("Unable to get information for a generator instance when updating.");
                player.sendMessage(ForgeFrontier.CHAT_PREFIX + "Sorry. An error occurred when attempting to update your generator. Please try again later.");
                return;
            }
            Bukkit.getScheduler().runTask(ForgeFrontier.getInstance(), () -> {
                long currentTime = System.currentTimeMillis();

                GeneratorLevel currentLevel = getGeneratorLevel();

                int collectAmt = (int) (currentTime - this.lastCollectTime) / currentLevel.generatorRate;
                int nextTimeRemain = (int) (currentTime - this.lastCollectTime) % currentLevel.generatorRate;
                if(collectAmt >= currentLevel.maxSize) {
                    collectAmt = currentLevel.maxSize;
                    nextTimeRemain = 0;
                }
                int amtLeft = this.generator.primaryMaterial.collect(player, collectAmt);
                this.setAmountLeft(amtLeft, currentTime, nextTimeRemain);
                ForgeFrontier.getInstance().getDatabaseManager().getGeneratorDB().sendGeneratorUpdate(this, (success) -> {
                    if(!success) {
                        Bukkit.getScheduler().runTask(ForgeFrontier.getInstance(), () -> {
                            ForgeFrontier.getInstance().getLogger().severe("Unable to send generator update to database. An unknown error occurred in doing so.");
                            player.sendMessage(ForgeFrontier.CHAT_PREFIX + "Sorry. An error occurred when attempting to update your generator. Please try again later.");
                        });
                    }
                });
            });
        });
    }

    private void setAmountLeft(int amtLeft, long currentTime, int remainingTime) {
        GeneratorLevel currentLevel = getGeneratorLevel();

        this.lastCollectTime = currentTime - remainingTime - amtLeft * currentLevel.generatorRate;

    }

    public int getCollectAmt() {
        long currentTime = System.currentTimeMillis();

        GeneratorLevel currentLevel = getGeneratorLevel();

        if(currentLevel.generatorRate == 0) currentLevel.generatorRate = 1;
        int collectAmt = (int) (currentTime - this.lastCollectTime) / currentLevel.generatorRate;
        if(collectAmt > currentLevel.maxSize) {
            collectAmt = currentLevel.maxSize;
        }

        return collectAmt;
    }

    public Location getLocation() {
        return this.location;
    }

    public long getLastCollectTime() {
        return this.lastCollectTime;
    }

    public int getMaxAmt() {
        return this.generator.generatorLevels.get(level).maxSize;
    }

    public GeneratorLevel getGeneratorLevel() {
        return this.generator.generatorLevels.get(level);
    }

    public void setDatabaseId(String databaseId) {
        this.databaseId = databaseId;
    }

    public String getDatabaseId() {
        return this.databaseId;
    }

    public void setLastCollectTime(long lastCollectionTime) {
        this.lastCollectTime = lastCollectionTime;
    }

    public void upgrade() {
        long currentTime = System.currentTimeMillis();

        GeneratorLevel currentLevel = getGeneratorLevel();

        int collectAmt = (int) (currentTime - this.lastCollectTime) / currentLevel.generatorRate;
        int nextTimeRemain = (int) (currentTime - this.lastCollectTime) % currentLevel.generatorRate;

        this.level += 1;

        this.setAmountLeft(collectAmt, currentTime, nextTimeRemain);

        ForgeFrontier.getInstance().getDatabaseManager().getGeneratorDB().sendGeneratorUpdate(this, (success) -> {
            if(!success) {
                ForgeFrontier.getInstance().getLogger().severe("Unable to send generator update to database. An unknown error occurred in doing so.");
            }
        });
    }

    @Override
    public int getX() {
        return this.getLocation().getBlockX();
    }

    @Override
    public int getY() {
        return this.getLocation().getBlockY();
    }

    @Override
    public int getZ() {
        return this.getLocation().getBlockZ();
    }

    @Override
    public boolean isAt(int x, int y, int z) {
        return this.getX() == x && this.getY() == y && this.getZ() == z;
    }

    public int getLevelInt() {
        return this.level;
    }

    @Override
    public String toString() {
        return "(" + this.generator.getId() + ", " + this.level + ", " + this.getX() + ", " + this.getY() + ", " + this.getZ() + ")";
    }

    public Generator getGenerator() {
        return this.generator;
    }

    public Island getIsland() {
        if(island == null) {
            Location l = this.getLocation().clone();
            l.setY(0);
            Optional<Island> island = BentoBox.getInstance().getIslandsManager().getIslandAt(l);
            if(!island.isPresent())
                return null;
            this.island = island.get();
        }
        return this.island;
    }

}
