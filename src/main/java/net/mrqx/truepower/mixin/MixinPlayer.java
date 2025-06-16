package net.mrqx.truepower.mixin;

import mods.flammpfeil.slashblade.item.ItemSlashBlade;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.Slice;

@Mixin(Player.class)
public abstract class MixinPlayer {
    @Redirect(method = "updatePlayerPose()V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;setPose(Lnet/minecraft/world/entity/Pose;)V"),
            slice = @Slice(
                    from = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;isSpectator()Z")
            ))
    private void redirectSetPoseInUpdatePlayerPose(Player instance, Pose pose) {
        if (pose.equals(Pose.CROUCHING) || pose.equals(Pose.SWIMMING)) {
            if (instance.getMainHandItem().getCapability(ItemSlashBlade.BLADESTATE).isPresent()) {
                instance.setPose(Pose.STANDING);
                return;
            }
        }
        instance.setPose(pose);
    }
}
