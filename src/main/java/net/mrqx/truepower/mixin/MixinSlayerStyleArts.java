package net.mrqx.truepower.mixin;

import mods.flammpfeil.slashblade.ability.SlayerStyleArts;
import mods.flammpfeil.slashblade.event.handler.InputCommandEvent;
import mods.flammpfeil.slashblade.util.InputCommand;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Collection;
import java.util.EnumSet;

@Mixin(SlayerStyleArts.class)
public abstract class MixinSlayerStyleArts {
    @Redirect(
            method = "lambda$onInputChange$0",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/nbt/CompoundTag;getInt(Ljava/lang/String;)I", ordinal = 0)
    )
    private int redirectTrickUp(CompoundTag instance, String pKey) {
        if ("sb.avoid.trickup".equals(pKey)) {
            return 1;
        }
        return instance.getInt(pKey);
    }

    @Redirect(
            method = "onInputChange(Lmods/flammpfeil/slashblade/event/handler/InputCommandEvent;)V",
            at = @At(value = "INVOKE", target = "Ljava/util/EnumSet;containsAll(Ljava/util/Collection;)Z"),
            slice = @Slice(
                    from = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerPlayer;onGround()Z"),
                    to = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerPlayer;setPos(Lnet/minecraft/world/phys/Vec3;)V")
            )
    )
    private boolean redirectTrickDown(EnumSet<InputCommand> instance, Collection<InputCommand> collection) {
        return false;
    }

    @Redirect(
            method = "onInputChange(Lmods/flammpfeil/slashblade/event/handler/InputCommandEvent;)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerPlayer;onGround()Z", ordinal = 2)
    )
    private boolean redirectTrickDodge(ServerPlayer instance) {
        return instance.getPersistentData().getBoolean("truePower.canMove") && instance.onGround();
    }

    @Inject(
            method = "onInputChange(Lmods/flammpfeil/slashblade/event/handler/InputCommandEvent;)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;isEmpty()Z"),
            cancellable = true
    )
    private void injectAllTrick(InputCommandEvent event, CallbackInfo ci) {
        ServerPlayer sender = event.getEntity();
        if (sender.getPersistentData().getInt("truepower.avoid.trick") > 0) {
            ci.cancel();
        }
    }
}
