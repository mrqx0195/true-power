package net.mrqx.truepower.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import mods.flammpfeil.slashblade.ability.LockOnManager;
import net.minecraft.world.entity.LivingEntity;
import net.mrqx.truepower.util.LockOnUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Mixin(LockOnManager.class)
public abstract class MixinLockOnManager {
    @WrapOperation(method = "onInputChange(Lmods/flammpfeil/slashblade/event/handler/InputCommandEvent;)V", at = @At(value = "INVOKE", target = "Ljava/util/stream/Stream;min(Ljava/util/Comparator;)Ljava/util/Optional;", remap = false), remap = false)
    private static Optional<LivingEntity> wrapRayTrace(Stream<LivingEntity> instance, Comparator<? super LivingEntity> comparator, Operation<Optional<LivingEntity>> original) {
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
