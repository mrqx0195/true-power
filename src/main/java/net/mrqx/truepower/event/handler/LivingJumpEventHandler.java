package net.mrqx.truepower.event.handler;

import mods.flammpfeil.slashblade.capability.slashblade.BladeStateAccess;
import mods.flammpfeil.slashblade.registry.ComboStateRegistry;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingEvent;

@EventBusSubscriber
public class LivingJumpEventHandler {
    @SubscribeEvent
    public static void onPlayerJump(LivingEvent.LivingJumpEvent event) {
        BladeStateAccess.of(event.getEntity().getMainHandItem()).ifPresent(state -> state.updateComboSeq(event.getEntity(), ComboStateRegistry.NONE.getId()));
    }
}
