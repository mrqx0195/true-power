package net.mrqx.truepower.compat.shouldersurfing.util;

import com.github.exopandora.shouldersurfing.api.client.world.phys.PickContext;
import com.github.exopandora.shouldersurfing.api.client.world.phys.PickVector;
import com.github.exopandora.shouldersurfing.api.math.Vec2f;
import com.github.exopandora.shouldersurfing.api.util.Couple;
import com.github.exopandora.shouldersurfing.client.ShoulderSurfing;
import com.github.exopandora.shouldersurfing.client.ShoulderSurfingCamera;
import mods.flammpfeil.slashblade.util.InputCommand;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.Input;
import net.minecraft.client.player.KeyboardInput;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.*;
import net.mrqx.truepower.util.LockOnUtils;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import java.util.EnumSet;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

@OnlyIn(Dist.CLIENT)
public final class TruePowerShoulderSurfingUtils {
    public static void processInputCommand(EnumSet<InputCommand> commands, LocalPlayer player) {
        List.of(InputCommand.FORWARD, InputCommand.BACK, InputCommand.LEFT, InputCommand.RIGHT).forEach(commands::remove);
        KeyboardInput input = new KeyboardInput(Minecraft.getInstance().options);
        input.tick(player.isMovingSlowly(), (float) player.getAttributeValue(Attributes.SNEAKING_SPEED));
        Vec2f rotated = getRotatedInput(input, player, ShoulderSurfing.getInstance());
        Vec2 v = new Vec2(rotated.x(), rotated.y()).normalized();
        float x = v.x;
        float y = v.y;
        if (Math.abs(x) > Math.abs(y)) {
            if (x > 0.5) {
                commands.add(InputCommand.LEFT);
            } else if (x < -0.5) {
                commands.add(InputCommand.RIGHT);
            }
        } else {
            if (y > 0.5) {
                commands.add(InputCommand.FORWARD);
            } else if (y < -0.5) {
                commands.add(InputCommand.BACK);
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
    
    public static Vec3 getShoulderSurfingCameraPosition(Minecraft minecraft, Entity cameraEntity) {
        ShoulderSurfingCamera camera = ShoulderSurfing.getInstance().getCamera();
        Vec3 targetOffset = camera.getTargetOffset();
        float partialTick = minecraft.getTimer().getGameTimeDeltaPartialTick(true);
        Vec3 eyePos = cameraEntity.getEyePosition(partialTick);
        double yawRad = Math.toRadians(camera.getYRot());
        double pitchRad = Math.toRadians(camera.getXRot());
        double cosP = Math.cos(pitchRad), sinP = Math.sin(pitchRad);
        double y1 = targetOffset.y() * cosP - targetOffset.z() * sinP;
        double z1 = targetOffset.y() * sinP + targetOffset.z() * cosP;
        double cosY = Math.cos(yawRad), sinY = Math.sin(yawRad);
        return eyePos.add(
            -targetOffset.x() * cosY + z1 * sinY,
            y1,
            -targetOffset.x() * sinY - z1 * cosY
        );
    }
    
    public static Vec2f getRotatedInput(Input input, LocalPlayer player, ShoulderSurfing shoulderSurfing) {
        Vec2f moveVector = new Vec2f(input.getMoveVector());
        float yRot = player.getYRot();
        return moveVector.rotateDegrees(Mth.degreesDifference(yRot, shoulderSurfing.getCamera().getYRot()));
    }
    
    public static Vec3 worldToCameraLocal(Vec3 worldVec, ShoulderSurfingCamera camera) {
        double yawRad = Math.toRadians(camera.getYRot());
        double pitchRad = Math.toRadians(camera.getXRot());
        
        double cosY = Math.cos(yawRad), sinY = Math.sin(yawRad);
        double x1 = worldVec.x() * cosY + worldVec.z() * sinY;
        double y1 = worldVec.y();
        double z1 = -worldVec.x() * sinY + worldVec.z() * cosY;
        
        double cosP = Math.cos(pitchRad), sinP = Math.sin(pitchRad);
        return new Vec3(x1, y1 * cosP + z1 * sinP, -y1 * sinP + z1 * cosP);
    }
    
    public static Vec3 getCameraForwards(ShoulderSurfingCamera camera) {
        double yawRad = Math.toRadians(camera.getYRot());
        double pitchRad = Math.toRadians(camera.getXRot());
        return new Vec3(
            -Math.sin(yawRad) * Math.cos(pitchRad),
            -Math.sin(pitchRad),
            Math.cos(yawRad) * Math.cos(pitchRad)
        );
    }
}
