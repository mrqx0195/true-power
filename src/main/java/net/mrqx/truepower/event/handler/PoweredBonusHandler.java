package net.mrqx.truepower.event.handler;

import mods.flammpfeil.slashblade.capability.concentrationrank.ConcentrationRankCapabilityProvider;
import mods.flammpfeil.slashblade.capability.concentrationrank.IConcentrationRank;
import mods.flammpfeil.slashblade.registry.ModAttributes;
import mods.flammpfeil.slashblade.util.AttackManager;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.UUID;

@Mod.EventBusSubscriber
public class PoweredBonusHandler {
    @SubscribeEvent
    public static void onTick(LivingEvent.LivingTickEvent event) {
        LivingEntity livingEntity = event.getEntity();
        boolean isPowered = AttackManager.isPowered(livingEntity);

        AttributeModifier poweredBonus = new AttributeModifier(
                UUID.fromString("1340d4b5-b4fa-49a0-ad4e-f55f7dae03fe"),
                "Powered Bonus", 1.15 - 1, AttributeModifier.Operation.MULTIPLY_TOTAL);

        AttributeInstance attributeInstance = livingEntity.getAttribute(ModAttributes.getSlashBladeDamage());
        if (attributeInstance == null) {
            return;
        }
        attributeInstance.removeModifier(poweredBonus);
        if (isPowered) {
            attributeInstance.addPermanentModifier(poweredBonus);
        }

        livingEntity.getCapability(ConcentrationRankCapabilityProvider.RANK_POINT).ifPresent(rank -> {
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
                    UUID.fromString("9cc4973f-1a68-4775-9dc7-65f205bce8b3"),
                    "Concentration Bonus", amount - 1, AttributeModifier.Operation.MULTIPLY_TOTAL);

            attributeInstance.removeModifier(concentrationBonus);
            if (amount != 1) {
                attributeInstance.addPermanentModifier(concentrationBonus);
            }
        });
    }

    @SubscribeEvent
    public static void onLivingHurtEvent(LivingHurtEvent event) {
        if (AttackManager.isPowered(event.getEntity())) {
            event.setAmount(event.getAmount() * 0.5F);
        }
    }
}
