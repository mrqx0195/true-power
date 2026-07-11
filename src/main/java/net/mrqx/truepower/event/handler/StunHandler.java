package net.mrqx.truepower.event.handler;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.mrqx.sbr_core.events.StunEvent;
import net.mrqx.truepower.capability.stun.ITruePowerStunData;
import net.mrqx.truepower.capability.stun.TruePowerStunDataProvider;
import net.mrqx.truepower.config.TruePowerCommonConfig;
import net.mrqx.truepower.entity.ai.TruePowerStunGoal;

@Mod.EventBusSubscriber
public final class StunHandler {
    @SubscribeEvent
    public static void onStunEvent(StunEvent event) {
        if (TruePowerCommonConfig.ENABLE_STUN_VALUE.get()) {
            event.setCanceled(true);
        }
    }
    
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onEntityJoinWorldEvent(EntityJoinLevelEvent event) {
        if (!(event.getEntity() instanceof Mob mob)) {
            return;
        }
        mob.goalSelector.addGoal(-100, new TruePowerStunGoal(mob));
        mob.targetSelector.addGoal(-100, new TruePowerStunGoal(mob));
    }
    
    /**
     * @see TruePowerStunGoal
     */
    @SuppressWarnings("DataFlowIssue")
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onLivingTickEvent(LivingEvent.LivingTickEvent event) {
        LivingEntity entity = event.getEntity();
        if (entity instanceof Mob) {
            return;
        }
        entity.getCapability(TruePowerStunDataProvider.TRUE_POWER_STUN_DATA)
            .filter(ITruePowerStunData::isStunning)
            .ifPresent(data -> {
                entity.setLastHurtByMob(null);
                entity.setLastHurtMob(null);
                entity.setLastHurtByPlayer(null);
                data.resetStunValue();
                if (entity.level() instanceof ServerLevel serverLevel) {
                    Vec3 eyePosition = entity.getEyePosition();
                    double size = entity.getBoundingBox().getSize() / 4;
                    serverLevel.sendParticles(ParticleTypes.ELECTRIC_SPARK,
                        eyePosition.x(), eyePosition.y(), eyePosition.z(), 1, size, size, size, 0);
                }
            });
    }
}
