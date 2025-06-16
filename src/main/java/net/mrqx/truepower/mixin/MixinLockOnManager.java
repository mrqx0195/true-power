package net.mrqx.truepower.mixin;

import mods.flammpfeil.slashblade.ability.LockOnManager;
import net.minecraft.util.Mth;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.mrqx.truepower.TruePowerModConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@OnlyIn(Dist.CLIENT)
@Mixin(LockOnManager.class)
public class MixinLockOnManager {
    @Redirect(method = "lambda$onEntityUpdate$9", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/Mth;rotLerp(FFF)F"))
    private static float modifyStep(float pDelta, float pStart, float pEnd) {
        return Mth.rotLerp(pDelta * TruePowerModConfig.LOCK_ON_SPEED.get().floatValue(), pStart, pEnd);
    }
}
