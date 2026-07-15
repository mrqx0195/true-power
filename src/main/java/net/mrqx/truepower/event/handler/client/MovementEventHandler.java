package net.mrqx.truepower.event.handler.client;

import mods.flammpfeil.slashblade.item.ItemSlashBlade;
import mods.flammpfeil.slashblade.registry.ComboStateRegistry;
import net.minecraft.client.player.Input;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.MovementInputUpdateEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.mrqx.truepower.capability.data.ITruePowerData;
import net.mrqx.truepower.network.ComboCancelMessage;
import net.mrqx.truepower.network.NetworkManager;
import net.mrqx.truepower.util.ITruePowerInput;

@Mod.EventBusSubscriber(Dist.CLIENT)
@OnlyIn(Dist.CLIENT)
public final class MovementEventHandler {
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onMovementInputUpdateEvent(MovementInputUpdateEvent event) {
        Player player = event.getEntity();
        ItemStack itemStack = player.getMainHandItem();
        if (itemStack.isEmpty()) {
            return;
        }
        itemStack.getCapability(ItemSlashBlade.BLADESTATE).ifPresent(state -> {
            ITruePowerData data = ITruePowerData.get(player);
            if (data == null) {
                return;
            }
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
                ComboCancelMessage comboCancelMessage = new ComboCancelMessage();
                comboCancelMessage.isJump = input.jumping;
                NetworkManager.INSTANCE.sendToServer(comboCancelMessage);
                truePowerInput.true_power$setTruePowerCanMove(true);
            }
        });
    }
}
