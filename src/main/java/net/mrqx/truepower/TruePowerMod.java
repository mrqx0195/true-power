package net.mrqx.truepower;

import com.google.common.base.CaseFormat;
import com.mojang.logging.LogUtils;
import mods.flammpfeil.slashblade.client.renderer.entity.SummonedSwordRenderer;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobCategory;
import net.mrqx.truepower.attachment.TruePowerAttachments;
import net.mrqx.truepower.compat.TruePowerCompatManager;
import net.mrqx.truepower.config.TruePowerClientConfig;
import net.mrqx.truepower.config.TruePowerCommonConfig;
import net.mrqx.truepower.entity.EntityBlastSummonedSword;
import net.mrqx.truepower.registry.TruePowerAttributeRegistry;
import net.mrqx.truepower.registry.TruePowerComboStateRegistry;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeModificationEvent;
import net.neoforged.neoforge.registries.RegisterEvent;
import org.slf4j.Logger;

@Mod(TruePowerMod.MODID)
public class TruePowerMod {
    public static final String MODID = "truepower";
    public static final Logger LOGGER = LogUtils.getLogger();
    
    public static ResourceLocation prefix(String path) {
        return ResourceLocation.fromNamespaceAndPath("truepower", path);
    }
    
    public TruePowerMod(IEventBus modEventBus, ModContainer container) {
        container.registerConfig(ModConfig.Type.CLIENT, TruePowerClientConfig.CLIENT_CONFIG);
        container.registerConfig(ModConfig.Type.COMMON, TruePowerCommonConfig.COMMON_CONFIG);
        TruePowerComboStateRegistry.COMBO_STATE.register(modEventBus);
        TruePowerAttachments.ATTACHMENTS.register(modEventBus);
        TruePowerAttributeRegistry.ATTRIBUTES.register(modEventBus);
        modEventBus.addListener(TruePowerCompatManager::commonInit);
        modEventBus.addListener(TruePowerMod::onEntityAttributeModification);
        TruePowerCompatManager.onModConstruct(modEventBus, container);
    }
    
    public static void onEntityAttributeModification(EntityAttributeModificationEvent event) {
        event.getTypes().forEach(type -> {
            if (type.getBaseClass().isAssignableFrom(LivingEntity.class)) {
                event.add(type, TruePowerAttributeRegistry.STUN_RESISTANCE);
            }
        });
    }
    
    @EventBusSubscriber
    public final static class RegistryEvents {
        public static final ResourceLocation ENTITY_BLAST_SUMMONED_SWORD_RESOURCE_LOCATION = prefix(classToString(EntityBlastSummonedSword.class));
        @SuppressWarnings("NotNullFieldNotInitialized")
        public static EntityType<EntityBlastSummonedSword> BlastSummonedSword;
        
        @SubscribeEvent
        public static void register(RegisterEvent event) {
            event.register(Registries.ENTITY_TYPE, entityTypeRegisterHelper -> {
                {
                    BlastSummonedSword = EntityType.Builder
                        .of(EntityBlastSummonedSword::new, MobCategory.MISC).sized(0.5F, 0.5F)
                        .setTrackingRange(4).setUpdateInterval(20)
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
