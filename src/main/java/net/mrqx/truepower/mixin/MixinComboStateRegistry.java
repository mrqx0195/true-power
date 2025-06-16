package net.mrqx.truepower.mixin;

import mods.flammpfeil.slashblade.entity.EntitySlashEffect;
import mods.flammpfeil.slashblade.registry.ComboStateRegistry;
import mods.flammpfeil.slashblade.registry.combo.ComboState;
import mods.flammpfeil.slashblade.util.AttackManager;
import mods.flammpfeil.slashblade.util.InputCommand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import net.mrqx.sbr_core.utils.InputStream;
import net.mrqx.truepower.util.TruePowerComboHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@Mixin(ComboStateRegistry.class)
public abstract class MixinComboStateRegistry {
    @ModifyArg(method = "/lambda\\$static\\$(5|27)/", at = @At(value = "INVOKE", target = "Lmods/flammpfeil/slashblade/util/AttackManager;doSlash(Lnet/minecraft/world/entity/LivingEntity;FZZD)Lmods/flammpfeil/slashblade/entity/EntitySlashEffect;"), index = 4)
    private static double modifyComboA1(double comboRatio) {
        return 0.4;
    }

    @ModifyArg(method = "/lambda\\$static\\$(13|3[78])/", at = @At(value = "INVOKE", target = "Lmods/flammpfeil/slashblade/util/AttackManager;doSlash(Lnet/minecraft/world/entity/LivingEntity;FZZD)Lmods/flammpfeil/slashblade/entity/EntitySlashEffect;"), index = 4)
    private static double modifyComboA2(double comboRatio) {
        return 0.5;
    }

    @ModifyArg(method = "lambda$static$28", at = @At(value = "INVOKE", target = "Lmods/flammpfeil/slashblade/util/AttackManager;doSlash(Lnet/minecraft/world/entity/LivingEntity;FZZD)Lmods/flammpfeil/slashblade/entity/EntitySlashEffect;"), index = 4)
    private static double modifyComboA3(double comboRatio) {
        return 0.7;
    }

    @ModifyArg(method = "/lambda\\$static\\$(50|51)/", at = @At(value = "INVOKE", target = "Lmods/flammpfeil/slashblade/util/AttackManager;doSlash(Lnet/minecraft/world/entity/LivingEntity;FZZD)Lmods/flammpfeil/slashblade/entity/EntitySlashEffect;"), index = 4)
    private static double modifyComboA4Ex(double comboRatio) {
        return 0.8;
    }

    @ModifyArg(method = "/lambda\\$static\\$(58|59|60)/", at = @At(value = "INVOKE", target = "Lmods/flammpfeil/slashblade/util/AttackManager;doSlash(Lnet/minecraft/world/entity/LivingEntity;FZZD)Lmods/flammpfeil/slashblade/entity/EntitySlashEffect;"), index = 4)
    private static double modifyComboA5(double comboRatio) {
        return 2.64;
    }

    @ModifyArg(method = "lambda$static$20", at = @At(value = "INVOKE", target = "Lmods/flammpfeil/slashblade/util/AttackManager;doSlash(Lnet/minecraft/world/entity/LivingEntity;FZZD)Lmods/flammpfeil/slashblade/entity/EntitySlashEffect;"), index = 4)
    private static double modifyComboC1(double comboRatio) {
        return 1.6;
    }

    @ModifyArg(method = "lambda$static$21", at = @At(value = "INVOKE", target = "Lmods/flammpfeil/slashblade/util/AttackManager;doSlash(Lnet/minecraft/world/entity/LivingEntity;FZZD)Lmods/flammpfeil/slashblade/entity/EntitySlashEffect;"), index = 4)
    private static double modifyComboC2(double comboRatio) {
        return 1.7;
    }

    @ModifyArg(method = "/lambda\\$static\\$(7[2-9]|80|9[89]|10[0-4]|10[7-9]|11[0-3]|11[6-9]|12[0-2]|12[5-9]|13[0-1]|13[4-9]|140|14[3-9]|150)/", at = @At(value = "INVOKE", target = "Lmods/flammpfeil/slashblade/util/AttackManager;doSlash(Lnet/minecraft/world/entity/LivingEntity;FLnet/minecraft/world/phys/Vec3;ZZD)Lmods/flammpfeil/slashblade/entity/EntitySlashEffect;"), index = 5)
    private static double modifyComboB1(double comboRatio) {
        return 0.15;
    }

    @ModifyArg(method = "/lambda\\$static\\$(84|85|15[12]|16[45])/", at = @At(value = "INVOKE", target = "Lmods/flammpfeil/slashblade/util/AttackManager;doSlash(Lnet/minecraft/world/entity/LivingEntity;FLnet/minecraft/world/phys/Vec3;ZZD)Lmods/flammpfeil/slashblade/entity/EntitySlashEffect;"), index = 5)
    private static double modifyComboB2(double comboRatio) {
        return 0.8;
    }

    @ModifyArg(method = "lambda$static$178", at = @At(value = "INVOKE", target = "Lmods/flammpfeil/slashblade/util/AttackManager;doSlash(Lnet/minecraft/world/entity/LivingEntity;FZZD)Lmods/flammpfeil/slashblade/entity/EntitySlashEffect;"), index = 4)
    private static double modifyAerialRaveA1(double comboRatio) {
        return 0.5;
    }

    @ModifyArg(method = "lambda$static$184", at = @At(value = "INVOKE", target = "Lmods/flammpfeil/slashblade/util/AttackManager;doSlash(Lnet/minecraft/world/entity/LivingEntity;FZZD)Lmods/flammpfeil/slashblade/entity/EntitySlashEffect;"), index = 4)
    private static double modifyAerialRaveA2(double comboRatio) {
        return 0.6;
    }

    @ModifyArg(method = "/lambda\\$static\\$(191|192)/", at = @At(value = "INVOKE", target = "Lmods/flammpfeil/slashblade/util/AttackManager;doSlash(Lnet/minecraft/world/entity/LivingEntity;FLnet/minecraft/world/phys/Vec3;ZZDLmods/flammpfeil/slashblade/util/KnockBacks;)Lmods/flammpfeil/slashblade/entity/EntitySlashEffect;"), index = 5)
    private static double modifyAerialRaveA3(double comboRatio) {
        return 0.6;
    }

    @ModifyArg(method = "/lambda\\$static\\$(200|201)/", at = @At(value = "INVOKE", target = "Lmods/flammpfeil/slashblade/util/AttackManager;doSlash(Lnet/minecraft/world/entity/LivingEntity;FLnet/minecraft/world/phys/Vec3;ZZDLmods/flammpfeil/slashblade/util/KnockBacks;)Lmods/flammpfeil/slashblade/entity/EntitySlashEffect;"), index = 5)
    private static double modifyAerialRaveB3(double comboRatio) {
        return 0.4;
    }

    @ModifyArg(method = "/lambda\\$static\\$(214|215)/", at = @At(value = "INVOKE", target = "Lmods/flammpfeil/slashblade/util/AttackManager;doSlash(Lnet/minecraft/world/entity/LivingEntity;FLnet/minecraft/world/phys/Vec3;ZZDLmods/flammpfeil/slashblade/util/KnockBacks;)Lmods/flammpfeil/slashblade/entity/EntitySlashEffect;"), index = 5)
    private static double modifyAerialRaveB4(double comboRatio) {
        return 0.5;
    }

    @Redirect(method = "<clinit>", at = @At(value = "INVOKE", target = "Lmods/flammpfeil/slashblade/registry/combo/ComboState$Builder;addTickAction(Ljava/util/function/Consumer;)Lmods/flammpfeil/slashblade/registry/combo/ComboState$Builder;", ordinal = 54))
    private static ComboState.Builder redirectUpperSlashTickAction(ComboState.Builder instance, Consumer<LivingEntity> tickAction) {
        instance.addTickAction(livingEntity -> {
            if (AttackManager.isPowered(livingEntity)) {
                TruePowerComboHelper.POWERED_UPPER_SLASH.accept(livingEntity);
            } else {
                TruePowerComboHelper.UPPER_SLASH.accept(livingEntity);
            }
        });
        return instance;
    }

    @ModifyArg(method = "/lambda\\$static\\$(250|259)/", at = @At(value = "INVOKE", target = "Lmods/flammpfeil/slashblade/util/AttackManager;areaAttack(Lnet/minecraft/world/entity/LivingEntity;Ljava/util/function/Consumer;FZZZ)Ljava/util/List;"), index = 2)
    private static float modifyAerialCleaveAreaAttack(float comboRatio) {
        return 0.5F;
    }

    @ModifyArg(method = "/lambda\\$static\\$(250|259|263)/", at = @At(value = "INVOKE", target = "Lmods/flammpfeil/slashblade/util/AttackManager;doSlash(Lnet/minecraft/world/entity/LivingEntity;FLnet/minecraft/world/phys/Vec3;ZZDLmods/flammpfeil/slashblade/util/KnockBacks;)Lmods/flammpfeil/slashblade/entity/EntitySlashEffect;"), index = 5)
    private static double modifyAerialCleaveDoSlash(double comboRatio) {
        return 2;
    }

    @Redirect(method = "lambda$static$272", at = @At(value = "INVOKE", target = "Lmods/flammpfeil/slashblade/util/AttackManager;areaAttack(Lnet/minecraft/world/entity/LivingEntity;Ljava/util/function/Consumer;FZZZ)Ljava/util/List;"))
    private static List<Entity> redirectRapidSlashAreaAttack(LivingEntity playerIn, Consumer<LivingEntity> beforeHit, float comboRatio, boolean forceHit, boolean resetHit, boolean mute) {
        InputStream inputStream = InputStream.getOrCreateInputStream(playerIn);
        if (playerIn.getTicksUsingItem() < 5 || inputStream.checkInputWithTime(InputCommand.R_DOWN, InputStream.InputType.START, 3)) {
            return new ArrayList<>();
        }
        return AttackManager.areaAttack(playerIn, beforeHit, 0.15F, forceHit, resetHit, mute);
    }

    @Redirect(method = "lambda$static$275", at = @At(value = "INVOKE", target = "Lmods/flammpfeil/slashblade/util/AttackManager;doSlash(Lnet/minecraft/world/entity/LivingEntity;FLnet/minecraft/world/phys/Vec3;ZZD)Lmods/flammpfeil/slashblade/entity/EntitySlashEffect;"))
    private static EntitySlashEffect redirectRapidSlashAreaAttack(LivingEntity playerIn, float roll, Vec3 centerOffset, boolean mute, boolean critical, double comboRatio) {
        if (roll == -30) {
            return AttackManager.doSlash(playerIn, roll, centerOffset, mute, critical, 0.4);
        }
        return AttackManager.doSlash(playerIn, roll, centerOffset, mute, critical, 0.15);
    }

    @ModifyArg(method = "lambda$static$274", at = @At(value = "INVOKE", target = "Lmods/flammpfeil/slashblade/util/AttackManager;areaAttack(Lnet/minecraft/world/entity/LivingEntity;Ljava/util/function/Consumer;FZZZ)Ljava/util/List;"), index = 2)
    private static float modifyRapidSlashAreaAttack(float comboRatio) {
        return 0.4F;
    }

    @ModifyArg(method = "lambda$static$284", at = @At(value = "INVOKE", target = "Lmods/flammpfeil/slashblade/util/AttackManager;doSlash(Lnet/minecraft/world/entity/LivingEntity;FLnet/minecraft/world/phys/Vec3;ZZDLmods/flammpfeil/slashblade/util/KnockBacks;)Lmods/flammpfeil/slashblade/entity/EntitySlashEffect;"), index = 5)
    private static double modifyRisingStarDoSlash1(double comboRatio) {
        return 0.8;
    }

    @ModifyArg(method = "lambda$static$285", at = @At(value = "INVOKE", target = "Lmods/flammpfeil/slashblade/util/AttackManager;doSlash(Lnet/minecraft/world/entity/LivingEntity;FLnet/minecraft/world/phys/Vec3;ZZDLmods/flammpfeil/slashblade/util/KnockBacks;)Lmods/flammpfeil/slashblade/entity/EntitySlashEffect;"), index = 5)
    private static double modifyRisingStarDoSlash2(double comboRatio) {
        return 0.8;
    }

    @Redirect(method = "lambda$static$302", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;moveRelative(FLnet/minecraft/world/phys/Vec3;)V"))
    private static void redirectJudgementCutMoveRelative(LivingEntity instance, float v, Vec3 vec3) {
        instance.moveRelative(v * 3, vec3);
    }

}
