package net.mrqx.truepower.compat.shouldersurfing;

import com.github.exopandora.shouldersurfing.api.client.event.*;
import com.github.exopandora.shouldersurfing.api.math.Vec2f;
import com.github.exopandora.shouldersurfing.client.ShoulderSurfing;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.Input;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec2;
import net.mrqx.sbr_core.utils.SlashBladeAttackUtils;
import net.mrqx.truepower.util.ITruePowerInput;
import net.mrqx.truepower.util.TruePowerComboHelper;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ShoulderSurfingEventHandler {
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
        if (minecraft.player != null && shoulderSurfing.isShoulderSurfing()) {
            LocalPlayer player = minecraft.player;
            if (TruePowerComboHelper.hasTargetOrSneak(player) && player.input instanceof ITruePowerInput truePowerInput) {
                Input input = player.input;
                Vec2f moveVector = new Vec2f(new Vec2(truePowerInput.true_power$getTruePowerLeftImpulse(), truePowerInput.true_power$getTruePowerForwardImpulse()));
                float yRot = player.getYRot();
                Vec2f rotated = moveVector.rotateDegrees(Mth.degreesDifference(yRot, shoulderSurfing.getCamera().getYRot()));
                
                if (truePowerInput.true_power$getTruePowerCanMove()) {
                    input.leftImpulse = moveVector.x();
                    input.forwardImpulse = moveVector.y();
                }
                truePowerInput.setTrue_power$truePowerLeftImpulse(rotated.x());
                truePowerInput.setTrue_power$truePowerForwardImpulse(rotated.y());
                
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
