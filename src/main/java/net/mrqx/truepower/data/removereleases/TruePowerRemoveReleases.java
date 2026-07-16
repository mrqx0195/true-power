package net.mrqx.truepower.data.removereleases;

import mods.flammpfeil.slashblade.init.DefaultResources;
import mods.flammpfeil.slashblade.registry.ComboStateRegistry;
import mods.flammpfeil.slashblade.registry.combo.ComboState;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.RegistryObject;
import net.mrqx.truepower.TruePowerMod;
import net.mrqx.truepower.data.ComboModifierData;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

@SuppressWarnings({"SameParameterValue", "UnusedReturnValue"})
public final class TruePowerRemoveReleases {
    private static final List<EntryHolder> ENTRIES = new ArrayList<>();
    
    public static void registerAll(BootstapContext<ComboModifierData.RemoveReleaseEntry> context) {
        for (EntryHolder holder : ENTRIES) {
            context.register(holder.key(), holder.entry());
        }
    }
    
    public static EntryHolder build(String name, int startFrame, int endFrame, int priority, Consumer<EntryHolder> consumer) {
        return build(name, startFrame, endFrame, priority, DefaultResources.ExMotionLocation, consumer);
    }
    
    public static EntryHolder build(String name, int startFrame, int endFrame, int priority, ResourceLocation motionLoc, Consumer<EntryHolder> consumer) {
        ResourceKey<ComboModifierData.RemoveReleaseEntry> key = ResourceKey.create(
            ComboModifierData.REMOVE_RELEASE_REGISTRY_KEY, TruePowerMod.prefix(name));
        ComboModifierData.RemoveReleaseEntry entry = new ComboModifierData.RemoveReleaseEntry(
            name, startFrame, endFrame, priority, motionLoc);
        EntryHolder holder = new EntryHolder(key, entry);
        consumer.accept(holder);
        return holder;
    }
    
    public static EntryHolder build(RegistryObject<ComboState> combo, Consumer<EntryHolder> consumer) {
        ComboState comboState = combo.get();
        return build(Objects.requireNonNull(combo.getId()).toLanguageKey(), comboState.getStartFrame(), comboState.getEndFrame(), comboState.getPriority(), comboState.getMotionLoc(), consumer);
    }
    
    static {
        build(ComboStateRegistry.COMBO_A3_END3, ENTRIES::add);
        build(ComboStateRegistry.COMBO_A4_END, ENTRIES::add);
        build(ComboStateRegistry.COMBO_A4_EX_END2, ENTRIES::add);
        build(ComboStateRegistry.COMBO_B7_END3, ENTRIES::add);
    }
    
    public record EntryHolder(ResourceKey<ComboModifierData.RemoveReleaseEntry> key,
                              ComboModifierData.RemoveReleaseEntry entry) {
    }
}
