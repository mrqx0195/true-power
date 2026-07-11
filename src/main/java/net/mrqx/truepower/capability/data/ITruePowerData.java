package net.mrqx.truepower.capability.data;

import net.minecraft.world.entity.LivingEntity;
import net.mrqx.truepower.util.CollideAction;

import javax.annotation.Nullable;
import java.util.List;

public interface ITruePowerData {
    String getCombo();
    
    void setCombo(String combo);
    
    boolean canMove();
    
    void setCanMove(boolean canMove);
    
    boolean isJumpCancelOnly();
    
    void setJumpCancelOnly(boolean jumpCancelOnly);
    
    boolean isNoMoveEnable();
    
    void setNoMoveEnable(boolean noMoveEnable);
    
    int getAvoidTrick();
    
    void setAvoidTrick(int ticks);
    
    boolean isTrickDowning();
    
    void setTrickDowning(boolean trickDowning);
    
    @Nullable
    List<CollideInterval> getCollideIntervals();
    
    void setCollideIntervals(@Nullable List<CollideInterval> intervals);
    
    record CollideInterval(int start, int end, CollideAction action) {
    }
    
    @Nullable
    List<BoolInterval> getLockOnIntervals();
    
    void setLockOnIntervals(@Nullable List<BoolInterval> intervals);
    
    record BoolInterval(int start, int end, boolean value) {
    }
    
    @Nullable
    List<BoolInterval> getSnapLockOnIntervals();
    
    void setSnapLockOnIntervals(@Nullable List<BoolInterval> intervals);
    
    @Nullable
    static ITruePowerData get(LivingEntity entity) {
        return entity.getCapability(TruePowerDataProvider.TRUE_POWER_DATA).resolve().orElse(null);
    }
}
