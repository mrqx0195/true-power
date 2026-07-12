package net.mrqx.truepower.event.handler.client;

import mods.flammpfeil.slashblade.capability.slashblade.BladeStateAccess;
import mods.flammpfeil.slashblade.registry.ComboStateRegistry;
import net.minecraft.client.player.Input;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.mrqx.truepower.attachment.ITruePowerData;
import net.mrqx.truepower.network.ComboCancelMessage;
import net.mrqx.truepower.util.ITruePowerInput;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.MovementInputUpdateEvent;
import net.neoforged.neoforge.network.PacketDistributor;

@EventBusSubscriber(Dist.CLIENT)
public final class MovementEventHandler {
    @SubscribeEvent
    public static void onMovementInputUpdateEvent(MovementInputUpdateEvent event) {
        Player player = event.getEntity();
        ItemStack itemStack = player.getMainHandItem();
        if (itemStack.isEmpty()) {
            return;
        }
        BladeStateAccess.of(itemStack).ifPresent(state -> {
            ITruePowerData data = ITruePowerData.get(player);
            Input input = event.getInput();
            ITruePowerInput truePowerInput = (ITruePowerInput) input;
            truePowerInput.setTrue_power$truePowerForwardImpulse(input.forwardImpulse);
            truePowerInput.setTrue_power$truePowerLeftImpulse(input.leftImpulse);
            
            if (!data.isNoMoveEnable()
                || state.getComboSeq().equals(ComboStateRegistry.NONE.getId())
                || state.getComboSeq().equals(ComboStateRegistry.STANDBY.getId())) {
                return;
            }
            if (state.getComboSeq().equals(ResourceLocation.tryParse(data.getCombo()))) {
                if (!player.onGround()) {
                    input.forwardImpulse = 0;
                    input.leftImpulse = 0;
                    truePowerInput.true_power$setTruePowerCanMove(false);
                } else {
                    boolean canNotMove = !data.canMove();
                    boolean jumpCancelOnly = data.isJumpCancelOnly();
                    if (canNotMove) {
                        input.forwardImpulse = 0;
                        input.leftImpulse = 0;
                        input.jumping = false;
                        truePowerInput.true_power$setTruePowerCanMove(false);
                    } else if (jumpCancelOnly) {
                        input.forwardImpulse = 0;
                        input.leftImpulse = 0;
                        truePowerInput.true_power$setTruePowerCanMove(false);
                    } else {
                        truePowerInput.true_power$setTruePowerCanMove(true);
                    }
                }
            } else {
                data.setCombo(state.getComboSeq().toString());
                input.forwardImpulse = 0;
                input.leftImpulse = 0;
                input.jumping = false;
                truePowerInput.true_power$setTruePowerCanMove(false);
            }
            
            boolean isJumping = input.jumping && player.onGround();
            if (input.forwardImpulse != 0 || input.leftImpulse != 0 || isJumping) {
                ComboCancelMessage comboCancelMessage = new ComboCancelMessage(input.jumping);
                PacketDistributor.sendToServer(comboCancelMessage);
            }
        });
    }
}
