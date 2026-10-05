package net.mrqx.truepower.event.handler;

import mods.flammpfeil.slashblade.SlashBlade;
import mods.flammpfeil.slashblade.compat.playerAnim.VmdAnimation;
import mods.flammpfeil.slashblade.event.client.PlayerAnimationInitEvent;
import net.minecraft.resources.ResourceLocation;
import net.mrqx.truepower.registry.TruePowerComboStateRegistry;
import net.neoforged.bus.api.SubscribeEvent;

public final class PlayerAnimationRegistryHandler {
    private static final ResourceLocation MOTION_LOCATION = SlashBlade.prefix("model/pa/player_motion.vmd");
    private static final PlayerAnimationRegistryHandler INSTANCE = new PlayerAnimationRegistryHandler();
    
    public static PlayerAnimationRegistryHandler getInstance() {
        return PlayerAnimationRegistryHandler.INSTANCE;
    }
    
    public void register() {
        SlashBlade.MOD_EVENT_BUS.register(this);
    }
    
    @SubscribeEvent
    public void onSlashBladePlayerAnimationRegistryEvent(PlayerAnimationInitEvent event) {
        event.register(TruePowerComboStateRegistry.VOID_SLASH.getId(), () -> new VmdAnimation(MOTION_LOCATION, 2200, 2299, false).setBlendLegs(false));
        event.register(TruePowerComboStateRegistry.VOID_SLASH_SHEATH.getId(), () -> new VmdAnimation(MOTION_LOCATION, 2278, 2299, false).setBlendLegs(false));
    }
}
