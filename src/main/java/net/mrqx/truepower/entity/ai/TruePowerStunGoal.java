package net.mrqx.truepower.entity.ai;

import mods.flammpfeil.slashblade.capability.slashblade.BladeStateAccess;
import mods.flammpfeil.slashblade.registry.ComboStateRegistry;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.phys.Vec3;
import net.mrqx.truepower.attachment.ITruePowerStunData;
import net.mrqx.truepower.config.TruePowerCommonConfig;
import net.mrqx.truepower.registry.TruePowerComboStateRegistry;

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
        if (TruePowerCommonConfig.POWERFUL_STUN.get()) {
            this.entity.setLastHurtByMob(null);
            this.entity.setLastHurtMob(null);
            this.entity.setLastHurtByPlayer(null);
            this.entity.getBrain().clearMemories();
            if (this.entity instanceof PathfinderMob pathfinderMob) {
                pathfinderMob.setTarget(null);
            }
        }
        BladeStateAccess.of(this.entity.getMainHandItem())
            .ifPresent(state -> state.updateComboSeq(this.entity, TruePowerComboStateRegistry.STUN.getId()));
        ITruePowerStunData data = ITruePowerStunData.get(this.entity);
        data.resetStunValue();
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
        return ITruePowerStunData.get(this.entity).isStunning();
    }
    
    @Override
    public void stop() {
        ITruePowerStunData data = ITruePowerStunData.get(this.entity);
        data.resetStunValue();
        data.setStunEndTick(0);
        BladeStateAccess.of(this.entity.getMainHandItem())
            .ifPresent(state -> state.updateComboSeq(this.entity, ComboStateRegistry.NONE.getId()));
    }
}
