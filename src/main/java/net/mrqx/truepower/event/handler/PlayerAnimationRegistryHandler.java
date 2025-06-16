package net.mrqx.truepower.event.handler;

import mods.flammpfeil.slashblade.compat.playerAnim.VmdAnimation;
import mods.flammpfeil.slashblade.registry.ComboStateRegistry;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.mrqx.sbr_core.events.SlashBladePlayerAnimationRegistryEvent;
import net.mrqx.truepower.registry.TruePowerComboStateRegistry;

import java.util.Map;

@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber
public class PlayerAnimationRegistryHandler {
    private static final ResourceLocation MOTION_LOCATION = new ResourceLocation("slashblade", "model/pa/player_motion.vmd");

    @SubscribeEvent
    public static void onSlashBladePlayerAnimationRegistryEvent(SlashBladePlayerAnimationRegistryEvent event) {
        Map<ResourceLocation, VmdAnimation> animation = event.getAnimation();
        animation.put(ComboStateRegistry.NONE.getId(), (new VmdAnimation(MOTION_LOCATION, 0, 1, false)));
        animation.put(ComboStateRegistry.STANDBY.getId(), (new VmdAnimation(MOTION_LOCATION, 0, 1, false)));
        animation.put(TruePowerComboStateRegistry.VOID_SLASH.getId(), (new VmdAnimation(MOTION_LOCATION, 2200, 2299, false)).setBlendLegs(false));
    }
}
