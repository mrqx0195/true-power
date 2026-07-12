package net.mrqx.truepower.data.removereleases;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.data.PackOutput;
import net.mrqx.truepower.TruePowerMod;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class TruePowerRemoveReleasesProvider extends DatapackBuiltinEntriesProvider {
    public TruePowerRemoveReleasesProvider(
        PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider,
        RegistrySetBuilder removeReleaseBuilder
    ) {
        super(output, lookupProvider, removeReleaseBuilder, Set.of(TruePowerMod.MODID));
    }
    
    @Override
    public String getName() {
        return "TruePower Remove Release Actions";
    }
}
