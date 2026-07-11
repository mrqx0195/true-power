package net.mrqx.truepower.capability.stun;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.DefaultAttributes;
import net.minecraftforge.common.MinecraftForge;
import net.mrqx.truepower.TruePowerMod;
import net.mrqx.truepower.event.TruePowerStunEvent;
import net.mrqx.truepower.registry.TruePowerAttributeRegistry;
import org.jetbrains.annotations.Nullable;

public interface ITruePowerStunData {
    float getStunValue();
    
    void setStunValue(float value);
    
    long getStunEndTick();
    
    void setStunEndTick(long stunEndTick);
    
    boolean isStunning();
    
    LivingEntity getEntity();
    
    default void addStunValue(@Nullable LivingEntity source, float value) {
        if (this.isStunning()) {
            return;
        }
        TruePowerStunEvent.AddStunValue event = new TruePowerStunEvent.AddStunValue(this, source, value);
        MinecraftForge.EVENT_BUS.post(event);
        if (!event.isCanceled()) {
            this.setStunValue(this.getStunValue() + event.getAdditionValue());
            TruePowerMod.LOGGER.debug(String.valueOf(this.getStunValue()));
            this.update();
        }
    }
    
    default void resetStunValue() {
        this.setStunValue(0);
    }
    
    @SuppressWarnings("unchecked")
    default float getMaxStunValue() {
        double maxHealth;
        LivingEntity entity = this.getEntity();
        try {
            maxHealth = DefaultAttributes.getSupplier((EntityType<? extends LivingEntity>) entity.getType()).getValue(Attributes.MAX_HEALTH);
        } catch (Exception ignore) {
            maxHealth = entity.getAttributeBaseValue(Attributes.MAX_HEALTH);
        }
        return (float) (maxHealth * entity.getAttributeValue(TruePowerAttributeRegistry.STUN_RESISTANCE.get()));
    }
    
    void update();
    
    @Nullable
    static ITruePowerStunData get(LivingEntity livingEntity) {
        return livingEntity.getCapability(TruePowerStunDataProvider.TRUE_POWER_STUN_DATA).resolve().orElse(null);
    }
    
    static float getStunValue(LivingEntity livingEntity) {
        ITruePowerStunData data = get(livingEntity);
        return data != null ? data.getStunValue() : 0.0f;
    }
    
    void triggerStun(long stunDuration);
}
