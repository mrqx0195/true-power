package net.mrqx.truepower.data;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.mrqx.truepower.data.combomodifier.TruePowerComboModifiers;
import net.mrqx.truepower.data.combomodifier.TruePowerComboModifiersProvider;
import net.mrqx.truepower.data.removereleases.TruePowerRemoveReleases;
import net.mrqx.truepower.data.removereleases.TruePowerRemoveReleasesProvider;
import net.mrqx.truepower.data.tag.TruePowerDamageTypeTagGenerator;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.concurrent.CompletableFuture;

@EventBusSubscriber
public final class DataGen {
    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput output = generator.getPackOutput();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();
        
        DatapackBuiltinEntriesProvider datapackProvider = new TruePowerRegistryDataGenerator(output, lookupProvider);
        generator.addProvider(true, datapackProvider);
        lookupProvider = datapackProvider.getRegistryProvider();
        
        RegistrySetBuilder modifiersBuilder = new RegistrySetBuilder()
            .add(ComboModifierData.MODIFIER_REGISTRY_KEY, TruePowerComboModifiers::registerAll);
        RegistrySetBuilder removeReleaseBuilder = new RegistrySetBuilder()
            .add(ComboModifierData.REMOVE_RELEASE_REGISTRY_KEY, TruePowerRemoveReleases::registerAll);
        
        generator.addProvider(event.includeServer(),
            new TruePowerComboModifiersProvider(output, lookupProvider, modifiersBuilder));
        generator.addProvider(event.includeServer(),
            new TruePowerRemoveReleasesProvider(output, lookupProvider, removeReleaseBuilder));
        
        generator.addProvider(event.includeServer(),
            new TruePowerDamageTypeTagGenerator(output, lookupProvider, existingFileHelper));
    }
}
