package net.mrqx.truepower.mixin;

import mods.flammpfeil.slashblade.item.ItemSlashBlade;
import net.minecraft.client.player.LocalPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.Slice;

@Mixin(LocalPlayer.class)
public abstract class MixinLocalPlayer {
    @Redirect(method = "aiStep()V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;isUsingItem()Z"),
            slice = @Slice(
                    from = @At(value = "INVOKE", target = "Lnet/minecraftforge/client/ForgeHooksClient;onMovementInputUpdate(Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/client/player/Input;)V"),
                    to = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;moveTowardsClosestSpace(DD)V")
            )
    )
    private boolean redirectIsUsingItemInAiStep(LocalPlayer instance) {
        return !instance.getMainHandItem().getCapability(ItemSlashBlade.BLADESTATE).isPresent() && instance.isUsingItem();
    }
}
