package com.forgefrontier.forgefrontier.generators;

import java.util.List;

public class Generator {

    CustomMaterial primaryMaterial;
    List<GeneratorLevel> generatorLevels;

    public Generator(CustomMaterial primaryMaterial, List<GeneratorLevel> generatorLevels) {
        this.primaryMaterial = primaryMaterial;
        this.generatorLevels = generatorLevels;
    }


    public String getCode() {
        return this.primaryMaterial.name.replace(" ", "-");
    }
}
