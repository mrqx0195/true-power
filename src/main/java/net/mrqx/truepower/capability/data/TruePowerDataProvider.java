package net.mrqx.truepower.capability.data;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nullable;

public class TruePowerDataProvider implements ICapabilityProvider, INBTSerializable<CompoundTag> {
    public static final Capability<ITruePowerData> TRUE_POWER_DATA = CapabilityManager.get(new CapabilityToken<>() {
    });
    private final ITruePowerData data = new TruePowerData();
    private final LazyOptional<ITruePowerData> lazyOptional = LazyOptional.of(() -> this.data);
    
    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, @Nullable Direction side) {
        return TRUE_POWER_DATA.orEmpty(cap, this.lazyOptional);
    }
    
    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        tag.putString("combo", this.data.getCombo());
        tag.putBoolean("canMove", this.data.canMove());
        tag.putBoolean("jumpCancelOnly", this.data.isJumpCancelOnly());
        tag.putBoolean("noMoveEnable", this.data.isNoMoveEnable());
        tag.putInt("avoidTrick", this.data.getAvoidTrick());
        return tag;
    }
    
    @Override
    public void deserializeNBT(CompoundTag tag) {
        this.data.setCombo(tag.getString("combo"));
        this.data.setCanMove(tag.getBoolean("canMove"));
        this.data.setJumpCancelOnly(tag.getBoolean("jumpCancelOnly"));
        this.data.setNoMoveEnable(tag.getBoolean("noMoveEnable"));
        this.data.setAvoidTrick(tag.getInt("avoidTrick"));
    }
}
