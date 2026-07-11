package net.mrqx.truepower.compat.shouldersurfing.mixin;

import com.github.exopandora.shouldersurfing.client.ShoulderSurfing;
import com.llamalad7.mixinextras.sugar.Local;
import mods.flammpfeil.slashblade.event.handler.MoveInputHandler;
import mods.flammpfeil.slashblade.util.InputCommand;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.event.TickEvent;
import net.mrqx.truepower.compat.shouldersurfing.util.TruePowerShoulderSurfingUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.EnumSet;
import java.util.Objects;

@Mixin(MoveInputHandler.class)
public abstract class MixinMoveInputHandler {
    @Inject(method = "onPlayerPostTick(Lnet/minecraftforge/event/TickEvent$ClientTickEvent;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;getCommandSenderWorld()Lnet/minecraft/world/level/Level;"), remap = false)
    private static void wrapOnPlayerPostTick(TickEvent.ClientTickEvent event, CallbackInfo ci, @Local(name = "commands") EnumSet<InputCommand> commands) {
        Minecraft minecraft = Minecraft.getInstance();
        Entity cameraEntity = minecraft.getCameraEntity();
        LocalPlayer player = minecraft.player;
        ShoulderSurfing shoulderSurfing = ShoulderSurfing.getInstance();
        if (shoulderSurfing.isShoulderSurfing() && player != null && Objects.equals(cameraEntity, player)) {
            TruePowerShoulderSurfingUtils.processInputCommand(commands, player);
        }
    }
}
