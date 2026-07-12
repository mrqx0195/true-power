package net.mrqx.truepower.compat;

import net.minecraft.server.packs.PackResources;
import net.minecraft.server.packs.resources.ReloadableResourceManager;
import net.mrqx.truepower.compat.kubejs.TruePowerKubeJS;
import net.mrqx.truepower.event.handler.PlayerAnimationRegistryHandler;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.loading.LoadingModList;

import java.util.List;

public final class TruePowerCompatManager {
    public static final boolean PLAYER_ANIMATOR = LoadingModList.get().getModFileById("playeranimator") != null;
    public static final boolean SHOULDER_SURFING = LoadingModList.get().getModFileById("shouldersurfing") != null;
    public static final boolean KUBEJS = LoadingModList.get().getModFileById("kubejs") != null;
    
    public static void clientInit(FMLClientSetupEvent event) {
        if (PLAYER_ANIMATOR) {
            PlayerAnimationRegistryHandler.getInstance().register();
        }
    }
    
    public static void onCustomResourceLoad(List<PackResources> packs, ReloadableResourceManager resourceManager) {
        if (KUBEJS) {
            TruePowerKubeJS.onCustomResourceLoad(packs);
        }
    }
}
