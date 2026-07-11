package net.mrqx.truepower.event.handler;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.mrqx.truepower.TruePowerMod;
import net.mrqx.truepower.capability.data.TruePowerDataProvider;
import net.mrqx.truepower.capability.stun.TruePowerStunDataProvider;

@Mod.EventBusSubscriber
public final class CapabilityHandler {
    private static final ResourceLocation CAPABILITY_KEY = TruePowerMod.prefix("data");
    private static final ResourceLocation STUN_CAPABILITY_KEY = TruePowerMod.prefix("stun_data");
    
    @SubscribeEvent
    public static void onAttachCapabilities(AttachCapabilitiesEvent<?> event) {
        if (event.getObject() instanceof LivingEntity livingEntity) {
            event.addCapability(CAPABILITY_KEY, new TruePowerDataProvider());
            event.addCapability(STUN_CAPABILITY_KEY, new TruePowerStunDataProvider(livingEntity));
        }
    }
}
