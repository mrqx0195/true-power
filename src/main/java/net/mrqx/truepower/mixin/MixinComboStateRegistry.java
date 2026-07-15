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
    @WrapOperation(method = {"lambda$static$5", "lambda$static$26"}, at = @At(value = "INVOKE", target = "Lmods/flammpfeil/slashblade/util/AttackManager;doSlash(Lnet/minecraft/world/entity/LivingEntity;FZZD)Lmods/flammpfeil/slashblade/entity/EntitySlashEffect;", remap = false), remap = false)
    private static EntitySlashEffect modifyComboA1(LivingEntity playerIn, float roll, boolean mute, boolean critical, double comboRatio, Operation<EntitySlashEffect> original) {
        return original.call(playerIn, roll, mute, critical, 0.4);
    }
    
    @WrapOperation(method = {"lambda$static$13", "lambda$static$37", "lambda$static$38"}, at = @At(value = "INVOKE", target = "Lmods/flammpfeil/slashblade/util/AttackManager;doSlash(Lnet/minecraft/world/entity/LivingEntity;FZZD)Lmods/flammpfeil/slashblade/entity/EntitySlashEffect;", remap = false), remap = false)
    private static EntitySlashEffect modifyComboA2(LivingEntity playerIn, float roll, boolean mute, boolean critical, double comboRatio, Operation<EntitySlashEffect> original) {
        return original.call(playerIn, roll, mute, critical, 0.5);
    }
    
    @WrapOperation(method = "lambda$static$27", at = @At(value = "INVOKE", target = "Lmods/flammpfeil/slashblade/util/AttackManager;doSlash(Lnet/minecraft/world/entity/LivingEntity;FZZD)Lmods/flammpfeil/slashblade/entity/EntitySlashEffect;", remap = false), remap = false)
    private static EntitySlashEffect modifyComboA3(LivingEntity playerIn, float roll, boolean mute, boolean critical, double comboRatio, Operation<EntitySlashEffect> original) {
        return original.call(playerIn, roll, mute, critical, 0.7);
    }
    
    @WrapOperation(method = "/lambda\\$static\\$(43|44)/", at = @At(value = "INVOKE", target = "Lmods/flammpfeil/slashblade/util/AttackManager;doSlash(Lnet/minecraft/world/entity/LivingEntity;FZZD)Lmods/flammpfeil/slashblade/entity/EntitySlashEffect;", remap = false), remap = false)
    private static EntitySlashEffect modifyComboA4Ex(LivingEntity playerIn, float roll, boolean mute, boolean critical, double comboRatio, Operation<EntitySlashEffect> original) {
        return original.call(playerIn, roll, mute, critical, 0.8);
    }
    
    @WrapOperation(method = "lambda$static$53", at = @At(value = "INVOKE", target = "Lmods/flammpfeil/slashblade/util/AttackManager;doSlash(Lnet/minecraft/world/entity/LivingEntity;FZZD)Lmods/flammpfeil/slashblade/entity/EntitySlashEffect;", remap = false), remap = false)
    private static EntitySlashEffect modifyComboA5(LivingEntity playerIn, float roll, boolean mute, boolean critical, double comboRatio, Operation<EntitySlashEffect> original) {
        return original.call(playerIn, roll, mute, critical, 2.64);
    }
    
    @WrapOperation(method = "lambda$static$19", at = @At(value = "INVOKE", target = "Lmods/flammpfeil/slashblade/util/AttackManager;doSlash(Lnet/minecraft/world/entity/LivingEntity;FZZD)Lmods/flammpfeil/slashblade/entity/EntitySlashEffect;", remap = false), remap = false)
    private static EntitySlashEffect modifyComboC1(LivingEntity playerIn, float roll, boolean mute, boolean critical, double comboRatio, Operation<EntitySlashEffect> original) {
        return original.call(playerIn, roll, mute, critical, 1.6);
    }
    
    @WrapOperation(method = "lambda$static$20", at = @At(value = "INVOKE", target = "Lmods/flammpfeil/slashblade/util/AttackManager;doSlash(Lnet/minecraft/world/entity/LivingEntity;FZZD)Lmods/flammpfeil/slashblade/entity/EntitySlashEffect;", remap = false), remap = false)
    private static EntitySlashEffect modifyComboC2(LivingEntity playerIn, float roll, boolean mute, boolean critical, double comboRatio, Operation<EntitySlashEffect> original) {
        return original.call(playerIn, roll, mute, critical, 1.7);
    }
    
    @WrapOperation(method = "/lambda\\$static\\$(6[0-7]|7[129]|8[0-589]|9[0-47-9]|10[0-36-9]|11[0-25-9]|12[014-9]|13[0-39]|140)/", at = @At(value = "INVOKE", target = "Lmods/flammpfeil/slashblade/util/AttackManager;doSlash(Lnet/minecraft/world/entity/LivingEntity;FLnet/minecraft/world/phys/Vec3;ZZD)Lmods/flammpfeil/slashblade/entity/EntitySlashEffect;", remap = false), remap = false)
    private static EntitySlashEffect modifyComboB1(LivingEntity playerIn, float roll, Vec3 centerOffset, boolean mute, boolean critical, double comboRatio, Operation<EntitySlashEffect> original) {
        return original.call(playerIn, roll, centerOffset, mute, critical, 0.15);
    }
    
    @WrapOperation(method = {"/lambda\\$static\\$(71|72)/", "/lambda\\$static\\$(132|133)/", "/lambda\\$static\\$(139|140)/"}, at = @At(value = "INVOKE", target = "Lmods/flammpfeil/slashblade/util/AttackManager;doSlash(Lnet/minecraft/world/entity/LivingEntity;FLnet/minecraft/world/phys/Vec3;ZZD)Lmods/flammpfeil/slashblade/entity/EntitySlashEffect;", remap = false), remap = false)
    private static EntitySlashEffect modifyComboB2(LivingEntity playerIn, float roll, Vec3 centerOffset, boolean mute, boolean critical, double comboRatio, Operation<EntitySlashEffect> original) {
        return original.call(playerIn, roll, centerOffset, mute, critical, 0.8);
    }
    
    @WrapOperation(method = "lambda$static$147", at = @At(value = "INVOKE", target = "Lmods/flammpfeil/slashblade/util/AttackManager;doSlash(Lnet/minecraft/world/entity/LivingEntity;FZZD)Lmods/flammpfeil/slashblade/entity/EntitySlashEffect;", remap = false), remap = false)
    private static EntitySlashEffect modifyAerialRaveA1(LivingEntity playerIn, float roll, boolean mute, boolean critical, double comboRatio, Operation<EntitySlashEffect> original) {
        return original.call(playerIn, roll, mute, critical, 0.5);
    }
    
    @WrapOperation(method = "lambda$static$152", at = @At(value = "INVOKE", target = "Lmods/flammpfeil/slashblade/util/AttackManager;doSlash(Lnet/minecraft/world/entity/LivingEntity;FZZD)Lmods/flammpfeil/slashblade/entity/EntitySlashEffect;", remap = false), remap = false)
    private static EntitySlashEffect modifyAerialRaveA2(LivingEntity playerIn, float roll, boolean mute, boolean critical, double comboRatio, Operation<EntitySlashEffect> original) {
        return original.call(playerIn, roll, mute, critical, 0.6);
    }
    
    @WrapOperation(method = "/lambda\\$static\\$(159|160)/", at = @At(value = "INVOKE", target = "Lmods/flammpfeil/slashblade/util/AttackManager;doSlash(Lnet/minecraft/world/entity/LivingEntity;FLnet/minecraft/world/phys/Vec3;ZZDLmods/flammpfeil/slashblade/util/KnockBacks;)Lmods/flammpfeil/slashblade/entity/EntitySlashEffect;", remap = false), remap = false)
    private static EntitySlashEffect modifyAerialRaveA3(LivingEntity playerIn, float roll, Vec3 centerOffset, boolean mute, boolean critical, double comboRatio, KnockBacks knockback, Operation<EntitySlashEffect> original) {
        return original.call(playerIn, roll, centerOffset, mute, critical, 0.6, knockback);
    }
    
    @WrapOperation(method = "/lambda\\$static\\$(167|168)/", at = @At(value = "INVOKE", target = "Lmods/flammpfeil/slashblade/util/AttackManager;doSlash(Lnet/minecraft/world/entity/LivingEntity;FLnet/minecraft/world/phys/Vec3;ZZDLmods/flammpfeil/slashblade/util/KnockBacks;)Lmods/flammpfeil/slashblade/entity/EntitySlashEffect;", remap = false), remap = false)
    private static EntitySlashEffect modifyAerialRaveB3(LivingEntity playerIn, float roll, Vec3 centerOffset, boolean mute, boolean critical, double comboRatio, KnockBacks knockback, Operation<EntitySlashEffect> original) {
        return original.call(playerIn, roll, centerOffset, mute, critical, 0.4, knockback);
    }
    
    @WrapOperation(method = "/lambda\\$static\\$(173|174)/", at = @At(value = "INVOKE", target = "Lmods/flammpfeil/slashblade/util/AttackManager;doSlash(Lnet/minecraft/world/entity/LivingEntity;FLnet/minecraft/world/phys/Vec3;ZZDLmods/flammpfeil/slashblade/util/KnockBacks;)Lmods/flammpfeil/slashblade/entity/EntitySlashEffect;", remap = false), remap = false)
    private static EntitySlashEffect modifyAerialRaveB4(LivingEntity playerIn, float roll, Vec3 centerOffset, boolean mute, boolean critical, double comboRatio, KnockBacks knockback, Operation<EntitySlashEffect> original) {
        return original.call(playerIn, roll, centerOffset, mute, critical, 0.5, knockback);
    }
    
    @WrapOperation(method = "<clinit>", at = @At(value = "INVOKE", target = "Lmods/flammpfeil/slashblade/registry/combo/ComboState$Builder;addTickAction(Ljava/util/function/Consumer;)Lmods/flammpfeil/slashblade/registry/combo/ComboState$Builder;", ordinal = 45, remap = false), remap = false)
    private static ComboState.Builder wrapOperationUpperSlashTickAction(ComboState.Builder instance, Consumer<LivingEntity> tickAction, Operation<ComboState.Builder> original) {
        return original.call(instance, (Consumer<LivingEntity>) livingEntity -> {
            if (AttackManager.isPowered(livingEntity)) {
                TruePowerComboHelper.POWERED_UPPER_SLASH.accept(livingEntity);
            } else {
                TruePowerComboHelper.UPPER_SLASH.accept(livingEntity);
            }
        });
    }
    
    @WrapOperation(method = "/lambda\\$static\\$(197|201)/", at = @At(value = "INVOKE", target = "Lmods/flammpfeil/slashblade/util/AttackManager;areaAttack(Lnet/minecraft/world/entity/LivingEntity;Ljava/util/function/Consumer;FZZZ)Ljava/util/List;", remap = false), remap = false)
    private static List<Entity> modifyAerialCleaveAreaAttack(LivingEntity playerIn, Consumer<LivingEntity> beforeHit, float comboRatio, boolean forceHit, boolean resetHit, boolean mute, Operation<List<Entity>> original) {
        return original.call(playerIn, beforeHit, 0.5f, forceHit, resetHit, mute);
    }
    
    @WrapOperation(method = "/lambda\\$static\\$(197|201|205)/", at = @At(value = "INVOKE", target = "Lmods/flammpfeil/slashblade/util/AttackManager;doSlash(Lnet/minecraft/world/entity/LivingEntity;FLnet/minecraft/world/phys/Vec3;ZZDLmods/flammpfeil/slashblade/util/KnockBacks;)Lmods/flammpfeil/slashblade/entity/EntitySlashEffect;", remap = false), remap = false)
    private static EntitySlashEffect modifyAerialCleaveDoSlash(LivingEntity playerIn, float roll, Vec3 centerOffset, boolean mute, boolean critical, double comboRatio, KnockBacks knockback, Operation<EntitySlashEffect> original) {
        return original.call(playerIn, roll, centerOffset, mute, critical, 2.0, knockback);
    }
    
    @WrapOperation(method = "lambda$static$211", at = @At(value = "INVOKE", target = "Lmods/flammpfeil/slashblade/util/AttackManager;areaAttack(Lnet/minecraft/world/entity/LivingEntity;Ljava/util/function/Consumer;FZZZ)Ljava/util/List;", remap = false), remap = false)
    private static List<Entity> wrapOperationRapidSlashAreaAttack(LivingEntity playerIn, Consumer<LivingEntity> beforeHit, float comboRatio, boolean forceHit, boolean resetHit, boolean mute, Operation<List<Entity>> original) {
        InputStream inputStream = InputStream.getOrCreateInputStream(playerIn);
        if (playerIn.getTicksUsingItem() < 5 || inputStream.checkInputWithTime(InputCommand.R_DOWN, InputStream.InputType.START, 3)) {
            return new ArrayList<>();
        }
        return original.call(playerIn, beforeHit, 0.15F, forceHit, resetHit, mute);
    }
    
    @WrapOperation(method = "lambda$static$214", at = @At(value = "INVOKE", target = "Lmods/flammpfeil/slashblade/util/AttackManager;doSlash(Lnet/minecraft/world/entity/LivingEntity;FLnet/minecraft/world/phys/Vec3;ZZD)Lmods/flammpfeil/slashblade/entity/EntitySlashEffect;", remap = false), remap = false)
    private static EntitySlashEffect wrapOperationRapidSlashAreaAttack(LivingEntity playerIn, float roll, Vec3 centerOffset, boolean mute, boolean critical, double comboRatio, Operation<EntitySlashEffect> original) {
        if (roll == -30) {
            return original.call(playerIn, roll, centerOffset, mute, critical, 0.4);
        }
        return original.call(playerIn, roll, centerOffset, mute, critical, 0.15);
    }
    
    @WrapOperation(method = "lambda$static$213", at = @At(value = "INVOKE", target = "Lmods/flammpfeil/slashblade/util/AttackManager;areaAttack(Lnet/minecraft/world/entity/LivingEntity;Ljava/util/function/Consumer;FZZZ)Ljava/util/List;", remap = false), remap = false)
    private static List<Entity> modifyRapidSlashAreaAttack(LivingEntity playerIn, Consumer<LivingEntity> beforeHit, float comboRatio, boolean forceHit, boolean resetHit, boolean mute, Operation<List<Entity>> original) {
        return original.call(playerIn, beforeHit, 0.4f, forceHit, resetHit, mute);
    }
    
    @WrapOperation(method = "lambda$static$223", at = @At(value = "INVOKE", target = "Lmods/flammpfeil/slashblade/util/AttackManager;doSlash(Lnet/minecraft/world/entity/LivingEntity;FLnet/minecraft/world/phys/Vec3;ZZDLmods/flammpfeil/slashblade/util/KnockBacks;)Lmods/flammpfeil/slashblade/entity/EntitySlashEffect;", remap = false), remap = false)
    private static EntitySlashEffect modifyRisingStarDoSlash1(LivingEntity playerIn, float roll, Vec3 centerOffset, boolean mute, boolean critical, double comboRatio, KnockBacks knockback, Operation<EntitySlashEffect> original) {
        return original.call(playerIn, roll, centerOffset, mute, critical, 0.8, knockback);
    }
    
    @WrapOperation(method = "lambda$static$224", at = @At(value = "INVOKE", target = "Lmods/flammpfeil/slashblade/util/AttackManager;doSlash(Lnet/minecraft/world/entity/LivingEntity;FLnet/minecraft/world/phys/Vec3;ZZDLmods/flammpfeil/slashblade/util/KnockBacks;)Lmods/flammpfeil/slashblade/entity/EntitySlashEffect;", remap = false), remap = false)
    private static EntitySlashEffect modifyRisingStarDoSlash2(LivingEntity playerIn, float roll, Vec3 centerOffset, boolean mute, boolean critical, double comboRatio, KnockBacks knockback, Operation<EntitySlashEffect> original) {
        return original.call(playerIn, roll, centerOffset, mute, critical, 0.8, knockback);
    }
    
    @WrapOperation(method = "lambda$static$230", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;moveRelative(FLnet/minecraft/world/phys/Vec3;)V"), remap = false)
    private static void wrapOperationJudgementCutMoveRelative(LivingEntity instance, float v, Vec3 vec3, Operation<Void> original) {
        original.call(instance, v * 3, vec3);
    }
}
