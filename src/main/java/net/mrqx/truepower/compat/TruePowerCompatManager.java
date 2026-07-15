package net.mrqx.truepower.compat;

import net.minecraft.server.packs.PackResources;
import net.minecraft.server.packs.resources.ReloadableResourceManager;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.loading.LoadingModList;
import net.mrqx.truepower.compat.kubejs.TruePowerKubeJS;
import net.mrqx.truepower.event.handler.PlayerAnimationRegistryHandler;

import java.util.List;

@SuppressWarnings("unused")
public class TruePowerCompatManager {
    public static final boolean PLAYER_ANIMATOR = LoadingModList.get().getModFileById("playeranimator") != null;
    public static final boolean SHOULDER_SURFING = LoadingModList.get().getModFileById("shouldersurfing") != null;
    public static final boolean KUBEJS = LoadingModList.get().getModFileById("kubejs") != null;
    
    @OnlyIn(Dist.CLIENT)
    public static void clientInit(FMLClientSetupEvent event) {
        if (PLAYER_ANIMATOR) {
            PlayerAnimationRegistryHandler.getInstance().register();
        }
    }
    
    public static void onCustomResourceLoad(List<PackResources> packs, ReloadableResourceManager resourceManager) {
        if (KUBEJS) {
            TruePowerKubeJS.onCustomResourceLoad(packs, resourceManager);
        }
    }
}
