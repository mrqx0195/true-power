package net.mrqx.truepower.event.handler;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.phys.Vec3;
import net.mrqx.sbr_core.events.StunEvent;
import net.mrqx.truepower.attachment.ITruePowerStunData;
import net.mrqx.truepower.config.TruePowerCommonConfig;
import net.mrqx.truepower.entity.ai.TruePowerStunGoal;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.tick.EntityTickEvent;

@EventBusSubscriber
public final class StunHandler {
    @SubscribeEvent
    public static void onStunEvent(StunEvent event) {
        if (TruePowerCommonConfig.ENABLE_STUN_VALUE.get()) {
            event.setCanceled(true);
        }
    }
    
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onEntityJoinWorldEvent(EntityJoinLevelEvent event) {
        if (event.getEntity() instanceof Mob mob) {
            mob.goalSelector.addGoal(-100, new TruePowerStunGoal(mob));
            mob.targetSelector.addGoal(-100, new TruePowerStunGoal(mob));
        }
    }
    
    @SuppressWarnings("DataFlowIssue")
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onLivingTickEvent(EntityTickEvent.Pre event) {
        if (!(event.getEntity() instanceof LivingEntity entity)) {
            return;
        }
        if (entity instanceof Mob) {
            return;
        }
        ITruePowerStunData data = ITruePowerStunData.get(entity);
        if (data.isStunning()) {
            entity.setLastHurtByMob(null);
            entity.setLastHurtMob(null);
            entity.setLastHurtByPlayer(null);
            data.resetStunValue();
            if (entity.level() instanceof ServerLevel sl) {
                Vec3 ep = entity.getEyePosition();
                double size = entity.getBoundingBox().getSize() / 4;
                sl.sendParticles(ParticleTypes.ELECTRIC_SPARK, ep.x, ep.y, ep.z, 1, size, size, size, 0);
            }
        }
    }
}
