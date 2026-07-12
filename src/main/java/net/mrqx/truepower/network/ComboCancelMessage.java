package net.mrqx.truepower.network;

import mods.flammpfeil.slashblade.capability.slashblade.BladeStateAccess;
import mods.flammpfeil.slashblade.registry.ComboStateRegistry;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.mrqx.truepower.TruePowerMod;
import net.mrqx.truepower.attachment.ITruePowerData;
import net.mrqx.truepower.event.ComboCancelEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record ComboCancelMessage(boolean isJump) implements CustomPacketPayload {
    public static final Type<ComboCancelMessage> TYPE = new Type<>(TruePowerMod.prefix("combo_cancel"));
    public static final StreamCodec<RegistryFriendlyByteBuf, ComboCancelMessage> STREAM_CODEC = CustomPacketPayload
        .codec(ComboCancelMessage::write, ComboCancelMessage::new);
    
    private ComboCancelMessage(RegistryFriendlyByteBuf buf) {
        this(buf.readBoolean());
    }
    
    private void write(RegistryFriendlyByteBuf buf) {
        buf.writeBoolean(this.isJump());
    }
    
    @Override
    public Type<ComboCancelMessage> type() {
        return TYPE;
    }
    
    public static void handle(ComboCancelMessage msg, IPayloadContext ctx) {
        Player player = ctx.player();
        if (player instanceof ServerPlayer serverPlayer) {
            BladeStateAccess.of(serverPlayer.getMainHandItem()).ifPresent(state -> {
                ComboCancelEvent event = new ComboCancelEvent(serverPlayer.getMainHandItem(), state, serverPlayer, msg.isJump());
                if (!NeoForge.EVENT_BUS.post(event).isCanceled()) {
                    state.updateComboSeq(serverPlayer, ComboStateRegistry.NONE.getId());
                    
                    ResourceLocation noneId = ComboStateRegistry.NONE.getId();
                    state.updateComboSeq(serverPlayer, noneId);
                    ITruePowerData data = ITruePowerData.get(serverPlayer);
                    ComboSyncMessage comboSyncMessage = new ComboSyncMessage(
                        noneId,
                        state.getLastActionTime(),
                        data.canMove(),
                        data.isJumpCancelOnly(),
                        data.isNoMoveEnable(),
                        true
                    );
                    
                    PacketDistributor.sendToPlayer(serverPlayer, comboSyncMessage);
                }
            });
        }
    }
}
