package net.mrqx.truepower.util;

import dev.kosmx.playerAnim.api.layered.AnimationStack;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationAccess;
import mods.flammpfeil.slashblade.item.ItemSlashBlade;
import mods.flammpfeil.slashblade.registry.ComboStateRegistry;
import mods.flammpfeil.slashblade.registry.combo.ComboState;
import mods.flammpfeil.slashblade.slasharts.SlashArts;
import mods.flammpfeil.slashblade.util.AdvancementHelper;
import mods.flammpfeil.slashblade.util.AttackManager;
import mods.flammpfeil.slashblade.util.KnockBacks;
import mods.flammpfeil.slashblade.util.TimeValueHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.mrqx.truepower.network.ComboSyncMessage;

import java.util.function.Consumer;

public class TruePowerComboHelper {
    public static final ComboState.TimeLineTickAction UPPER_SLASH = ComboState.TimeLineTickAction.getBuilder()
            .put((int) TimeValueHelper.getTicksFromFrames(7.0F), (entityIn) -> AttackManager.doSlash(entityIn, -80.0F, Vec3.ZERO, false, false, 0.9, KnockBacks.toss))
            .build();

    public static final ComboState.TimeLineTickAction POWERED_UPPER_SLASH = ComboState.TimeLineTickAction.getBuilder()
            .put((int) TimeValueHelper.getTicksFromFrames(7.0F), (entityIn) -> AttackManager.doSlash(entityIn, -80.0F, Vec3.ZERO, false, false, 0.25, KnockBacks.toss))
            .put((int) TimeValueHelper.getTicksFromFrames(9.0F), (entityIn) -> AttackManager.doSlash(entityIn, -80.0F, Vec3.ZERO, false, false, 1.35, KnockBacks.toss))
            .build();

    public static SlashArts.ArtsType releaseActionQuickCharge(LivingEntity user, Integer elapsed, Integer startFrame) {
        int level = user.getMainHandItem().getEnchantmentLevel(Enchantments.SOUL_SPEED);
        if (elapsed > startFrame && elapsed <= 3 + level + startFrame) {
            AdvancementHelper.grantedIf(Enchantments.SOUL_SPEED, user);
            AdvancementHelper.grantCriterion(user, AdvancementHelper.ADVANCEMENT_QUICK_CHARGE);
            return SlashArts.ArtsType.Jackpot;
        } else {
            return SlashArts.ArtsType.Fail;
        }
    }

    public static SlashArts.ArtsType releaseActionQuickCharge(LivingEntity user, Integer elapsed, Integer startFrame, Integer endFrame) {
        int level = user.getMainHandItem().getEnchantmentLevel(Enchantments.SOUL_SPEED);
        if (elapsed > startFrame && elapsed < endFrame + level) {
            AdvancementHelper.grantedIf(Enchantments.SOUL_SPEED, user);
            AdvancementHelper.grantCriterion(user, AdvancementHelper.ADVANCEMENT_QUICK_CHARGE);
            return SlashArts.ArtsType.Jackpot;
        } else {
            return SlashArts.ArtsType.Fail;
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static Consumer<ComboSyncMessage> setClientCombo() {
        return (msg) -> {
            LocalPlayer player = Minecraft.getInstance().player;
            if (player != null) {
                if (msg.syncCombo) {
                    player.getMainHandItem().getCapability(ItemSlashBlade.BLADESTATE).ifPresent(state -> {
                        state.setComboSeq(msg.comboState);
                        state.setLastActionTime(msg.lastActionTime);
                        if (msg.comboState.equals(ComboStateRegistry.NONE.getId()) || msg.comboState.equals(ComboStateRegistry.STANDBY.getId())) {
                            AnimationStack animationStack = PlayerAnimationAccess.getPlayerAnimLayer(player);
                            animationStack.removeLayer(0);
                        }
                    });
                }
                player.getPersistentData().putString("truePower.combo", msg.comboState.toString());
                player.getPersistentData().putBoolean("truePower.canMove", msg.canMove);
                player.getPersistentData().putBoolean("truePower.jumpCancelOnly", msg.jumpCancelOnly);
                player.getPersistentData().putBoolean("truePower.noMoveEnable", msg.noMoveEnable);
            }
        };
    }
}
