package net.mrqx.truepower.compat.shouldersurfing.util;

import com.github.exopandora.shouldersurfing.api.client.world.phys.PickContext;
import com.github.exopandora.shouldersurfing.api.client.world.phys.PickVector;
import com.github.exopandora.shouldersurfing.api.util.Couple;
import mods.flammpfeil.slashblade.util.InputCommand;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.mrqx.truepower.util.ITruePowerInput;
import net.mrqx.truepower.util.LockOnUtils;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import java.util.EnumSet;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

@OnlyIn(Dist.CLIENT)
public class TruePowerShoulderSurfingUtils {
    public static void processInputCommand(EnumSet<InputCommand> commands, LocalPlayer player) {
        if (player.input instanceof ITruePowerInput truePowerInput) {
            List.of(InputCommand.FORWARD, InputCommand.BACK, InputCommand.LEFT, InputCommand.RIGHT).forEach(commands::remove);
            float x = truePowerInput.true_power$getTruePowerLeftImpulse();
            float y = truePowerInput.true_power$getTruePowerForwardImpulse();
            if (x != 0 && y != 0) {
                if (Math.abs(x) > Math.abs(y)) {
                    if (x >= 0) {
                        commands.add(InputCommand.LEFT);
                    } else {
                        commands.add(InputCommand.RIGHT);
                    }
                } else {
                    if (y >= 0) {
                        commands.add(InputCommand.FORWARD);
                    } else {
                        commands.add(InputCommand.BACK);
                    }
                }
            }
        }
    }
    
    public static Optional<HitResult> lockOnRayTraceShoulderSurfing(Level worldIn, Entity entityIn, double entityReach, Predicate<Entity> selector) {
        Minecraft minecraft = Minecraft.getInstance();
        Camera camera = minecraft.gameRenderer.getMainCamera();
        PickContext pickContext = new PickContext.Builder(camera).withPickVector(PickVector.CAMERA).dynamicTrace().build();
        HitResult hitResult = null;
        Couple<Vec3> vec3Couple = pickContext.entityTrace(entityReach, minecraft.getTimer().getGameTimeDeltaPartialTick(true));
        AABB area = entityIn.getBoundingBox().expandTowards(vec3Couple.left().vectorTo(vec3Couple.right()).scale(entityReach)).inflate(1.0F);
        EntityHitResult entityHitResult = LockOnUtils.advanceRayTrace(worldIn, entityIn, vec3Couple.left(), vec3Couple.right(), area, selector);
        if (entityHitResult != null) {
            hitResult = entityHitResult;
        }
        
        return Optional.ofNullable(hitResult);
    }
}
