package net.mrqx.truepower.network;

import mods.flammpfeil.slashblade.item.ItemSlashBlade;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class LockOnTargetChangeMessage {
    public int entityId;
    
    public static LockOnTargetChangeMessage decode(FriendlyByteBuf buf) {
        LockOnTargetChangeMessage message = new LockOnTargetChangeMessage();
        message.entityId = buf.readInt();
        return message;
    }
    
    public static void encode(LockOnTargetChangeMessage msg, FriendlyByteBuf buf) {
        buf.writeInt(msg.entityId);
    }
    
    public static void handle(LockOnTargetChangeMessage msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer serverPlayer = ctx.get().getSender();
            if (serverPlayer != null) {
                ItemStack itemStack = serverPlayer.getMainHandItem();
                if (!itemStack.isEmpty()) {
                    itemStack.getCapability(ItemSlashBlade.BLADESTATE)
                        .ifPresent(state -> state.setTargetEntityId(msg.entityId));
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
