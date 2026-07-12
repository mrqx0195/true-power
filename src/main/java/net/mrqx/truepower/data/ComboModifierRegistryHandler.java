package net.mrqx.truepower.data;

import net.mrqx.truepower.util.ComboModifierManager;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLConstructModEvent;
import net.neoforged.neoforge.registries.DataPackRegistryEvent;

@EventBusSubscriber
public final class ComboModifierRegistryHandler {
    private ComboModifierRegistryHandler() {
    }
    
    @SubscribeEvent
    public static void onConstructMod(FMLConstructModEvent event) {
        ComboModifierManager.init();
    }
    
    @SubscribeEvent
    public static void onRegisterDataPackTypes(DataPackRegistryEvent.NewRegistry event) {
        event.dataPackRegistry(
            ComboModifierData.MODIFIER_REGISTRY_KEY,
            ComboModifierData.ComboModifierEntry.CODEC,
            ComboModifierData.ComboModifierEntry.CODEC
        );
        event.dataPackRegistry(
            ComboModifierData.REMOVE_RELEASE_REGISTRY_KEY,
            ComboModifierData.RemoveReleaseEntry.CODEC,
            ComboModifierData.RemoveReleaseEntry.CODEC
        );
    }
}
