package net.mrqx.truepower.compat.kubejs;

import dev.latvian.mods.kubejs.server.ServerScriptManager;
import net.minecraft.server.packs.resources.ReloadableResourceManager;
import net.mrqx.truepower.mixin.AccessorReloadableResourceManager;

public class TruePowerKubeJS {
    public static void onCustomResourceLoad(ReloadableResourceManager resourceManager) {
        if (resourceManager instanceof AccessorReloadableResourceManager accessor) {
            ServerScriptManager oldInstance = ServerScriptManager.instance;
            ServerScriptManager.instance = new ServerScriptManager(null);
            accessor.setResources(ServerScriptManager.instance.wrapResourceManager(accessor.getResources()));
            ServerScriptManager.instance = oldInstance;
        }
    }
}
