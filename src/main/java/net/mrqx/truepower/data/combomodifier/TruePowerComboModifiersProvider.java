package net.mrqx.truepower.data.combomodifier;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.DatapackBuiltinEntriesProvider;
import net.mrqx.truepower.TruePowerMod;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class TruePowerComboModifiersProvider extends DatapackBuiltinEntriesProvider {
    public TruePowerComboModifiersProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider, RegistrySetBuilder modifiersBuilder) {
        super(packOutput, lookupProvider, modifiersBuilder, Set.of(TruePowerMod.MODID));
    }
    
    @Override
    public String getName() {
        return "TruePower Combo Modifiers";
    }
}
