package net.mrqx.truepower.network;

import mods.flammpfeil.slashblade.capability.slashblade.BladeStateAccess;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.mrqx.truepower.TruePowerMod;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record LockOnTargetChangeMessage(int entityId) implements CustomPacketPayload {
    public static final Type<LockOnTargetChangeMessage> TYPE = new Type<>(TruePowerMod.prefix("lockon_target_change"));
    public static final StreamCodec<RegistryFriendlyByteBuf, LockOnTargetChangeMessage> STREAM_CODEC =
        CustomPacketPayload.codec(LockOnTargetChangeMessage::write, LockOnTargetChangeMessage::new);
    
    private LockOnTargetChangeMessage(RegistryFriendlyByteBuf buf) {
        this(buf.readInt());
    }
    
    private void write(RegistryFriendlyByteBuf buf) {
        buf.writeInt(this.entityId());
    }
    
    @Override
    public Type<LockOnTargetChangeMessage> type() {
        return TYPE;
    }
    
    public static void handle(LockOnTargetChangeMessage msg, IPayloadContext ctx) {
        ctx.enqueueWork(() -> {
            if (ctx.player() instanceof ServerPlayer serverPlayer) {
                ItemStack stack = serverPlayer.getMainHandItem();
                if (!stack.isEmpty()) {
                    BladeStateAccess.of(stack)
                        .ifPresent(state -> state.setTargetEntityId(msg.entityId()));
                }
            }
        });
    }
}
