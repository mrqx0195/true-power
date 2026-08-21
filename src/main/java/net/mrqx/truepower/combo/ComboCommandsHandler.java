package net.mrqx.truepower.combo;

import mods.flammpfeil.slashblade.capability.slashblade.BladeStateAccess;
import mods.flammpfeil.slashblade.util.InputCommand;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.mrqx.sbr_core.utils.InputStream;
import net.mrqx.sbr_core.utils.JustSlashArtManager;
import net.mrqx.truepower.config.TruePowerCommonConfig;
import net.mrqx.truepower.util.TruePowerInputTimeLines;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;
import java.util.LinkedList;
import java.util.Map;

public final class ComboCommandsHandler {
    @Nullable
    public static ResourceLocation processComboCommand(LivingEntity livingEntity, EnumSet<InputCommand> commands) {
        if (BladeStateAccess.of(livingEntity.getMainHandItem()).isEmpty()
            || livingEntity.level().isClientSide
            || (TruePowerCommonConfig.BLADE_ARTS_NEED_SHIFT.get() && !commands.contains(InputCommand.SNEAK))) {
            return null;
        }
        ResourceLocation id = null;
        InputStream inputStream = InputStream.getOrCreateInputStream(livingEntity);
        for (Map.Entry<LinkedList<InputStream.TimeLineKeyInput>, ResourceLocation> entry : TruePowerInputTimeLines.INPUTS.entrySet()) {
            if (inputStream.checkTimeLineInput(entry.getKey())) {
                id = entry.getValue();
                break;
            }
        }
        if (id != null) {
            JustSlashArtManager.resetJustCount(livingEntity);
        }
        return id;
    }
}
