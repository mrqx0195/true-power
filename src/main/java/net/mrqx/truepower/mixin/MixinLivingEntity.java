package net.mrqx.truepower.mixin;

import mods.flammpfeil.slashblade.item.ItemSlashBlade;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.mrqx.truepower.util.CollideAction;
import net.mrqx.truepower.util.TruePowerComboHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class MixinLivingEntity extends Entity {
    public MixinLivingEntity(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }
    
    @Shadow
    public abstract ItemStack getMainHandItem();
    
    @Inject(method = "isPushable()Z", at = @At("HEAD"), cancellable = true)
    private void injectIsPushable(CallbackInfoReturnable<Boolean> cir) {
        ItemStack itemStack = this.getMainHandItem();
        itemStack.getCapability(ItemSlashBlade.BLADESTATE).ifPresent(state -> {
            if (!itemStack.isEmpty() && TruePowerComboHelper.getCollideAction(this, state).equals(CollideAction.IGNORE)) {
                cir.setReturnValue(false);
            }
        });
    }
}
