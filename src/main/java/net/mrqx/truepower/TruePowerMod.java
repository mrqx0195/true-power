package net.mrqx.truepower;

import com.google.common.base.CaseFormat;
import com.mojang.logging.LogUtils;
import mods.flammpfeil.slashblade.client.renderer.entity.SummonedSwordRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegisterEvent;
import net.mrqx.truepower.entity.EntityBlastSummonedSword;
import net.mrqx.truepower.network.NetworkManager;
import net.mrqx.truepower.registry.TruePowerComboStateRegistry;
import org.slf4j.Logger;

@Mod(TruePowerMod.MODID)
public class TruePowerMod {
    public static final String MODID = "truepower";
    public static final Logger LOGGER = LogUtils.getLogger();

    public static ResourceLocation prefix(String path) {
        return new ResourceLocation("truepower", path);
    }

    public TruePowerMod() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, TruePowerModConfig.COMMON_CONFIG);
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        TruePowerComboStateRegistry.COMBO_STATE.register(modEventBus);
        NetworkManager.register();
    }

    @Mod.EventBusSubscriber(
            bus = Mod.EventBusSubscriber.Bus.MOD
    )
    public static class RegistryEvents {
        public static final ResourceLocation ENTITY_BLAST_SUMMONED_SWORD_RESOURCE_LOCATION = new ResourceLocation(TruePowerMod.MODID, classToString(EntityBlastSummonedSword.class));
        public static EntityType<EntityBlastSummonedSword> BlastSummonedSword;

        @SubscribeEvent
        public static void register(RegisterEvent event) {
            event.register(ForgeRegistries.Keys.ENTITY_TYPES, entityTypeRegisterHelper -> {
                {
                    BlastSummonedSword = EntityType.Builder
                            .of(EntityBlastSummonedSword::new, MobCategory.MISC).sized(0.5F, 0.5F)
                            .setTrackingRange(4).setUpdateInterval(20)
                            .setCustomClientFactory(EntityBlastSummonedSword::createInstance)
                            .build(ENTITY_BLAST_SUMMONED_SWORD_RESOURCE_LOCATION.toString());
                    entityTypeRegisterHelper.register(ENTITY_BLAST_SUMMONED_SWORD_RESOURCE_LOCATION, BlastSummonedSword);
                }
            });
        }

        @SubscribeEvent
        public static void onRegisterRenderers(EntityRenderersEvent.RegisterRenderers event) {
            event.registerEntityRenderer(BlastSummonedSword, SummonedSwordRenderer::new);
        }

        @SuppressWarnings("SameParameterValue")
        private static String classToString(Class<? extends Entity> entityClass) {
            return CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, entityClass.getSimpleName()).replace("entity_", "");
        }
    }
}
