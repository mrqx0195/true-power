package net.mrqx.truepower.event.handler;

import mods.flammpfeil.slashblade.capability.concentrationrank.CapabilityConcentrationRank;
import mods.flammpfeil.slashblade.item.ItemSlashBlade;
import mods.flammpfeil.slashblade.registry.ComboStateRegistry;
import mods.flammpfeil.slashblade.util.AttackManager;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.mrqx.truepower.TruePowerModConfig;
import net.mrqx.truepower.entity.EntityBlastSummonedSword;
import net.mrqx.truepower.util.RankManager;

import java.util.List;

@Mod.EventBusSubscriber
public class AttackEventHandler {
    @SubscribeEvent
    public static void onLivingAttackEvent(LivingAttackEvent event) {
        if (event.getSource().getEntity() instanceof LivingEntity livingEntity) {
            livingEntity.getMainHandItem().getCapability(ItemSlashBlade.BLADESTATE).ifPresent(state -> {
                if (((state.getComboSeq().equals(ComboStateRegistry.RAPID_SLASH.getId()) && AttackManager.isPowered(livingEntity)) ||
                        state.getComboSeq().equals(ComboStateRegistry.COMBO_C.getId())) && !event.getSource().isIndirect()
                ) {
                    List<LivingEntity> entityList = EntityBlastSummonedSword.getPreSummonSwordList(livingEntity);
                    if (!entityList.contains(event.getEntity())) {
                        entityList.add(event.getEntity());
                    }
                }

                if ((RankManager.addCombo(livingEntity, state.getComboSeq()) || RankManager.checkCombo(livingEntity, state.getComboSeq(), 3))
                        && RankManager.getRankCooldown(livingEntity) <= livingEntity.level().getGameTime()
                ) {
                    livingEntity.getCapability(CapabilityConcentrationRank.RANK_POINT).ifPresent(rank -> {
                        rank.addRankPoint(livingEntity, TruePowerModConfig.RANK_INCREASE_FOR_HIT.get());
                        RankManager.setRankCooldown(livingEntity, livingEntity.level().getGameTime() + 1);
                    });
                }
            });
        }
    }

    @SubscribeEvent
    public static void onLivingDeathEvent(LivingDeathEvent event) {
        if (event.getSource().getEntity() instanceof LivingEntity livingEntity) {
            livingEntity.getMainHandItem().getCapability(ItemSlashBlade.BLADESTATE).ifPresent(state -> RankManager.setPreAddRank(livingEntity, RankManager.getPreAddRank(livingEntity) + TruePowerModConfig.RANK_INCREASE_FOR_KILL.get()));
        }
    }
}
