package net.mrqx.truepower.util;

import com.mojang.logging.LogUtils;
import net.minecraft.server.packs.PackResources;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.*;
import net.minecraft.util.Unit;
import net.minecraftforge.fml.loading.LoadingModList;
import net.minecraftforge.forgespi.language.IModFileInfo;
import net.minecraftforge.resource.ResourcePackLoader;
import net.mrqx.truepower.TruePowerMod;
import net.mrqx.truepower.compat.TruePowerCompatManager;
import net.mrqx.truepower.mixin.AccessorReloadableResourceManager;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.stream.Collectors;

public final class CustomEarlyResourceLoader {
    public static void load(List<PreparableReloadListener> listeners) {
        List<PackResources> packs = new ArrayList<>();
        LoadingModList loadingModList = LoadingModList.get();
        for (IModFileInfo modFileInfo : loadingModList.getModFiles()) {
            packs.add(ResourcePackLoader.createPackForMod(modFileInfo));
        }
        if (packs.isEmpty()) {
            return;
        }
        try (ReloadableResourceManager resourceManager = new CustomServerResourceManager(packs)) {
            listeners.forEach(resourceManager::registerReloadListener);
            ReloadInstance reloadInstance = resourceManager.createReload(Runnable::run, Runnable::run,
                CompletableFuture.completedFuture(Unit.INSTANCE), packs);
            reloadInstance.done();
        }
    }
    
    public static class CustomServerResourceManager extends ReloadableResourceManager {
        private final List<PackResources> packs;
        
        public CustomServerResourceManager(List<PackResources> packs) {
            super(PackType.SERVER_DATA);
            this.packs = packs;
        }
        
        @Override
        public ReloadInstance createReload(Executor backgroundExecutor, Executor gameExecutor, CompletableFuture<Unit> waitingFor, List<PackResources> resourcePacks) {
            TruePowerMod.LOGGER.info("Reloading ResourceManager: {}", LogUtils.defer(() -> resourcePacks.stream().map(PackResources::packId).collect(Collectors.joining(", "))));
            AccessorReloadableResourceManager accessor = (AccessorReloadableResourceManager) this;
            accessor.getResources().close();
            accessor.setResources(new MultiPackResourceManager(accessor.getType(), resourcePacks));
            TruePowerCompatManager.onCustomResourceLoad(packs, this);
            return SimpleReloadInstance.create(accessor.getResources(), accessor.getListeners(), backgroundExecutor, gameExecutor, waitingFor, TruePowerMod.LOGGER.isDebugEnabled());
        }
    }
}
