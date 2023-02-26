package com.forgefrontier.forgefrontier.generators;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class GeneratorInstance {

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

    public Inventory getInventory() {
        return new GeneratorInventoryHolder(this).getInventory();
    }

    public void collect(Player player) {
        long currentTime = System.currentTimeMillis();

        GeneratorLevel currentLevel = getGeneratorLevel();

        int collectAmt = (int) (currentTime - this.lastCollectTime) / currentLevel.generatorRate;
        if(collectAmt > currentLevel.maxSize) {
            collectAmt = currentLevel.maxSize;
        }
        this.generator.primaryMaterial.collect(player, collectAmt);
        this.lastCollectTime = currentTime;
    }

    public BoundingBox getBoundingBox() {
        return this.boundingBox;
    }

    private void setAmountLeft(int amtLeft, long currentTime) {
        GeneratorLevel currentLevel = getGeneratorLevel();



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

    public int getMaxAmt() {
        return this.generator.generatorLevels.get(level).maxSize;
    }

    public GeneratorLevel getGeneratorLevel() {
        return this.generator.generatorLevels.get(level);
    }
}
