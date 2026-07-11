package net.mrqx.truepower.compat.shouldersurfing.mixin;

import com.github.exopandora.shouldersurfing.api.math.Vec2f;
import com.github.exopandora.shouldersurfing.client.ShoulderSurfingCamera;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.player.LocalPlayer;
import net.mrqx.truepower.util.TruePowerComboHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ShoulderSurfingCamera.class)
public abstract class MixinShoulderSurfingCamera {
    @Inject(method = "turnPlayerWithCamera(Lnet/minecraft/client/player/LocalPlayer;Lcom/github/exopandora/shouldersurfing/api/math/Vec2f;Z)V", at = @At("HEAD"), remap = false, cancellable = true)
    private void injectTurnPlayerWithCamera(LocalPlayer player, Vec2f scaledRot, boolean isMoving, CallbackInfo ci) {
        if (TruePowerComboHelper.hasTargetOrSneak(player)) {
            ci.cancel();
        }
    }
    
    @ModifyVariable(method = "turn(Lnet/minecraft/client/player/LocalPlayer;DD)Z", at = @At("STORE"), name = "isMoving", remap = false)
    private boolean wrapTurn(boolean isMoving, @Local(argsOnly = true) LocalPlayer player) {
        boolean flag = isMoving;
        if (TruePowerComboHelper.hasTargetOrSneak(player)) {
            flag = false;
        }
        return flag;
    }
}
