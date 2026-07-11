package net.mrqx.truepower.capability.stun;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nullable;

public class TruePowerStunDataProvider implements ICapabilityProvider, INBTSerializable<CompoundTag> {
    public static final Capability<ITruePowerStunData> TRUE_POWER_STUN_DATA = CapabilityManager.get(new CapabilityToken<>() {
    });
    private final ITruePowerStunData data;
    private final LazyOptional<ITruePowerStunData> lazyOptional;
    
    public TruePowerStunDataProvider(LivingEntity livingEntity) {
        this.data = new TruePowerStunData(livingEntity);
        this.lazyOptional = LazyOptional.of(() -> this.data);
    }
    
    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, @Nullable Direction side) {
        return TRUE_POWER_STUN_DATA.orEmpty(cap, this.lazyOptional);
    }
    
    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        tag.putFloat("stunValue", this.data.getStunValue());
        return tag;
    }
    
    @Override
    public void deserializeNBT(CompoundTag tag) {
        this.data.setStunValue(tag.getFloat("stunValue"));
    }
}
