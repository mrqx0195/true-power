package net.mrqx.truepower.compat.epicfight.util;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import yesman.epicfight.gameasset.Animations;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;
import yesman.epicfight.world.capabilities.entitypatch.LivingEntityPatch;
import yesman.epicfight.world.damagesource.EpicFightDamageSource;
import yesman.epicfight.world.damagesource.EpicFightDamageSources;
import yesman.epicfight.world.damagesource.EpicFightDamageTypeTags;

public final class TruePowerEpicFightUtils {
    public static DamageSource getEpicFightDamageSource(LivingEntity attacker, DamageSource damageSource) {
        LivingEntityPatch<?> entityPatch = EpicFightCapabilities.getEntityPatch(attacker, LivingEntityPatch.class);
        if (entityPatch != null) {
            damageSource = entityPatch
                .getDamageSource(Animations.EMPTY_ANIMATION, InteractionHand.MAIN_HAND)
                .addRuntimeTag(EpicFightDamageTypeTags.NO_STUN);
        }
        return damageSource;
    }
    
    public static EpicFightDamageSource getEpicFightSummonedSwordDamageSource(DamageSource damageSource) {
        return EpicFightDamageSources
            .fromVanillaDamageSource(damageSource)
            .addRuntimeTag(EpicFightDamageTypeTags.NO_STUN);
    }
}
