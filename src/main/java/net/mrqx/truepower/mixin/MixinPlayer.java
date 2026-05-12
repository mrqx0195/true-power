package net.mrqx.truepower.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import mods.flammpfeil.slashblade.item.ItemSlashBlade;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
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
            ItemStack itemStack = instance.getMainHandItem();
            if (!itemStack.isEmpty() && itemStack.getCapability(ItemSlashBlade.BLADESTATE).isPresent()) {
                original.call(instance, Pose.STANDING);
                return;
            }
        }
        original.call(instance, pose);
    }
}
