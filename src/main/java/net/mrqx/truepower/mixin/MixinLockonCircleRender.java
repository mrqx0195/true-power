package net.mrqx.truepower.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import mods.flammpfeil.slashblade.client.renderer.LockonCircleRender;
import net.minecraft.util.Mth;
import net.mrqx.truepower.config.TruePowerClientConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(LockonCircleRender.class)
public class MixinLockonCircleRender {
    @WrapOperation(method = "onEntityUpdate(Lnet/neoforged/neoforge/client/event/RenderFrameEvent$Pre;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/Mth;rotLerp(FFF)F"), remap = false)
    private static float modifyStep(float delta, float start, float end, Operation<Float> original) {
        return Mth.rotLerp(delta * TruePowerClientConfig.LOCK_ON_SPEED.get().floatValue(), start, end);
    }
}
