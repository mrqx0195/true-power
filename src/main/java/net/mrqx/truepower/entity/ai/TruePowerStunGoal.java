package net.mrqx.truepower.entity.ai;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.phys.Vec3;
import net.mrqx.truepower.capability.stun.ITruePowerStunData;
import net.mrqx.truepower.capability.stun.TruePowerStunDataProvider;

import java.util.EnumSet;

public class TruePowerStunGoal extends Goal {
    private final Mob entity;
    
    public TruePowerStunGoal(Mob entity) {
        this.entity = entity;
        this.setFlags(EnumSet.allOf(Flag.class));
    }
    
    @Override
    public boolean isInterruptable() {
        return false;
    }
    
    @Override
    public boolean requiresUpdateEveryTick() {
        return true;
    }
    
    @SuppressWarnings("DataFlowIssue")
    @Override
    public void tick() {
        this.entity.setLastHurtByMob(null);
        this.entity.setLastHurtMob(null);
        this.entity.setLastHurtByPlayer(null);
        this.entity.getBrain().clearMemories();
        if (this.entity instanceof PathfinderMob pathfinderMob) {
            pathfinderMob.setTarget(null);
        }
        this.entity.getCapability(TruePowerStunDataProvider.TRUE_POWER_STUN_DATA)
            .ifPresent(ITruePowerStunData::resetStunValue);
        if (this.entity.level() instanceof ServerLevel serverLevel) {
            Vec3 eyePosition = this.entity.getEyePosition();
            double size = this.entity.getBoundingBox().getSize() / 4;
            serverLevel.sendParticles(ParticleTypes.ELECTRIC_SPARK,
                eyePosition.x(), eyePosition.y(), eyePosition.z(), 1, size, size, size, 0);
        }
        super.tick();
    }
    
    @Override
    public boolean canUse() {
        return this.entity.getCapability(TruePowerStunDataProvider.TRUE_POWER_STUN_DATA)
            .filter(ITruePowerStunData::isStunning)
            .isPresent();
    }
    
    @Override
    public void stop() {
        this.entity.getCapability(TruePowerStunDataProvider.TRUE_POWER_STUN_DATA)
            .ifPresent(data -> {
                data.resetStunValue();
                data.setStunEndTick(0);
            });
    }
}
