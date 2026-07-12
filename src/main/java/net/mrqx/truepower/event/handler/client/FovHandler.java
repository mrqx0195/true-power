package net.mrqx.truepower.event.handler.client;

import mods.flammpfeil.slashblade.capability.slashblade.BladeStateAccess;
import net.minecraft.world.entity.player.Player;
import net.mrqx.truepower.config.TruePowerClientConfig;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ComputeFovModifierEvent;

@EventBusSubscriber(Dist.CLIENT)
public final class FovHandler {
    @SubscribeEvent
    public static void onComputeFovModifierEvent(ComputeFovModifierEvent event) {
        Player player = event.getPlayer();
        if (BladeStateAccess.of(player.getMainHandItem()).isPresent() && TruePowerClientConfig.LOCK_FOV.get()) {
            event.setNewFovModifier(TruePowerClientConfig.LOCKED_FOV_MODIFIER.get().floatValue());
        }
    }
}
