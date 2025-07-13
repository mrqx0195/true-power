package net.mrqx.truepower.mixin;

import mods.flammpfeil.slashblade.capability.concentrationrank.ConcentrationRank;
import mods.flammpfeil.slashblade.capability.concentrationrank.IConcentrationRank;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ConcentrationRank.class)
public abstract class MixinConcentrationRank implements IConcentrationRank {
    @Override
    public long reductionLimitter(long reduction) {
        return reduction;
    }
}
