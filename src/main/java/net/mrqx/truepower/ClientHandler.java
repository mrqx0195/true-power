package net.mrqx.truepower;

import dev.kosmx.playerAnim.api.layered.AnimationStack;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationAccess;
import mods.flammpfeil.slashblade.capability.slashblade.ISlashBladeState;
import mods.flammpfeil.slashblade.item.ItemSlashBlade;
import mods.flammpfeil.slashblade.registry.ComboStateRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.mrqx.truepower.capability.data.ITruePowerData;
import net.mrqx.truepower.compat.TruePowerCompatManager;
import net.mrqx.truepower.network.ComboSyncMessage;

import java.util.List;
import java.util.function.Consumer;

@Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
@OnlyIn(Dist.CLIENT)
public class ClientHandler {
    @SubscribeEvent
    public static void doClientStuff(FMLClientSetupEvent event) {
        TruePowerCompatManager.clientInit(event);
    }
    
    public static boolean shouldLockOnRot(Entity entity, ISlashBladeState state) {
        if (entity instanceof LivingEntity livingEntity) {
            ITruePowerData data = ITruePowerData.get(livingEntity);
            if (data != null) {
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
        }
        return true;
    }
    
    @OnlyIn(Dist.CLIENT)
    public static Consumer<ComboSyncMessage> setClientCombo() {
        return msg -> {
            LocalPlayer player = Minecraft.getInstance().player;
            if (player != null) {
                if (msg.syncCombo) {
                    ItemStack itemStack = player.getMainHandItem();
                    if (itemStack.isEmpty()) {
                        return;
                    }
                    itemStack.getCapability(ItemSlashBlade.BLADESTATE).ifPresent(state -> {
                        state.setComboSeq(msg.comboState);
                        state.setLastActionTime(msg.lastActionTime);
                        if (msg.comboState.equals(ComboStateRegistry.NONE.getId()) || msg.comboState.equals(ComboStateRegistry.STANDBY.getId())) {
                            AnimationStack animationStack = PlayerAnimationAccess.getPlayerAnimLayer(player);
                            animationStack.removeLayer(0);
                        }
                    });
                }
                ITruePowerData data = ITruePowerData.get(player);
                if (data != null) {
                    data.setCombo(msg.comboState.toString());
                    data.setCanMove(msg.canMove);
                    data.setJumpCancelOnly(msg.jumpCancelOnly);
                    data.setNoMoveEnable(msg.noMoveEnable);
                }
            }
        };
    }
}
