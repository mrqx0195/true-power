package net.mrqx.truepower.event.handler.client;

import mods.flammpfeil.slashblade.capability.inputstate.CapabilityInputState;
import mods.flammpfeil.slashblade.item.ItemSlashBlade;
import mods.flammpfeil.slashblade.registry.ComboStateRegistry;
import mods.flammpfeil.slashblade.util.InputCommand;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.mrqx.truepower.network.LockOnTargetChangeMessage;
import net.mrqx.truepower.network.NetworkManager;
import net.mrqx.truepower.util.LockOnUtils;

import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber(Dist.CLIENT)
@OnlyIn(Dist.CLIENT)
public class InputHandler {
    @SubscribeEvent
    public static void mouseScrollEvent(InputEvent.MouseScrollingEvent event) {
        Minecraft minecraft = Minecraft.getInstance();
        LocalPlayer player = minecraft.player;
        if (player == null) {
            return;
        }
        ItemStack itemStack = player.getMainHandItem();
        if (itemStack.isEmpty()) {
            return;
        }
        itemStack.getCapability(ItemSlashBlade.BLADESTATE).ifPresent(state -> {
            if (!state.resolvCurrentComboState(player).equals(ComboStateRegistry.NONE.getId())) {
                event.setCanceled(true);
            }
            
            Entity target = state.getTargetEntity(player.level());
            if (target != null
                && target.isAlive()
                && player.level().isClientSide()
                && player.getCapability(CapabilityInputState.INPUT_STATE)
                .filter(input -> input.getCommands().contains(InputCommand.SNEAK)).isPresent()) {
                List<Entity> entities = LockOnUtils.getLockOnEntitySortedListWithoutRayTrace(player, new ArrayList<>());
                if (!entities.isEmpty()) {
                    if (entities.contains(target)) {
                        state.setTargetEntityId(entities.get(
                            (int) (entities.indexOf(target) + entities.size() + Math.signum(event.getScrollDelta())) % entities.size()
                        ).getId());
                    } else {
                        state.setTargetEntityId(entities.get(0));
                    }
                    LockOnTargetChangeMessage message = new LockOnTargetChangeMessage();
                    message.entityId = state.getTargetEntityId();
                    NetworkManager.INSTANCE.sendToServer(message);
                    event.setCanceled(true);
                }
            }
        });
    }
}
