package net.mrqx.truepower.event.handler;

import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.mrqx.truepower.util.JustSlashArtManager;

@Mod.EventBusSubscriber
public class JustSlashArtHandler {
    @SubscribeEvent
    public static void onPlayerTickEvent(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.START || !(event.player instanceof ServerPlayer)) {
            return;
        }
        long cooldown = JustSlashArtManager.getJustCooldown(event.player);
        if (cooldown > 0) {
            cooldown--;
            JustSlashArtManager.setJustCooldown(event.player, cooldown);
            if (cooldown == 0) {
                JustSlashArtManager.resetJustCount(event.player);
            }
        }
    }
}
