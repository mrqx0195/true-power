package net.mrqx.truepower.event.handler.client;

import mods.flammpfeil.slashblade.item.ItemSlashBlade;
import mods.flammpfeil.slashblade.registry.ComboStateRegistry;
import net.minecraft.client.player.Input;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.MovementInputUpdateEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.mrqx.truepower.network.ComboCancelMessage;
import net.mrqx.truepower.network.NetworkManager;

@Mod.EventBusSubscriber(Dist.CLIENT)
@OnlyIn(Dist.CLIENT)
public class MovementEventHandler {
    @SubscribeEvent
    public static void onMovementInputUpdateEvent(MovementInputUpdateEvent event) {
        Player player = event.getEntity();
        if (player.getMainHandItem().is(Items.AIR)) {
            return;
        }
        player.getMainHandItem().getCapability(ItemSlashBlade.BLADESTATE).ifPresent(state -> {
            CompoundTag persistentData = player.getPersistentData();
            Input input = event.getInput();
            if (!persistentData.getBoolean("truePower.no_move_enable") || state.getComboSeq().equals(ComboStateRegistry.NONE.getId())) {
                return;
            }
            if (state.getComboSeq().equals(new ResourceLocation(persistentData.getString("truePower.combo")))) {
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
            } else {
                persistentData.putString("truePower.combo", state.getComboSeq().toString());
                input.forwardImpulse = 0;
                input.leftImpulse = 0;
                input.jumping = false;
            }

            if (input.forwardImpulse != 0 || input.leftImpulse != 0) {
                ComboCancelMessage comboCancelMessage = new ComboCancelMessage();
                comboCancelMessage.isJump = input.jumping;
                NetworkManager.INSTANCE.sendToServer(comboCancelMessage);
            }
        });
    }
}
