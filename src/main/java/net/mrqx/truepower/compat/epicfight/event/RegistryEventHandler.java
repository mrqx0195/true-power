package net.mrqx.truepower.compat.epicfight.event;

import mods.flammpfeil.slashblade.item.ItemSlashBlade;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.mrqx.truepower.compat.epicfight.registry.TruePowerEpicFightRegistries;
import yesman.epicfight.api.event.types.registry.WeaponCapabilityPresetRegistryEvent;
import yesman.epicfight.world.capabilities.item.CapabilityItem;

import java.util.Map;
import java.util.function.Function;

public final class RegistryEventHandler {
    public static void onWeaponCapabilityPresetRegistryEvent(WeaponCapabilityPresetRegistryEvent event) {
        Map<ResourceLocation, Function<Item, ? extends CapabilityItem.Builder<?>>> typeEntry = event.getTypeEntry();
        BuiltInRegistries.ITEM.entrySet().forEach(item -> {
            if (item.getValue() instanceof ItemSlashBlade) {
                typeEntry.put(item.getKey().location(), __ -> TruePowerEpicFightRegistries.SLASHBLADE_WEAPON.get());
            }
        });
    }
}
