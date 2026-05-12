package net.mrqx.truepower.event.handler;

import mods.flammpfeil.slashblade.capability.concentrationrank.CapabilityConcentrationRank;
import mods.flammpfeil.slashblade.capability.slashblade.BladeStateAccess;
import mods.flammpfeil.slashblade.event.SlashBladeEvent;
import net.minecraft.world.entity.LivingEntity;
import net.mrqx.truepower.config.TruePowerCommonConfig;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;

@EventBusSubscriber
public class PowerBladeEventHandler {
    @SubscribeEvent
    public static void onPowerBladeEvent(SlashBladeEvent.PowerBladeEvent event) {
        LivingEntity entity = event.getUser();
        if (!event.isPowered() && BladeStateAccess.of(event.getBlade()).isPresent()) {
            if (entity.getData(CapabilityConcentrationRank.RANK_POINT).getRankPoint(entity.level().getGameTime()) >= TruePowerCommonConfig.POWERED_RANK_REQUIRE.get()) {
                event.setPowered(true);
            }
        }
    }
}
