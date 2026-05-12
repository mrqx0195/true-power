package net.mrqx.truepower.event.handler.client;

import mods.flammpfeil.slashblade.capability.slashblade.BladeStateAccess;
import mods.flammpfeil.slashblade.registry.ComboStateRegistry;
import net.minecraft.client.player.Input;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.mrqx.truepower.network.ComboCancelMessage;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.MovementInputUpdateEvent;
import net.neoforged.neoforge.network.PacketDistributor;

@EventBusSubscriber(Dist.CLIENT)
public class MovementEventHandler {
    @SubscribeEvent
    public static void onMovementInputUpdateEvent(MovementInputUpdateEvent event) {
        Player player = event.getEntity();
        ItemStack itemStack = player.getMainHandItem();
        if (itemStack.isEmpty()) {
            return;
        }
        BladeStateAccess.of(itemStack).ifPresent(state -> {
            CompoundTag persistentData = player.getPersistentData();
            Input input = event.getInput();
            if (!persistentData.getBoolean("truePower.noMoveEnable")
                || state.getComboSeq().equals(ComboStateRegistry.NONE.getId())
                || state.getComboSeq().equals(ComboStateRegistry.STANDBY.getId())) {
                return;
            }
            if (state.getComboSeq().equals(ResourceLocation.tryParse(persistentData.getString("truePower.combo")))) {
                if (!player.onGround()) {
                    input.forwardImpulse = 0;
                    input.leftImpulse = 0;
                } else {
                    boolean canNotMove = !persistentData.getBoolean("truePower.canMove");
                    boolean jumpCancelOnly = persistentData.getBoolean("truePower.jumpCancelOnly");
                    if (canNotMove) {
                        input.forwardImpulse = 0;
                        input.leftImpulse = 0;
                        input.jumping = false;
                    } else if (jumpCancelOnly) {
                        input.forwardImpulse = 0;
                        input.leftImpulse = 0;
                    }
                }
            } else {
                persistentData.putString("truePower.combo", state.getComboSeq().toString());
                input.forwardImpulse = 0;
                input.leftImpulse = 0;
                input.jumping = false;
            }
            
            boolean isJumping = input.jumping && player.onGround();
            if (input.forwardImpulse != 0 || input.leftImpulse != 0 || isJumping) {
                ComboCancelMessage comboCancelMessage = new ComboCancelMessage(input.jumping);
                PacketDistributor.sendToServer(comboCancelMessage);
            }
        });
    }
}
