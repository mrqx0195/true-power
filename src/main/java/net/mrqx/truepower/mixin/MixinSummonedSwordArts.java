package net.mrqx.truepower.mixin;

import mods.flammpfeil.slashblade.ability.SummonedSwordArts;
import mods.flammpfeil.slashblade.event.Scheduler;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.timers.TimerCallback;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(SummonedSwordArts.class)
public class MixinSummonedSwordArts {
    @Redirect(method = "lambda$onInputChange$0",
            at = @At(
                    value = "INVOKE",
                    target = "Lmods/flammpfeil/slashblade/event/Scheduler;schedule(Ljava/lang/String;JLnet/minecraft/world/level/timers/TimerCallback;)V",
                    remap = false
            ),
            remap = false
    )
    private void redirectSchedule(Scheduler instance, String key, long time, TimerCallback<LivingEntity> callback) {
        if (!"HeavyRainSwords".equals(key)) {
            instance.schedule(key, time, callback);
        }
    }
}
