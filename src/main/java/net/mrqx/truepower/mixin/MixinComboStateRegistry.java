package net.mrqx.truepower.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import mods.flammpfeil.slashblade.entity.EntitySlashEffect;
import mods.flammpfeil.slashblade.registry.ComboStateRegistry;
import mods.flammpfeil.slashblade.registry.combo.ComboState;
import mods.flammpfeil.slashblade.util.AttackManager;
import mods.flammpfeil.slashblade.util.InputCommand;
import mods.flammpfeil.slashblade.util.KnockBacks;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import net.mrqx.sbr_core.utils.InputStream;
import net.mrqx.truepower.util.TruePowerComboHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@SuppressWarnings("SameReturnValue")
@Mixin(ComboStateRegistry.class)
public abstract class MixinComboStateRegistry {
    @SuppressWarnings({"InvalidInjectorMethodSignature", "MixinAnnotationTarget"})
    @WrapOperation(method = "/lambda\\$static\\$(5|27)/", at = @At(value = "INVOKE", target = "Lmods/flammpfeil/slashblade/util/AttackManager;doSlash(Lnet/minecraft/world/entity/LivingEntity;FZZD)Lmods/flammpfeil/slashblade/entity/EntitySlashEffect;", remap = false), remap = false)
    private static EntitySlashEffect modifyComboA1(LivingEntity playerIn, float roll, boolean mute, boolean critical, double comboRatio, Operation<EntitySlashEffect> original) {
        return original.call(playerIn, roll, mute, critical, 0.4);
    }
    
    @SuppressWarnings({"InvalidInjectorMethodSignature", "MixinAnnotationTarget"})
    @WrapOperation(method = "/lambda\\$static\\$(13|3[78])/", at = @At(value = "INVOKE", target = "Lmods/flammpfeil/slashblade/util/AttackManager;doSlash(Lnet/minecraft/world/entity/LivingEntity;FZZD)Lmods/flammpfeil/slashblade/entity/EntitySlashEffect;", remap = false), remap = false)
    private static EntitySlashEffect modifyComboA2(LivingEntity playerIn, float roll, boolean mute, boolean critical, double comboRatio, Operation<EntitySlashEffect> original) {
        return original.call(playerIn, roll, mute, critical, 0.5);
    }
    
    @WrapOperation(method = "lambda$static$28", at = @At(value = "INVOKE", target = "Lmods/flammpfeil/slashblade/util/AttackManager;doSlash(Lnet/minecraft/world/entity/LivingEntity;FZZD)Lmods/flammpfeil/slashblade/entity/EntitySlashEffect;", remap = false), remap = false)
    private static EntitySlashEffect modifyComboA3(LivingEntity playerIn, float roll, boolean mute, boolean critical, double comboRatio, Operation<EntitySlashEffect> original) {
        return original.call(playerIn, roll, mute, critical, 0.7);
    }
    
    @WrapOperation(method = "/lambda\\$static\\$(50|51)/", at = @At(value = "INVOKE", target = "Lmods/flammpfeil/slashblade/util/AttackManager;doSlash(Lnet/minecraft/world/entity/LivingEntity;FZZD)Lmods/flammpfeil/slashblade/entity/EntitySlashEffect;", remap = false), remap = false)
    private static EntitySlashEffect modifyComboA4Ex(LivingEntity playerIn, float roll, boolean mute, boolean critical, double comboRatio, Operation<EntitySlashEffect> original) {
        return original.call(playerIn, roll, mute, critical, 0.8);
    }
    
    @WrapOperation(method = "/lambda\\$static\\$(58|59|60)/", at = @At(value = "INVOKE", target = "Lmods/flammpfeil/slashblade/util/AttackManager;doSlash(Lnet/minecraft/world/entity/LivingEntity;FZZD)Lmods/flammpfeil/slashblade/entity/EntitySlashEffect;", remap = false), remap = false)
    private static EntitySlashEffect modifyComboA5(LivingEntity playerIn, float roll, boolean mute, boolean critical, double comboRatio, Operation<EntitySlashEffect> original) {
        return original.call(playerIn, roll, mute, critical, 2.64);
    }
    
    @WrapOperation(method = "lambda$static$20", at = @At(value = "INVOKE", target = "Lmods/flammpfeil/slashblade/util/AttackManager;doSlash(Lnet/minecraft/world/entity/LivingEntity;FZZD)Lmods/flammpfeil/slashblade/entity/EntitySlashEffect;", remap = false), remap = false)
    private static EntitySlashEffect modifyComboC1(LivingEntity playerIn, float roll, boolean mute, boolean critical, double comboRatio, Operation<EntitySlashEffect> original) {
        return original.call(playerIn, roll, mute, critical, 1.6);
    }
    
    @WrapOperation(method = "lambda$static$21", at = @At(value = "INVOKE", target = "Lmods/flammpfeil/slashblade/util/AttackManager;doSlash(Lnet/minecraft/world/entity/LivingEntity;FZZD)Lmods/flammpfeil/slashblade/entity/EntitySlashEffect;", remap = false), remap = false)
    private static EntitySlashEffect modifyComboC2(LivingEntity playerIn, float roll, boolean mute, boolean critical, double comboRatio, Operation<EntitySlashEffect> original) {
        return original.call(playerIn, roll, mute, critical, 1.7);
    }
    
    @WrapOperation(method = "/lambda\\$static\\$(7[2-9]|80|9[89]|10[0-4]|10[7-9]|11[0-3]|11[6-9]|12[0-2]|12[5-9]|13[0-1]|13[4-9]|140|14[3-9]|150)/", at = @At(value = "INVOKE", target = "Lmods/flammpfeil/slashblade/util/AttackManager;doSlash(Lnet/minecraft/world/entity/LivingEntity;FLnet/minecraft/world/phys/Vec3;ZZD)Lmods/flammpfeil/slashblade/entity/EntitySlashEffect;", remap = false), remap = false)
    private static EntitySlashEffect modifyComboB1(LivingEntity playerIn, float roll, Vec3 centerOffset, boolean mute, boolean critical, double comboRatio, Operation<EntitySlashEffect> original) {
        return original.call(playerIn, roll, centerOffset, mute, critical, 0.15);
    }
    
    @WrapOperation(method = "/lambda\\$static\\$(84|85|15[12]|16[45])/", at = @At(value = "INVOKE", target = "Lmods/flammpfeil/slashblade/util/AttackManager;doSlash(Lnet/minecraft/world/entity/LivingEntity;FLnet/minecraft/world/phys/Vec3;ZZD)Lmods/flammpfeil/slashblade/entity/EntitySlashEffect;", remap = false), remap = false)
    private static EntitySlashEffect modifyComboB2(LivingEntity playerIn, float roll, Vec3 centerOffset, boolean mute, boolean critical, double comboRatio, Operation<EntitySlashEffect> original) {
        return original.call(playerIn, roll, centerOffset, mute, critical, 0.8);
    }
    
    @WrapOperation(method = "lambda$static$178", at = @At(value = "INVOKE", target = "Lmods/flammpfeil/slashblade/util/AttackManager;doSlash(Lnet/minecraft/world/entity/LivingEntity;FZZD)Lmods/flammpfeil/slashblade/entity/EntitySlashEffect;", remap = false), remap = false)
    private static EntitySlashEffect modifyAerialRaveA1(LivingEntity playerIn, float roll, boolean mute, boolean critical, double comboRatio, Operation<EntitySlashEffect> original) {
        return original.call(playerIn, roll, mute, critical, 0.5);
    }
    
    @WrapOperation(method = "lambda$static$184", at = @At(value = "INVOKE", target = "Lmods/flammpfeil/slashblade/util/AttackManager;doSlash(Lnet/minecraft/world/entity/LivingEntity;FZZD)Lmods/flammpfeil/slashblade/entity/EntitySlashEffect;", remap = false), remap = false)
    private static EntitySlashEffect modifyAerialRaveA2(LivingEntity playerIn, float roll, boolean mute, boolean critical, double comboRatio, Operation<EntitySlashEffect> original) {
        return original.call(playerIn, roll, mute, critical, 0.6);
    }
    
    @WrapOperation(method = "/lambda\\$static\\$(191|192)/", at = @At(value = "INVOKE", target = "Lmods/flammpfeil/slashblade/util/AttackManager;doSlash(Lnet/minecraft/world/entity/LivingEntity;FLnet/minecraft/world/phys/Vec3;ZZDLmods/flammpfeil/slashblade/util/KnockBacks;)Lmods/flammpfeil/slashblade/entity/EntitySlashEffect;", remap = false), remap = false)
    private static EntitySlashEffect modifyAerialRaveA3(LivingEntity playerIn, float roll, Vec3 centerOffset, boolean mute, boolean critical, double comboRatio, KnockBacks knockback, Operation<EntitySlashEffect> original) {
        return original.call(playerIn, roll, centerOffset, mute, critical, 0.6, knockback);
    }
    
    @WrapOperation(method = "/lambda\\$static\\$(200|201)/", at = @At(value = "INVOKE", target = "Lmods/flammpfeil/slashblade/util/AttackManager;doSlash(Lnet/minecraft/world/entity/LivingEntity;FLnet/minecraft/world/phys/Vec3;ZZDLmods/flammpfeil/slashblade/util/KnockBacks;)Lmods/flammpfeil/slashblade/entity/EntitySlashEffect;", remap = false), remap = false)
    private static EntitySlashEffect modifyAerialRaveB3(LivingEntity playerIn, float roll, Vec3 centerOffset, boolean mute, boolean critical, double comboRatio, KnockBacks knockback, Operation<EntitySlashEffect> original) {
        return original.call(playerIn, roll, centerOffset, mute, critical, 0.4, knockback);
    }
    
    @WrapOperation(method = "/lambda\\$static\\$(214|215)/", at = @At(value = "INVOKE", target = "Lmods/flammpfeil/slashblade/util/AttackManager;doSlash(Lnet/minecraft/world/entity/LivingEntity;FLnet/minecraft/world/phys/Vec3;ZZDLmods/flammpfeil/slashblade/util/KnockBacks;)Lmods/flammpfeil/slashblade/entity/EntitySlashEffect;", remap = false), remap = false)
    private static EntitySlashEffect modifyAerialRaveB4(LivingEntity playerIn, float roll, Vec3 centerOffset, boolean mute, boolean critical, double comboRatio, KnockBacks knockback, Operation<EntitySlashEffect> original) {
        return original.call(playerIn, roll, centerOffset, mute, critical, 0.5, knockback);
    }
    
    @WrapOperation(method = "<clinit>", at = @At(value = "INVOKE", target = "Lmods/flammpfeil/slashblade/registry/combo/ComboState$Builder;addTickAction(Ljava/util/function/Consumer;)Lmods/flammpfeil/slashblade/registry/combo/ComboState$Builder;", ordinal = 54, remap = false), remap = false)
    private static ComboState.Builder wrapOperationUpperSlashTickAction(ComboState.Builder instance, Consumer<LivingEntity> tickAction, Operation<ComboState.Builder> original) {
        return original.call(instance, (Consumer<LivingEntity>) livingEntity -> {
            if (AttackManager.isPowered(livingEntity)) {
                TruePowerComboHelper.POWERED_UPPER_SLASH.accept(livingEntity);
            } else {
                TruePowerComboHelper.UPPER_SLASH.accept(livingEntity);
            }
        });
    }
    
    @WrapOperation(method = "/lambda\\$static\\$(250|259)/", at = @At(value = "INVOKE", target = "Lmods/flammpfeil/slashblade/util/AttackManager;areaAttack(Lnet/minecraft/world/entity/LivingEntity;Ljava/util/function/Consumer;FZZZ)Ljava/util/List;", remap = false), remap = false)
    private static List<Entity> modifyAerialCleaveAreaAttack(LivingEntity playerIn, Consumer<LivingEntity> beforeHit, float comboRatio, boolean forceHit, boolean resetHit, boolean mute, Operation<List<Entity>> original) {
        return original.call(playerIn, beforeHit, 0.5f, forceHit, resetHit, mute);
    }
    
    @WrapOperation(method = "/lambda\\$static\\$(250|259|263)/", at = @At(value = "INVOKE", target = "Lmods/flammpfeil/slashblade/util/AttackManager;doSlash(Lnet/minecraft/world/entity/LivingEntity;FLnet/minecraft/world/phys/Vec3;ZZDLmods/flammpfeil/slashblade/util/KnockBacks;)Lmods/flammpfeil/slashblade/entity/EntitySlashEffect;", remap = false), remap = false)
    private static EntitySlashEffect modifyAerialCleaveDoSlash(LivingEntity playerIn, float roll, Vec3 centerOffset, boolean mute, boolean critical, double comboRatio, KnockBacks knockback, Operation<EntitySlashEffect> original) {
        return original.call(playerIn, roll, centerOffset, mute, critical, 2.0, knockback);
    }
    
    @WrapOperation(method = "lambda$static$272", at = @At(value = "INVOKE", target = "Lmods/flammpfeil/slashblade/util/AttackManager;areaAttack(Lnet/minecraft/world/entity/LivingEntity;Ljava/util/function/Consumer;FZZZ)Ljava/util/List;", remap = false), remap = false)
    private static List<Entity> wrapOperationRapidSlashAreaAttack(LivingEntity playerIn, Consumer<LivingEntity> beforeHit, float comboRatio, boolean forceHit, boolean resetHit, boolean mute, Operation<List<Entity>> original) {
        InputStream inputStream = InputStream.getOrCreateInputStream(playerIn);
        if (playerIn.getTicksUsingItem() < 5 || inputStream.checkInputWithTime(InputCommand.R_DOWN, InputStream.InputType.START, 3)) {
            return new ArrayList<>();
        }
        return original.call(playerIn, beforeHit, 0.15F, forceHit, resetHit, mute);
    }
    
    @WrapOperation(method = "lambda$static$275", at = @At(value = "INVOKE", target = "Lmods/flammpfeil/slashblade/util/AttackManager;doSlash(Lnet/minecraft/world/entity/LivingEntity;FLnet/minecraft/world/phys/Vec3;ZZD)Lmods/flammpfeil/slashblade/entity/EntitySlashEffect;", remap = false), remap = false)
    private static EntitySlashEffect wrapOperationRapidSlashAreaAttack(LivingEntity playerIn, float roll, Vec3 centerOffset, boolean mute, boolean critical, double comboRatio, Operation<EntitySlashEffect> original) {
        if (roll == -30) {
            return original.call(playerIn, roll, centerOffset, mute, critical, 0.4);
        }
        return original.call(playerIn, roll, centerOffset, mute, critical, 0.15);
    }
    
    @WrapOperation(method = "lambda$static$274", at = @At(value = "INVOKE", target = "Lmods/flammpfeil/slashblade/util/AttackManager;areaAttack(Lnet/minecraft/world/entity/LivingEntity;Ljava/util/function/Consumer;FZZZ)Ljava/util/List;", remap = false), remap = false)
    private static List<Entity> modifyRapidSlashAreaAttack(LivingEntity playerIn, Consumer<LivingEntity> beforeHit, float comboRatio, boolean forceHit, boolean resetHit, boolean mute, Operation<List<Entity>> original) {
        return original.call(playerIn, beforeHit, 0.4f, forceHit, resetHit, mute);
    }
    
    @WrapOperation(method = "lambda$static$284", at = @At(value = "INVOKE", target = "Lmods/flammpfeil/slashblade/util/AttackManager;doSlash(Lnet/minecraft/world/entity/LivingEntity;FLnet/minecraft/world/phys/Vec3;ZZDLmods/flammpfeil/slashblade/util/KnockBacks;)Lmods/flammpfeil/slashblade/entity/EntitySlashEffect;", remap = false), remap = false)
    private static EntitySlashEffect modifyRisingStarDoSlash1(LivingEntity playerIn, float roll, Vec3 centerOffset, boolean mute, boolean critical, double comboRatio, KnockBacks knockback, Operation<EntitySlashEffect> original) {
        return original.call(playerIn, roll, centerOffset, mute, critical, 0.8, knockback);
    }
    
    @WrapOperation(method = "lambda$static$285", at = @At(value = "INVOKE", target = "Lmods/flammpfeil/slashblade/util/AttackManager;doSlash(Lnet/minecraft/world/entity/LivingEntity;FLnet/minecraft/world/phys/Vec3;ZZDLmods/flammpfeil/slashblade/util/KnockBacks;)Lmods/flammpfeil/slashblade/entity/EntitySlashEffect;", remap = false), remap = false)
    private static EntitySlashEffect modifyRisingStarDoSlash2(LivingEntity playerIn, float roll, Vec3 centerOffset, boolean mute, boolean critical, double comboRatio, KnockBacks knockback, Operation<EntitySlashEffect> original) {
        return original.call(playerIn, roll, centerOffset, mute, critical, 0.8, knockback);
    }
    
    @WrapOperation(method = "lambda$static$302", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;moveRelative(FLnet/minecraft/world/phys/Vec3;)V"), remap = false)
    private static void wrapOperationJudgementCutMoveRelative(LivingEntity instance, float v, Vec3 vec3, Operation<Void> original) {
        original.call(instance, v * 3, vec3);
    }
}
