package net.mrqx.truepower.mixin;

import mods.flammpfeil.slashblade.ability.EnemyStep;
import mods.flammpfeil.slashblade.ability.KickJump;
import mods.flammpfeil.slashblade.ability.SlayerStyleArts;
import mods.flammpfeil.slashblade.capability.slashblade.BladeStateAccess;
import mods.flammpfeil.slashblade.registry.ComboStateRegistry;
import mods.flammpfeil.slashblade.util.AdvancementHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
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
            if (resourcelocation.equals(SlayerStyleArts.ADVANCEMENT_TRICK_DODGE)
                || resourcelocation.equals(SlayerStyleArts.ADVANCEMENT_TRICK_DOWN)
                || resourcelocation.equals(SlayerStyleArts.ADVANCEMENT_AIR_TRICK)) {
                player.getPersistentData().putInt(SlayerStyleArts.AVOID_TRICKUP_PATH, 2);
                
                CompoundTag persistentData = player.getPersistentData();
                ComboSyncMessage comboSyncMessage = new ComboSyncMessage(
                    ComboStateRegistry.NONE.getId(),
                    state.getLastActionTime(),
                    persistentData.getBoolean("truePower.canMove"),
                    persistentData.getBoolean("truePower.jumpCancelOnly"),
                    persistentData.getBoolean("truePower.noMoveEnable"),
                    true
                );
                PacketDistributor.sendToPlayer(player, comboSyncMessage);
            } else if (resourcelocation.equals(SlayerStyleArts.ADVANCEMENT_TRICK_UP)
                || resourcelocation.equals(EnemyStep.ADVANCEMENT_ENEMY_STEP)
                || resourcelocation.equals(KickJump.ADVANCEMENT_KICK_JUMP)) {
                CompoundTag persistentData = player.getPersistentData();
                ComboSyncMessage comboSyncMessage = new ComboSyncMessage(
                    ComboStateRegistry.NONE.getId(),
                    state.getLastActionTime(),
                    persistentData.getBoolean("truePower.canMove"),
                    persistentData.getBoolean("truePower.jumpCancelOnly"),
                    persistentData.getBoolean("truePower.noMoveEnable"),
                    true
                );
                PacketDistributor.sendToPlayer(player, comboSyncMessage);
            }
        });
    }
}
