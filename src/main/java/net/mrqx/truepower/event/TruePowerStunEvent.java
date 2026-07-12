package net.mrqx.truepower.event;

import net.minecraft.world.entity.LivingEntity;
import net.mrqx.truepower.attachment.ITruePowerStunData;
import net.neoforged.bus.api.ICancellableEvent;
import net.neoforged.neoforge.event.entity.living.LivingEvent;
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
    
    public static class AddStunValue extends TruePowerStunEvent implements ICancellableEvent {
        @Nullable
        private final LivingEntity source;
        private float additionValue;
        
        public AddStunValue(ITruePowerStunData data, @Nullable LivingEntity source, float additionValue) {
            super(data);
            this.source = source;
            this.additionValue = additionValue;
        }
        
        @Nullable
        public LivingEntity getSource() {
            return source;
        }
        
        public float getAdditionValue() {
            return additionValue;
        }
        
        public void setAdditionValue(float additionValue) {
            this.additionValue = additionValue;
        }
    }
    
    public static class StunTriggered extends TruePowerStunEvent implements ICancellableEvent {
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
