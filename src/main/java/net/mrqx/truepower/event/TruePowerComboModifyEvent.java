package net.mrqx.truepower.event;

import mods.flammpfeil.slashblade.registry.combo.ComboState;
import net.minecraftforge.eventbus.api.Cancelable;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.fml.event.IModBusEvent;
import net.mrqx.sbr_core.events.ComboStateRegistryEvent;

/**
 *
 */
@Cancelable
public class TruePowerComboModifyEvent extends Event implements IModBusEvent {
    private final ComboState.Builder builder;
    private final ComboState combo;
    private final ComboStateRegistryEvent comboStateRegistryEvent;
    private boolean removeDefaultAction = false;
    
    public TruePowerComboModifyEvent(ComboState.Builder builder, ComboState combo, ComboStateRegistryEvent event) {
        this.builder = builder;
        this.combo = combo;
        this.comboStateRegistryEvent = event;
    }
    
    public ComboState.Builder getBuilder() {
        return this.builder;
    }
    
    public ComboState getCombo() {
        return this.combo;
    }
    
    public ComboStateRegistryEvent getComboStateRegistryEvent() {
        return comboStateRegistryEvent;
    }
    
    public boolean shouldRemoveDefaultAction() {
        return removeDefaultAction;
    }
    
    public void setRemoveDefaultAction(boolean removeDefaultAction) {
        this.removeDefaultAction = removeDefaultAction;
    }
}
