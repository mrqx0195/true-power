package net.mrqx.truepower.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import mods.flammpfeil.slashblade.ability.SlayerStyleArts;
import mods.flammpfeil.slashblade.event.handler.InputCommandEvent;
import mods.flammpfeil.slashblade.util.InputCommand;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.mrqx.truepower.capability.data.ITruePowerData;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.EnumSet;

@SuppressWarnings("SameReturnValue")
@Mixin(SlayerStyleArts.class)
public abstract class MixinSlayerStyleArts {
    @Shadow(remap = false)
    @Final
    static EnumSet<InputCommand> MOVE_COMMAND;
    
    @Inject(method = "<clinit>", at = @At("RETURN"), remap = false)
    private static void injectClInit(CallbackInfo ci) {
        MOVE_COMMAND.remove(InputCommand.BACK);
    }
    
    @WrapOperation(
        method = "lambda$handleForwardSprintSneak$0",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/nbt/CompoundTag;getInt(Ljava/lang/String;)I", ordinal = 0),
        remap = false
    )
    private int wrapOperationTrickUp(CompoundTag instance, String key, Operation<Integer> original) {
        if (SlayerStyleArts.AVOID_TRICKUP_PATH.equals(key)) {
            return 1;
        }
        return original.call(instance, key);
    }
    
    @WrapOperation(
        method = "processInputCommands",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerPlayer;onGround()Z", ordinal = 0)
    )
    private boolean wrapOperationTrickDown(ServerPlayer instance, Operation<Boolean> original) {
        return true;
    }
    
    @WrapOperation(
        method = "processInputCommands",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerPlayer;onGround()Z", ordinal = 1)
    )
    private boolean wrapOperationTrickDodge(ServerPlayer instance, Operation<Boolean> original) {
        ITruePowerData data = ITruePowerData.get(instance);
        return (data == null || data.canMove()) && original.call(instance);
    }
    
    @Inject(
        method = "onInputChange(Lmods/flammpfeil/slashblade/event/handler/InputCommandEvent;)V",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;isEmpty()Z"),
        cancellable = true
    )
    private void injectAllTrick(InputCommandEvent event, CallbackInfo ci) {
        ServerPlayer sender = event.getEntity();
        ITruePowerData data = ITruePowerData.get(sender);
        if (data != null && data.getAvoidTrick() > 0) {
            ci.cancel();
        }
    }
}
