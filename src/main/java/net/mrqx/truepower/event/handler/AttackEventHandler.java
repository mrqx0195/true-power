package net.mrqx.truepower.event.handler;

import mods.flammpfeil.slashblade.capability.concentrationrank.CapabilityConcentrationRank;
import mods.flammpfeil.slashblade.capability.slashblade.BladeStateAccess;
import mods.flammpfeil.slashblade.registry.ComboStateRegistry;
import mods.flammpfeil.slashblade.util.AttackManager;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.mrqx.truepower.config.TruePowerCommonConfig;
import net.mrqx.truepower.entity.EntityBlastSummonedSword;
import net.mrqx.truepower.util.RankManager;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;

import java.util.List;

@EventBusSubscriber
public class AttackEventHandler {
    @SubscribeEvent
    public static void onLivingAttackEvent(LivingIncomingDamageEvent event) {
        if (event.getSource().getEntity() instanceof LivingEntity livingEntity) {
            ItemStack itemStack = livingEntity.getMainHandItem();
            if (itemStack.isEmpty()) {
                return;
            }
            BladeStateAccess.of(itemStack).ifPresent(state -> {
                boolean isSummonSwordCombo = (state.getComboSeq().equals(ComboStateRegistry.RAPID_SLASH.getId()) && AttackManager.isPowered(livingEntity))
                    || state.getComboSeq().equals(ComboStateRegistry.COMBO_C.getId());
                if (isSummonSwordCombo && event.getSource().isDirect()) {
                    List<LivingEntity> entityList = EntityBlastSummonedSword.getPreSummonSwordList(livingEntity);
                    if (!entityList.contains(event.getEntity())) {
                        entityList.add(event.getEntity());
                    }
                }
                
                boolean rankManagerCheck = (RankManager.addCombo(livingEntity, state.getComboSeq()) || RankManager.checkCombo(livingEntity, state.getComboSeq(), 3))
                    && RankManager.getRankCooldown(livingEntity) <= livingEntity.level().getGameTime();
                if (rankManagerCheck) {
                    livingEntity.getData(CapabilityConcentrationRank.RANK_POINT)
                        .addRankPoint(livingEntity, TruePowerCommonConfig.RANK_INCREASE_FOR_HIT.get());
                    RankManager.setRankCooldown(livingEntity, livingEntity.level().getGameTime() + 1);
                }
            });
        }
    }
    
    @SubscribeEvent
    public static void onLivingDeathEvent(LivingDeathEvent event) {
        if (event.getSource().getEntity() instanceof LivingEntity livingEntity) {
            ItemStack itemStack = livingEntity.getMainHandItem();
            if (itemStack.isEmpty()) {
                return;
            }
            BladeStateAccess.of(itemStack).ifPresent(state -> RankManager.setPreAddRank(livingEntity, RankManager.getPreAddRank(livingEntity) + TruePowerCommonConfig.RANK_INCREASE_FOR_KILL.get()));
        }
    }
}
