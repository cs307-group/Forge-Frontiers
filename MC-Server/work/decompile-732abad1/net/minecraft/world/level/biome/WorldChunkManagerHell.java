package net.minecraft.world.level.biome;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Sets;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.function.Predicate;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPosition;
import net.minecraft.core.Holder;

public class WorldChunkManagerHell extends WorldChunkManager implements BiomeManager.Provider {

    public static final Codec<WorldChunkManagerHell> CODEC = BiomeBase.CODEC.fieldOf("biome").xmap(WorldChunkManagerHell::new, (worldchunkmanagerhell) -> {
        return worldchunkmanagerhell.biome;
    }).stable().codec();
    private final Holder<BiomeBase> biome;

    public WorldChunkManagerHell(Holder<BiomeBase> holder) {
        super((List) ImmutableList.of(holder));
        this.biome = holder;
    }

    @Override
    protected Codec<? extends WorldChunkManager> codec() {
        return WorldChunkManagerHell.CODEC;
    }

    @Override
    public WorldChunkManager withSeed(long i) {
        return this;
    }

    @Override
    public Holder<BiomeBase> getNoiseBiome(int i, int j, int k, Climate.Sampler climate_sampler) {
        return this.biome;
    }

    @Override
    public Holder<BiomeBase> getNoiseBiome(int i, int j, int k) {
        return this.biome;
    }

    @Nullable
    @Override
    public Pair<BlockPosition, Holder<BiomeBase>> findBiomeHorizontal(int i, int j, int k, int l, int i1, Predicate<Holder<BiomeBase>> predicate, Random random, boolean flag, Climate.Sampler climate_sampler) {
        return predicate.test(this.biome) ? (flag ? Pair.of(new BlockPosition(i, j, k), this.biome) : Pair.of(new BlockPosition(i - l + random.nextInt(l * 2 + 1), j, k - l + random.nextInt(l * 2 + 1)), this.biome)) : null;
    }

    @Override
    public Set<Holder<BiomeBase>> getBiomesWithin(int i, int j, int k, int l, Climate.Sampler climate_sampler) {
        return Sets.newHashSet(Set.of(this.biome));
    }
}
