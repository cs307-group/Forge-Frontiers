package com.forgefrontier.forgefrontier.generators;

import com.forgefrontier.forgefrontier.ForgeFrontier;
import com.forgefrontier.forgefrontier.connections.GeneratorDB;
import com.forgefrontier.forgefrontier.utils.Locatable;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class GeneratorInstance implements Locatable {

    String databaseId;
    Generator generator;
    BoundingBox boundingBox;
    int level;
    long lastCollectTime;

    public GeneratorInstance(Generator generator, Location location) {
        this.generator = generator;
        this.boundingBox = new BoundingBox(location);
        this.level = 0;
        this.lastCollectTime = System.currentTimeMillis();
    }

    public GeneratorInstance(Generator generator, Location location, int level, long lastCollectTime, String databaseId) {
        this.generator = generator;
        this.boundingBox = new BoundingBox(location);
        this.level = level;
        this.lastCollectTime = lastCollectTime;
        this.databaseId = databaseId;
    }

    public Inventory getInventory() {
        return new GeneratorInventoryHolder(this).getInventory();
    }

    public void collect(Player player) {
        ForgeFrontier.getInstance().getDatabaseManager().getGeneratorDB().updateGenerator(this, (status) -> {
            Bukkit.getScheduler().runTask(ForgeFrontier.getInstance(), () -> {
                if(status == GeneratorDB.Status.ERROR) {
                    ForgeFrontier.getInstance().getLogger().severe("Unable to get information for a generator instance when updating.");
                }
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
            });
        });
    }

    public BoundingBox getBoundingBox() {
        return this.boundingBox;
    }

    private void setAmountLeft(int amtLeft, long currentTime, int remainingTime) {
        GeneratorLevel currentLevel = getGeneratorLevel();

        this.lastCollectTime = currentTime - amtLeft * currentLevel.generatorRate - remainingTime;

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

    }

    @Override
    public int getX() {
        return this.boundingBox.getLocation().getBlockX();
    }

    @Override
    public int getY() {
        return this.boundingBox.getLocation().getBlockY();
    }

    @Override
    public int getZ() {
        return this.boundingBox.getLocation().getBlockZ();
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

}
