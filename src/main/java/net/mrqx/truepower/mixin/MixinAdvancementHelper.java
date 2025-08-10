package net.mrqx.truepower.mixin;

import mods.flammpfeil.slashblade.ability.EnemyStep;
import mods.flammpfeil.slashblade.ability.KickJump;
import mods.flammpfeil.slashblade.ability.SlayerStyleArts;
import mods.flammpfeil.slashblade.item.ItemSlashBlade;
import mods.flammpfeil.slashblade.registry.ComboStateRegistry;
import mods.flammpfeil.slashblade.util.AdvancementHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.PacketDistributor;
import net.mrqx.truepower.network.ComboSyncMessage;
import net.mrqx.truepower.network.NetworkManager;
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

                CompoundTag persistentData = player.getPersistentData();
                ComboSyncMessage comboSyncMessage = new ComboSyncMessage();

                comboSyncMessage.comboState = ComboStateRegistry.NONE.getId();
                comboSyncMessage.lastActionTime = state.getLastActionTime();
                comboSyncMessage.canMove = persistentData.getBoolean("truePower.canMove");
                comboSyncMessage.jumpCancelOnly = persistentData.getBoolean("truePower.jumpCancelOnly");
                comboSyncMessage.noMoveEnable = persistentData.getBoolean("truePower.noMoveEnable");
                comboSyncMessage.syncCombo = true;

                NetworkManager.INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), comboSyncMessage);
            } else if (resourcelocation.equals(SlayerStyleArts.ADVANCEMENT_TRICK_UP)
                    || resourcelocation.equals(EnemyStep.ADVANCEMENT_ENEMY_STEP)
                    || resourcelocation.equals(KickJump.ADVANCEMENT_KICK_JUMP)) {
                CompoundTag persistentData = player.getPersistentData();
                ComboSyncMessage comboSyncMessage = new ComboSyncMessage();

                comboSyncMessage.comboState = ComboStateRegistry.NONE.getId();
                comboSyncMessage.lastActionTime = state.getLastActionTime();
                comboSyncMessage.canMove = persistentData.getBoolean("truePower.canMove");
                comboSyncMessage.jumpCancelOnly = persistentData.getBoolean("truePower.jumpCancelOnly");
                comboSyncMessage.noMoveEnable = persistentData.getBoolean("truePower.noMoveEnable");
                comboSyncMessage.syncCombo = true;

                NetworkManager.INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), comboSyncMessage);
            }
        });
    }
}
