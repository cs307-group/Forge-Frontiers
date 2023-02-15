package com.forgefrontier.forgefrontier.generators;

import java.util.ArrayList;
import java.util.List;

public class GeneratorLevel {

    public int maxSize;
    public int generatorRate;
    public List<MaterialDrop> rareAdditionalDrops;
    public List<MaterialCost> upgradeCost;

    public GeneratorLevel(int generatorRate, int maxSize) {
        this.generatorRate = generatorRate;
        this.maxSize = maxSize;
        this.rareAdditionalDrops = new ArrayList<>();
        this.upgradeCost = null;
    }



}
