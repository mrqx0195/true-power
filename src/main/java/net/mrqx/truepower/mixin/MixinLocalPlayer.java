package net.mrqx.truepower.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import mods.flammpfeil.slashblade.capability.slashblade.BladeStateAccess;
import net.minecraft.client.player.LocalPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Slice;

@Mixin(LocalPlayer.class)
public abstract class MixinLocalPlayer {
    @Shadow
    private boolean lastOnGround;
    
    @WrapOperation(method = "aiStep()V",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;isUsingItem()Z"),
        slice = @Slice(
            from = @At(value = "INVOKE", target = "Lnet/neoforged/neoforge/client/ClientHooks;onMovementInputUpdate(Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/client/player/Input;)V", remap = false),
            to = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;moveTowardsClosestSpace(DD)V")
        )
    )
    private boolean wrapOperationIsUsingItemInAiStep(LocalPlayer instance, Operation<Boolean> original) {
        if (BladeStateAccess.of(instance.getMainHandItem()).isPresent()) {
            return false;
        }
        return original.call(instance);
    }
    
    @WrapOperation(method = "sendPosition()V", at = {
        @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;onGround()Z", ordinal = 0),
        @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;onGround()Z", ordinal = 1),
        @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;onGround()Z", ordinal = 2),
        @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;onGround()Z", ordinal = 3),
        @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;onGround()Z", ordinal = 4),
        @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;onGround()Z", ordinal = 5)
    })
    private boolean wrapSendPosition(LocalPlayer instance, Operation<Boolean> original) {
        boolean isOnGround = original.call(instance);
        if (BladeStateAccess.of(instance.getMainHandItem()).isPresent()) {
            if (!isOnGround && this.lastOnGround) {
                return true;
            }
        }
        return isOnGround;
    }
}
