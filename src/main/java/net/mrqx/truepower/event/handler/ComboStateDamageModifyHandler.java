package net.mrqx.truepower.event.handler;

import mods.flammpfeil.slashblade.event.SlashBladeEvent;
import mods.flammpfeil.slashblade.registry.ComboStateRegistry;
import mods.flammpfeil.slashblade.registry.combo.ComboState;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;

@EventBusSubscriber
public final class ComboStateDamageModifyHandler {
    @SubscribeEvent
    public static void onDoSlashEvent(SlashBladeEvent.DoSlashEvent event) {
        ComboState combo = ComboStateRegistry.REGISTRY.get(event.getSlashBladeState().getComboSeq());
        if (combo == null) {
            return;
        }
        LivingEntity entity = event.getUser();
        if (combo.equals(ComboStateRegistry.COMBO_A1.get())) {
            event.setDamage(0.4);
        } else if (combo.equals(ComboStateRegistry.COMBO_A2.get())) {
            event.setDamage(0.5);
        } else if (combo.equals(ComboStateRegistry.COMBO_A3.get())) {
            event.setDamage(0.7);
        } else if (combo.equals(ComboStateRegistry.COMBO_A4_EX.get())) {
            event.setDamage(0.8);
        } else if (combo.equals(ComboStateRegistry.COMBO_A5.get())) {
            event.setDamage(2.64);
        } else if (combo.equals(ComboStateRegistry.COMBO_C.get())) {
            event.setDamage(ComboState.getElapsed(entity) < 3 ? 1.6 : 1.7);
        } else if (combo.equals(ComboStateRegistry.COMBO_B1.get())
            || combo.equals(ComboStateRegistry.COMBO_B2.get())
            || combo.equals(ComboStateRegistry.COMBO_B3.get())
            || combo.equals(ComboStateRegistry.COMBO_B4.get())
            || combo.equals(ComboStateRegistry.COMBO_B5.get())
            || combo.equals(ComboStateRegistry.COMBO_B6.get())
        ) {
            event.setDamage(0.15);
        } else if (combo.equals(ComboStateRegistry.COMBO_B1_END.get())
            || combo.equals(ComboStateRegistry.COMBO_B_END.get())) {
            event.setDamage(0.8);
        } else if (combo.equals(ComboStateRegistry.COMBO_B7.get())) {
            event.setDamage(ComboState.getElapsed(entity) < 10 ? 0.15 : 0.8);
        } else if (combo.equals(ComboStateRegistry.AERIAL_RAVE_A1.get())) {
            event.setDamage(0.5);
        } else if (combo.equals(ComboStateRegistry.AERIAL_RAVE_A2.get())) {
            event.setDamage(0.6);
        } else if (combo.equals(ComboStateRegistry.AERIAL_RAVE_A3.get())) {
            event.setDamage(0.6);
        } else if (combo.equals(ComboStateRegistry.AERIAL_RAVE_B3.get())) {
            event.setDamage(0.8);
        } else if (combo.equals(ComboStateRegistry.AERIAL_RAVE_B4.get())) {
            event.setDamage(0.5);
        } else if (combo.equals(ComboStateRegistry.AERIAL_CLEAVE.get())) {
            event.setDamage(1);
        } else if (combo.equals(ComboStateRegistry.AERIAL_CLEAVE_LOOP.get())) {
            event.setDamage(1);
        } else if (combo.equals(ComboStateRegistry.AERIAL_CLEAVE_LANDING.get())) {
            event.setDamage(1);
        } else if (combo.equals(ComboStateRegistry.RAPID_SLASH.get())) {
            event.setDamage(event.getRoll() == -30 ? 0.4 : 0.15);
        } else if (combo.equals(ComboStateRegistry.RISING_STAR.get())) {
            event.setDamage(0.8);
        }
    }
}
