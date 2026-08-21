package net.mrqx.truepower.compat.epicfight.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import mods.flammpfeil.slashblade.util.AttackHelper;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.mrqx.truepower.compat.epicfight.util.TruePowerEpicFightUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import yesman.epicfight.world.damagesource.EpicFightDamageSource;

@Mixin(AttackHelper.class)
public abstract class MixinAttackHelper {
    @WrapOperation(method = "attack(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/entity/Entity;F)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;hurt(Lnet/minecraft/world/damagesource/DamageSource;F)Z"), remap = false)
    private static boolean onHurt(Entity instance, DamageSource source, float amount, Operation<Boolean> original, @Local(argsOnly = true) LivingEntity attacker) {
        DamageSource damageSource = source;
        if (!(source instanceof EpicFightDamageSource)) {
            damageSource = TruePowerEpicFightUtils.getEpicFightDamageSource(attacker, damageSource);
        }
        return original.call(instance, damageSource, amount);
    }
}
