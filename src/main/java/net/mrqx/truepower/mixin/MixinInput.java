package net.mrqx.truepower.mixin;

import net.minecraft.client.player.Input;
import net.mrqx.truepower.util.ITruePowerInput;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(Input.class)
public class MixinInput implements ITruePowerInput {
    @Unique
    float true_power$truePowerLeftImpulse;
    @Unique
    float true_power$truePowerForwardImpulse;
    @Unique
    boolean true_power$canMove;
    
    @Unique
    @Override
    public float true_power$getTruePowerLeftImpulse() {
        return true_power$truePowerLeftImpulse;
    }
    
    @Unique
    @Override
    public float true_power$getTruePowerForwardImpulse() {
        return true_power$truePowerForwardImpulse;
    }
    
    @Unique
    @Override
    public void setTrue_power$truePowerLeftImpulse(float true_power$truePowerLeftImpulse) {
        this.true_power$truePowerLeftImpulse = true_power$truePowerLeftImpulse;
    }
    
    @Unique
    @Override
    public void setTrue_power$truePowerForwardImpulse(float true_power$truePowerForwardImpulse) {
        this.true_power$truePowerForwardImpulse = true_power$truePowerForwardImpulse;
    }
    
    @Unique
    @Override
    public boolean true_power$getTruePowerCanMove() {
        return true_power$canMove;
    }
    
    @Unique
    @Override
    public void true_power$setTruePowerCanMove(boolean true_power$canMove) {
        this.true_power$canMove = true_power$canMove;
    }
}
