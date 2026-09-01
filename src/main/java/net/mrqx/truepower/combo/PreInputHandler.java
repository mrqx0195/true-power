package net.mrqx.truepower.combo;

import mods.flammpfeil.slashblade.capability.inputstate.CapabilityInputState;
import mods.flammpfeil.slashblade.capability.slashblade.ISlashBladeState;
import mods.flammpfeil.slashblade.event.SlashBladeEvent;
import mods.flammpfeil.slashblade.registry.ComboStateRegistry;
import mods.flammpfeil.slashblade.registry.combo.ComboState;
import mods.flammpfeil.slashblade.util.InputCommand;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.mrqx.sbr_core.utils.InputStream;
import net.mrqx.sbr_core.utils.JustSlashArtManager;
import net.mrqx.truepower.config.TruePowerCommonConfig;
import net.mrqx.truepower.event.PreInputEvent;
import net.mrqx.truepower.util.TruePowerInputTimeLines;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.NeoForge;

import java.util.EnumSet;
import java.util.LinkedList;
import java.util.Map;

@EventBusSubscriber
public final class PreInputHandler {
    @SubscribeEvent
    public static void onTick(SlashBladeEvent.UpdateEvent event) {
        Level level = event.getLevel();
        if (!event.isSelected() || level.isClientSide || !TruePowerCommonConfig.ENABLE_PRE_INPUT.get()) {
            return;
        }
        ISlashBladeState state = event.getSlashBladeState();
        Entity entity = event.getEntity();
        if (!state.getComboRoot().equals(ComboStateRegistry.STANDBY.getId()) || state.getLastActionTime() >= entity.level().getGameTime() - 1 || !(entity instanceof LivingEntity livingEntity)) {
            return;
        }
        Map.Entry<Integer, ResourceLocation> currentLoc = state.resolvCurrentComboStateTicks(livingEntity);
        ResourceLocation id = null;
        InputStream inputStream = InputStream.getOrCreateInputStream(livingEntity);
        EnumSet<InputCommand> commands = entity.getData(CapabilityInputState.INPUT_STATE.get()).getCommands(livingEntity);
        if (!TruePowerCommonConfig.BLADE_ARTS_NEED_SHIFT.get() || commands.contains(InputCommand.SNEAK)) {
            for (Map.Entry<LinkedList<InputStream.TimeLineKeyInput>, ResourceLocation> entry : TruePowerInputTimeLines.PRE_INPUTS.entrySet()) {
                if (inputStream.checkTimeLineInput(entry.getKey())) {
                    id = entry.getValue();
                    break;
                }
            }
        }
        PreInputEvent preInputEvent = new PreInputEvent(event.getBlade(), state, level, livingEntity, inputStream, id, currentLoc);
        NeoForge.EVENT_BUS.post(preInputEvent);
        id = preInputEvent.getPreInputComboStateId();
        if (id != null) {
            ComboState current = ComboStateRegistry.REGISTRY.get(currentLoc.getValue());
            ComboState comboState = ComboStateRegistry.REGISTRY.get(id);
            if (!ComboStateRegistry.NONE.getId().equals(id) && !currentLoc.getValue().equals(id)) {
                if (current != null && comboState != null && current.getPriority() > comboState.getPriority()) {
                    JustSlashArtManager.resetJustCount(livingEntity);
                    state.updateComboSeq(livingEntity, id);
                }
            }
        }
    }
}
