package net.mrqx.truepower.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import mods.flammpfeil.slashblade.capability.slashblade.BladeStateAccess;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Slice;

@Mixin(Player.class)
public abstract class MixinPlayer {
    @WrapOperation(method = "updatePlayerPose()V",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;setPose(Lnet/minecraft/world/entity/Pose;)V"),
        slice = @Slice(
            from = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;isSpectator()Z")
        )
    )
    private void wrapOperationSetPoseInUpdatePlayerPose(Player instance, Pose pose, Operation<Void> original) {
        if (pose.equals(Pose.CROUCHING) || pose.equals(Pose.SWIMMING)) {
            if (BladeStateAccess.of(instance.getMainHandItem()).isPresent()) {
                original.call(instance, Pose.STANDING);
                return;
            }
        }
        original.call(instance, pose);
    }
}
