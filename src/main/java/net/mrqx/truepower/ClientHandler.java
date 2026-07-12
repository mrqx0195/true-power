package net.mrqx.truepower;

import dev.kosmx.playerAnim.api.layered.AnimationStack;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationAccess;
import mods.flammpfeil.slashblade.capability.slashblade.BladeStateAccess;
import mods.flammpfeil.slashblade.capability.slashblade.ISlashBladeState;
import mods.flammpfeil.slashblade.registry.ComboStateRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.mrqx.truepower.attachment.ITruePowerData;
import net.mrqx.truepower.compat.TruePowerCompatManager;
import net.mrqx.truepower.network.ComboSyncMessage;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;

import java.util.List;

@EventBusSubscriber(Dist.CLIENT)
public final class ClientHandler {
    @SubscribeEvent
    public static void doClientStuff(FMLClientSetupEvent event) {
        TruePowerCompatManager.clientInit(event);
    }
    
    public static boolean shouldLockOnRot(Entity entity, ISlashBladeState state) {
        if (entity instanceof LivingEntity livingEntity) {
            ITruePowerData data = ITruePowerData.get(livingEntity);
            List<ITruePowerData.BoolInterval> intervals = data.getLockOnIntervals();
            if (intervals != null && !intervals.isEmpty()) {
                long elapsed = state.getElapsedTime(livingEntity);
                for (ITruePowerData.BoolInterval interval : intervals) {
                    if (elapsed >= interval.start() && elapsed <= interval.end()) {
                        return interval.value();
                    }
                }
            }
        }
        return true;
    }
    
    public static void handleComboSyncMessage(ComboSyncMessage msg) {
        LocalPlayer player = Minecraft.getInstance().player;
        if (player != null) {
            if (msg.syncCombo()) {
                ItemStack itemStack = player.getMainHandItem();
                if (itemStack.isEmpty()) {
                    return;
                }
                BladeStateAccess.of(itemStack).ifPresent(state -> {
                    state.setComboSeq(msg.comboState());
                    state.setLastActionTime(msg.lastActionTime());
                    if (msg.comboState().equals(ComboStateRegistry.NONE.getId()) || msg.comboState().equals(ComboStateRegistry.STANDBY.getId())) {
                        AnimationStack animationStack = PlayerAnimationAccess.getPlayerAnimLayer(player);
                        animationStack.removeLayer(0);
                    }
                });
            }
            ITruePowerData data = ITruePowerData.get(player);
            data.setCombo(msg.comboState().toString());
            data.setCanMove(msg.canMove());
            data.setJumpCancelOnly(msg.jumpCancelOnly());
            data.setNoMoveEnable(msg.noMoveEnable());
        }
    }
}
