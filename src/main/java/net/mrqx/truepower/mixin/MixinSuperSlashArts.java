package net.mrqx.truepower.mixin;

import mods.flammpfeil.slashblade.ability.SuperSlashArts;
import mods.flammpfeil.slashblade.event.handler.InputCommandEvent;
import net.mrqx.truepower.config.TruePowerCommonConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SuperSlashArts.class)
public abstract class MixinSuperSlashArts {
    @Inject(method = "onInputChange(Lmods/flammpfeil/slashblade/event/handler/InputCommandEvent;)V", at = @At("HEAD"), cancellable = true)
    private void injectOnInputChange(InputCommandEvent event, CallbackInfo ci) {
        if (TruePowerCommonConfig.REMOVE_ORIGINAL_SSA_INPUT.get()) {
            ci.cancel();
        }
    }
}
