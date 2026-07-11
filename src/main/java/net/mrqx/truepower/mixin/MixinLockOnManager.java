package net.mrqx.truepower.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import mods.flammpfeil.slashblade.ability.LockOnManager;
import mods.flammpfeil.slashblade.capability.slashblade.ISlashBladeState;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.mrqx.truepower.ClientHandler;
import net.mrqx.truepower.capability.data.ITruePowerData;
import net.mrqx.truepower.config.TruePowerClientConfig;
import net.mrqx.truepower.util.LockOnUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@OnlyIn(Dist.CLIENT)
@Mixin(LockOnManager.class)
public abstract class MixinLockOnManager {
    @WrapOperation(method = "lambda$onEntityUpdate$9", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/Mth;rotLerp(FFF)F"), remap = false)
    private static float modifyStep(float delta, float start, float end, Operation<Float> original,
                                    @Local(argsOnly = true) LocalPlayer player,
                                    @Local(argsOnly = true) ISlashBladeState s) {
        ITruePowerData data = ITruePowerData.get(player);
        if (data != null) {
            List<ITruePowerData.BoolInterval> intervals = data.getSnapLockOnIntervals();
            if (intervals != null && !intervals.isEmpty()) {
                long elapsed = s.getElapsedTime(player);
                for (ITruePowerData.BoolInterval interval : intervals) {
                    if (elapsed >= interval.start() && elapsed <= interval.end()) {
                        return end;
                    }
                }
            }
        }
        return Mth.rotLerp(delta * TruePowerClientConfig.LOCK_ON_SPEED.get().floatValue(), start, end);
    }
    
    @WrapOperation(method = "lambda$onEntityUpdate$9", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;isClientSide()Z"), remap = false)
    private static boolean wrapIsClientSide(Level instance, Operation<Boolean> original, @Local(argsOnly = true) LocalPlayer player, @Local(argsOnly = true) ISlashBladeState s) {
        return original.call(instance) && ClientHandler.shouldLockOnRot(player, s);
    }
    
    @WrapOperation(method = "onInputChange(Lmods/flammpfeil/slashblade/event/handler/InputCommandEvent;)V", at = @At(value = "INVOKE", target = "Ljava/util/stream/Stream;min(Ljava/util/Comparator;)Ljava/util/Optional;", remap = false), remap = false)
    private Optional<LivingEntity> wrapRayTrace(Stream<LivingEntity> instance, Comparator<? super LivingEntity> comparator, Operation<Optional<LivingEntity>> original) {
        List<LivingEntity> sorted = instance.sorted(comparator).toList();
        
        LivingEntity firstInvisible = null;
        for (LivingEntity entity : sorted) {
            if (LockOnUtils.isVisible(entity, Double.MAX_VALUE)) {
                return Optional.of(entity);
            } else if (firstInvisible == null) {
                firstInvisible = entity;
            }
        }
        return Optional.ofNullable(firstInvisible);
    }
}
