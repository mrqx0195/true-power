package net.mrqx.truepower.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import mods.flammpfeil.slashblade.capability.slashblade.BladeStateAccess;
import mods.flammpfeil.slashblade.capability.slashblade.ISlashBladeState;
import mods.flammpfeil.slashblade.client.renderer.LockonCircleRender;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.mrqx.truepower.ClientHandler;
import net.mrqx.truepower.attachment.ITruePowerData;
import net.mrqx.truepower.config.TruePowerClientConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.List;
import java.util.Optional;

@Mixin(LockonCircleRender.class)
public class MixinLockonCircleRender {
    @WrapOperation(method = "onEntityUpdate(Lnet/neoforged/neoforge/client/event/RenderFrameEvent$Pre;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/Mth;rotLerp(FFF)F"), remap = false)
    private static float modifyStep(float delta, float start, float end, Operation<Float> original) {
        return Mth.rotLerp(delta * TruePowerClientConfig.LOCK_ON_SPEED.get().floatValue(), start, end);
    }
    
    @WrapOperation(method = "onEntityUpdate(Lnet/neoforged/neoforge/client/event/RenderFrameEvent$Pre;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/Mth;rotLerp(FFF)F"), remap = false)
    private static float modifyStep(float delta, float start, float end, Operation<Float> original,
                                    @Local(name = "player") LocalPlayer player,
                                    @Local(name = "stack") ItemStack stack) {
        ITruePowerData data = ITruePowerData.get(player);
        List<ITruePowerData.BoolInterval> intervals = data.getSnapLockOnIntervals();
        Optional<ISlashBladeState> bladeStateOptional = BladeStateAccess.of(stack);
        if (intervals != null && !intervals.isEmpty() && bladeStateOptional.isPresent()) {
            long elapsed = bladeStateOptional.get().getElapsedTime(player);
            for (ITruePowerData.BoolInterval interval : intervals) {
                if (elapsed >= interval.start() && elapsed <= interval.end()) {
                    return end;
                }
            }
        }
        return Mth.rotLerp(delta * TruePowerClientConfig.LOCK_ON_SPEED.get().floatValue(), start, end);
    }
    
    @WrapOperation(method = "onEntityUpdate(Lnet/neoforged/neoforge/client/event/RenderFrameEvent$Pre;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;isAlive()Z"), remap = false)
    private static boolean wrapIsClientSide(Entity instance, Operation<Boolean> original, @Local(name = "player") LocalPlayer player, @Local(name = "stack") ItemStack stack) {
        return original.call(instance) && BladeStateAccess.of(stack).map(s -> ClientHandler.shouldLockOnRot(player, s)).orElse(true);
    }
}
