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

    public static ComboSyncMessage decode(FriendlyByteBuf buf) {
        ComboSyncMessage msg = new ComboSyncMessage();
        msg.comboState = buf.readResourceLocation();
        return msg;
    }

    public static void encode(ComboSyncMessage msg, FriendlyByteBuf buf) {
        buf.writeResourceLocation(msg.comboState);
    }

    public static void handle(ComboSyncMessage msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().setPacketHandled(true);
        if (ctx.get().getDirection() == NetworkDirection.PLAY_TO_CLIENT) {
            Consumer<ResourceLocation> handler = DistExecutor.safeCallWhenOn(Dist.CLIENT, () -> TruePowerComboHelper::setClientCombo);
            if (handler != null) {
                ctx.get().enqueueWork(() -> handler.accept(msg.comboState));
            }

        }
    }
}
