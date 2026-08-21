package net.mrqx.truepower.compat.shouldersurfing.mixin;

import com.github.exopandora.shouldersurfing.client.ShoulderSurfing;
import com.github.exopandora.shouldersurfing.client.ShoulderSurfingCamera;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.mrqx.truepower.compat.shouldersurfing.util.TruePowerShoulderSurfingUtils;
import net.mrqx.truepower.util.LockOnUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Optional;
import java.util.function.Predicate;

@Mixin(LockOnUtils.class)
public abstract class MixinLockOnUtils {
    @WrapOperation(method = "getLockOnEntitySortedList(Lnet/minecraft/world/entity/LivingEntity;)Ljava/util/List;", at = @At(value = "INVOKE", target = "Lmods/flammpfeil/slashblade/util/RayTraceHelper;rayTrace(Lnet/minecraft/world/level/Level;Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/phys/Vec3;Lnet/minecraft/world/phys/Vec3;DDLjava/util/function/Predicate;)Ljava/util/Optional;", remap = false), remap = false)
    private static Optional<HitResult> wrapRayTrace(Level worldIn, Entity entityIn, Vec3 start, Vec3 dir, double blockReach, double entityReach, Predicate<Entity> selector, Operation<Optional<HitResult>> original) {
        if (ShoulderSurfing.getInstance().isShoulderSurfing()) {
            return TruePowerShoulderSurfingUtils.lockOnRayTraceShoulderSurfing(worldIn, entityIn, entityReach, selector);
        } else {
            return original.call(worldIn, entityIn, start, dir, blockReach, entityReach, selector);
        }
    }
    
    @WrapOperation(method = "isVisible(Lnet/minecraft/world/entity/Entity;D)Z", at = @At(value = "INVOKE", target = "Lnet/mrqx/truepower/util/LockOnUtils;isInViewCone(Lnet/minecraft/world/phys/Vec3;Lnet/minecraft/world/phys/AABB;FF)Z", remap = false), remap = false)
    private static boolean wrapIsInViewCone(Vec3 cameraPos, AABB bb, float pitch, float yaw, Operation<Boolean> original) {
        if (ShoulderSurfing.getInstance().isShoulderSurfing()) {
            ShoulderSurfingCamera camera = ShoulderSurfing.getInstance().getCamera();
            return original.call(cameraPos, bb, camera.getXRot(), camera.getYRot());
        } else {
            return original.call(cameraPos, bb, pitch, yaw);
        }
    }
    
    @WrapOperation(method = "isVisible(Lnet/minecraft/world/entity/Entity;D)Z", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Camera;getPosition()Lnet/minecraft/world/phys/Vec3;"), remap = false)
    private static Vec3 wrapGetPosition(Camera instance, Operation<Vec3> original) {
        Minecraft minecraft = Minecraft.getInstance();
        Entity cameraEntity = minecraft.getCameraEntity();
        if (ShoulderSurfing.getInstance().isShoulderSurfing() && cameraEntity != null) {
            return TruePowerShoulderSurfingUtils.getShoulderSurfingCameraPosition(minecraft, cameraEntity);
        } else {
            return original.call(instance);
        }
    }
}
