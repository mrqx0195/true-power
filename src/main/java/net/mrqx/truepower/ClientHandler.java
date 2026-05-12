package net.mrqx.truepower;

import net.mrqx.truepower.event.handler.PlayerAnimationRegistryHandler;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;

@EventBusSubscriber(Dist.CLIENT)
public class ClientHandler {
    @SubscribeEvent
    public static void doClientStuff(FMLClientSetupEvent event) {
        if (ModList.get().isLoaded("playeranimator")) {
            PlayerAnimationRegistryHandler.getInstance().register();
        }
    }
}
