package net.minecraft.world.level.levelgen.blockpredicates;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BaseBlockPosition;
import net.minecraft.core.HolderSet;
import net.minecraft.core.IRegistry;
import net.minecraft.core.RegistryCodecs;
import net.minecraft.world.level.block.state.IBlockData;
import net.minecraft.world.level.material.FluidType;

class MatchingFluidsPredicate extends StateTestingPredicate {

    private final HolderSet<FluidType> fluids;
    public static final Codec<MatchingFluidsPredicate> CODEC = RecordCodecBuilder.create((instance) -> {
        return stateTestingCodec(instance).and(RegistryCodecs.homogeneousList(IRegistry.FLUID_REGISTRY).fieldOf("fluids").forGetter((matchingfluidspredicate) -> {
            return matchingfluidspredicate.fluids;
        })).apply(instance, MatchingFluidsPredicate::new);
    });

    public MatchingFluidsPredicate(BaseBlockPosition baseblockposition, HolderSet<FluidType> holderset) {
        super(baseblockposition);
        this.fluids = holderset;
    }

    @Override
    protected boolean test(IBlockData iblockdata) {
        return iblockdata.getFluidState().is(this.fluids);
    }

    @Override
    public BlockPredicateType<?> type() {
        return BlockPredicateType.MATCHING_FLUIDS;
    }
}
