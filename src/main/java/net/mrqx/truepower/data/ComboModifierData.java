package net.mrqx.truepower.data;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ExtraCodecs;
import net.mrqx.truepower.TruePowerMod;

import javax.annotation.Nullable;
import java.util.Optional;

public final class ComboModifierData {
    public static final ResourceKey<Registry<ComboModifierEntry>> MODIFIER_REGISTRY_KEY =
        ResourceKey.createRegistryKey(TruePowerMod.prefix("combo_modifiers"));
    public static final ResourceKey<Registry<RemoveReleaseEntry>> REMOVE_RELEASE_REGISTRY_KEY =
        ResourceKey.createRegistryKey(TruePowerMod.prefix("remove_releases"));
    
    public static final String BEHAVIOR_JUMP_VELOCITY_BOOST = "jumpVelocityBoost";
    public static final String BEHAVIOR_AERIAL_RAVE_B3_MULTIHIT = "aerialRaveB3Multihit";
    public static final String BEHAVIOR_ZERO_VELOCITY = "zeroVelocity";
    public static final String BEHAVIOR_RELEASE_QUICK_CHARGE = "releaseActionQuickCharge";
    public static final String BEHAVIOR_CLICK_RAPID_SLASH = "clickRapidSlash";
    public static final String BEHAVIOR_CLICK_SUMMON_BLAST_SWORD = "clickSummonBlastSword";
    public static final String BEHAVIOR_TICK_SUMMON_BLAST_SWORD = "tickSummonBlastSword";
    public static final String BEHAVIOR_TICK_COLLIDE_ACTION = "tickCollideAction";
    public static final String BEHAVIOR_TICK_SHOULD_LOCK_ON = "tickShouldLockOn";
    public static final String BEHAVIOR_TICK_SNAP_LOCK_ON = "tickSnapLockOn";
    public static final String BEHAVIOR_TICK_STUN = "tickStun";
    
    public static final String KEY_CANCEL_ACTION = "cancelAction";
    public static final String KEY_STEP = "step";
    public static final String KEY_FRAME = "frame";
    public static final String KEY_JUMP_ONLY = "jumpOnly";
    public static final String KEY_TICK = "tick";
    public static final String KEY_DISTANCE = "distance";
    public static final String KEY_ACTION = "action";
    public static final String KEY_START = "start";
    public static final String KEY_END = "end";
    public static final String KEY_VALUE = "value";
    
    public static final String PARAM_CHARGE = "charge";
    public static final String PARAM_CHARGE_MIN = "chargeMin";
    public static final String PARAM_CHARGE_MAX = "chargeMax";
    public static final String PARAM_POWERED_COUNT = "poweredCount";
    public static final String PARAM_NORMAL_COUNT = "normalCount";
    public static final String PARAM_MULTIPLIER = "multiplier";
    public static final String PARAM_DURATION = "duration";
    public static final String PARAM_START_TICK = "startTick";
    public static final String PARAM_COUNT = "count";
    public static final String PARAM_ANGLE = "angle";
    public static final String PARAM_DAMAGE = "damage";
    public static final String PARAM_TICK = "tick";
    public static final String PARAM_STUN_VALUE = "stunValue";
    public static final String PARAM_NEED_POWER = "needPower";
    
    public static int optInt(JsonObject obj, String key, int defaultValue) {
        if (obj.has(key) && obj.get(key).isJsonPrimitive()) {
            return obj.get(key).getAsInt();
        }
        return defaultValue;
    }
    
    public static double optDouble(JsonObject obj, String key, double defaultValue) {
        if (obj.has(key) && obj.get(key).isJsonPrimitive()) {
            return obj.get(key).getAsDouble();
        }
        return defaultValue;
    }
    
    public static String optString(JsonObject obj, String key, String defaultValue) {
        if (obj.has(key) && obj.get(key).isJsonPrimitive()) {
            return obj.get(key).getAsString();
        }
        return defaultValue;
    }
    
    public static boolean optBool(JsonObject obj, String key, boolean defaultValue) {
        if (obj.has(key) && obj.get(key).isJsonPrimitive()) {
            return obj.get(key).getAsBoolean();
        }
        return defaultValue;
    }
    
    public record ComboModifierEntry(
        String name,
        int startFrame,
        int endFrame,
        int priority,
        ResourceLocation motionLoc,
        @Nullable JsonElement behavior
    ) {
        public static final Codec<ComboModifierEntry> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                Codec.STRING.fieldOf("name").forGetter(ComboModifierEntry::name),
                Codec.INT.fieldOf("start_frame").forGetter(ComboModifierEntry::startFrame),
                Codec.INT.fieldOf("end_frame").forGetter(ComboModifierEntry::endFrame),
                Codec.INT.fieldOf("priority").forGetter(ComboModifierEntry::priority),
                ResourceLocation.CODEC.fieldOf("motionLoc").forGetter(ComboModifierEntry::motionLoc),
                ExtraCodecs.JSON.optionalFieldOf("behavior").forGetter(e -> Optional.ofNullable(e.behavior))
            ).apply(instance, (name, startFrame, endFrame, priority, motionLoc, behaviorOpt) ->
                new ComboModifierEntry(name, startFrame, endFrame, priority, motionLoc, behaviorOpt.orElse(null))
            )
        );
    }
    
    public record RemoveReleaseEntry(String name, int startFrame, int endFrame, int priority,
                                     ResourceLocation motionLoc) {
        public static final Codec<RemoveReleaseEntry> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                Codec.STRING.fieldOf("name").forGetter(RemoveReleaseEntry::name),
                Codec.INT.fieldOf("start_frame").forGetter(RemoveReleaseEntry::startFrame),
                Codec.INT.fieldOf("end_frame").forGetter(RemoveReleaseEntry::endFrame),
                Codec.INT.fieldOf("priority").forGetter(RemoveReleaseEntry::priority),
                ResourceLocation.CODEC.fieldOf("motionLoc").forGetter(RemoveReleaseEntry::motionLoc)
            ).apply(instance, RemoveReleaseEntry::new)
        );
    }
}
