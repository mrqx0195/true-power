package net.mrqx.truepower.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import mods.flammpfeil.slashblade.capability.inputstate.CapabilityInputState;
import mods.flammpfeil.slashblade.capability.inputstate.IInputState;
import mods.flammpfeil.slashblade.entity.EntityHeavyRainSwords;
import mods.flammpfeil.slashblade.util.InputCommand;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(EntityHeavyRainSwords.class)
public class MixinEntityHeavyRainSwords {
    @WrapOperation(method = "rideTick()V",
        at = @At(value = "INVOKE", target = "Lmods/flammpfeil/slashblade/entity/EntityHeavyRainSwords;itFired()Z", ordinal = 1),
        remap = false
    )
    private boolean wrapOperationItFired(EntityHeavyRainSwords instance, Operation<Boolean> original) {
        if (instance.getOwner() instanceof LivingEntity livingEntity) {
            IInputState inputState = instance.getOwner().getData(CapabilityInputState.INPUT_STATE);
            if (inputState.getCommands(livingEntity).contains(InputCommand.M_DOWN)) {
                return false;
            }
        }
        return original.call(instance);
    }
}
