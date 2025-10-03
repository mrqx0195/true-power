package net.mrqx.truepower.mixin;

import mods.flammpfeil.slashblade.ability.SlayerStyleArts;
import mods.flammpfeil.slashblade.event.handler.InputCommandEvent;
import mods.flammpfeil.slashblade.util.InputCommand;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.EnumSet;

@Mixin(SlayerStyleArts.class)
public abstract class MixinSlayerStyleArts {
    @Shadow(remap = false)
    @Final
    static EnumSet<InputCommand> MOVE_COMMAND;

    @Inject(method = "<clinit>", at = @At("RETURN"), remap = false)
    private static void injectClInit(CallbackInfo ci) {
        MOVE_COMMAND.remove(InputCommand.BACK);
    }

    @Redirect(
            method = "lambda$handleForwardSprintSneak$0",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/nbt/CompoundTag;getInt(Ljava/lang/String;)I", ordinal = 0),
            remap = false
    )
    private int redirectTrickUp(CompoundTag instance, String pKey) {
        if (SlayerStyleArts.AVOID_TRICKUP_PATH.equals(pKey)) {
            return 1;
        }
        return instance.getInt(pKey);
    }

    @Redirect(
            method = "processInputCommands",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerPlayer;onGround()Z", ordinal = 0)
    )
    private boolean redirectTrickDown(ServerPlayer instance) {
        return true;
    }

    @Redirect(
            method = "processInputCommands",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerPlayer;onGround()Z", ordinal = 1)
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
