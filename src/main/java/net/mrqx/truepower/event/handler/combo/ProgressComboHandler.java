package net.mrqx.truepower.event.handler.combo;

import mods.flammpfeil.slashblade.event.handler.InputCommandEvent;
import mods.flammpfeil.slashblade.item.ItemSlashBlade;
import mods.flammpfeil.slashblade.registry.ComboStateRegistry;
import mods.flammpfeil.slashblade.util.InputCommand;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.EnumSet;

@Mod.EventBusSubscriber
public class ProgressComboHandler {
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void doBaseCombo(InputCommandEvent event) {
        ServerPlayer serverPlayer = event.getEntity();

        EnumSet<InputCommand> old = event.getOld();
        EnumSet<InputCommand> current = event.getCurrent();
        boolean doAttack = (!old.contains(InputCommand.L_DOWN) && current.contains(InputCommand.L_DOWN))
                || (!old.contains(InputCommand.R_DOWN) && current.contains(InputCommand.R_DOWN));

        serverPlayer.getMainHandItem().getCapability(ItemSlashBlade.BLADESTATE).ifPresent(state -> {
            if (doAttack) {
                state.progressCombo(serverPlayer);
                if (!state.getComboSeq().equals(ComboStateRegistry.NONE.getId())) {
                    serverPlayer.swing(InteractionHand.MAIN_HAND);
                }
            }
        });
    }
}
