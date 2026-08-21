package net.mrqx.truepower.compat.epicfight.mixin;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageSources;
import net.minecraft.world.entity.Entity;
import net.mrqx.truepower.compat.epicfight.util.TruePowerEpicFightUtils;
import net.mrqx.truepower.util.TruePowerAttackManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(TruePowerAttackManager.class)
public abstract class MixinTruePowerAttackManager {
    @Inject(method = "getSummonedSwordDamageSource(Lnet/minecraft/world/damagesource/DamageSources;Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/entity/Entity;)Lnet/minecraft/world/damagesource/DamageSource;", at = @At("RETURN"), cancellable = true)
    private static void injectGetSummonedSwordDamageSource(DamageSources instance, Entity causingEntity, Entity directEntity, CallbackInfoReturnable<DamageSource> cir) {
        cir.setReturnValue(TruePowerEpicFightUtils.getEpicFightSummonedSwordDamageSource(cir.getReturnValue()));
    }
}
