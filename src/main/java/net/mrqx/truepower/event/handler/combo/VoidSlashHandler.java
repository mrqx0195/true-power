package net.mrqx.truepower.event.handler.combo;

import mods.flammpfeil.slashblade.item.ItemSlashBlade;
import mods.flammpfeil.slashblade.util.InputCommand;
import net.minecraft.world.entity.LivingEntity;
import net.mrqx.sbr_core.utils.InputStream;

import java.util.EnumSet;
import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicBoolean;

public class VoidSlashHandler {
    private static final LinkedList<InputStream.TimeLineKeyInput> VOID_SLASH_INPUT_TIME_LINE = new LinkedList<>();

    static {
        VOID_SLASH_INPUT_TIME_LINE.add(new InputStream.TimeLineKeyInput(4, 0, InputCommand.FORWARD, EnumSet.noneOf(InputCommand.class), InputStream.InputType.START));
        VOID_SLASH_INPUT_TIME_LINE.add(new InputStream.TimeLineKeyInput(4, 0, InputCommand.BACK, EnumSet.noneOf(InputCommand.class), InputStream.InputType.START));
    }

    public static boolean doVoidSlash(LivingEntity livingEntity, EnumSet<InputCommand> commands) {
        InputStream inputStream = InputStream.getOrCreateInputStream(livingEntity);
        AtomicBoolean flag = new AtomicBoolean(false);

        livingEntity.getMainHandItem().getCapability(ItemSlashBlade.BLADESTATE).ifPresent(state -> {
            if (livingEntity.onGround()
                    && commands.contains(InputCommand.SNEAK)
                    && commands.contains(InputCommand.R_CLICK)
                    && inputStream.checkTimeLineInput(VOID_SLASH_INPUT_TIME_LINE)
            ) {
                flag.set(true);
            }
        });
        return flag.get();
    }

}
