package net.mrqx.truepower;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.mrqx.truepower.event.handler.PlayerAnimationRegistryHandler;

@Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
@OnlyIn(Dist.CLIENT)
public class ClientHandler {
    @SubscribeEvent
    public static void doClientStuff(FMLClientSetupEvent event) {
        if (ModList.get().isLoaded("playeranimator")) {
            PlayerAnimationRegistryHandler.getInstance().register();
        }
    }
}
