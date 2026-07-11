package net.mrqx.truepower.data;

import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLConstructModEvent;
import net.minecraftforge.registries.DataPackRegistryEvent;
import net.mrqx.truepower.util.ComboModifierManager;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public final class ComboModifierRegistryHandler {
    private ComboModifierRegistryHandler() {
    }
    
    @SubscribeEvent
    public static void onConstructMod(FMLConstructModEvent event) {
        ComboModifierManager.init();
    }
    
    @SubscribeEvent
    public static void onDatapackRegister(DataPackRegistryEvent.NewRegistry event) {
        event.dataPackRegistry(ComboModifierData.MODIFIER_REGISTRY_KEY,
            ComboModifierData.ComboModifierEntry.CODEC,
            ComboModifierData.ComboModifierEntry.CODEC);
        event.dataPackRegistry(ComboModifierData.REMOVE_RELEASE_REGISTRY_KEY,
            ComboModifierData.RemoveReleaseEntry.CODEC,
            ComboModifierData.RemoveReleaseEntry.CODEC);
    }
}
