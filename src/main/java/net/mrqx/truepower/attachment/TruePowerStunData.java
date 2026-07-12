package net.mrqx.truepower.attachment;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;
import net.mrqx.truepower.event.TruePowerStunEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.common.util.INBTSerializable;

public class TruePowerStunData implements ITruePowerStunData, INBTSerializable<CompoundTag> {
    private static final long DEFAULT_STUN_DURATION = 120;
    
    private final LivingEntity entity;
    private float stunValue;
    private long stunEndTick;
    
    public TruePowerStunData(LivingEntity entity) {
        this.entity = entity;
        this.stunValue = 0;
        this.stunEndTick = 0;
    }
    
    @Override
    public float getStunValue() {
        return stunValue;
    }
    
    @Override
    public void setStunValue(float stunValue) {
        this.stunValue = stunValue;
    }
    
    @Override
    public long getStunEndTick() {
        return stunEndTick;
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
        return entity;
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
            NeoForge.EVENT_BUS.post(event);
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
    
    @Override
    public CompoundTag serializeNBT(HolderLookup.Provider provider) {
        CompoundTag tag = new CompoundTag();
        tag.putFloat("stunValue", this.getStunValue());
        return tag;
    }
    
    @Override
    public void deserializeNBT(HolderLookup.Provider provider, CompoundTag tag) {
        this.setStunValue(tag.getFloat("stunValue"));
    }
}
