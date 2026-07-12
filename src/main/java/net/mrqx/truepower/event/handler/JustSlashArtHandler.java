package net.mrqx.truepower.event.handler;

import net.minecraft.server.level.ServerPlayer;
import net.mrqx.truepower.util.JustSlashArtManager;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

@EventBusSubscriber
public final class JustSlashArtHandler {
    @SubscribeEvent
    public static void onPlayerTickEvent(PlayerTickEvent.Pre event) {
        if (!(event.getEntity() instanceof ServerPlayer serverPlayer)) {
            return;
        }
        long cooldown = JustSlashArtManager.getJustCooldown(serverPlayer);
        if (cooldown > 0) {
            cooldown--;
            JustSlashArtManager.setJustCooldown(serverPlayer, cooldown);
            if (cooldown == 0) {
                JustSlashArtManager.resetJustCount(serverPlayer);
            }
        }
    }
}
