package net.mrqx.truepower.compat.epicfight.mixin;

import mods.flammpfeil.slashblade.capability.slashblade.BladeStateAccess;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import yesman.epicfight.world.capabilities.entitypatch.LivingEntityPatch;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;

@Mixin(value = PlayerPatch.class, remap = false)
public abstract class MixinPlayerPatch<T extends Player> extends LivingEntityPatch<T> {
    private MixinPlayerPatch(T entity) {
        super(entity);
    }
    
    @Inject(method = "isEpicFightMode()Z", at = @At("HEAD"), cancellable = true)
    private void injectIsEpicFightMode(CallbackInfoReturnable<Boolean> cir) {
        if (BladeStateAccess.of(this.getOriginal().getMainHandItem()).isPresent()) {
            cir.setReturnValue(false);
        }
    }
    
    @Inject(method = "isVanillaMode()Z", at = @At("HEAD"), cancellable = true)
    private void injectIsVanillaMode(CallbackInfoReturnable<Boolean> cir) {
        if (BladeStateAccess.of(this.getOriginal().getMainHandItem()).isPresent()) {
            cir.setReturnValue(true);
        }
    }
}
