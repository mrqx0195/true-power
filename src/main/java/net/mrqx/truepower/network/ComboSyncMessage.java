package net.mrqx.truepower.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;
import net.mrqx.truepower.util.TruePowerComboHelper;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class ComboSyncMessage {
    public ResourceLocation comboState;
    public boolean canMove;
    public boolean jumpCancelOnly;
    public boolean noMoveEnable;
    public boolean syncCombo;

    public static ComboSyncMessage decode(FriendlyByteBuf buf) {
        ComboSyncMessage msg = new ComboSyncMessage();
        msg.comboState = buf.readResourceLocation();
        msg.canMove = buf.readBoolean();
        msg.jumpCancelOnly = buf.readBoolean();
        msg.noMoveEnable = buf.readBoolean();
        msg.syncCombo = buf.readBoolean();
        return msg;
    }

    public static void encode(ComboSyncMessage msg, FriendlyByteBuf buf) {
        buf.writeResourceLocation(msg.comboState);
        buf.writeBoolean(msg.canMove);
        buf.writeBoolean(msg.jumpCancelOnly);
        buf.writeBoolean(msg.noMoveEnable);
        buf.writeBoolean(msg.syncCombo);
    }

    public static void handle(ComboSyncMessage msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().setPacketHandled(true);
        if (ctx.get().getDirection() == NetworkDirection.PLAY_TO_CLIENT) {
            Consumer<ComboSyncMessage> handler = DistExecutor.safeCallWhenOn(Dist.CLIENT, () -> TruePowerComboHelper::setClientCombo);
            if (handler != null) {
                ctx.get().enqueueWork(() -> handler.accept(msg));
            }

        }
    }
}
