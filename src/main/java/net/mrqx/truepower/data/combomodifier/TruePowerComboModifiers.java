package net.mrqx.truepower.data.combomodifier;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import mods.flammpfeil.slashblade.init.DefaultResources;
import mods.flammpfeil.slashblade.registry.ComboStateRegistry;
import mods.flammpfeil.slashblade.registry.combo.ComboState;
import mods.flammpfeil.slashblade.util.TimeValueHelper;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.mrqx.truepower.TruePowerMod;
import net.mrqx.truepower.data.ComboModifierData;
import net.mrqx.truepower.registry.TruePowerComboStateRegistry;
import net.mrqx.truepower.util.CollideAction;
import net.neoforged.neoforge.registries.DeferredHolder;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

@SuppressWarnings("UnusedReturnValue")
public final class TruePowerComboModifiers {
    public static final List<EntryHolder> ENTRIES = new ArrayList<>();
    
    public static void registerAll(BootstrapContext<ComboModifierData.ComboModifierEntry> context) {
        for (EntryHolder holder : ENTRIES) {
            context.register(holder.key(), holder.entry());
        }
    }
    
    static {
        builder(ComboStateRegistry.COMBO_A1)
            .behavior(b -> b
                .cancel(2).jumpCancelOnly()
                .step(0, 0.5)
            )
            .build(ENTRIES::add);
        builder(ComboStateRegistry.COMBO_A1_END)
            .behavior(b -> b
                .cancel(0).jumpCancelOnly()
            )
            .build(ENTRIES::add);
        builder(ComboStateRegistry.COMBO_A2)
            .behavior()
            .build(ENTRIES::add);
        builder(ComboStateRegistry.COMBO_A2_END)
            .behavior()
            .build(ENTRIES::add);
        
        builder(ComboStateRegistry.COMBO_A3)
            .behavior(b -> b
                .cancel(10).jumpCancelOnly()
                .step(1, 1)
                .step(5, 1)
            )
            .build(ENTRIES::add);
        builder(ComboStateRegistry.COMBO_A3_END)
            .behavior(b -> b
                .cancel(0).jumpCancelOnly()
            )
            .build(ENTRIES::add);
        builder(ComboStateRegistry.COMBO_A3_END2)
            .behavior(b -> b
                .jumpCancelOnly()
                .releaseActionQuickCharge()
            )
            .build(ENTRIES::add);
        
        builder(ComboStateRegistry.COMBO_A4)
            .behavior(b -> b
                .cancel(15)
                .step(7, 1.5)
                .releaseActionQuickCharge(15)
            )
            .build(ENTRIES::add);
        builder(ComboStateRegistry.COMBO_A4_EX)
            .behavior(b -> b
                .cancel(25)
                .step(6, 2)
                .releaseActionQuickCharge(25)
            )
            .build(ENTRIES::add);
        
        builder(ComboStateRegistry.COMBO_A5)
            .behavior(b -> b
                .cancel(70)
                .step(14, 3)
                .releaseActionQuickCharge(30, 50)
            )
            .build(ENTRIES::add);
        
        builder(ComboStateRegistry.COMBO_B1)
            .behavior(b -> b
                .step(0, 1.5)
            )
            .build(ENTRIES::add);
        builder(ComboStateRegistry.COMBO_B2)
            .behavior(b -> b
                .stun(0, 1)
                .stun(1, 1)
                .stun(2, 1)
                .stun(3, 1)
                .stun(4, 1)
                .stun(5, 1)
                .stun(6, 1)
            )
            .build(ENTRIES::add);
        builder(ComboStateRegistry.COMBO_B_END)
            .behavior(b -> b
                .cancel(12).jumpCancelOnly()
                .step(7, 1.5).releaseActionQuickCharge(12)
                .stun(9, 50)
                .stun(9, 20, true)
                .stun(10, 50)
                .stun(10, 20, true)
            )
            .build(ENTRIES::add);
        builder(ComboStateRegistry.COMBO_B7)
            .behavior(b -> b
                .cancel(15).jumpCancelOnly()
                .step(10, 1.5)
                .releaseActionQuickCharge(15)
                .stun(0, 1)
                .stun(1, 1)
                .stun(2, 1)
                .stun(3, 1)
                .stun(4, 1)
                .stun(5, 1)
                .stun(6, 1)
                .stun(7, 1)
                .stun(12, 50)
                .stun(12, 20, true)
                .stun(13, 50)
                .stun(13, 20, true)
            )
            .build(ENTRIES::add);
        
        builder(ComboStateRegistry.COMBO_C)
            .behavior(b -> b
                .cancel(20).jumpCancelOnly()
                .tickSummonBlastSword(6, 1)
            )
            .build(ENTRIES::add);
        builder(ComboStateRegistry.COMBO_C_END)
            .behavior(b -> b.cancel(0))
            .build(ENTRIES::add);
        
        builder(ComboStateRegistry.AERIAL_RAVE_A1)
            .behavior(BehaviorBuilder::zeroVelocity)
            .build(ENTRIES::add);
        builder(ComboStateRegistry.AERIAL_RAVE_A2)
            .behavior()
            .build(ENTRIES::add);
        builder(ComboStateRegistry.AERIAL_RAVE_A3)
            .behavior()
            .build(ENTRIES::add);
        builder(ComboStateRegistry.AERIAL_RAVE_B3)
            .behavior(b -> b
                .aerialRaveB3Multihit()
                .stun((int) TimeValueHelper.getTicksFromFrames(5), 15)
                .stun((int) TimeValueHelper.getTicksFromFrames(10), 15)
            )
            .build(ENTRIES::add);
        builder(ComboStateRegistry.AERIAL_RAVE_B4)
            .behavior(b -> b
                .stun((int) TimeValueHelper.getTicksFromFrames(10), 15)
                .stun((int) TimeValueHelper.getTicksFromFrames(10) + 1, 15)
            )
            .build(ENTRIES::add);
        
        builder(ComboStateRegistry.UPPERSLASH)
            .behavior(b -> b.stun((int) TimeValueHelper.getTicksFromFrames(7), 35, true))
            .build(ENTRIES::add);
        builder(ComboStateRegistry.UPPERSLASH_JUMP)
            .behavior(BehaviorBuilder::jumpVelocityBoost)
            .build(ENTRIES::add);
        
        builder(ComboStateRegistry.AERIAL_CLEAVE)
            .behavior()
            .build(ENTRIES::add);
        builder(ComboStateRegistry.AERIAL_CLEAVE_LOOP)
            .behavior(b -> b
                .cancel(0).jumpCancelOnly()
            )
            .build(ENTRIES::add);
        builder(ComboStateRegistry.AERIAL_CLEAVE_LANDING)
            .behavior()
            .build(ENTRIES::add);
        
        builder(ComboStateRegistry.RAPID_SLASH)
            .behavior(b -> b
                .clickRapidSlash()
                .tickCollideAction(0, 6, CollideAction.IGNORE)
                .tickShouldLockOn(2, 6, false)
                .tickSnapLockOn(0, 2)
            )
            .build(ENTRIES::add);
        builder(ComboStateRegistry.RAPID_SLASH_END)
            .behavior(BehaviorBuilder::clickSummonBlastSword)
            .build(ENTRIES::add);
        builder(ComboStateRegistry.RAPID_SLASH_QUICK)
            .behavior()
            .build(ENTRIES::add);
        
        builder(ComboStateRegistry.RISING_STAR)
            .behavior(b -> b
                .jumpVelocityBoost()
                .stun((int) TimeValueHelper.getTicksFromFrames(9), 20)
            )
            .build(ENTRIES::add);
        
        builder(ComboStateRegistry.JUDGEMENT_CUT)
            .behavior()
            .build(ENTRIES::add);
        builder(ComboStateRegistry.JUDGEMENT_CUT_SLASH)
            .behavior()
            .build(ENTRIES::add);
        builder(ComboStateRegistry.JUDGEMENT_CUT_SLASH_JUST)
            .behavior()
            .build(ENTRIES::add);
        
        builder(ComboStateRegistry.VOID_SLASH)
            .behavior(b -> b
                .cancel(20).jumpCancelOnly()
            )
            .build(ENTRIES::add);
        builder(TruePowerComboStateRegistry.VOID_SLASH)
            .behavior(b -> b
                .cancel(20).jumpCancelOnly()
            )
            .build(ENTRIES::add);
        
        builder(ComboStateRegistry.PIERCING_2)
            .behavior(b -> b
                .stun(0, 20)
                .stun(1, 20)
                .stun(2, 20)
                .cancel(10).jumpCancelOnly()
            )
            .build(ENTRIES::add);
    }
    
    public record EntryHolder(ResourceKey<ComboModifierData.ComboModifierEntry> key,
                              ComboModifierData.ComboModifierEntry entry) {
    }
    
    public static final class BehaviorBuilder {
        public final JsonObject root;
        
        private BehaviorBuilder() {
            this.root = new JsonObject();
            JsonObject cancel = new JsonObject();
            cancel.addProperty(ComboModifierData.KEY_FRAME, -1);
            cancel.addProperty(ComboModifierData.KEY_JUMP_ONLY, false);
            this.root.add(ComboModifierData.KEY_CANCEL_ACTION, cancel);
        }
        
        public BehaviorBuilder cancel(int frame) {
            root.getAsJsonObject(ComboModifierData.KEY_CANCEL_ACTION).addProperty(ComboModifierData.KEY_FRAME, frame);
            return this;
        }
        
        public BehaviorBuilder jumpCancelOnly() {
            root.getAsJsonObject(ComboModifierData.KEY_CANCEL_ACTION).addProperty(ComboModifierData.KEY_JUMP_ONLY, true);
            return this;
        }
        
        public BehaviorBuilder step(int tick, double distance) {
            JsonArray steps = root.has(ComboModifierData.KEY_STEP) ? root.getAsJsonArray(ComboModifierData.KEY_STEP) : new JsonArray();
            if (!root.has(ComboModifierData.KEY_STEP)) {
                root.add(ComboModifierData.KEY_STEP, steps);
            }
            JsonObject s = new JsonObject();
            s.addProperty(ComboModifierData.KEY_TICK, tick);
            s.addProperty(ComboModifierData.KEY_DISTANCE, distance);
            steps.add(s);
            return this;
        }
        
        public BehaviorBuilder releaseActionQuickCharge() {
            root.add(ComboModifierData.BEHAVIOR_RELEASE_QUICK_CHARGE, new JsonObject());
            return this;
        }
        
        public BehaviorBuilder releaseActionQuickCharge(int charge) {
            JsonObject obj = new JsonObject();
            obj.addProperty(ComboModifierData.PARAM_CHARGE, charge);
            root.add(ComboModifierData.BEHAVIOR_RELEASE_QUICK_CHARGE, obj);
            return this;
        }
        
        public BehaviorBuilder releaseActionQuickCharge(int chargeMin, int chargeMax) {
            JsonObject obj = new JsonObject();
            obj.addProperty(ComboModifierData.PARAM_CHARGE_MIN, chargeMin);
            obj.addProperty(ComboModifierData.PARAM_CHARGE_MAX, chargeMax);
            root.add(ComboModifierData.BEHAVIOR_RELEASE_QUICK_CHARGE, obj);
            return this;
        }
        
        public BehaviorBuilder jumpVelocityBoost() {
            return jumpVelocityBoost(1.1, 3);
        }
        
        public BehaviorBuilder jumpVelocityBoost(double multiplier, int duration) {
            JsonObject obj = new JsonObject();
            obj.addProperty(ComboModifierData.PARAM_MULTIPLIER, multiplier);
            obj.addProperty(ComboModifierData.PARAM_DURATION, duration);
            root.add(ComboModifierData.BEHAVIOR_JUMP_VELOCITY_BOOST, obj);
            return this;
        }
        
        public BehaviorBuilder aerialRaveB3Multihit() {
            return aerialRaveB3Multihit(6, 4, 237, 0.4);
        }
        
        public BehaviorBuilder aerialRaveB3Multihit(int startTick, int count, double angle, double damage) {
            JsonObject obj = new JsonObject();
            obj.addProperty(ComboModifierData.PARAM_START_TICK, startTick);
            obj.addProperty(ComboModifierData.PARAM_COUNT, count);
            obj.addProperty(ComboModifierData.PARAM_ANGLE, angle);
            obj.addProperty(ComboModifierData.PARAM_DAMAGE, damage);
            root.add(ComboModifierData.BEHAVIOR_AERIAL_RAVE_B3_MULTIHIT, obj);
            return this;
        }
        
        public BehaviorBuilder zeroVelocity() {
            root.add(ComboModifierData.BEHAVIOR_ZERO_VELOCITY, new JsonObject());
            return this;
        }
        
        public BehaviorBuilder clickRapidSlash() {
            root.add(ComboModifierData.BEHAVIOR_CLICK_RAPID_SLASH, new JsonObject());
            return this;
        }
        
        public BehaviorBuilder clickSummonBlastSword() {
            root.add(ComboModifierData.BEHAVIOR_CLICK_SUMMON_BLAST_SWORD, new JsonObject());
            return this;
        }
        
        public BehaviorBuilder tickSummonBlastSword(int poweredCount, int normalCount) {
            return tickSummonBlastSword(10, poweredCount, normalCount);
        }
        
        public BehaviorBuilder tickSummonBlastSword(int tick, int poweredCount, int normalCount) {
            JsonObject obj = new JsonObject();
            obj.addProperty(ComboModifierData.PARAM_TICK, tick);
            obj.addProperty(ComboModifierData.PARAM_POWERED_COUNT, poweredCount);
            obj.addProperty(ComboModifierData.PARAM_NORMAL_COUNT, normalCount);
            root.add(ComboModifierData.BEHAVIOR_TICK_SUMMON_BLAST_SWORD, obj);
            return this;
        }
        
        public BehaviorBuilder tickCollideAction(int start, int end, CollideAction action) {
            JsonArray arr = root.has(ComboModifierData.BEHAVIOR_TICK_COLLIDE_ACTION)
                ? root.getAsJsonArray(ComboModifierData.BEHAVIOR_TICK_COLLIDE_ACTION) : new JsonArray();
            if (!root.has(ComboModifierData.BEHAVIOR_TICK_COLLIDE_ACTION)) {
                root.add(ComboModifierData.BEHAVIOR_TICK_COLLIDE_ACTION, arr);
            }
            JsonObject entry = new JsonObject();
            entry.addProperty(ComboModifierData.KEY_START, start);
            entry.addProperty(ComboModifierData.KEY_END, end);
            entry.addProperty(ComboModifierData.KEY_ACTION, action.name());
            arr.add(entry);
            return this;
        }
        
        public BehaviorBuilder tickSnapLockOn(int start, int end) {
            JsonArray arr = root.has(ComboModifierData.BEHAVIOR_TICK_SNAP_LOCK_ON)
                ? root.getAsJsonArray(ComboModifierData.BEHAVIOR_TICK_SNAP_LOCK_ON) : new JsonArray();
            if (!root.has(ComboModifierData.BEHAVIOR_TICK_SNAP_LOCK_ON)) {
                root.add(ComboModifierData.BEHAVIOR_TICK_SNAP_LOCK_ON, arr);
            }
            JsonObject entry = new JsonObject();
            entry.addProperty(ComboModifierData.KEY_START, start);
            entry.addProperty(ComboModifierData.KEY_END, end);
            arr.add(entry);
            return this;
        }
        
        public BehaviorBuilder tickShouldLockOn(int start, int end, boolean value) {
            JsonArray arr = root.has(ComboModifierData.BEHAVIOR_TICK_SHOULD_LOCK_ON)
                ? root.getAsJsonArray(ComboModifierData.BEHAVIOR_TICK_SHOULD_LOCK_ON) : new JsonArray();
            if (!root.has(ComboModifierData.BEHAVIOR_TICK_SHOULD_LOCK_ON)) {
                root.add(ComboModifierData.BEHAVIOR_TICK_SHOULD_LOCK_ON, arr);
            }
            JsonObject entry = new JsonObject();
            entry.addProperty(ComboModifierData.KEY_START, start);
            entry.addProperty(ComboModifierData.KEY_END, end);
            entry.addProperty(ComboModifierData.KEY_VALUE, value);
            arr.add(entry);
            return this;
        }
        
        public BehaviorBuilder stun(int tick, double stunValue) {
            return this.stun(tick, stunValue, false);
        }
        
        public BehaviorBuilder stun(int tick, double stunValue, boolean needPower) {
            JsonArray stuns = root.has(ComboModifierData.BEHAVIOR_TICK_STUN) ? root.getAsJsonArray(ComboModifierData.BEHAVIOR_TICK_STUN) : new JsonArray();
            if (!root.has(ComboModifierData.BEHAVIOR_TICK_STUN)) {
                root.add(ComboModifierData.BEHAVIOR_TICK_STUN, stuns);
            }
            JsonObject s = new JsonObject();
            s.addProperty(ComboModifierData.KEY_TICK, tick);
            s.addProperty(ComboModifierData.PARAM_STUN_VALUE, stunValue);
            s.addProperty(ComboModifierData.PARAM_NEED_POWER, needPower);
            stuns.add(s);
            return this;
        }
        
        private JsonObject build() {
            return root;
        }
    }
    
    public static final class ModifierBuilder {
        private final String name;
        private int startFrame;
        private int endFrame;
        private int priority;
        private ResourceLocation motionLoc = DefaultResources.ExMotionLocation;
        @Nullable
        private JsonObject behavior;
        
        private ModifierBuilder(String name) {
            this.name = name;
        }
        
        public ModifierBuilder startFrame(int startFrame) {
            this.startFrame = startFrame;
            return this;
        }
        
        public ModifierBuilder endFrame(int endFrame) {
            this.endFrame = endFrame;
            return this;
        }
        
        public ModifierBuilder priority(int priority) {
            this.priority = priority;
            return this;
        }
        
        public void motionLoc(ResourceLocation motionLoc) {
            this.motionLoc = motionLoc;
        }
        
        public ModifierBuilder behavior() {
            this.behavior = new BehaviorBuilder().build();
            return this;
        }
        
        public ModifierBuilder behavior(Consumer<BehaviorBuilder> consumer) {
            BehaviorBuilder bb = new BehaviorBuilder();
            consumer.accept(bb);
            this.behavior = bb.build();
            return this;
        }
        
        public EntryHolder build(Consumer<EntryHolder> consumer) {
            ResourceKey<ComboModifierData.ComboModifierEntry> key = ResourceKey.create(ComboModifierData.MODIFIER_REGISTRY_KEY, TruePowerMod.prefix(name));
            ComboModifierData.ComboModifierEntry entry = new ComboModifierData.ComboModifierEntry(name, startFrame, endFrame, priority, motionLoc, behavior);
            EntryHolder holder = new EntryHolder(key, entry);
            consumer.accept(holder);
            return holder;
        }
    }
    
    public static ModifierBuilder builder(String name) {
        return new ModifierBuilder(name);
    }
    
    public static ModifierBuilder builder(DeferredHolder<ComboState, ComboState> combo) {
        ModifierBuilder builder = builder(Objects.requireNonNull(combo.getId()).toLanguageKey());
        ComboState comboState = combo.get();
        builder.startFrame(comboState.getStartFrame());
        builder.endFrame(comboState.getEndFrame());
        builder.priority(comboState.getPriority());
        builder.motionLoc(comboState.getMotionLoc());
        return builder;
    }
}
