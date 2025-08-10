package net.mrqx.truepower.network;

import mods.flammpfeil.slashblade.item.ItemSlashBlade;
import mods.flammpfeil.slashblade.registry.ComboStateRegistry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.PacketDistributor;
import net.mrqx.truepower.event.ComboCancelEvent;

import java.util.function.Supplier;

public class ComboCancelMessage {
    public boolean isJump;

    public static ComboCancelMessage decode(FriendlyByteBuf buf) {
        ComboCancelMessage comboCancelMessage = new ComboCancelMessage();
        comboCancelMessage.isJump = buf.readBoolean();
        return comboCancelMessage;
    }

    public static void encode(ComboCancelMessage msg, FriendlyByteBuf buf) {
        buf.writeBoolean(msg.isJump);
    }

    public static void handle(ComboCancelMessage msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer serverPlayer = ctx.get().getSender();
            if (serverPlayer != null) {
                serverPlayer.getMainHandItem().getCapability(ItemSlashBlade.BLADESTATE).ifPresent(state -> {
                    ComboCancelEvent event = new ComboCancelEvent(serverPlayer.getMainHandItem(), state, serverPlayer, msg.isJump);
                    if (!MinecraftForge.EVENT_BUS.post(event)) {
                        state.updateComboSeq(serverPlayer, ComboStateRegistry.NONE.getId());
                        CompoundTag persistentData = serverPlayer.getPersistentData();
                        ComboSyncMessage comboSyncMessage = new ComboSyncMessage();

                        comboSyncMessage.comboState = ComboStateRegistry.NONE.getId();
                        comboSyncMessage.lastActionTime = state.getLastActionTime();
                        comboSyncMessage.canMove = persistentData.getBoolean("truePower.canMove");
                        comboSyncMessage.jumpCancelOnly = persistentData.getBoolean("truePower.jumpCancelOnly");
                        comboSyncMessage.noMoveEnable = persistentData.getBoolean("truePower.noMoveEnable");
                        comboSyncMessage.syncCombo = true;

                        NetworkManager.INSTANCE.send(PacketDistributor.PLAYER.with(() -> serverPlayer), comboSyncMessage);
                    }
                });
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
