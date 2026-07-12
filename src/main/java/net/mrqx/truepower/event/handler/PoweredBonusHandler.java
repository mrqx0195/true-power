package net.mrqx.truepower.event.handler;

import mods.flammpfeil.slashblade.capability.concentrationrank.CapabilityConcentrationRank;
import mods.flammpfeil.slashblade.capability.concentrationrank.IConcentrationRank;
import mods.flammpfeil.slashblade.registry.ModAttributes;
import mods.flammpfeil.slashblade.util.AttackManager;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.mrqx.truepower.TruePowerMod;
import net.mrqx.truepower.event.TruePowerStunEvent;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.tick.EntityTickEvent;

@EventBusSubscriber
public final class PoweredBonusHandler {
    @SubscribeEvent
    public static void onTick(EntityTickEvent.Post event) {
        Entity entity = event.getEntity();
        if (!(entity instanceof LivingEntity livingEntity)) {
            return;
        }
        boolean isPowered = AttackManager.isPowered(livingEntity);
        
        AttributeModifier poweredBonus = new AttributeModifier(
            TruePowerMod.prefix("powered_bonus"), 1.15 - 1, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
        
        AttributeInstance attributeInstance = livingEntity.getAttribute(ModAttributes.SLASHBLADE_DAMAGE);
        if (attributeInstance == null) {
            return;
        }
        attributeInstance.removeModifier(poweredBonus);
        if (isPowered) {
            attributeInstance.addPermanentModifier(poweredBonus);
        }
        
        IConcentrationRank rank = livingEntity.getData(CapabilityConcentrationRank.RANK_POINT);
        double amount;
        if (rank.getRank(livingEntity.level().getGameTime()).level > IConcentrationRank.ConcentrationRanks.SS.level) {
            if (isPowered) {
                amount = 1.4;
            } else {
                amount = 1.25;
            }
        } else if (rank.getRank(livingEntity.level().getGameTime()).level < IConcentrationRank.ConcentrationRanks.C.level) {
            if (isPowered) {
                amount = 0.75;
            } else {
                amount = 0.5;
            }
        } else {
            if (isPowered) {
                amount = 1.25;
            } else {
                amount = 1;
            }
        }
        
        AttributeModifier concentrationBonus = new AttributeModifier(
            TruePowerMod.prefix("concentration_bonus"), amount - 1, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
        
        attributeInstance.removeModifier(concentrationBonus);
        if (amount != 1) {
            attributeInstance.addPermanentModifier(concentrationBonus);
        }
    }
    
    @SubscribeEvent
    public static void onLivingHurtEvent(LivingDamageEvent.Pre event) {
        if (AttackManager.isPowered(event.getEntity())) {
            event.setNewDamage(event.getNewDamage() * 0.5F);
        }
    }
    
    @SubscribeEvent
    public static void onAddStunValue(TruePowerStunEvent.AddStunValue event) {
        if (event.getSource() != null && AttackManager.isPowered(event.getSource())) {
            event.setAdditionValue(event.getAdditionValue() * 1.2F);
        }
        if (AttackManager.isPowered(event.getEntity())) {
            event.setAdditionValue(event.getAdditionValue() * 0.75F);
        }
    }
}
