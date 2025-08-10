package net.mrqx.truepower.mixin;

import com.google.common.collect.Range;
import mods.flammpfeil.slashblade.capability.concentrationrank.IConcentrationRank;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(IConcentrationRank.ConcentrationRanks.class)
public abstract class MixinConcentrationRanks {
    @Mutable
    @Shadow
    @Final
    Range<Float> pointRange;

    @Shadow
    @Final
    public int level;

    @Shadow
    public static float MAX_LEVEL;

    @Inject(method = "<init>", at = @At("RETURN"))
    private void injectInit(String par1, int par2, int level, Range<Float> pointRange, CallbackInfo ci) {
        if (this.level == 5) {
            this.pointRange = Range.closedOpen(5.0F, 6.0F);
        } else if (this.level == 6) {
            this.pointRange = Range.closedOpen(6.0F, 7.0F);
        } else if (this.level == 7) {
            this.pointRange = Range.atLeast(7.0F);
        }
    }

    @Inject(method = "<clinit>", at = @At("RETURN"))
    private static void injectClInit(CallbackInfo ci) {
        MAX_LEVEL = 8;
    }
}
