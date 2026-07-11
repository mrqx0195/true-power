package net.mrqx.truepower.network;

import mods.flammpfeil.slashblade.SlashBlade;
import mods.flammpfeil.slashblade.item.ItemSlashBlade;
import mods.flammpfeil.slashblade.registry.ComboStateRegistry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.PacketDistributor;
import net.mrqx.truepower.capability.data.ITruePowerData;
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
                ItemStack itemStack = serverPlayer.getMainHandItem();
                if (!itemStack.isEmpty()) {
                    itemStack.getCapability(ItemSlashBlade.BLADESTATE).ifPresent(state -> {
                        ComboCancelEvent event = new ComboCancelEvent(serverPlayer.getMainHandItem(), state, serverPlayer, msg.isJump);
                        if (!MinecraftForge.EVENT_BUS.post(event)) {
                            ResourceLocation noneId = ComboStateRegistry.NONE.getId();
                            state.updateComboSeq(serverPlayer, noneId);
                            ITruePowerData data = ITruePowerData.get(serverPlayer);
                            ComboSyncMessage comboSyncMessage = new ComboSyncMessage();
                            
                            comboSyncMessage.comboState = noneId != null ? noneId : SlashBlade.prefix("none");
                            comboSyncMessage.lastActionTime = state.getLastActionTime();
                            comboSyncMessage.canMove = data != null && data.canMove();
                            comboSyncMessage.jumpCancelOnly = data != null && data.isJumpCancelOnly();
                            comboSyncMessage.noMoveEnable = data != null && data.isNoMoveEnable();
                            comboSyncMessage.syncCombo = true;
                            
                            NetworkManager.INSTANCE.send(PacketDistributor.PLAYER.with(() -> serverPlayer), comboSyncMessage);
                        }
                    });
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
