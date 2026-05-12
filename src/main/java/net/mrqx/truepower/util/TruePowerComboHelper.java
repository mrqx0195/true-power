package net.mrqx.truepower.util;

import mods.flammpfeil.slashblade.registry.combo.ComboState;
import mods.flammpfeil.slashblade.slasharts.SlashArts;
import mods.flammpfeil.slashblade.util.AdvancementHelper;
import mods.flammpfeil.slashblade.util.AttackManager;
import mods.flammpfeil.slashblade.util.KnockBacks;
import mods.flammpfeil.slashblade.util.TimeValueHelper;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.phys.Vec3;

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
}
