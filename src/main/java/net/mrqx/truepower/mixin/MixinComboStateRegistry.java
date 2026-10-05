package net.mrqx.truepower.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
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

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@SuppressWarnings("SameReturnValue")
@Mixin(ComboStateRegistry.class)
public abstract class MixinComboStateRegistry {
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
    
    @WrapOperation(method = "/lambda\\$static\\$(200|204)/", at = @At(value = "INVOKE", target = "Lmods/flammpfeil/slashblade/util/AttackManager;areaAttack(Lnet/minecraft/world/entity/LivingEntity;Ljava/util/function/Consumer;FZZZ)Ljava/util/List;", remap = false), remap = false)
    private static List<Entity> modifyAerialCleaveAreaAttack(LivingEntity playerIn, Consumer<LivingEntity> beforeHit, float comboRatio, boolean forceHit, boolean resetHit, boolean mute, Operation<List<Entity>> original) {
        return original.call(playerIn, beforeHit, 0.25f, forceHit, resetHit, mute);
    }
    
    @WrapOperation(method = "lambda$static$214", at = @At(value = "INVOKE", target = "Lmods/flammpfeil/slashblade/util/AttackManager;areaAttack(Lnet/minecraft/world/entity/LivingEntity;Ljava/util/function/Consumer;FZZZ)Ljava/util/List;", remap = false), remap = false)
    private static List<Entity> wrapOperationRapidSlashAreaAttack(LivingEntity playerIn, Consumer<LivingEntity> beforeHit, float comboRatio, boolean forceHit, boolean resetHit, boolean mute, Operation<List<Entity>> original) {
        InputStream inputStream = InputStream.getOrCreateInputStream(playerIn);
        if (playerIn.getTicksUsingItem() < 5 || inputStream.checkInputWithTime(InputCommand.R_DOWN, InputStream.InputType.START, 3)) {
            return new ArrayList<>();
        }
        return original.call(playerIn, beforeHit, 0.15F, forceHit, resetHit, mute);
    }
    
    @WrapOperation(method = "lambda$static$216", at = @At(value = "INVOKE", target = "Lmods/flammpfeil/slashblade/util/AttackManager;areaAttack(Lnet/minecraft/world/entity/LivingEntity;Ljava/util/function/Consumer;FZZZ)Ljava/util/List;", remap = false), remap = false)
    private static List<Entity> modifyRapidSlashAreaAttack(LivingEntity playerIn, Consumer<LivingEntity> beforeHit, float comboRatio, boolean forceHit, boolean resetHit, boolean mute, Operation<List<Entity>> original) {
        return original.call(playerIn, beforeHit, 0.4f, forceHit, resetHit, mute);
    }
    
    @WrapOperation(method = "lambda$static$233", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;moveRelative(FLnet/minecraft/world/phys/Vec3;)V"), remap = false)
    private static void wrapOperationJudgementCutMoveRelative(LivingEntity instance, float v, Vec3 vec3, Operation<Void> original) {
        original.call(instance, v * 3, vec3);
    }
}
