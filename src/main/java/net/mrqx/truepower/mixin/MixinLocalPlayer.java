package net.mrqx.truepower.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import mods.flammpfeil.slashblade.item.ItemSlashBlade;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Slice;

@Mixin(LocalPlayer.class)
public abstract class MixinLocalPlayer {
    @WrapOperation(method = "aiStep()V",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;isUsingItem()Z"),
        slice = @Slice(
            from = @At(value = "INVOKE", target = "Lnet/minecraftforge/client/ForgeHooksClient;onMovementInputUpdate(Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/client/player/Input;)V", remap = false),
            to = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;moveTowardsClosestSpace(DD)V")
        )
    )
    private boolean wrapOperationIsUsingItemInAiStep(LocalPlayer instance, Operation<Boolean> original) {
        ItemStack itemStack = instance.getMainHandItem();
        if (!itemStack.isEmpty() && itemStack.getCapability(ItemSlashBlade.BLADESTATE).isPresent()) {
            return false;
        }
        return original.call(instance);
    }
}
