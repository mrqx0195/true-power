package net.mrqx.truepower.mixin;

import net.minecraft.server.level.ServerPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ServerPlayer.class)
public interface AccessorServerPlayer {
    @Accessor("isChangingDimension")
    void setIsChangingDimension(boolean isChangingDimension);
}
