package net.mrqx.truepower.network;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.mrqx.truepower.ClientHandler;
import net.mrqx.truepower.TruePowerMod;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record ComboSyncMessage(ResourceLocation comboState, long lastActionTime,
                               boolean canMove, boolean jumpCancelOnly, boolean noMoveEnable, boolean syncCombo
) implements CustomPacketPayload {
    public static final Type<ComboSyncMessage> TYPE = new Type<>(TruePowerMod.prefix("combo_sync"));
    public static final StreamCodec<RegistryFriendlyByteBuf, ComboSyncMessage> STREAM_CODEC = CustomPacketPayload
        .codec(ComboSyncMessage::write, ComboSyncMessage::new);
    
    private ComboSyncMessage(RegistryFriendlyByteBuf buf) {
        this(buf.readResourceLocation(), buf.readLong(), buf.readBoolean(), buf.readBoolean(), buf.readBoolean(), buf.readBoolean());
    }
    
    private void write(RegistryFriendlyByteBuf buf) {
        buf.writeResourceLocation(this.comboState());
        buf.writeLong(this.lastActionTime());
        buf.writeBoolean(this.canMove());
        buf.writeBoolean(this.jumpCancelOnly());
        buf.writeBoolean(this.noMoveEnable());
        buf.writeBoolean(this.syncCombo());
    }
    
    @Override
    public Type<ComboSyncMessage> type() {
        return TYPE;
    }
    
    public static void handle(ComboSyncMessage msg, IPayloadContext ignoredCtx) {
        ClientHandler.handleComboSyncMessage(msg);
    }
}
