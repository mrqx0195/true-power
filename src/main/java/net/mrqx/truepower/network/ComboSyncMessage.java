package net.mrqx.truepower.network;

import dev.kosmx.playerAnim.api.layered.AnimationStack;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationAccess;
import mods.flammpfeil.slashblade.capability.slashblade.BladeStateAccess;
import mods.flammpfeil.slashblade.registry.ComboStateRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
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
        buf.writeResourceLocation(this.comboState);
        buf.writeLong(this.lastActionTime);
        buf.writeBoolean(this.canMove);
        buf.writeBoolean(this.jumpCancelOnly);
        buf.writeBoolean(this.noMoveEnable);
        buf.writeBoolean(this.syncCombo);
    }
    
    @Override
    public Type<ComboSyncMessage> type() {
        return TYPE;
    }
    
    public static void handle(ComboSyncMessage msg, IPayloadContext ignoredCtx) {
        LocalPlayer player = Minecraft.getInstance().player;
        if (player != null) {
            if (msg.syncCombo) {
                ItemStack itemStack = player.getMainHandItem();
                if (itemStack.isEmpty()) {
                    return;
                }
                BladeStateAccess.of(itemStack).ifPresent(state -> {
                    state.setComboSeq(msg.comboState);
                    state.setLastActionTime(msg.lastActionTime);
                    if (msg.comboState.equals(ComboStateRegistry.NONE.getId()) || msg.comboState.equals(ComboStateRegistry.STANDBY.getId())) {
                        AnimationStack animationStack = PlayerAnimationAccess.getPlayerAnimLayer(player);
                        animationStack.removeLayer(0);
                    }
                });
            }
            player.getPersistentData().putString("truePower.combo", msg.comboState.toString());
            player.getPersistentData().putBoolean("truePower.canMove", msg.canMove);
            player.getPersistentData().putBoolean("truePower.jumpCancelOnly", msg.jumpCancelOnly);
            player.getPersistentData().putBoolean("truePower.noMoveEnable", msg.noMoveEnable);
        }
    }
}
