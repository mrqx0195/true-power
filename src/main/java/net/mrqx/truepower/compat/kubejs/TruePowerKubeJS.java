package net.mrqx.truepower.compat.kubejs;

import dev.latvian.mods.kubejs.KubeJSPaths;
import dev.latvian.mods.kubejs.server.GeneratedServerResourcePack;
import dev.latvian.mods.kubejs.server.ServerScriptManager;
import net.minecraft.server.packs.FilePackResources;
import net.minecraft.server.packs.PackResources;
import net.minecraft.server.packs.resources.ReloadableResourceManager;
import net.mrqx.truepower.mixin.AccessorReloadableResourceManager;

import java.io.File;
import java.util.List;
import java.util.Objects;

public class TruePowerKubeJS {
    public static void onCustomResourceLoad(List<PackResources> packs, ReloadableResourceManager resourceManager) {
        if (resourceManager instanceof AccessorReloadableResourceManager accessor) {
            ServerScriptManager oldInstance = ServerScriptManager.instance;
            try {
                ServerScriptManager.instance = new ServerScriptManager(null);
                accessor.setResources(ServerScriptManager.instance.wrapResourceManager(accessor.getResources()));
            } catch (Exception e) {
                packs.add(new GeneratedServerResourcePack());
                for (File file : Objects.requireNonNull(KubeJSPaths.DATA.toFile().listFiles())) {
                    if (file.isFile() && file.getName().endsWith(".zip")) {
                        packs.add(new FilePackResources(file.getName(), file, false));
                    }
                }
            } finally {
                ServerScriptManager.instance = oldInstance;
            }
        }
    }
}
