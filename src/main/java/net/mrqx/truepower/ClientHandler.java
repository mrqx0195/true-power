package net.mrqx.truepower;

import dev.kosmx.playerAnim.api.layered.AnimationStack;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationAccess;
import mods.flammpfeil.slashblade.capability.slashblade.BladeStateAccess;
import mods.flammpfeil.slashblade.registry.ComboStateRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.item.ItemStack;
import net.mrqx.truepower.event.handler.PlayerAnimationRegistryHandler;
import net.mrqx.truepower.network.ComboSyncMessage;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;

@EventBusSubscriber(Dist.CLIENT)
public class ClientHandler {
    @SubscribeEvent
    public static void doClientStuff(FMLClientSetupEvent event) {
        if (ModList.get().isLoaded("playeranimator")) {
            PlayerAnimationRegistryHandler.getInstance().register();
        }
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
            player.getPersistentData().putString("truePower.combo", msg.comboState().toString());
            player.getPersistentData().putBoolean("truePower.canMove", msg.canMove());
            player.getPersistentData().putBoolean("truePower.jumpCancelOnly", msg.jumpCancelOnly());
            player.getPersistentData().putBoolean("truePower.noMoveEnable", msg.noMoveEnable());
        }
    }
}
