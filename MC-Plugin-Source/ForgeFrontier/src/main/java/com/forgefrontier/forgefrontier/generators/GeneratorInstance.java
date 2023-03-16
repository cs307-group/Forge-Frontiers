package com.forgefrontier.forgefrontier.generators;

import com.forgefrontier.forgefrontier.utils.Locatable;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class GeneratorInstance implements Locatable {

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

        boolean resetCounter = false;
        int collectAmt = (int) (currentTime - this.lastCollectTime) / currentLevel.generatorRate;
        int nextTimeRemain = (int) (currentTime - this.lastCollectTime) % currentLevel.generatorRate;
        if(collectAmt > currentLevel.maxSize) {
            collectAmt = currentLevel.maxSize;
            nextTimeRemain = 0;
            resetCounter = true;
        }
        int amtLeft = this.generator.primaryMaterial.collect(player, collectAmt);
        this.setAmountLeft(amtLeft, currentTime, nextTimeRemain);
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
}
