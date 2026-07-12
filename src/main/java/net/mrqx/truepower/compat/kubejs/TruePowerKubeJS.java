package net.mrqx.truepower.compat.kubejs;

import dev.latvian.mods.kubejs.server.ServerScriptManager;
import net.minecraft.server.packs.PackResources;

import java.util.List;

public class TruePowerKubeJS {
    public static void onCustomResourceLoad(List<PackResources> packs) {
        List<PackResources> packResources = ServerScriptManager.createPackResources(packs);
        packs.clear();
        packs.addAll(packResources);
    }
}
