package net.mrqx.truepower.compat.shouldersurfing.mixin;

import com.github.exopandora.shouldersurfing.client.ShoulderSurfing;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value = ShoulderSurfing.class, remap = false)
public interface AccessorShoulderSurfing {
    @Accessor("playerXRotO")
    void setPlayerXRotO(float playerXRotO);

    @Accessor("playerYRotO")
    void setPlayerYRotO(float playerYRotO);
}
