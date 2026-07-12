package net.mrqx.truepower.event.handler;

import com.google.common.collect.Maps;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import mods.flammpfeil.slashblade.capability.slashblade.BladeStateAccess;
import mods.flammpfeil.slashblade.init.DefaultResources;
import mods.flammpfeil.slashblade.registry.combo.ComboState;
import mods.flammpfeil.slashblade.slasharts.SlashArts;
import mods.flammpfeil.slashblade.util.AdvancementHelper;
import mods.flammpfeil.slashblade.util.AttackManager;
import mods.flammpfeil.slashblade.util.KnockBacks;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.phys.Vec3;
import net.mrqx.sbr_core.events.ComboStateRegistryEvent;
import net.mrqx.truepower.attachment.ITruePowerData;
import net.mrqx.truepower.attachment.ITruePowerStunData;
import net.mrqx.truepower.config.TruePowerCommonConfig;
import net.mrqx.truepower.data.ComboModifierData;
import net.mrqx.truepower.entity.EntityBlastSummonedSword;
import net.mrqx.truepower.event.TruePowerComboModifyEvent;
import net.mrqx.truepower.network.ComboSyncMessage;
import net.mrqx.truepower.util.CollideAction;
import net.mrqx.truepower.util.ComboModifierManager;
import net.mrqx.truepower.util.RankManager;
import net.mrqx.truepower.util.TruePowerComboHelper;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModLoader;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

@EventBusSubscriber
public final class ComboModifyHandler {
    @SubscribeEvent
    public static void onComboStateRegistryEvent(ComboStateRegistryEvent event) {
        ComboState.Builder builder = event.getBuilder();
        ComboState combo = event.getCombo();
        
        TruePowerComboModifyEvent e = new TruePowerComboModifyEvent(builder, combo, event);
        ModLoader.postEvent(e);
        if (e.isCanceled()) {
            if (!e.shouldRemoveDefaultAction()) {
                builder.addTickAction(ComboModifyHandler::defaultAction);
            }
            return;
        }
        
        if (combo.getStartFrame() == 0
            && combo.getEndFrame() == 1
            && combo.getPriority() == 1000) {
            builder.addTickAction(livingEntity -> RankManager.setPreAddRank(livingEntity, 0));
        }
        
        ComboModifierData.RemoveReleaseEntry removeRelease =
            ComboModifierManager.findRemoveRelease(combo.getStartFrame(), combo.getEndFrame(), combo.getPriority());
        if (removeRelease != null) {
            builder.releaseAction((livingEntity, integer) -> SlashArts.ArtsType.Fail);
        }
        
        boolean matched = false;
        if (combo.getMotionLoc().equals(DefaultResources.ExMotionLocation)) {
            ComboModifierData.ComboModifierEntry entry =
                ComboModifierManager.findModifier(combo.getStartFrame(), combo.getEndFrame(), combo.getPriority());
            if (entry != null) {
                matched = true;
                applyEntryBehavior(entry, combo, builder);
            }
        }
        
        if (!matched) {
            if (!e.shouldRemoveDefaultAction()) {
                builder.addTickAction(ComboModifyHandler::defaultAction);
            }
        }
    }
    
    private static void applyEntryBehavior(ComboModifierData.ComboModifierEntry entry, ComboState combo, ComboState.Builder builder) {
        JsonElement behaviorEl = entry.behavior();
        if (behaviorEl == null || !behaviorEl.isJsonObject()) {
            return;
        }
        JsonObject behavior = behaviorEl.getAsJsonObject();
        
        if (behavior.has(ComboModifierData.KEY_CANCEL_ACTION)) {
            JsonObject cancel = behavior.getAsJsonObject(ComboModifierData.KEY_CANCEL_ACTION);
            int frame = ComboModifierData.optInt(cancel, ComboModifierData.KEY_FRAME, -1);
            boolean jumpOnly = ComboModifierData.optBool(cancel, ComboModifierData.KEY_JUMP_ONLY, false);
            builder.addTickAction(livingEntity -> cancelAction(livingEntity, frame, jumpOnly));
        }
        
        if (behavior.has(ComboModifierData.KEY_STEP)) {
            JsonArray steps = behavior.getAsJsonArray(ComboModifierData.KEY_STEP);
            AdditionalTimeLineTickAction.Builder timelineBuilder =
                AdditionalTimeLineTickAction.getBuilder();
            for (JsonElement stepEl : steps) {
                if (stepEl.isJsonObject()) {
                    JsonObject stepObj = stepEl.getAsJsonObject();
                    int tick = ComboModifierData.optInt(stepObj, ComboModifierData.KEY_TICK, 0);
                    double distance = ComboModifierData.optDouble(stepObj, ComboModifierData.KEY_DISTANCE, 0);
                    timelineBuilder.put(tick, (livingEntity) -> step(livingEntity, distance));
                }
            }
            builder.addTickAction(timelineBuilder.build());
        }
        
        if (behavior.has(ComboModifierData.BEHAVIOR_ZERO_VELOCITY)) {
            builder.addTickAction(ComboState.TimeLineTickAction.getBuilder()
                .put(0, (livingEntity) -> livingEntity.setDeltaMovement(0, 0, 0))
                .build());
        }
        
        if (behavior.has(ComboModifierData.BEHAVIOR_JUMP_VELOCITY_BOOST) && combo.isAerial()) {
            JsonObject config = behavior.getAsJsonObject(ComboModifierData.BEHAVIOR_JUMP_VELOCITY_BOOST);
            double multiplier = ComboModifierData.optDouble(config, ComboModifierData.PARAM_MULTIPLIER, 1.1);
            int duration = ComboModifierData.optInt(config, ComboModifierData.PARAM_DURATION, 3);
            builder.addTickAction((entityIn) -> {
                long elapsed = ComboState.getElapsed(entityIn);
                if (elapsed < duration) {
                    entityIn.setDeltaMovement(0, entityIn.getDeltaMovement().y * multiplier, 0);
                    if (entityIn instanceof ServerPlayer serverPlayer) {
                        serverPlayer.connection.send(new ClientboundSetEntityMotionPacket(serverPlayer));
                    }
                }
            });
        }
        
        if (behavior.has(ComboModifierData.BEHAVIOR_AERIAL_RAVE_B3_MULTIHIT) && combo.isAerial()) {
            JsonObject config = behavior.getAsJsonObject(ComboModifierData.BEHAVIOR_AERIAL_RAVE_B3_MULTIHIT);
            int startTick = ComboModifierData.optInt(config, ComboModifierData.PARAM_START_TICK, 6);
            int count = ComboModifierData.optInt(config, ComboModifierData.PARAM_COUNT, 4);
            double angle = ComboModifierData.optDouble(config, ComboModifierData.PARAM_ANGLE, 180 + 57);
            double damage = ComboModifierData.optDouble(config, ComboModifierData.PARAM_DAMAGE, 0.4);
            AdditionalTimeLineTickAction.Builder timelineBuilder =
                AdditionalTimeLineTickAction.getBuilder();
            for (int i = 0; i < count; i++) {
                final int tick = startTick + i;
                timelineBuilder.put(tick, (entityIn) ->
                    AttackManager.doSlash(entityIn, (float) angle, Vec3.ZERO, false, false, damage, KnockBacks.toss));
            }
            builder.addTickAction(timelineBuilder.build());
        }
        
        if (behavior.has(ComboModifierData.BEHAVIOR_RELEASE_QUICK_CHARGE)) {
            JsonObject config = behavior.getAsJsonObject(ComboModifierData.BEHAVIOR_RELEASE_QUICK_CHARGE);
            int charge = ComboModifierData.optInt(config, ComboModifierData.PARAM_CHARGE, -1);
            int chargeMin = ComboModifierData.optInt(config, ComboModifierData.PARAM_CHARGE_MIN, -1);
            int chargeMax = ComboModifierData.optInt(config, ComboModifierData.PARAM_CHARGE_MAX, -1);
            
            if (chargeMin != -1 && chargeMax != -1) {
                builder.releaseAction((livingEntity, integer) ->
                    TruePowerComboHelper.releaseActionQuickCharge(livingEntity, integer, chargeMin, chargeMax));
            } else if (charge != -1) {
                builder.releaseAction((livingEntity, integer) ->
                    TruePowerComboHelper.releaseActionQuickCharge(livingEntity, integer, charge));
            } else {
                builder.releaseAction(ComboState::releaseActionQuickCharge);
            }
        }
        
        if (behavior.has(ComboModifierData.BEHAVIOR_CLICK_RAPID_SLASH)) {
            builder.clickAction((livingEntity) -> {
                AdvancementHelper.grantCriterion(livingEntity, AdvancementHelper.ADVANCEMENT_RAPID_SLASH);
                AttackManager.doSlash(livingEntity, -30, AttackManager.genRushOffset(livingEntity), false, true, 0.2f);
            });
        }
        
        if (behavior.has(ComboModifierData.BEHAVIOR_CLICK_SUMMON_BLAST_SWORD)) {
            builder.clickAction((livingEntity) -> {
                if (AttackManager.isPowered(livingEntity)) {
                    EntityBlastSummonedSword.setPreBlastSwordList(livingEntity, 1);
                }
            });
        }
        
        if (behavior.has(ComboModifierData.BEHAVIOR_TICK_SUMMON_BLAST_SWORD)) {
            JsonObject config = behavior.getAsJsonObject(ComboModifierData.BEHAVIOR_TICK_SUMMON_BLAST_SWORD);
            int tick = ComboModifierData.optInt(config, ComboModifierData.PARAM_TICK, 10);
            int poweredCount = ComboModifierData.optInt(config, ComboModifierData.PARAM_POWERED_COUNT, 6);
            int normalCount = ComboModifierData.optInt(config, ComboModifierData.PARAM_NORMAL_COUNT, 1);
            builder.addTickAction(ComboState.TimeLineTickAction.getBuilder()
                .put(tick, (livingEntity) -> EntityBlastSummonedSword.setPreBlastSwordList(livingEntity,
                    AttackManager.isPowered(livingEntity) ? poweredCount : normalCount))
                .build());
        }
        
        if (behavior.has(ComboModifierData.BEHAVIOR_TICK_COLLIDE_ACTION)) {
            JsonArray entries = behavior.getAsJsonArray(ComboModifierData.BEHAVIOR_TICK_COLLIDE_ACTION);
            List<ITruePowerData.CollideInterval> intervals = new ArrayList<>();
            for (JsonElement el : entries) {
                if (el.isJsonObject()) {
                    JsonObject obj = el.getAsJsonObject();
                    int start = ComboModifierData.optInt(obj, ComboModifierData.KEY_START, 0);
                    int end = ComboModifierData.optInt(obj, ComboModifierData.KEY_END, 0);
                    CollideAction action = CollideAction.valueOf(
                        ComboModifierData.optString(obj, ComboModifierData.KEY_ACTION, "SOLID"));
                    intervals.add(new ITruePowerData.CollideInterval(start, end, action));
                }
            }
            builder.addTickAction((livingEntity) -> {
                ITruePowerData data = ITruePowerData.get(livingEntity);
                data.setCollideIntervals(intervals);
            });
        }
        
        if (behavior.has(ComboModifierData.BEHAVIOR_TICK_SNAP_LOCK_ON)) {
            JsonArray entries = behavior.getAsJsonArray(ComboModifierData.BEHAVIOR_TICK_SNAP_LOCK_ON);
            List<ITruePowerData.BoolInterval> intervals = new ArrayList<>();
            for (JsonElement el : entries) {
                if (el.isJsonObject()) {
                    JsonObject obj = el.getAsJsonObject();
                    int start = ComboModifierData.optInt(obj, ComboModifierData.KEY_START, 0);
                    int end = ComboModifierData.optInt(obj, ComboModifierData.KEY_END, 0);
                    intervals.add(new ITruePowerData.BoolInterval(start, end, true));
                }
            }
            builder.addTickAction((livingEntity) -> {
                ITruePowerData data = ITruePowerData.get(livingEntity);
                data.setSnapLockOnIntervals(intervals);
            });
        }
        
        if (behavior.has(ComboModifierData.BEHAVIOR_TICK_SHOULD_LOCK_ON)) {
            JsonArray entries = behavior.getAsJsonArray(ComboModifierData.BEHAVIOR_TICK_SHOULD_LOCK_ON);
            List<ITruePowerData.BoolInterval> intervals = new ArrayList<>();
            for (JsonElement el : entries) {
                if (el.isJsonObject()) {
                    JsonObject obj = el.getAsJsonObject();
                    int start = ComboModifierData.optInt(obj, ComboModifierData.KEY_START, 0);
                    int end = ComboModifierData.optInt(obj, ComboModifierData.KEY_END, 0);
                    boolean value = ComboModifierData.optBool(obj, ComboModifierData.KEY_VALUE, true);
                    intervals.add(new ITruePowerData.BoolInterval(start, end, value));
                }
            }
            builder.addTickAction((livingEntity) -> {
                ITruePowerData data = ITruePowerData.get(livingEntity);
                data.setLockOnIntervals(intervals);
            });
        }
        
        if (behavior.has(ComboModifierData.BEHAVIOR_TICK_STUN)) {
            JsonArray stuns = behavior.getAsJsonArray(ComboModifierData.BEHAVIOR_TICK_STUN);
            AdditionalTimeLineTickAction.Builder timelineBuilder =
                AdditionalTimeLineTickAction.getBuilder();
            for (JsonElement stunEl : stuns) {
                if (stunEl.isJsonObject()) {
                    JsonObject stunObj = stunEl.getAsJsonObject();
                    int tick = ComboModifierData.optInt(stunObj, ComboModifierData.KEY_TICK, 0);
                    double stunValue = ComboModifierData.optDouble(stunObj, ComboModifierData.PARAM_STUN_VALUE, 0);
                    boolean needPower = ComboModifierData.optBool(stunObj, ComboModifierData.PARAM_NEED_POWER, false);
                    timelineBuilder.put(tick, (livingEntity) -> stun(livingEntity, (float) stunValue, needPower));
                }
            }
            builder.addTickAction(timelineBuilder.build());
        }
    }
    
    public static void cancelAction(LivingEntity livingEntity, int canCancelFrame, boolean isJumpCancelOnly) {
        BladeStateAccess.of(livingEntity.getMainHandItem()).ifPresent(state -> {
            if (!livingEntity.level().isClientSide) {
                ITruePowerData data = ITruePowerData.get(livingEntity);
                data.setCombo(state.getComboSeq().toString());
                long elapsedTime = state.getElapsedTime(livingEntity);
                data.setCanMove((canCancelFrame != -1) && (elapsedTime >= canCancelFrame));
                data.setJumpCancelOnly(isJumpCancelOnly);
                data.setNoMoveEnable(TruePowerCommonConfig.CAN_NOT_MOVE_WHILE_COMBO.get());
                data.setCollideIntervals(null);
                data.setLockOnIntervals(null);
                data.setSnapLockOnIntervals(null);
                
                if (livingEntity instanceof ServerPlayer serverPlayer) {
                    ComboSyncMessage comboSyncMessage = new ComboSyncMessage(
                        state.getComboSeq(),
                        state.getLastActionTime(),
                        data.canMove(),
                        data.isJumpCancelOnly(),
                        data.isNoMoveEnable(),
                        false
                    );
                    
                    PacketDistributor.sendToPlayer(serverPlayer, comboSyncMessage);
                }
            }
        });
    }
    
    public static void defaultAction(LivingEntity livingEntity) {
        BladeStateAccess.of(livingEntity.getMainHandItem()).ifPresent(state -> {
            if (!livingEntity.level().isClientSide) {
                ITruePowerData data = ITruePowerData.get(livingEntity);
                data.setCombo(state.getComboSeq().toString());
                data.setCanMove(true);
                data.setJumpCancelOnly(false);
                data.setNoMoveEnable(TruePowerCommonConfig.CAN_NOT_MOVE_WHILE_COMBO.get());
                data.setCollideIntervals(null);
                data.setLockOnIntervals(null);
                data.setSnapLockOnIntervals(null);
                
                if (livingEntity instanceof ServerPlayer serverPlayer) {
                    ComboSyncMessage comboSyncMessage = new ComboSyncMessage(
                        state.getComboSeq(),
                        state.getLastActionTime(),
                        data.canMove(),
                        data.isJumpCancelOnly(),
                        data.isNoMoveEnable(),
                        false
                    );
                    
                    PacketDistributor.sendToPlayer(serverPlayer, comboSyncMessage);
                }
            }
        });
    }
    
    public static void step(LivingEntity livingEntity, double step) {
        if (TruePowerCommonConfig.STEP_WHEN_USING_COMBO.get()) {
            Vec3 input = new Vec3(0, 0, step / 4);
            livingEntity.moveRelative(1, input);
            Vec3 motion = TrickHandler.maybeBackOffFromEdge(livingEntity.getDeltaMovement(), livingEntity);
            livingEntity.move(MoverType.SELF, motion);
        }
    }
    
    public static void stun(LivingEntity livingEntity, float stunValue, boolean needPower) {
        if (TruePowerCommonConfig.ENABLE_STUN_VALUE.get()) {
            if (needPower && !AttackManager.isPowered(livingEntity)) {
                return;
            }
            AttackManager.areaAttack(livingEntity, __ -> {
                }, 0, true, false, true)
                .stream()
                .filter(e -> e instanceof LivingEntity)
                .map(e -> (LivingEntity) e)
                .map(ITruePowerStunData::get)
                .forEach(data -> data.addStunValue(livingEntity, stunValue));
        }
    }
    
    // TODO: 移到前置
    public static class AdditionalTimeLineTickAction implements Consumer<LivingEntity> {
        public static Builder getBuilder() {
            return new Builder();
        }
        
        public static class Builder {
            Map<Integer, Consumer<LivingEntity>> timeLine = Maps.newHashMap();
            
            public Builder put(int ticks, Consumer<LivingEntity> action) {
                timeLine.put(ticks, action);
                return this;
            }
            
            public AdditionalTimeLineTickAction build() {
                return new AdditionalTimeLineTickAction(timeLine);
            }
        }
        
        Map<Integer, Consumer<LivingEntity>> timeLine = Maps.newHashMap();
        
        public AdditionalTimeLineTickAction(Map<Integer, Consumer<LivingEntity>> timeLine) {
            this.timeLine.putAll(timeLine);
        }
        
        @Override
        public void accept(LivingEntity livingEntity) {
            long elapsed = ComboState.getElapsed(livingEntity);
            int adjustElapsed = (int) elapsed - 1;
            
            BladeStateAccess.of(livingEntity.getMainHandItem()).ifPresent(state -> {
                if (state.getLastProcessedComboTick() != -1 && state.getLastProcessedComboTick() != adjustElapsed) {
                    return;
                }
                
                Consumer<LivingEntity> action = timeLine.get(adjustElapsed);
                if (action != null) {
                    action.accept(livingEntity);
                }
            });
        }
    }
}
