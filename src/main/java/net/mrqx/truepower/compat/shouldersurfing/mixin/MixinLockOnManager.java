package net.mrqx.truepower.compat.shouldersurfing.mixin;

import com.github.exopandora.shouldersurfing.client.ShoulderSurfing;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import mods.flammpfeil.slashblade.ability.LockOnManager;
import mods.flammpfeil.slashblade.capability.slashblade.ISlashBladeState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.protocol.game.ServerboundMovePlayerPacket;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.mrqx.truepower.compat.shouldersurfing.util.TruePowerShoulderSurfingUtils;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;
import java.util.function.Predicate;

@Mixin(LockOnManager.class)
public abstract class MixinLockOnManager {
    @WrapOperation(method = "onInputChange(Lmods/flammpfeil/slashblade/event/handler/InputCommandEvent;)V", at = @At(value = "INVOKE", target = "Lmods/flammpfeil/slashblade/util/RayTraceHelper;rayTrace(Lnet/minecraft/world/level/Level;Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/phys/Vec3;Lnet/minecraft/world/phys/Vec3;DDLjava/util/function/Predicate;)Ljava/util/Optional;", remap = false), remap = false)
    private Optional<HitResult> wrapRayTrace(Level worldIn, Entity entityIn, Vec3 start, Vec3 dir, double blockReach, double entityReach, Predicate<Entity> selector, Operation<Optional<HitResult>> original) {
        if (ShoulderSurfing.getInstance().isShoulderSurfing()) {
            return TruePowerShoulderSurfingUtils.lockOnRayTraceShoulderSurfing(worldIn, entityIn, entityReach, selector);
        } else {
            return original.call(worldIn, entityIn, start, dir, blockReach, entityReach, selector);
        }
    }
    
    @Inject(method = "lambda$onEntityUpdate$9", at = @At(value = "FIELD", target = "Lnet/minecraft/client/player/LocalPlayer;xRotO:F", opcode = Opcodes.PUTFIELD, shift = At.Shift.AFTER), remap = false)
    private static void onLockOnRenderTick(LocalPlayer player, Minecraft mcinstance, ISlashBladeState s, CallbackInfo ci) {
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
