package net.mrqx.truepower.attachment;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.neoforged.neoforge.common.util.INBTSerializable;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class TruePowerData implements ITruePowerData, INBTSerializable<CompoundTag> {
    private String combo = "";
    private boolean canMove;
    private boolean jumpCancelOnly;
    private boolean noMoveEnable;
    private int avoidTrick;
    private boolean trickDowning;
    @Nullable
    private List<ITruePowerData.CollideInterval> collideIntervals;
    @Nullable
    private List<ITruePowerData.BoolInterval> lockOnIntervals;
    @Nullable
    private List<ITruePowerData.BoolInterval> snapLockOnIntervals;
    
    @Override
    public String getCombo() {
        return combo;
    }
    
    @Override
    public void setCombo(String combo) {
        this.combo = combo;
    }
    
    @Override
    public boolean canMove() {
        return canMove;
    }
    
    @Override
    public void setCanMove(boolean canMove) {
        this.canMove = canMove;
    }
    
    @Override
    public boolean isJumpCancelOnly() {
        return jumpCancelOnly;
    }
    
    @Override
    public void setJumpCancelOnly(boolean jumpCancelOnly) {
        this.jumpCancelOnly = jumpCancelOnly;
    }
    
    @Override
    public boolean isNoMoveEnable() {
        return noMoveEnable;
    }
    
    @Override
    public void setNoMoveEnable(boolean noMoveEnable) {
        this.noMoveEnable = noMoveEnable;
    }
    
    @Override
    public int getAvoidTrick() {
        return avoidTrick;
    }
    
    @Override
    public void setAvoidTrick(int avoidTrick) {
        this.avoidTrick = avoidTrick;
    }
    
    @Override
    public boolean isTrickDowning() {
        return trickDowning;
    }
    
    @Override
    public void setTrickDowning(boolean trickDowning) {
        this.trickDowning = trickDowning;
    }
    
    @Override
    @Nullable
    public List<ITruePowerData.CollideInterval> getCollideIntervals() {
        return collideIntervals;
    }
    
    @Override
    public void setCollideIntervals(@Nullable List<ITruePowerData.CollideInterval> collideIntervals) {
        this.collideIntervals = collideIntervals;
    }
    
    @Override
    @Nullable
    public List<ITruePowerData.BoolInterval> getLockOnIntervals() {
        return lockOnIntervals;
    }
    
    @Override
    public void setLockOnIntervals(@Nullable List<ITruePowerData.BoolInterval> lockOnIntervals) {
        this.lockOnIntervals = lockOnIntervals;
    }
    
    @Override
    @Nullable
    public List<ITruePowerData.BoolInterval> getSnapLockOnIntervals() {
        return snapLockOnIntervals;
    }
    
    @Override
    public void setSnapLockOnIntervals(@Nullable List<ITruePowerData.BoolInterval> snapLockOnIntervals) {
        this.snapLockOnIntervals = snapLockOnIntervals;
    }
    
    @Override
    public CompoundTag serializeNBT(HolderLookup.Provider provider) {
        CompoundTag tag = new CompoundTag();
        tag.putString("combo", this.combo);
        tag.putBoolean("canMove", this.canMove);
        tag.putBoolean("jumpCancelOnly", this.jumpCancelOnly);
        tag.putBoolean("noMoveEnable", this.noMoveEnable);
        tag.putInt("avoidTrick", this.avoidTrick);
        return tag;
    }
    
    @Override
    public void deserializeNBT(HolderLookup.Provider provider, CompoundTag tag) {
        this.combo = tag.getString("combo");
        this.canMove = tag.getBoolean("canMove");
        this.jumpCancelOnly = tag.getBoolean("jumpCancelOnly");
        this.noMoveEnable = tag.getBoolean("noMoveEnable");
        this.avoidTrick = tag.getInt("avoidTrick");
    }
}
