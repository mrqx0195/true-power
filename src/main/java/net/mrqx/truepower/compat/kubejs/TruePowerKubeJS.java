package net.mrqx.truepower.compat.kubejs;

import dev.latvian.mods.kubejs.KubeJSPaths;
import dev.latvian.mods.kubejs.script.data.KubeFileResourcePack;
import dev.latvian.mods.kubejs.server.ServerScriptManager;
import net.minecraft.server.packs.PackResources;
import net.minecraft.server.packs.PackType;
import net.mrqx.truepower.mixin.AccessorServerScriptManager;

import java.util.ArrayList;
import java.util.List;

public class TruePowerKubeJS {
    public static void onCustomResourceLoad(List<PackResources> packs) {
        ServerScriptManager staticInstance = AccessorServerScriptManager.getStaticInstance();
        try {
            List<PackResources> packResources = ServerScriptManager.createPackResources(packs);
            packs.clear();
            packs.addAll(packResources);
        } catch (Exception e) {
            List<PackResources> filePacks = new ArrayList<>();
            KubeFileResourcePack.scanAndLoad(KubeJSPaths.DATA, filePacks);
            filePacks.sort((p1, p2) -> p1.packId().compareToIgnoreCase(p2.packId()));
            filePacks.add(new KubeFileResourcePack(PackType.SERVER_DATA));
            packs.addAll(filePacks);
        } finally {
            AccessorServerScriptManager.setStaticInstance(staticInstance);
        }
    }
}
