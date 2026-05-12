package net.mrqx.truepower.mixin;

import mods.flammpfeil.slashblade.event.handler.RankPointHandler;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RankPointHandler.class)
public class MixinRankPointHandler {
    @Inject(method = "onLivingHurtEvent(Lnet/neoforged/neoforge/event/entity/living/LivingDamageEvent$Pre;)V", at = @At("HEAD"), cancellable = true, remap = false)
    private void inject(LivingDamageEvent.Pre event, CallbackInfo ci) {
        ci.cancel();
    }
}
