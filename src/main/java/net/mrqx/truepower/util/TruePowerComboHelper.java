package net.mrqx.truepower.util;

import mods.flammpfeil.slashblade.capability.slashblade.ISlashBladeState;
import mods.flammpfeil.slashblade.item.ItemSlashBlade;
import mods.flammpfeil.slashblade.registry.combo.ComboState;
import mods.flammpfeil.slashblade.slasharts.SlashArts;
import mods.flammpfeil.slashblade.util.*;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.phys.Vec3;
import net.mrqx.truepower.capability.data.ITruePowerData;
import net.mrqx.truepower.config.TruePowerCommonConfig;

import java.util.List;

public class TruePowerComboHelper {
    public static final ComboState.TimeLineTickAction UPPER_SLASH = ComboState.TimeLineTickAction.getBuilder()
        .put((int) TimeValueHelper.getTicksFromFrames(7.0F), (entityIn) -> AttackManager.doSlash(entityIn, -80.0F, Vec3.ZERO, false, false, 0.9, KnockBacks.toss))
        .build();
    
    public static final ComboState.TimeLineTickAction POWERED_UPPER_SLASH = ComboState.TimeLineTickAction.getBuilder()
        .put((int) TimeValueHelper.getTicksFromFrames(7.0F), (entityIn) -> AttackManager.doSlash(entityIn, -80.0F, Vec3.ZERO, false, false, 0.25, KnockBacks.toss))
        .put((int) TimeValueHelper.getTicksFromFrames(9.0F), (entityIn) -> AttackManager.doSlash(entityIn, -80.0F, Vec3.ZERO, false, false, 1.35, KnockBacks.toss))
        .build();
    
    public static SlashArts.ArtsType releaseActionQuickCharge(LivingEntity user, Integer elapsed, Integer startFrame) {
        int level = user.getMainHandItem().getEnchantmentLevel(Enchantments.SOUL_SPEED);
        if (elapsed > startFrame && elapsed <= 3 + level + startFrame) {
            AdvancementHelper.grantedIf(Enchantments.SOUL_SPEED, user);
            AdvancementHelper.grantCriterion(user, AdvancementHelper.ADVANCEMENT_QUICK_CHARGE);
            return SlashArts.ArtsType.Jackpot;
        } else {
            return SlashArts.ArtsType.Fail;
        }
    }
    
    public static SlashArts.ArtsType releaseActionQuickCharge(LivingEntity user, Integer elapsed, Integer startFrame, Integer endFrame) {
        int level = user.getMainHandItem().getEnchantmentLevel(Enchantments.SOUL_SPEED);
        if (elapsed > startFrame && elapsed < endFrame + level) {
            AdvancementHelper.grantedIf(Enchantments.SOUL_SPEED, user);
            AdvancementHelper.grantCriterion(user, AdvancementHelper.ADVANCEMENT_QUICK_CHARGE);
            return SlashArts.ArtsType.Jackpot;
        } else {
            return SlashArts.ArtsType.Fail;
        }
    }
    
    public static CollideAction getCollideAction(Entity entity, ISlashBladeState state) {
        if (entity instanceof LivingEntity livingEntity) {
            if (!entity.onGround()) {
                return CollideAction.IGNORE;
            }
            ITruePowerData data = ITruePowerData.get(livingEntity);
            if (data != null) {
                List<ITruePowerData.CollideInterval> intervals = data.getCollideIntervals();
                if (intervals != null && !intervals.isEmpty()) {
                    long elapsed = state.getElapsedTime(livingEntity);
                    for (ITruePowerData.CollideInterval interval : intervals) {
                        if (elapsed >= interval.start() && elapsed <= interval.end()) {
                            return interval.action();
                        }
                    }
                }
            }
        }
        return TruePowerCommonConfig.COLLIDE_ACTION.get();
    }
    
    public static boolean hasTargetOrSneak(LivingEntity entity) {
        return entity.getCapability(ItemSlashBlade.INPUT_STATE).map(s -> s.getCommands(entity).contains(InputCommand.SNEAK)).orElse(false)
            || hasTarget(entity);
    }
    
    public static boolean hasTarget(LivingEntity entity) {
        return hasTarget(entity, entity.getMainHandItem());
    }
    
    public static boolean hasTarget(Entity entity, ItemStack stack) {
        if (!stack.isEmpty()) {
            ISlashBladeState state = stack.getCapability(ItemSlashBlade.BLADESTATE).resolve().orElse(null);
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
}
