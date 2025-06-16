package net.mrqx.truepower.mixin;

import mods.flammpfeil.slashblade.capability.slashblade.ISlashBladeState;
import mods.flammpfeil.slashblade.item.ItemSlashBlade;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemSlashBlade.class)
public abstract class MixinItemSlashBlade {
    @Inject(method = "lambda$onLeftClickEntity$7", at = @At("HEAD"), cancellable = true)
    private static void injectOnLeftClickEntity(Player playerIn, ISlashBladeState state, CallbackInfo ci) {
        ci.cancel();
    }

    @Inject(method = "lambda$use$3", at = @At("HEAD"), cancellable = true)
    private static void injectUse(Player playerIn, InteractionHand handIn, ISlashBladeState state, CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(true);
    }
}
