package net.mrqx.truepower.compat.epicfight;

import net.mrqx.truepower.compat.epicfight.event.RegistryEventHandler;
import net.mrqx.truepower.compat.epicfight.event.TruePowerEventHandler;
import net.mrqx.truepower.compat.epicfight.registry.TruePowerEpicFightRegistries;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import yesman.epicfight.api.event.EpicFightEventHooks;

public class TruePowerEpicFight {
    public static void onModConstruct(IEventBus modEventBus, ModContainer container) {
        TruePowerEpicFightRegistries.SKILL_REGISTRY.register(modEventBus);
        TruePowerEpicFightRegistries.MOVESET_REGISTRY.register(modEventBus);
        TruePowerEpicFightRegistries.ITEM_PRESET_REGISTRY.register(modEventBus);
        NeoForge.EVENT_BUS.register(TruePowerEventHandler.class);
    }
    
    public static void commonInit(FMLCommonSetupEvent event) {
        EpicFightEventHooks.Registry.WEAPON_CAPABILITY_PRESET.registerEvent(RegistryEventHandler::onWeaponCapabilityPresetRegistryEvent);
    }
}
