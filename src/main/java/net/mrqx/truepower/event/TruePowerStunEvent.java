package net.mrqx.truepower.event;

import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.Cancelable;
import net.mrqx.truepower.capability.stun.ITruePowerStunData;
import org.jetbrains.annotations.Nullable;

public abstract class TruePowerStunEvent extends LivingEvent {
    private final ITruePowerStunData data;
    
    public TruePowerStunEvent(ITruePowerStunData data) {
        super(data.getEntity());
        this.data = data;
    }
    
    public ITruePowerStunData getData() {
        return data;
    }
    
    @Cancelable
    public static class AddStunValue extends TruePowerStunEvent {
        @Nullable
        private final LivingEntity source;
        private float additionValue;
        
        public AddStunValue(ITruePowerStunData data, @Nullable LivingEntity source, float additionValue) {
            super(data);
            this.source = source;
            this.additionValue = additionValue;
        }
        
        public float getAdditionValue() {
            return additionValue;
        }
        
        public void setAdditionValue(float additionValue) {
            this.additionValue = additionValue;
        }
        
        public @Nullable LivingEntity getSource() {
            return source;
        }
    }
    
    @Cancelable
    public static class StunTriggered extends TruePowerStunEvent {
        private long stunDuration;
        
        public StunTriggered(ITruePowerStunData data, long stunDuration) {
            super(data);
            this.stunDuration = stunDuration;
        }
        
        public long getStunDuration() {
            return stunDuration;
        }
        
        public void setStunDuration(long stunDuration) {
            this.stunDuration = stunDuration;
        }
    }
}
