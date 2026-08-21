package net.mrqx.truepower.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import mods.flammpfeil.slashblade.entity.EntityAbstractSummonedSword;
import mods.flammpfeil.slashblade.entity.IShootable;
import mods.flammpfeil.slashblade.entity.Projectile;
import mods.flammpfeil.slashblade.util.AttackHelper;
import mods.flammpfeil.slashblade.util.AttackManager;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageSources;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.mrqx.truepower.config.TruePowerCommonConfig;
import net.mrqx.truepower.util.TruePowerAttackManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.concurrent.atomic.AtomicBoolean;

@Mixin(EntityAbstractSummonedSword.class)
public abstract class MixinEntityAbstractSummonedSword extends Projectile implements IShootable {
    protected MixinEntityAbstractSummonedSword(EntityType<? extends net.minecraft.world.entity.projectile.Projectile> p_37248_, Level p_37249_) {
        super(p_37248_, p_37249_);
    }
    
    @WrapOperation(method = "onHitEntity(Lnet/minecraft/world/phys/EntityHitResult;)V",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/entity/Entity;hurt(Lnet/minecraft/world/damagesource/DamageSource;F)Z"
        ),
        remap = false)
    private boolean wrapOperationOnHitEntity(Entity instance, DamageSource source, float amount, Operation<Boolean> original) {
        Entity shooter = this.getShooter();
        if (shooter instanceof LivingEntity livingEntity && TruePowerCommonConfig.MODIFY_SUMMONED_SWORD_DAMAGE.get()) {
            AtomicBoolean flag = new AtomicBoolean();
            AttackManager.doManagedAttack(target ->
                    flag.set(target.hurt(source, (float) AttackHelper.calculateTotalDamage(livingEntity, target,
                        (float) (amount * TruePowerCommonConfig.SUMMONED_SWORD_DAMAGE_MULTIPLIER.get()), false))),
                instance, true, true);
            return flag.get();
        }
        return original.call(instance, source, amount);
    }
    
    @WrapOperation(method = "onHitEntity(Lnet/minecraft/world/phys/EntityHitResult;)V",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/damagesource/DamageSources;indirectMagic(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/entity/Entity;)Lnet/minecraft/world/damagesource/DamageSource;"
        ),
        remap = false)
    private DamageSource wrapDamageSource(DamageSources instance, Entity causingEntity, Entity directEntity, Operation<DamageSource> original) {
        return TruePowerAttackManager.getSummonedSwordDamageSource(instance, causingEntity, directEntity);
    }
}
