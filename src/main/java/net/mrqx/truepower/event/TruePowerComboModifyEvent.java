package net.mrqx.truepower.event;

import mods.flammpfeil.slashblade.registry.combo.ComboState;
import net.mrqx.sbr_core.events.ComboStateRegistryEvent;
import net.neoforged.bus.api.Event;
import net.neoforged.bus.api.ICancellableEvent;
import net.neoforged.fml.event.IModBusEvent;

public class TruePowerComboModifyEvent extends Event implements IModBusEvent, ICancellableEvent {
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
        return builder;
    }
    
    public ComboState getCombo() {
        return combo;
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
