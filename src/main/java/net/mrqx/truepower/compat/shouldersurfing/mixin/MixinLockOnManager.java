package net.mrqx.truepower.compat.shouldersurfing.mixin;

import com.github.exopandora.shouldersurfing.client.ShoulderSurfing;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import mods.flammpfeil.slashblade.ability.LockOnManager;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.mrqx.truepower.compat.shouldersurfing.util.TruePowerShoulderSurfingUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Optional;
import java.util.function.Predicate;

@Mixin(LockOnManager.class)
public abstract class MixinLockOnManager {
    @WrapOperation(method = "onInputChange(Lmods/flammpfeil/slashblade/event/handler/InputCommandEvent;)V", at = @At(value = "INVOKE", target = "Lmods/flammpfeil/slashblade/util/RayTraceHelper;rayTrace(Lnet/minecraft/world/level/Level;Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/phys/Vec3;Lnet/minecraft/world/phys/Vec3;DDLjava/util/function/Predicate;)Ljava/util/Optional;", remap = false), remap = false)
    private static Optional<HitResult> wrapRayTrace(Level worldIn, Entity entityIn, Vec3 start, Vec3 dir, double blockReach, double entityReach, Predicate<Entity> selector, Operation<Optional<HitResult>> original) {
        if (ShoulderSurfing.getInstance().isShoulderSurfing()) {
            return TruePowerShoulderSurfingUtils.lockOnRayTraceShoulderSurfing(worldIn, entityIn, entityReach, selector);
        } else {
            return original.call(worldIn, entityIn, start, dir, blockReach, entityReach, selector);
        }
    }
}
