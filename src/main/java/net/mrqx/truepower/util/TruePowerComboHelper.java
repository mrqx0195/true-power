package net.mrqx.truepower.util;

import mods.flammpfeil.slashblade.capability.inputstate.CapabilityInputState;
import mods.flammpfeil.slashblade.capability.slashblade.BladeStateAccess;
import mods.flammpfeil.slashblade.capability.slashblade.ISlashBladeState;
import mods.flammpfeil.slashblade.registry.combo.ComboState;
import mods.flammpfeil.slashblade.slasharts.SlashArts;
import mods.flammpfeil.slashblade.util.*;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.phys.Vec3;
import net.mrqx.truepower.attachment.ITruePowerData;
import net.mrqx.truepower.config.TruePowerCommonConfig;
import org.jetbrains.annotations.Nullable;

public class TruePowerComboHelper {
    public static final ComboState.TimeLineTickAction UPPER_SLASH = ComboState.TimeLineTickAction.getBuilder()
        .put((int) TimeValueHelper.getTicksFromFrames(7.0F), (entityIn) -> AttackManager.doSlash(entityIn, -80.0F, Vec3.ZERO, false, false, 0.9, KnockBacks.toss))
        .build();
    
    public static final ComboState.TimeLineTickAction POWERED_UPPER_SLASH = ComboState.TimeLineTickAction.getBuilder()
        .put((int) TimeValueHelper.getTicksFromFrames(7.0F), (entityIn) -> AttackManager.doSlash(entityIn, -80.0F, Vec3.ZERO, false, false, 0.25, KnockBacks.toss))
        .put((int) TimeValueHelper.getTicksFromFrames(9.0F), (entityIn) -> AttackManager.doSlash(entityIn, -80.0F, Vec3.ZERO, false, false, 1.35, KnockBacks.toss))
        .build();
    
    public static SlashArts.ArtsType releaseActionQuickCharge(LivingEntity user, Integer elapsed, Integer startFrame) {
        Holder<Enchantment> holder = user.registryAccess().holderOrThrow(Enchantments.SOUL_SPEED);
        int level = user.getMainHandItem().getEnchantmentLevel(holder);
        if (elapsed > startFrame && elapsed <= 3 + level + startFrame) {
            AdvancementHelper.grantedIf(holder.value(), user);
            AdvancementHelper.grantCriterion(user, AdvancementHelper.ADVANCEMENT_QUICK_CHARGE);
            return SlashArts.ArtsType.Jackpot;
        } else {
            return SlashArts.ArtsType.Fail;
        }
    }
    
    public static SlashArts.ArtsType releaseActionQuickCharge(LivingEntity user, Integer elapsed, Integer startFrame, Integer endFrame) {
        Holder<Enchantment> holder = user.registryAccess().holderOrThrow(Enchantments.SOUL_SPEED);
        int level = user.getMainHandItem().getEnchantmentLevel(holder);
        if (elapsed > startFrame && elapsed < endFrame + level) {
            AdvancementHelper.grantedIf(holder.value(), user);
            AdvancementHelper.grantCriterion(user, AdvancementHelper.ADVANCEMENT_QUICK_CHARGE);
            return SlashArts.ArtsType.Jackpot;
        } else {
            return SlashArts.ArtsType.Fail;
        }
    }
    
    public static CollideAction getCollideAction(Entity entity, ISlashBladeState state) {
        return getCollideAction(entity, state, null);
    }
    
    public static CollideAction getCollideAction(Entity entity, ISlashBladeState state, @Nullable Entity target) {
        if (!entity.onGround()) {
            return CollideAction.IGNORE;
        }
        if (target != null && !target.onGround()) {
            return CollideAction.IGNORE;
        }
        if (entity instanceof LivingEntity living) {
            ITruePowerData data = ITruePowerData.get(living);
            var intervals = data.getCollideIntervals();
            if (intervals != null) {
                long comboTime = state.getLastActionTime();
                for (var interval : intervals) {
                    if (comboTime >= interval.start() && comboTime <= interval.end()) {
                        return interval.action();
                    }
                }
            }
        }
        return TruePowerCommonConfig.COLLIDE_ACTION.get();
    }
    
    public static boolean hasTargetOrSneak(LivingEntity entity) {
        return entity.getData(CapabilityInputState.INPUT_STATE).getCommands(entity).contains(InputCommand.SNEAK)
            || hasTarget(entity);
    }
    
    public static boolean hasTarget(LivingEntity entity) {
        return hasTarget(entity, entity.getMainHandItem());
    }
    
    public static boolean hasTarget(Entity entity, ItemStack stack) {
        if (!stack.isEmpty()) {
            ISlashBladeState state = BladeStateAccess.of(stack).orElse(null);
            if (state != null) {
                return hasTarget(entity, state);
            }
        }
        return false;
    }
    
    public static boolean hasTarget(Entity entity, ISlashBladeState state) {
        Entity target = state.getTargetEntity(entity.level());
        return target != null && target.isAlive();
    }
    
    @Nullable
    public static Entity getTarget(LivingEntity entity) {
        return getTarget(entity, entity.getMainHandItem());
    }
    
    @Nullable
    public static Entity getTarget(Entity entity, ItemStack stack) {
        if (!stack.isEmpty()) {
            ISlashBladeState state = BladeStateAccess.of(stack).orElse(null);
            if (state != null) {
                return getTarget(entity, state);
            }
        }
        return null;
    }
    
    @Nullable
    public static Entity getTarget(Entity entity, ISlashBladeState state) {
        return state.getTargetEntity(entity.level());
    }
}
