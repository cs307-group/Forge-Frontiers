package net.minecraft.world.level.levelgen.feature;

import com.mojang.serialization.Codec;
import java.util.Random;
import java.util.stream.Stream;
import net.minecraft.core.BlockPosition;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.IRegistry;
import net.minecraft.core.RegistryCodecs;
import net.minecraft.resources.RegistryFileCodec;
import net.minecraft.world.level.GeneratorAccessSeed;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.feature.configurations.WorldGenFeatureConfiguration;

public record WorldGenFeatureConfigured<FC extends WorldGenFeatureConfiguration, F extends WorldGenerator<FC>> (F d, FC e) {

    private final F feature;
    private final FC config;
    public static final Codec<WorldGenFeatureConfigured<?, ?>> DIRECT_CODEC = IRegistry.FEATURE.byNameCodec().dispatch((worldgenfeatureconfigured) -> {
        return worldgenfeatureconfigured.feature;
    }, WorldGenerator::configuredCodec);
    public static final Codec<Holder<WorldGenFeatureConfigured<?, ?>>> CODEC = RegistryFileCodec.create(IRegistry.CONFIGURED_FEATURE_REGISTRY, WorldGenFeatureConfigured.DIRECT_CODEC);
    public static final Codec<HolderSet<WorldGenFeatureConfigured<?, ?>>> LIST_CODEC = RegistryCodecs.homogeneousList(IRegistry.CONFIGURED_FEATURE_REGISTRY, WorldGenFeatureConfigured.DIRECT_CODEC);

    public WorldGenFeatureConfigured(F f0, FC fc) {
        this.feature = f0;
        this.config = fc;
    }

    public boolean place(GeneratorAccessSeed generatoraccessseed, ChunkGenerator chunkgenerator, Random random, BlockPosition blockposition) {
        return this.feature.place(this.config, generatoraccessseed, chunkgenerator, random, blockposition);
    }

    public Stream<WorldGenFeatureConfigured<?, ?>> getFeatures() {
        return Stream.concat(Stream.of(this), this.config.getFeatures());
    }

    public String toString() {
        return "Configured: " + this.feature + ": " + this.config;
    }

    public F feature() {
        return this.feature;
    }

    public FC config() {
        return this.config;
    }
}
