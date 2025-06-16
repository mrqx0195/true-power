package net.mrqx.truepower.event.handler;

import mods.flammpfeil.slashblade.item.ItemSlashBlade;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.client.event.ComputeFovModifierEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class FovHandler {
    @SubscribeEvent
    public static void onComputeFovModifierEvent(ComputeFovModifierEvent event) {
        Player player = event.getPlayer();
        if (player.getMainHandItem().getCapability(ItemSlashBlade.BLADESTATE).isPresent()) {
            event.setNewFovModifier(1);
        }
    }
}
