package net.mrqx.truepower.attachment;

import net.minecraft.world.entity.LivingEntity;
import net.mrqx.truepower.util.CollideAction;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface ITruePowerData {
    record CollideInterval(int start, int end, CollideAction action) {
    }
    
    record BoolInterval(int start, int end, boolean value) {
    }
    
    String getCombo();
    
    void setCombo(String combo);
    
    boolean canMove();
    
    void setCanMove(boolean canMove);
    
    boolean isJumpCancelOnly();
    
    void setJumpCancelOnly(boolean jumpCancelOnly);
    
    boolean isNoMoveEnable();
    
    void setNoMoveEnable(boolean noMoveEnable);
    
    int getAvoidTrick();
    
    void setAvoidTrick(int avoidTrick);
    
    boolean isTrickDowning();
    
    void setTrickDowning(boolean trickDowning);
    
    @Nullable List<CollideInterval> getCollideIntervals();
    
    void setCollideIntervals(@Nullable List<CollideInterval> collideIntervals);
    
    @Nullable List<BoolInterval> getLockOnIntervals();
    
    void setLockOnIntervals(@Nullable List<BoolInterval> lockOnIntervals);
    
    @Nullable List<BoolInterval> getSnapLockOnIntervals();
    
    void setSnapLockOnIntervals(@Nullable List<BoolInterval> snapLockOnIntervals);
    
    static ITruePowerData get(LivingEntity entity) {
        return entity.getData(TruePowerAttachments.TRUE_POWER_DATA.get());
    }
}
