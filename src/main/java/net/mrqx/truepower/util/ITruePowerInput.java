package net.mrqx.truepower.util;

import org.spongepowered.asm.mixin.Unique;

public interface ITruePowerInput {
    @Unique
    float true_power$getTruePowerLeftImpulse();
    
    @Unique
    float true_power$getTruePowerForwardImpulse();
    
    @Unique
    void setTrue_power$truePowerLeftImpulse(float true_power$truePowerLeftImpulse);
    
    @Unique
    void setTrue_power$truePowerForwardImpulse(float true_power$truePowerForwardImpulse);
    
    @Unique
    boolean true_power$getTruePowerCanMove();
    
    @Unique
    void true_power$setTruePowerCanMove(boolean true_power$canMove);
}
