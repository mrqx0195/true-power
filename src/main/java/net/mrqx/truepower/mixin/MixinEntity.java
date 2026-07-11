package net.mrqx.truepower.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import mods.flammpfeil.slashblade.item.ItemSlashBlade;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.mrqx.truepower.util.CollideAction;
import net.mrqx.truepower.util.TruePowerComboHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public abstract class MixinEntity {
    @Inject(method = "canBeCollidedWith()Z", at = @At("HEAD"), cancellable = true)
    private void injectCanBeCollidedWith(CallbackInfoReturnable<Boolean> cir) {
        if (((Object) this) instanceof LivingEntity livingEntity) {
            ItemStack itemStack = livingEntity.getMainHandItem();
            if (!itemStack.isEmpty()) {
                itemStack.getCapability(ItemSlashBlade.BLADESTATE).ifPresent(state -> {
                    if (TruePowerComboHelper.getCollideAction(livingEntity, state).equals(CollideAction.SOLID)) {
                        cir.setReturnValue(true);
                    }
                });
            }
        }
    }
    
    @Inject(method = "canCollideWith(Lnet/minecraft/world/entity/Entity;)Z", at = @At("HEAD"), cancellable = true)
    private void injectCanCollideWith(CallbackInfoReturnable<Boolean> cir, @Local(argsOnly = true) Entity entity) {
        if (((Object) this) instanceof LivingEntity livingEntity) {
            ItemStack itemStack = livingEntity.getMainHandItem();
            if (!itemStack.isEmpty()) {
                itemStack.getCapability(ItemSlashBlade.BLADESTATE).ifPresent(state -> {
                    if (entity instanceof LivingEntity && TruePowerComboHelper.getCollideAction(livingEntity, state).equals(CollideAction.SOLID)) {
                        cir.setReturnValue(true);
                    }
                });
            }
        }
    }
}
