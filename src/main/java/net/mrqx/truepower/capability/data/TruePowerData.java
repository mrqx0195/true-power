package net.mrqx.truepower.capability.data;

import javax.annotation.Nullable;
import java.util.List;

public class TruePowerData implements ITruePowerData {
    private String combo = "";
    private boolean canMove;
    private boolean jumpCancelOnly;
    private boolean noMoveEnable;
    private int avoidTrick;
    private boolean trickDowning;
    @Nullable
    private List<CollideInterval> collideIntervals;
    @Nullable
    private List<BoolInterval> lockOnIntervals;
    @Nullable
    private List<BoolInterval> snapLockOnIntervals;
    
    @Override
    public String getCombo() {
        return this.combo;
    }
    
    @Override
    public void setCombo(String combo) {
        this.combo = combo;
    }
    
    @Override
    public boolean canMove() {
        return this.canMove;
    }
    
    @Override
    public void setCanMove(boolean canMove) {
        this.canMove = canMove;
    }
    
    @Override
    public boolean isJumpCancelOnly() {
        return this.jumpCancelOnly;
    }
    
    @Override
    public void setJumpCancelOnly(boolean jumpCancelOnly) {
        this.jumpCancelOnly = jumpCancelOnly;
    }
    
    @Override
    public boolean isNoMoveEnable() {
        return this.noMoveEnable;
    }
    
    @Override
    public void setNoMoveEnable(boolean noMoveEnable) {
        this.noMoveEnable = noMoveEnable;
    }
    
    @Override
    public int getAvoidTrick() {
        return this.avoidTrick;
    }
    
    @Override
    public void setAvoidTrick(int ticks) {
        this.avoidTrick = ticks;
    }
    
    @Override
    public boolean isTrickDowning() {
        return this.trickDowning;
    }
    
    @Override
    public void setTrickDowning(boolean trickDowning) {
        this.trickDowning = trickDowning;
    }
    
    @Nullable
    @Override
    public List<CollideInterval> getCollideIntervals() {
        return this.collideIntervals;
    }
    
    @Override
    public void setCollideIntervals(@Nullable List<CollideInterval> intervals) {
        this.collideIntervals = intervals;
    }
    
    @Nullable
    @Override
    public List<BoolInterval> getLockOnIntervals() {
        return this.lockOnIntervals;
    }
    
    @Override
    public void setLockOnIntervals(@Nullable List<BoolInterval> intervals) {
        this.lockOnIntervals = intervals;
    }
    
    @Nullable
    @Override
    public List<BoolInterval> getSnapLockOnIntervals() {
        return this.snapLockOnIntervals;
    }
    
    @Override
    public void setSnapLockOnIntervals(@Nullable List<BoolInterval> intervals) {
        this.snapLockOnIntervals = intervals;
    }
}
