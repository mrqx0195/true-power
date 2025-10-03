package net.mrqx.truepower.mixin;

import mods.flammpfeil.slashblade.capability.concentrationrank.IConcentrationRank;
import mods.flammpfeil.slashblade.event.handler.RankPointHandler;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RankPointHandler.class)
public class MixinRankPointHandler {
    @Inject(method = "lambda$onLivingDeathEvent$1", at = @At("HEAD"), cancellable = true, remap = false)
    private static void inject(LivingHurtEvent event, IConcentrationRank cr, CallbackInfo ci) {
        ci.cancel();
    }
}
