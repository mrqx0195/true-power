package net.mrqx.truepower.event.handler.combo;

import mods.flammpfeil.slashblade.event.InputCommandEvent;
import mods.flammpfeil.slashblade.item.ItemSlashBlade;
import mods.flammpfeil.slashblade.registry.ComboStateRegistry;
import mods.flammpfeil.slashblade.util.InputCommand;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.mrqx.truepower.util.JustSlashArtManager;

import java.util.EnumSet;

@Mod.EventBusSubscriber
public class OtherSlashHandler {
    public static final EnumSet<InputCommand> UPPER_SLASH_AND_AERIAL_CLEAVE_COMMAND = EnumSet.of(InputCommand.BACK, InputCommand.SNEAK);
    public static final EnumSet<InputCommand> RAPID_SLASH_COMMAND = EnumSet.of(InputCommand.SNEAK, InputCommand.FORWARD);

    @SubscribeEvent
    public static void doOtherSlash(InputCommandEvent event) {
        ServerPlayer serverPlayer = event.getEntity();

        EnumSet<InputCommand> old = event.getOld();
        EnumSet<InputCommand> current = event.getCurrent();
        boolean doAttack = (!old.contains(InputCommand.L_DOWN) && current.contains(InputCommand.L_DOWN))
                || (!old.contains(InputCommand.R_DOWN) && current.contains(InputCommand.R_DOWN));

        serverPlayer.getMainHandItem().getCapability(ItemSlashBlade.BLADESTATE).ifPresent(state -> {
            if (doAttack && state.getComboSeq().equals(ComboStateRegistry.NONE.getId())) {
                if (current.containsAll(UPPER_SLASH_AND_AERIAL_CLEAVE_COMMAND)) {
                    JustSlashArtManager.resetJustCount(serverPlayer);
                    if (serverPlayer.onGround()) {
                        state.updateComboSeq(serverPlayer, ComboStateRegistry.UPPERSLASH.getId());
                    } else {
                        state.updateComboSeq(serverPlayer, ComboStateRegistry.AERIAL_CLEAVE.getId());
                    }
                } else if (current.containsAll(RAPID_SLASH_COMMAND) && serverPlayer.onGround()) {
                    JustSlashArtManager.resetJustCount(serverPlayer);
                    state.updateComboSeq(serverPlayer, ComboStateRegistry.RAPID_SLASH.getId());
                }
            }
        });
    }
}
