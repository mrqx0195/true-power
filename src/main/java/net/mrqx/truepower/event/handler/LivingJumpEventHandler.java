package net.mrqx.truepower.event.handler;

import mods.flammpfeil.slashblade.item.ItemSlashBlade;
import mods.flammpfeil.slashblade.registry.ComboStateRegistry;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class LivingJumpEventHandler {
    @SubscribeEvent
    public void onPlayerJump(LivingEvent.LivingJumpEvent event) {
        event.getEntity().getMainHandItem().getCapability(ItemSlashBlade.BLADESTATE).ifPresent(state -> {
            state.updateComboSeq(event.getEntity(), ComboStateRegistry.NONE.getId());
        });
    }
}
