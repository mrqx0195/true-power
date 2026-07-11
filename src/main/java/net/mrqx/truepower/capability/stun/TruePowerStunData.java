package net.mrqx.truepower.capability.stun;

import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.MinecraftForge;
import net.mrqx.truepower.event.TruePowerStunEvent;

public class TruePowerStunData implements ITruePowerStunData {
    public static final long DEFAULT_STUN_DURATION = 120;
    private final LivingEntity entity;
    private float stunValue;
    private long stunEndTick;
    
    public TruePowerStunData(LivingEntity entity) {
        this.entity = entity;
    }
    
    @Override
    public float getStunValue() {
        return this.stunValue;
    }
    
    @Override
    public void setStunValue(float value) {
        this.stunValue = value;
    }
    
    @Override
    public long getStunEndTick() {
        return this.stunEndTick;
    }
    
    @Override
    public void setStunEndTick(long stunEndTick) {
        this.stunEndTick = stunEndTick;
    }
    
    @Override
    public boolean isStunning() {
        return this.entity.tickCount < this.stunEndTick;
    }
    
    @Override
    public LivingEntity getEntity() {
        return this.entity;
    }
    
    @Override
    public void update() {
        if (!this.isStunning()) {
            this.stunEndTick = 0;
        }
        if (this.getStunValue() >= this.getMaxStunValue()) {
            this.triggerStun(DEFAULT_STUN_DURATION);
        }
    }
    
    @Override
    public void triggerStun(long stunDuration) {
        if (!this.isStunning()) {
            TruePowerStunEvent.StunTriggered event = new TruePowerStunEvent.StunTriggered(this, stunDuration);
            MinecraftForge.EVENT_BUS.post(event);
            if (!event.isCanceled()) {
                this.stunEndTick = this.entity.tickCount + stunDuration;
                if (this.entity.level() instanceof ServerLevel serverLevel) {
                    Vec3 eyePosition = this.entity.getEyePosition();
                    double size = this.entity.getBoundingBox().getSize() / 2;
                    serverLevel.sendParticles(new BlockParticleOption(ParticleTypes.BLOCK, Blocks.GLASS.defaultBlockState()),
                        eyePosition.x(), eyePosition.y(), eyePosition.z(), 100, size, size, size, 5);
                    serverLevel.playSound(this.entity, this.entity.blockPosition(), SoundEvents.GLASS_BREAK,
                        this.entity.getSoundSource(), 1, 1);
                }
            }
            this.resetStunValue();
        }
    }
}
