package net.mrqx.truepower.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import mods.flammpfeil.slashblade.entity.EntitySlashEffect;
import mods.flammpfeil.slashblade.entity.IShootable;
import mods.flammpfeil.slashblade.entity.Projectile;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntitySlashEffect.class)
public abstract class MixinEntitySlashEffect extends Projectile implements IShootable {
    protected MixinEntitySlashEffect(EntityType<? extends net.minecraft.world.entity.projectile.Projectile> p_37248_, Level p_37249_) {
        super(p_37248_, p_37249_);
    }
    
    @Unique
    @SuppressWarnings("WrongEntityDataParameterClass")
    private static final EntityDataAccessor<Integer> TRUE_POWER$DATA_LIFETIME =
        SynchedEntityData.defineId(EntitySlashEffect.class, EntityDataSerializers.INT);
    
    @Inject(method = "defineSynchedData", at = @At("RETURN"))
    private void onDefineSynchedData(SynchedEntityData.Builder builder, CallbackInfo ci) {
        builder.define(TRUE_POWER$DATA_LIFETIME, 10);
    }
    
    @Inject(method = "getLifetime", at = @At("HEAD"), cancellable = true, remap = false)
    private void onGetLifetime(CallbackInfoReturnable<Integer> cir) {
        int val = entityData.get(TRUE_POWER$DATA_LIFETIME);
        cir.setReturnValue(Math.min(val, 1000));
        cir.cancel();
    }
    
    @Inject(method = "setLifetime", at = @At("HEAD"), cancellable = true, remap = false)
    private void onSetLifetime(int value, CallbackInfo ci) {
        entityData.set(TRUE_POWER$DATA_LIFETIME, value);
        ci.cancel();
    }
    
    @WrapOperation(
        method = "tick",
        at = @At(value = "FIELD", target = "Lmods/flammpfeil/slashblade/entity/EntitySlashEffect;lifetime:I", opcode = Opcodes.GETFIELD, remap = false)
    )
    private static int wrapLifetimeRead(EntitySlashEffect instance, Operation<Integer> original) {
        return instance.getLifetime();
    }
}
