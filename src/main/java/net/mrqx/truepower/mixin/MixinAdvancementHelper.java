package net.mrqx.truepower.mixin;

import mods.flammpfeil.slashblade.ability.SlayerStyleArts;
import mods.flammpfeil.slashblade.item.ItemSlashBlade;
import mods.flammpfeil.slashblade.util.AdvancementHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AdvancementHelper.class)
public class MixinAdvancementHelper {
    @Inject(method = "grantCriterion(Lnet/minecraft/server/level/ServerPlayer;Lnet/minecraft/resources/ResourceLocation;)V", at = @At("HEAD"))
    private static void injectGrantCriterion(ServerPlayer player, ResourceLocation resourcelocation, CallbackInfo ci) {
        player.getMainHandItem().getCapability(ItemSlashBlade.BLADESTATE).ifPresent(state -> {
            if (resourcelocation.equals(SlayerStyleArts.ADVANCEMENT_TRICK_DODGE)
                    || resourcelocation.equals(SlayerStyleArts.ADVANCEMENT_TRICK_DOWN)
                    || resourcelocation.equals(SlayerStyleArts.ADVANCEMENT_AIR_TRICK)) {
                player.getPersistentData().putInt("sb.avoid.trickup", 2);
            }
        });
    }
}
