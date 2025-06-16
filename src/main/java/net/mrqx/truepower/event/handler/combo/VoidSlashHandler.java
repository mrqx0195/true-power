package net.mrqx.truepower.event.handler.combo;

import mods.flammpfeil.slashblade.event.InputCommandEvent;
import mods.flammpfeil.slashblade.item.ItemSlashBlade;
import mods.flammpfeil.slashblade.registry.ComboStateRegistry;
import mods.flammpfeil.slashblade.util.InputCommand;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.mrqx.sbr_core.utils.InputStream;
import net.mrqx.truepower.registry.TruePowerComboStateRegistry;
import net.mrqx.truepower.util.JustSlashArtManager;

import java.util.EnumSet;
import java.util.LinkedList;

@Mod.EventBusSubscriber
public class VoidSlashHandler {
    private static final LinkedList<InputStream.TimeLineKeyInput> VOID_SLASH_INPUT_TIME_LINE = new LinkedList<>();

    static {
        VOID_SLASH_INPUT_TIME_LINE.add(new InputStream.TimeLineKeyInput(3, 0, InputCommand.FORWARD, EnumSet.noneOf(InputCommand.class), InputStream.InputType.START));
        VOID_SLASH_INPUT_TIME_LINE.add(new InputStream.TimeLineKeyInput(3, 0, InputCommand.BACK, EnumSet.noneOf(InputCommand.class), InputStream.InputType.START));
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void doVoidSlash(InputCommandEvent event) {
        ServerPlayer serverPlayer = event.getEntity();
        InputStream inputStream = InputStream.getOrCreateInputStream(serverPlayer);

        EnumSet<InputCommand> old = event.getOld();
        EnumSet<InputCommand> current = event.getCurrent();

        boolean doAttack = (!old.contains(InputCommand.L_DOWN) && current.contains(InputCommand.L_DOWN))
                || (!old.contains(InputCommand.R_DOWN) && current.contains(InputCommand.R_DOWN));

        serverPlayer.getMainHandItem().getCapability(ItemSlashBlade.BLADESTATE).ifPresent(state -> {
            if (state.getComboSeq().equals(ComboStateRegistry.NONE.getId())) {
                if (serverPlayer.onGround() && event.getCurrent().contains(InputCommand.SNEAK) && doAttack
                        && inputStream.checkTimeLineInput(VOID_SLASH_INPUT_TIME_LINE)
                ) {
                    JustSlashArtManager.resetJustCount(serverPlayer);
                    state.updateComboSeq(serverPlayer, TruePowerComboStateRegistry.VOID_SLASH.getId());
                }
            }
        });
    }
}
