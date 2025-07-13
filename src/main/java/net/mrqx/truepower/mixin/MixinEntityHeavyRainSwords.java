package net.mrqx.truepower.mixin;

import mods.flammpfeil.slashblade.capability.inputstate.IInputState;
import mods.flammpfeil.slashblade.capability.inputstate.InputState;
import mods.flammpfeil.slashblade.capability.inputstate.InputStateCapabilityProvider;
import mods.flammpfeil.slashblade.entity.EntityHeavyRainSwords;
import mods.flammpfeil.slashblade.util.InputCommand;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(EntityHeavyRainSwords.class)
public class MixinEntityHeavyRainSwords {
    @Redirect(method = "rideTick()V",
            at = @At(value = "INVOKE", target = "Lmods/flammpfeil/slashblade/entity/EntityHeavyRainSwords;itFired()Z", ordinal = 1),
            remap = false
    )
    private boolean redirectItFired(EntityHeavyRainSwords instance) {
        if (instance.getOwner() instanceof LivingEntity livingEntity) {
            IInputState inputState = instance.getOwner().getCapability(InputStateCapabilityProvider.INPUT_STATE).orElse(new InputState());
            if (inputState.getCommands(livingEntity).contains(InputCommand.M_DOWN)) {
                return false;
            }
        }
        return instance.itFired();
    }
}
