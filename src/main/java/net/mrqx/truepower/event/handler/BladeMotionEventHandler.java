package net.mrqx.truepower.event.handler;

import mods.flammpfeil.slashblade.event.BladeMotionEvent;
import mods.flammpfeil.slashblade.registry.ComboStateRegistry;
import net.minecraft.nbt.CompoundTag;
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
            CompoundTag persistentData = serverPlayer.getPersistentData();
            ComboSyncMessage comboSyncMessage = new ComboSyncMessage();

            comboSyncMessage.comboState = event.getCombo();
            comboSyncMessage.canMove = persistentData.getBoolean("truePower.canMove");
            comboSyncMessage.jumpCancelOnly = persistentData.getBoolean("truePower.jumpCancelOnly");
            comboSyncMessage.noMoveEnable = persistentData.getBoolean("truePower.noMoveEnable");
            comboSyncMessage.syncCombo = !event.getCombo().equals(ComboStateRegistry.NONE.getId()) && !event.getCombo().equals(ComboStateRegistry.STANDBY.getId());

            NetworkManager.INSTANCE.send(PacketDistributor.PLAYER.with(() -> serverPlayer), comboSyncMessage);
        }
    }
}
