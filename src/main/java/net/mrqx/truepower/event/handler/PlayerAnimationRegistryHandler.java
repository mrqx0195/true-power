package net.mrqx.truepower.event.handler;

import dev.kosmx.playerAnim.api.layered.AnimationStack;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationAccess;
import mods.flammpfeil.slashblade.compat.playerAnim.VmdAnimation;
import mods.flammpfeil.slashblade.event.BladeMotionEvent;
import mods.flammpfeil.slashblade.item.ItemSlashBlade;
import mods.flammpfeil.slashblade.registry.ComboStateRegistry;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.mrqx.sbr_core.events.SlashBladePlayerAnimationRegistryEvent;
import net.mrqx.truepower.registry.TruePowerComboStateRegistry;

import java.util.Map;

@OnlyIn(Dist.CLIENT)
public class PlayerAnimationRegistryHandler {
    private static final ResourceLocation MOTION_LOCATION = new ResourceLocation("slashblade", "model/pa/player_motion.vmd");
    private static final PlayerAnimationRegistryHandler INSTANCE = new PlayerAnimationRegistryHandler();

    public static PlayerAnimationRegistryHandler getInstance() {
        return PlayerAnimationRegistryHandler.INSTANCE;
    }

    public void register() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public static void onSlashBladePlayerAnimationRegistryEvent(SlashBladePlayerAnimationRegistryEvent event) {
        Map<ResourceLocation, VmdAnimation> animation = event.getAnimation();
        animation.put(TruePowerComboStateRegistry.VOID_SLASH.getId(), (new VmdAnimation(MOTION_LOCATION, 2200, 2299, false)).setBlendLegs(false));
    }

    @SubscribeEvent
    public void onBladeAnimationStart(BladeMotionEvent event) {
        if (event.getEntity() instanceof AbstractClientPlayer player) {
            player.getMainHandItem().getCapability(ItemSlashBlade.BLADESTATE).ifPresent(state -> {
                AnimationStack animationStack = PlayerAnimationAccess.getPlayerAnimLayer(player);
                if (event.getCombo().equals(ComboStateRegistry.NONE.getId()) || event.getCombo().equals(ComboStateRegistry.STANDBY.getId())) {
                    if (!state.getComboSeq().equals(event.getCombo())) {
                        animationStack.removeLayer(0);
                    }
                }
            });
        }
    }
}
