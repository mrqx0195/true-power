package net.mrqx.truepower.mixin;

import mods.flammpfeil.slashblade.ability.EnemyStep;
import mods.flammpfeil.slashblade.ability.KickJump;
import mods.flammpfeil.slashblade.ability.SlayerStyleArts;
import mods.flammpfeil.slashblade.capability.slashblade.BladeStateAccess;
import mods.flammpfeil.slashblade.registry.ComboStateRegistry;
import mods.flammpfeil.slashblade.util.AdvancementHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.mrqx.truepower.attachment.ITruePowerData;
import net.mrqx.truepower.network.ComboSyncMessage;
import net.neoforged.neoforge.network.PacketDistributor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AdvancementHelper.class)
public class MixinAdvancementHelper {
    @Inject(method = "grantCriterion(Lnet/minecraft/server/level/ServerPlayer;Lnet/minecraft/resources/ResourceLocation;)V", at = @At("HEAD"), remap = false)
    private static void injectGrantCriterion(ServerPlayer player, ResourceLocation resourcelocation, CallbackInfo ci) {
        BladeStateAccess.of(player.getMainHandItem()).ifPresent(state -> {
            ITruePowerData data = ITruePowerData.get(player);
            ResourceLocation noneId = ComboStateRegistry.NONE.getId();
            if (resourcelocation.equals(SlayerStyleArts.ADVANCEMENT_TRICK_DODGE)
                || resourcelocation.equals(SlayerStyleArts.ADVANCEMENT_TRICK_DOWN)
                || resourcelocation.equals(SlayerStyleArts.ADVANCEMENT_AIR_TRICK)) {
                player.getPersistentData().putInt(SlayerStyleArts.AVOID_TRICKUP_PATH, 2);
                ComboSyncMessage comboSyncMessage = new ComboSyncMessage(
                    noneId,
                    state.getLastActionTime(),
                    data.canMove(),
                    data.isJumpCancelOnly(),
                    data.isNoMoveEnable(),
                    true
                );
                PacketDistributor.sendToPlayer(player, comboSyncMessage);
            } else if (resourcelocation.equals(SlayerStyleArts.ADVANCEMENT_TRICK_UP)
                || resourcelocation.equals(EnemyStep.ADVANCEMENT_ENEMY_STEP)
                || resourcelocation.equals(KickJump.ADVANCEMENT_KICK_JUMP)) {
                ComboSyncMessage comboSyncMessage = new ComboSyncMessage(
                    noneId,
                    state.getLastActionTime(),
                    data.canMove(),
                    data.isJumpCancelOnly(),
                    data.isNoMoveEnable(),
                    true
                );
                PacketDistributor.sendToPlayer(player, comboSyncMessage);
            }
        });
    }
}
