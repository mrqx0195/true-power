package net.mrqx.truepower.attachment;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.DefaultAttributes;
import net.mrqx.truepower.event.TruePowerStunEvent;
import net.mrqx.truepower.registry.TruePowerAttributeRegistry;
import org.jetbrains.annotations.Nullable;

public interface ITruePowerStunData {
    float getStunValue();
    
    void setStunValue(float stunValue);
    
    long getStunEndTick();
    
    void setStunEndTick(long stunEndTick);
    
    boolean isStunning();
    
    LivingEntity getEntity();
    
    default void addStunValue(@Nullable LivingEntity source, float value) {
        TruePowerStunEvent.AddStunValue event = new TruePowerStunEvent.AddStunValue(this, source, value);
        if (!event.isCanceled()) {
            this.setStunValue(this.getStunValue() + event.getAdditionValue());
            this.update();
        }
    }
    
    default void resetStunValue() {
        setStunValue(0);
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
        return (float) (maxHealth * entity.getAttributeValue(TruePowerAttributeRegistry.STUN_RESISTANCE));
    }
    
    void update();
    
    void triggerStun(long stunDuration);
    
    static ITruePowerStunData get(LivingEntity entity) {
        return entity.getData(TruePowerAttachments.TRUE_POWER_STUN_DATA.get());
    }
    
    static float getStunValue(LivingEntity entity) {
        ITruePowerStunData data = get(entity);
        return data.getStunValue();
    }
}
