package net.mrqx.truepower.compat.shouldersurfing.mixin;

import com.github.exopandora.shouldersurfing.client.ShoulderSurfing;
import com.llamalad7.mixinextras.sugar.Local;
import mods.flammpfeil.slashblade.client.renderer.LockonCircleRender;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.protocol.game.ServerboundMovePlayerPacket;
import net.neoforged.neoforge.client.event.RenderFrameEvent;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LockonCircleRender.class)
public abstract class MixinLockonCircleRender {
    @Inject(method = "onEntityUpdate(Lnet/neoforged/neoforge/client/event/RenderFrameEvent$Pre;)V", at = @At(value = "FIELD", target = "Lnet/minecraft/client/player/LocalPlayer;xRotO:F", opcode = Opcodes.PUTFIELD, shift = At.Shift.AFTER), remap = false)
    private static void onLockOnRenderTick(RenderFrameEvent.Pre event, CallbackInfo ci, @Local(name = "player") LocalPlayer player) {
        ShoulderSurfing shoulderSurfing = ShoulderSurfing.getInstance();
        if (shoulderSurfing.isShoulderSurfing()) {
            AccessorShoulderSurfing accessor = (AccessorShoulderSurfing) shoulderSurfing;
            accessor.setPlayerXRotO(player.xRotO);
            accessor.setPlayerYRotO(player.yRotO);
            player.connection.send(new ServerboundMovePlayerPacket.Rot(player.getYRot(), player.getXRot(), player.onGround()));
            shoulderSurfing.getCamera().setLastMovedYRot(player.getYRot());
        }
    }
}
