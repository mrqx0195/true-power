package net.mrqx.truepower.event.handler;

import mods.flammpfeil.slashblade.event.BladeMotionEvent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.PacketDistributor;
import net.mrqx.truepower.network.ComboSyncMessage;
import net.mrqx.truepower.network.NetworkManager;

@Mod.EventBusSubscriber
public class BladeMotionEventHandler {
    @SubscribeEvent
    public static void onBladeMotionEvent(BladeMotionEvent event) {
        if (event.getEntity() instanceof ServerPlayer serverPlayer) {
            ComboSyncMessage comboSyncMessage = new ComboSyncMessage();
            comboSyncMessage.comboState = event.getCombo();
            NetworkManager.INSTANCE.send(PacketDistributor.PLAYER.with(() -> serverPlayer), comboSyncMessage);
        }
    }
}
