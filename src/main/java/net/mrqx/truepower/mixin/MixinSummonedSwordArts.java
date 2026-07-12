package net.mrqx.truepower.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import mods.flammpfeil.slashblade.ability.SummonedSwordArts;
import mods.flammpfeil.slashblade.event.Scheduler;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.timers.TimerCallback;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(SummonedSwordArts.class)
public abstract class MixinSummonedSwordArts {
    @WrapOperation(method = "onInputChange(Lmods/flammpfeil/slashblade/event/handler/InputCommandEvent;)V",
        at = @At(
            value = "INVOKE",
            target = "Lmods/flammpfeil/slashblade/event/Scheduler;schedule(Ljava/lang/String;JLnet/minecraft/world/level/timers/TimerCallback;)V",
            remap = false
        ),
        remap = false
    )
    private void wrapOperationSchedule(Scheduler instance, String key, long time, TimerCallback<LivingEntity> callback, Operation<Void> original) {
        if (!"HeavyRainSwords".equals(key)) {
            original.call(instance, key, time, callback);
        }
    }
}
