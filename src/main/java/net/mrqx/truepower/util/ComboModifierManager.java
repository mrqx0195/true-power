package net.mrqx.truepower.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.mojang.serialization.JsonOps;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.mrqx.truepower.TruePowerMod;
import net.mrqx.truepower.data.ComboModifierData;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class ComboModifierManager {
    private static final Gson GSON = new GsonBuilder().create();
    private static final Map<ModifierKey, ComboModifierData.ComboModifierEntry> MODIFIERS = new HashMap<>();
    private static final Map<ModifierKey, ComboModifierData.RemoveReleaseEntry> REMOVE_RELEASES = new HashMap<>();
    private static boolean initialized = false;
    
    public static void init() {
        if (initialized) {
            TruePowerMod.LOGGER.warn("ComboModifierManager has already been initialized!");
            return;
        }
        CustomEarlyResourceLoader.load(List.of(new ComboModifierListener(), new RemoveReleaseListener()));
        TruePowerMod.LOGGER.info("ComboModifierManager initialized successfully: modifiers={}, removeReleases={}", MODIFIERS.size(), REMOVE_RELEASES.size());
        initialized = true;
    }
    
    @Nullable
    public static ComboModifierData.ComboModifierEntry findModifier(int startFrame, int endFrame, int priority, ResourceLocation motionLoc) {
        return MODIFIERS.get(new ModifierKey(startFrame, endFrame, priority, motionLoc));
    }
    
    @Nullable
    public static ComboModifierData.RemoveReleaseEntry findRemoveRelease(int startFrame, int endFrame, int priority, ResourceLocation motionLoc) {
        return REMOVE_RELEASES.get(new ModifierKey(startFrame, endFrame, priority, motionLoc));
    }
    
    public record ModifierKey(int startFrame, int endFrame, int priority, ResourceLocation motionLoc) {
    }
    
    public static class ComboModifierListener extends SimpleJsonResourceReloadListener {
        public ComboModifierListener() {
            super(GSON, "truepower/combo_modifiers");
        }
        
        @Override
        protected void apply(Map<ResourceLocation, JsonElement> objects,
                             ResourceManager manager, ProfilerFiller profiler) {
            for (Map.Entry<ResourceLocation, JsonElement> entry : objects.entrySet()) {
                try {
                    ComboModifierData.ComboModifierEntry parsed = ComboModifierData.ComboModifierEntry.CODEC
                        .parse(JsonOps.INSTANCE, entry.getValue())
                        .getOrThrow();
                    MODIFIERS.put(new ModifierKey(parsed.startFrame(), parsed.endFrame(), parsed.priority(), parsed.motionLoc()), parsed);
                } catch (Exception e) {
                    TruePowerMod.LOGGER.error("Failed to load combo_modifiers entry: {}: {}", entry.getKey(), e.getMessage());
                }
            }
        }
    }
    
    public static class RemoveReleaseListener extends SimpleJsonResourceReloadListener {
        public RemoveReleaseListener() {
            super(GSON, "truepower/remove_releases");
        }
        
        @Override
        protected void apply(Map<ResourceLocation, JsonElement> objects,
                             ResourceManager manager, ProfilerFiller profiler) {
            for (Map.Entry<ResourceLocation, JsonElement> entry : objects.entrySet()) {
                try {
                    ComboModifierData.RemoveReleaseEntry parsed = ComboModifierData.RemoveReleaseEntry.CODEC
                        .parse(JsonOps.INSTANCE, entry.getValue())
                        .getOrThrow();
                    REMOVE_RELEASES.put(new ModifierKey(parsed.startFrame(), parsed.endFrame(), parsed.priority(), parsed.motionLoc()), parsed);
                } catch (Exception e) {
                    TruePowerMod.LOGGER.error("Failed to load remove_releases entry: {}: {}", entry.getKey(), e.getMessage());
                }
            }
        }
    }
}
