package net.mrqx.truepower.event.handler.client;

import mods.flammpfeil.slashblade.capability.inputstate.CapabilityInputState;
import mods.flammpfeil.slashblade.capability.slashblade.BladeStateAccess;
import mods.flammpfeil.slashblade.registry.ComboStateRegistry;
import mods.flammpfeil.slashblade.util.InputCommand;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.mrqx.truepower.network.LockOnTargetChangeMessage;
import net.mrqx.truepower.util.LockOnUtils;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.InputEvent;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.ArrayList;
import java.util.List;

@EventBusSubscriber(Dist.CLIENT)
@OnlyIn(Dist.CLIENT)
public final class InputHandler {
    @SubscribeEvent
    public static void mouseScrollEvent(InputEvent.MouseScrollingEvent event) {
        Minecraft mc = Minecraft.getInstance();
        LocalPlayer player = mc.player;
        if (player == null) {
            return;
        }
        ItemStack stack = player.getMainHandItem();
        if (stack.isEmpty()) {
            return;
        }
        
        BladeStateAccess.of(stack).ifPresent(state -> {
            if (!state.resolvCurrentComboState(player).equals(ComboStateRegistry.NONE.getId())) {
                event.setCanceled(true);
            }
            Entity target = state.getTargetEntity(player.level());
            if (target != null && target.isAlive() && player.level().isClientSide
                && player.getData(CapabilityInputState.INPUT_STATE).getCommands().contains(InputCommand.SNEAK)) {
                List<Entity> entities = LockOnUtils.getLockOnEntitySortedListWithoutRayTrace(player, new ArrayList<>());
                if (!entities.isEmpty()) {
                    if (entities.contains(target)) {
                        int idx = entities.indexOf(target);
                        int nextIdx = (int) ((idx + entities.size() + Math.signum(event.getScrollDeltaY() == 0 ? -event.getScrollDeltaX() : event.getScrollDeltaY())) % entities.size());
                        state.setTargetEntityId(entities.get(nextIdx).getId());
                    } else {
                        state.setTargetEntityId(entities.getFirst().getId());
                    }
                    PacketDistributor.sendToServer(new LockOnTargetChangeMessage(state.getTargetEntityId()));
                    event.setCanceled(true);
                }
            }
        });
    }
}
