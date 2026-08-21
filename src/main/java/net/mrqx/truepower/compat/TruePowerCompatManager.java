package net.mrqx.truepower.compat;

import com.github.exopandora.shouldersurfing.ShoulderSurfingCommon;
import dev.latvian.mods.kubejs.KubeJS;
import net.minecraft.server.packs.PackResources;
import net.minecraft.server.packs.resources.ReloadableResourceManager;
import net.mrqx.truepower.compat.epicfight.TruePowerEpicFight;
import net.mrqx.truepower.compat.kubejs.TruePowerKubeJS;
import net.mrqx.truepower.event.handler.PlayerAnimationRegistryHandler;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.loading.LoadingModList;
import yesman.epicfight.EpicFight;

import java.util.List;

public final class TruePowerCompatManager {
    public static final boolean PLAYER_ANIMATOR = LoadingModList.get().getModFileById("playeranimator") != null;
    public static final boolean SHOULDER_SURFING = LoadingModList.get().getModFileById(ShoulderSurfingCommon.MOD_ID) != null;
    public static final boolean KUBEJS = LoadingModList.get().getModFileById(KubeJS.MOD_ID) != null;
    public static final boolean EPIC_FIGHT = LoadingModList.get().getModFileById(EpicFight.MODID) != null;
    
    public static void clientInit(FMLClientSetupEvent event) {
        if (PLAYER_ANIMATOR) {
            PlayerAnimationRegistryHandler.getInstance().register();
        }
    }
    
    public static void commonInit(FMLCommonSetupEvent event) {
        if (EPIC_FIGHT) {
            TruePowerEpicFight.commonInit(event);
        }
    }
    
    public static void onCustomResourceLoad(List<PackResources> packs, ReloadableResourceManager resourceManager) {
        if (KUBEJS) {
            TruePowerKubeJS.onCustomResourceLoad(packs);
        }
    }
    
    public static void onModConstruct(IEventBus modEventBus, ModContainer container) {
        if (EPIC_FIGHT) {
            TruePowerEpicFight.onModConstruct(modEventBus, container);
        }
    }
}
