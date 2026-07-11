package net.mrqx.truepower.data;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.mrqx.truepower.data.combomodifier.TruePowerComboModifiers;
import net.mrqx.truepower.data.combomodifier.TruePowerComboModifiersProvider;
import net.mrqx.truepower.data.removereleases.TruePowerRemoveReleases;
import net.mrqx.truepower.data.removereleases.TruePowerRemoveReleasesProvider;

import java.util.concurrent.CompletableFuture;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public final class DataGen {
    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator dataGenerator = event.getGenerator();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();
        PackOutput packOutput = dataGenerator.getPackOutput();
        boolean includeServer = event.includeServer();
        
        RegistrySetBuilder modifiersBuilder = new RegistrySetBuilder()
            .add(ComboModifierData.MODIFIER_REGISTRY_KEY, TruePowerComboModifiers::registerAll);
        RegistrySetBuilder removeReleaseBuilder = new RegistrySetBuilder()
            .add(ComboModifierData.REMOVE_RELEASE_REGISTRY_KEY, TruePowerRemoveReleases::registerAll);
        
        dataGenerator.addProvider(includeServer, new TruePowerComboModifiersProvider(packOutput, lookupProvider, modifiersBuilder));
        dataGenerator.addProvider(includeServer, new TruePowerRemoveReleasesProvider(packOutput, lookupProvider, removeReleaseBuilder));
    }
}
