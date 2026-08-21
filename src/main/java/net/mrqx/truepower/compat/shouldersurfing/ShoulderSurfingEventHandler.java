package net.mrqx.truepower.compat.shouldersurfing;

import com.github.exopandora.shouldersurfing.api.client.event.*;
import com.github.exopandora.shouldersurfing.api.math.Vec2f;
import com.github.exopandora.shouldersurfing.client.ShoulderSurfing;
import com.github.exopandora.shouldersurfing.client.ShoulderSurfingCamera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.Input;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.mrqx.sbr_core.utils.SlashBladeAttackUtils;
import net.mrqx.truepower.compat.shouldersurfing.util.TruePowerShoulderSurfingUtils;
import net.mrqx.truepower.config.TruePowerClientConfig;
import net.mrqx.truepower.util.ITruePowerInput;
import net.mrqx.truepower.util.LockOnUtils;
import net.mrqx.truepower.util.TruePowerComboHelper;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.Nullable;

@OnlyIn(Dist.CLIENT)
public class ShoulderSurfingEventHandler {
    @Nullable
    public static Entity lockOnTarget = null;
    public static double zoomFactor = 1;
    
    @SuppressWarnings("unused")
    public static void onTickEvent(TickEvent event) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || !SlashBladeAttackUtils.isHoldingSlashBlade(mc.player)) {
            lockOnTarget = null;
            return;
        }
        lockOnTarget = TruePowerComboHelper.getTarget(mc.player);
    }
    
    public static void onComputeCameraCouplingEvent(ComputeCameraCouplingEvent event) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.player != null) {
            LocalPlayer player = minecraft.player;
            if (SlashBladeAttackUtils.isHoldingSlashBlade(player)) {
                event.setResult(false);
            }
        }
    }
    
    public static void onForceVanillaPlayerInputEvent(ForceVanillaPlayerInputEvent event) {
        Minecraft minecraft = Minecraft.getInstance();
        ShoulderSurfing shoulderSurfing = ShoulderSurfing.getInstance();
        if (minecraft.player != null && shoulderSurfing.isShoulderSurfing() && TruePowerClientConfig.ROTATE_INPUT_WHILE_USING_SHOULDER_SURFING.get()) {
            LocalPlayer player = minecraft.player;
            if (TruePowerComboHelper.hasTargetOrSneak(player) && player.input instanceof ITruePowerInput truePowerInput) {
                Input input = player.input;
                Vec2f rotated = TruePowerShoulderSurfingUtils.getRotatedInput(input, player, shoulderSurfing);
                
                if (truePowerInput.true_power$getTruePowerCanMove()) {
                    input.leftImpulse = rotated.x();
                    input.forwardImpulse = rotated.y();
                }
                truePowerInput.setTrue_power$truePowerLeftImpulse(rotated.x());
                truePowerInput.setTrue_power$truePowerForwardImpulse(rotated.y());
                
                event.setResult(true);
            }
        }
    }
    
    public static void onComputeTargetCameraOffsetEvent(ComputeTargetCameraOffsetEvent event) {
        if (!TruePowerClientConfig.OVERRIDE_CAMARA_OFFSET_WHILE_USING_SHOULDER_SURFING.get()) {
            return;
        }
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) {
            return;
        }
        LocalPlayer player = mc.player;
        if (!SlashBladeAttackUtils.isHoldingSlashBlade(player)) {
            return;
        }
        
        ShoulderSurfingCamera camera = ShoulderSurfing.getInstance().getCamera();
        Vec3 offset = event.getDefaultOffset().multiply(0, 0, 1);
        
        float yRot = camera.getYRot();
        if (lockOnTarget != null && lockOnTarget.isAlive() && mc.getCameraEntity() != null) {
            double maxLockDist = TruePowerClientConfig.SHOULDER_CAMERA_MAX_LOCK_DISTANCE.get();
            double maxZoomFactor = TruePowerClientConfig.SHOULDER_CAMERA_MAX_ZOOM_FACTOR.get();
            float partialTick = mc.getTimer().getGameTimeDeltaPartialTick(true);
            Vec3 camPos = TruePowerShoulderSurfingUtils.getShoulderSurfingCameraPosition(mc, mc.getCameraEntity());
            
            double targetZoom = zoomFactor;
            if (player.distanceTo(lockOnTarget) <= maxLockDist) {
                Vec3 toTarget = LockOnUtils.getEntityCenterPosition(lockOnTarget, partialTick)
                    .subtract(player.getEyePosition(partialTick));
                Vec3 localToTarget = TruePowerShoulderSurfingUtils.worldToCameraLocal(toTarget, camera);
                Vec3 forwards = TruePowerShoulderSurfingUtils.getCameraForwards(camera);
                double offCenter = Mth.clamp(toTarget.normalize().dot(forwards), 0, 1);
                double blend = Math.max(0.2, offCenter) * 0.22 + 0.252 * (1 - offCenter);
                
                offset = offset.add(localToTarget.x() * blend, localToTarget.y() * blend, 0);
                
                AABB playerBB = player.getBoundingBox();
                AABB targetBB = lockOnTarget.getBoundingBox();
                if (!(targetBB.getXsize() > 50) && !(targetBB.getYsize() > 50) && !(targetBB.getZsize() > 50)) {
                    AABB zoomOutPlayerBox = LockOnUtils.scaleAABB(playerBB, 0.9);
                    AABB zoomOutTargetBox = LockOnUtils.scaleAABB(targetBB, 0.9);
                    float pitch = -camera.getXRot();
                    if (!LockOnUtils.isFullInViewCone(camPos, zoomOutPlayerBox, pitch, yRot)
                        || !LockOnUtils.isFullInViewCone(camPos, zoomOutTargetBox, pitch, yRot)) {
                        targetZoom = Math.min(maxZoomFactor, targetZoom / 0.9);
                    } else {
                        AABB zoomInPlayerBox = LockOnUtils.scaleAABB(playerBB, 1.8);
                        AABB zoomInTargetBox = LockOnUtils.scaleAABB(targetBB, 1.8);
                        if (LockOnUtils.isFullInViewCone(camPos, zoomInPlayerBox, pitch, yRot)
                            && LockOnUtils.isFullInViewCone(camPos, zoomInTargetBox, pitch, yRot)) {
                            targetZoom = Math.max(1, targetZoom * 0.9);
                        }
                    }
                }
            } else {
                targetZoom = 1;
            }
            zoomFactor += (targetZoom - zoomFactor) * 0.1;
        } else {
            zoomFactor = 1;
        }
        
        float yawDiff = Mth.degreesDifference(yRot, player.getYRot());
        if (Math.abs(yawDiff) >= 135 && zoomFactor < 1.2) {
            offset = offset.add(0, -TruePowerClientConfig.SHOULDER_CAMERA_DOWN_OFFSET.get(), 0);
        }
        offset = offset.scale(zoomFactor);
        
        event.setResult(offset);
    }
    
    public static void onComputePlayerAimStateEvent(ComputePlayerAimStateEvent event) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.player != null) {
            LocalPlayer player = minecraft.player;
            if (SlashBladeAttackUtils.isHoldingSlashBlade(player)) {
                event.setResult(false);
            }
        }
    }
    
    public static void onComputePlayerUseItemStateEvent(ComputePlayerUseItemStateEvent event) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.player != null) {
            LocalPlayer player = minecraft.player;
            if (SlashBladeAttackUtils.isHoldingSlashBlade(player)) {
                event.setResult(false);
            }
        }
    }
    
    public static void onComputePlayerInteractionStateEvent(ComputePlayerInteractionStateEvent event) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.player != null) {
            LocalPlayer player = minecraft.player;
            if (SlashBladeAttackUtils.isHoldingSlashBlade(player)) {
                event.setResult(false);
            }
        }
    }
    
    public static void onComputePlayerAttackStateEvent(ComputePlayerAttackStateEvent event) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.player != null) {
            LocalPlayer player = minecraft.player;
            if (SlashBladeAttackUtils.isHoldingSlashBlade(player)) {
                event.setResult(false);
            }
        }
    }
    
    public static void onComputePlayerPickStateEvent(ComputePlayerPickStateEvent event) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.player != null) {
            LocalPlayer player = minecraft.player;
            if (SlashBladeAttackUtils.isHoldingSlashBlade(player)) {
                event.setResult(false);
            }
        }
    }
}
