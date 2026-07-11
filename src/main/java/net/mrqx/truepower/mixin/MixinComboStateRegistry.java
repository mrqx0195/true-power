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
    private static EntitySlashEffect wrapOperationComboA1(LivingEntity playerIn, float roll, boolean mute, boolean critical, double comboRatio, Operation<EntitySlashEffect> original) {
        return original.call(playerIn, roll, mute, critical, 0.4);
    }
    
    @WrapOperation(method = {"lambda$static$13", "lambda$static$36", "lambda$static$37"}, at = @At(value = "INVOKE", target = "Lmods/flammpfeil/slashblade/util/AttackManager;doSlash(Lnet/minecraft/world/entity/LivingEntity;FZZD)Lmods/flammpfeil/slashblade/entity/EntitySlashEffect;", remap = false), remap = false)
    private static EntitySlashEffect wrapOperationComboA2(LivingEntity playerIn, float roll, boolean mute, boolean critical, double comboRatio, Operation<EntitySlashEffect> original) {
        return original.call(playerIn, roll, mute, critical, 0.5);
    }
    
    @WrapOperation(method = "lambda$static$27", at = @At(value = "INVOKE", target = "Lmods/flammpfeil/slashblade/util/AttackManager;doSlash(Lnet/minecraft/world/entity/LivingEntity;FZZD)Lmods/flammpfeil/slashblade/entity/EntitySlashEffect;", remap = false), remap = false)
    private static EntitySlashEffect wrapOperationComboA3(LivingEntity playerIn, float roll, boolean mute, boolean critical, double comboRatio, Operation<EntitySlashEffect> original) {
        return original.call(playerIn, roll, mute, critical, 0.7);
    }
    
    @WrapOperation(method = {"lambda$static$48", "lambda$static$49"}, at = @At(value = "INVOKE", target = "Lmods/flammpfeil/slashblade/util/AttackManager;doSlash(Lnet/minecraft/world/entity/LivingEntity;FZZD)Lmods/flammpfeil/slashblade/entity/EntitySlashEffect;", remap = false), remap = false)
    private static EntitySlashEffect wrapOperationComboA4Ex(LivingEntity playerIn, float roll, boolean mute, boolean critical, double comboRatio, Operation<EntitySlashEffect> original) {
        return original.call(playerIn, roll, mute, critical, 0.8);
    }
    
    @WrapOperation(method = {"lambda$static$56", "lambda$static$57", "lambda$static$58"}, at = @At(value = "INVOKE", target = "Lmods/flammpfeil/slashblade/util/AttackManager;doSlash(Lnet/minecraft/world/entity/LivingEntity;FZZD)Lmods/flammpfeil/slashblade/entity/EntitySlashEffect;", remap = false), remap = false)
    private static EntitySlashEffect wrapOperationComboA5(LivingEntity playerIn, float roll, boolean mute, boolean critical, double comboRatio, Operation<EntitySlashEffect> original) {
        return original.call(playerIn, roll, mute, critical, 2.64);
    }
    
    @WrapOperation(method = "lambda$static$19", at = @At(value = "INVOKE", target = "Lmods/flammpfeil/slashblade/util/AttackManager;doSlash(Lnet/minecraft/world/entity/LivingEntity;FZZD)Lmods/flammpfeil/slashblade/entity/EntitySlashEffect;", remap = false), remap = false)
    private static EntitySlashEffect wrapOperationComboC1(LivingEntity playerIn, float roll, boolean mute, boolean critical, double comboRatio, Operation<EntitySlashEffect> original) {
        return original.call(playerIn, roll, mute, critical, 1.6);
    }
    
    @WrapOperation(method = "lambda$static$20", at = @At(value = "INVOKE", target = "Lmods/flammpfeil/slashblade/util/AttackManager;doSlash(Lnet/minecraft/world/entity/LivingEntity;FZZD)Lmods/flammpfeil/slashblade/entity/EntitySlashEffect;", remap = false), remap = false)
    private static EntitySlashEffect wrapOperationComboC2(LivingEntity playerIn, float roll, boolean mute, boolean critical, double comboRatio, Operation<EntitySlashEffect> original) {
        return original.call(playerIn, roll, mute, critical, 1.7);
    }
    
    @WrapOperation(method = {
        "lambda$static$70", "lambda$static$71", "lambda$static$72", "lambda$static$73", "lambda$static$74", "lambda$static$75", "lambda$static$76", "lambda$static$77",
        "lambda$static$94", "lambda$static$95", "lambda$static$96", "lambda$static$97", "lambda$static$98", "lambda$static$99", "lambda$static$100",
        "lambda$static$103", "lambda$static$104", "lambda$static$105", "lambda$static$106", "lambda$static$107", "lambda$static$108", "lambda$static$109",
        "lambda$static$112", "lambda$static$113", "lambda$static$114", "lambda$static$115", "lambda$static$116", "lambda$static$117", "lambda$static$118",
        "lambda$static$121", "lambda$static$122", "lambda$static$123", "lambda$static$124", "lambda$static$125", "lambda$static$126", "lambda$static$127",
        "lambda$static$130", "lambda$static$131", "lambda$static$132", "lambda$static$133", "lambda$static$134", "lambda$static$135", "lambda$static$136",
        "lambda$static$139", "lambda$static$140", "lambda$static$141", "lambda$static$142", "lambda$static$143", "lambda$static$144", "lambda$static$145", "lambda$static$146"
    }, at = @At(value = "INVOKE", target = "Lmods/flammpfeil/slashblade/util/AttackManager;doSlash(Lnet/minecraft/world/entity/LivingEntity;FLnet/minecraft/world/phys/Vec3;ZZD)Lmods/flammpfeil/slashblade/entity/EntitySlashEffect;", remap = false), remap = false)
    private static EntitySlashEffect wrapOperationComboB1(LivingEntity playerIn, float roll, Vec3 centerOffset, boolean mute, boolean critical, double comboRatio, Operation<EntitySlashEffect> original) {
        return original.call(playerIn, roll, centerOffset, mute, critical, 0.15);
    }
    
    @WrapOperation(method = {
        "lambda$static$81", "lambda$static$82",
        "lambda$static$147", "lambda$static$148",
        "lambda$static$159", "lambda$static$160",
    }, at = @At(value = "INVOKE", target = "Lmods/flammpfeil/slashblade/util/AttackManager;doSlash(Lnet/minecraft/world/entity/LivingEntity;FLnet/minecraft/world/phys/Vec3;ZZD)Lmods/flammpfeil/slashblade/entity/EntitySlashEffect;", remap = false), remap = false)
    private static EntitySlashEffect wrapOperationComboB2(LivingEntity playerIn, float roll, Vec3 centerOffset, boolean mute, boolean critical, double comboRatio, Operation<EntitySlashEffect> original) {
        return original.call(playerIn, roll, centerOffset, mute, critical, 0.8);
    }
    
    @WrapOperation(method = "lambda$static$172", at = @At(value = "INVOKE", target = "Lmods/flammpfeil/slashblade/util/AttackManager;doSlash(Lnet/minecraft/world/entity/LivingEntity;FZZD)Lmods/flammpfeil/slashblade/entity/EntitySlashEffect;", remap = false), remap = false)
    private static EntitySlashEffect wrapOperationAerialRaveA1(LivingEntity playerIn, float roll, boolean mute, boolean critical, double comboRatio, Operation<EntitySlashEffect> original) {
        return original.call(playerIn, roll, mute, critical, 0.5);
    }
    
    @WrapOperation(method = "lambda$static$177", at = @At(value = "INVOKE", target = "Lmods/flammpfeil/slashblade/util/AttackManager;doSlash(Lnet/minecraft/world/entity/LivingEntity;FZZD)Lmods/flammpfeil/slashblade/entity/EntitySlashEffect;", remap = false), remap = false)
    private static EntitySlashEffect wrapOperationAerialRaveA2(LivingEntity playerIn, float roll, boolean mute, boolean critical, double comboRatio, Operation<EntitySlashEffect> original) {
        return original.call(playerIn, roll, mute, critical, 0.6);
    }
    
    @WrapOperation(method = {"lambda$static$184", "lambda$static$185"}, at = @At(value = "INVOKE", target = "Lmods/flammpfeil/slashblade/util/AttackManager;doSlash(Lnet/minecraft/world/entity/LivingEntity;FLnet/minecraft/world/phys/Vec3;ZZDLmods/flammpfeil/slashblade/util/KnockBacks;)Lmods/flammpfeil/slashblade/entity/EntitySlashEffect;", remap = false), remap = false)
    private static EntitySlashEffect wrapOperationAerialRaveA3(LivingEntity playerIn, float roll, Vec3 centerOffset, boolean mute, boolean critical, double comboRatio, KnockBacks knockback, Operation<EntitySlashEffect> original) {
        return original.call(playerIn, roll, centerOffset, mute, critical, 0.6, knockback);
    }
    
    @WrapOperation(method = {"lambda$static$192", "lambda$static$193"}, at = @At(value = "INVOKE", target = "Lmods/flammpfeil/slashblade/util/AttackManager;doSlash(Lnet/minecraft/world/entity/LivingEntity;FLnet/minecraft/world/phys/Vec3;ZZDLmods/flammpfeil/slashblade/util/KnockBacks;)Lmods/flammpfeil/slashblade/entity/EntitySlashEffect;", remap = false), remap = false)
    private static EntitySlashEffect wrapOperationAerialRaveB3(LivingEntity playerIn, float roll, Vec3 centerOffset, boolean mute, boolean critical, double comboRatio, KnockBacks knockback, Operation<EntitySlashEffect> original) {
        return original.call(playerIn, roll, centerOffset, mute, critical, 0.4, knockback);
    }
    
    @WrapOperation(method = {"lambda$static$205", "lambda$static$206"}, at = @At(value = "INVOKE", target = "Lmods/flammpfeil/slashblade/util/AttackManager;doSlash(Lnet/minecraft/world/entity/LivingEntity;FLnet/minecraft/world/phys/Vec3;ZZDLmods/flammpfeil/slashblade/util/KnockBacks;)Lmods/flammpfeil/slashblade/entity/EntitySlashEffect;", remap = false), remap = false)
    private static EntitySlashEffect wrapOperationAerialRaveB4(LivingEntity playerIn, float roll, Vec3 centerOffset, boolean mute, boolean critical, double comboRatio, KnockBacks knockback, Operation<EntitySlashEffect> original) {
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
    
    @WrapOperation(method = {"lambda$static$239", "lambda$static$247"}, at = @At(value = "INVOKE", target = "Lmods/flammpfeil/slashblade/util/AttackManager;areaAttack(Lnet/minecraft/world/entity/LivingEntity;Ljava/util/function/Consumer;FZZZ)Ljava/util/List;", remap = false), remap = false)
    private static List<Entity> wrapOperationAerialCleaveAreaAttack(LivingEntity playerIn, Consumer<LivingEntity> beforeHit, float comboRatio, boolean forceHit, boolean resetHit, boolean mute, Operation<List<Entity>> original) {
        return original.call(playerIn, beforeHit, 0.5f, forceHit, resetHit, mute);
    }
    
    @WrapOperation(method = {"lambda$static$239", "lambda$static$247", "lambda$static$251"}, at = @At(value = "INVOKE", target = "Lmods/flammpfeil/slashblade/util/AttackManager;doSlash(Lnet/minecraft/world/entity/LivingEntity;FLnet/minecraft/world/phys/Vec3;ZZDLmods/flammpfeil/slashblade/util/KnockBacks;)Lmods/flammpfeil/slashblade/entity/EntitySlashEffect;", remap = false), remap = false)
    private static EntitySlashEffect wrapOperationAerialCleaveDoSlash(LivingEntity playerIn, float roll, Vec3 centerOffset, boolean mute, boolean critical, double comboRatio, KnockBacks knockback, Operation<EntitySlashEffect> original) {
        return original.call(playerIn, roll, centerOffset, mute, critical, 2.0, knockback);
    }
    
    @WrapOperation(method = "lambda$static$260", at = @At(value = "INVOKE", target = "Lmods/flammpfeil/slashblade/util/AttackManager;areaAttack(Lnet/minecraft/world/entity/LivingEntity;Ljava/util/function/Consumer;FZZZ)Ljava/util/List;", remap = false), remap = false)
    private static List<Entity> wrapOperationRapidSlashInputCheck(LivingEntity playerIn, Consumer<LivingEntity> beforeHit, float comboRatio, boolean forceHit, boolean resetHit, boolean mute, Operation<List<Entity>> original) {
        InputStream inputStream = InputStream.getOrCreateInputStream(playerIn);
        if (playerIn.getTicksUsingItem() < 5 || inputStream.checkInputWithTime(InputCommand.R_DOWN, InputStream.InputType.START, 3)) {
            return new ArrayList<>();
        }
        return original.call(playerIn, beforeHit, 0.15F, forceHit, resetHit, mute);
    }
    
    @WrapOperation(method = "lambda$static$261", at = @At(value = "INVOKE", target = "Lmods/flammpfeil/slashblade/util/AttackManager;doSlash(Lnet/minecraft/world/entity/LivingEntity;FLnet/minecraft/world/phys/Vec3;ZZD)Lmods/flammpfeil/slashblade/entity/EntitySlashEffect;", remap = false), remap = false)
    private static EntitySlashEffect wrapOperationRapidSlashDoSlash(LivingEntity playerIn, float roll, Vec3 centerOffset, boolean mute, boolean critical, double comboRatio, Operation<EntitySlashEffect> original) {
        if (roll == -30) {
            return original.call(playerIn, roll, centerOffset, mute, critical, 0.4);
        }
        return original.call(playerIn, roll, centerOffset, mute, critical, 0.15);
    }
    
    @WrapOperation(method = "lambda$static$258", at = @At(value = "INVOKE", target = "Lmods/flammpfeil/slashblade/util/AttackManager;areaAttack(Lnet/minecraft/world/entity/LivingEntity;Ljava/util/function/Consumer;FZZZ)Ljava/util/List;", remap = false), remap = false)
    private static List<Entity> wrapOperationRapidSlashAreaAttack(LivingEntity playerIn, Consumer<LivingEntity> beforeHit, float comboRatio, boolean forceHit, boolean resetHit, boolean mute, Operation<List<Entity>> original) {
        return original.call(playerIn, beforeHit, 0.4f, forceHit, resetHit, mute);
    }
    
    @WrapOperation(method = "lambda$static$270", at = @At(value = "INVOKE", target = "Lmods/flammpfeil/slashblade/util/AttackManager;doSlash(Lnet/minecraft/world/entity/LivingEntity;FLnet/minecraft/world/phys/Vec3;ZZDLmods/flammpfeil/slashblade/util/KnockBacks;)Lmods/flammpfeil/slashblade/entity/EntitySlashEffect;", remap = false), remap = false)
    private static EntitySlashEffect wrapOperationRisingStarDoSlash1(LivingEntity playerIn, float roll, Vec3 centerOffset, boolean mute, boolean critical, double comboRatio, KnockBacks knockback, Operation<EntitySlashEffect> original) {
        return original.call(playerIn, roll, centerOffset, mute, critical, 0.8, knockback);
    }
    
    @WrapOperation(method = "lambda$static$271", at = @At(value = "INVOKE", target = "Lmods/flammpfeil/slashblade/util/AttackManager;doSlash(Lnet/minecraft/world/entity/LivingEntity;FLnet/minecraft/world/phys/Vec3;ZZDLmods/flammpfeil/slashblade/util/KnockBacks;)Lmods/flammpfeil/slashblade/entity/EntitySlashEffect;", remap = false), remap = false)
    private static EntitySlashEffect wrapOperationRisingStarDoSlash2(LivingEntity playerIn, float roll, Vec3 centerOffset, boolean mute, boolean critical, double comboRatio, KnockBacks knockback, Operation<EntitySlashEffect> original) {
        return original.call(playerIn, roll, centerOffset, mute, critical, 0.8, knockback);
    }
    
    @WrapOperation(method = "lambda$static$287", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;moveRelative(FLnet/minecraft/world/phys/Vec3;)V"), remap = false)
    private static void wrapOperationJudgementCutMoveRelative(LivingEntity instance, float v, Vec3 vec3, Operation<Void> original) {
        original.call(instance, v * 3, vec3);
    }
}
