package net.mrqx.truepower.data.combomodifier;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.data.PackOutput;
import net.mrqx.truepower.TruePowerMod;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class TruePowerComboModifiersProvider extends DatapackBuiltinEntriesProvider {
    public TruePowerComboModifiersProvider(
        PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider,
        RegistrySetBuilder modifiersBuilder
    ) {
        super(output, lookupProvider, modifiersBuilder, Set.of(TruePowerMod.MODID));
    }
    
    @Override
    public String getName() {
        return "TruePower Combo Modifiers";
    }
}
