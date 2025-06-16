package net.mrqx.truepower.event.handler;

import mods.flammpfeil.slashblade.capability.concentrationrank.CapabilityConcentrationRank;
import mods.flammpfeil.slashblade.event.SlashBladeEvent;
import mods.flammpfeil.slashblade.item.ItemSlashBlade;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.mrqx.truepower.TruePowerModConfig;

@Mod.EventBusSubscriber
public class PowerBladeEventHandler {
    @SubscribeEvent
    public static void onPowerBladeEvent(SlashBladeEvent.PowerBladeEvent event) {
        LivingEntity entity = event.getUser();
        if (!event.isPowered() &&
                event.getBlade().getCapability(ItemSlashBlade.BLADESTATE).isPresent()) {
            entity.getCapability(CapabilityConcentrationRank.RANK_POINT).ifPresent((cr) -> {
                if (cr.getRankPoint(entity.level().getGameTime()) >= TruePowerModConfig.POWERED_RANK_REQUIRE.get()) {
                    event.setPowered(true);
                }
            });
        }
    }
}
