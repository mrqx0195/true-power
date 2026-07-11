package net.mrqx.truepower.data.removereleases;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.DatapackBuiltinEntriesProvider;
import net.mrqx.truepower.TruePowerMod;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class TruePowerRemoveReleasesProvider extends DatapackBuiltinEntriesProvider {
    public TruePowerRemoveReleasesProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider, RegistrySetBuilder removeReleaseBuilder) {
        super(packOutput, lookupProvider, removeReleaseBuilder, Set.of(TruePowerMod.MODID));
    }
    
    @Override
    public String getName() {
        return "TruePower Remove Release Actions";
    }
}
