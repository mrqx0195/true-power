package net.mrqx.truepower.combo;

import mods.flammpfeil.slashblade.ability.SlayerStyleArts;
import mods.flammpfeil.slashblade.ability.SuperSlashArts;
import mods.flammpfeil.slashblade.capability.slashblade.BladeStateAccess;
import mods.flammpfeil.slashblade.item.SwordType;
import mods.flammpfeil.slashblade.registry.ComboStateRegistry;
import mods.flammpfeil.slashblade.slasharts.SlashArts;
import mods.flammpfeil.slashblade.util.InputCommand;
import mods.flammpfeil.slashblade.util.NBTHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.mrqx.sbr_core.utils.InputStream;
import net.mrqx.sbr_core.utils.JustSlashArtManager;
import net.mrqx.truepower.attachment.ITruePowerData;
import net.mrqx.truepower.config.TruePowerCommonConfig;
import net.mrqx.truepower.event.handler.TrickHandler;
import net.mrqx.truepower.util.TruePowerInputTimeLines;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;
import java.util.LinkedList;
import java.util.Map;

public final class ComboCommandsHandler {
    public static final InputStream.TimeLineKeyInput SUPER_SLASH_ARTS_SPRINT_INPUT_KEY = new InputStream.TimeLineKeyInput(2, -2,
        InputCommand.SPRINT, EnumSet.of(InputCommand.SNEAK, InputCommand.FORWARD), InputStream.InputType.START);
    
    @Nullable
    public static ResourceLocation processComboCommand(LivingEntity livingEntity, EnumSet<InputCommand> commands) {
        if (BladeStateAccess.of(livingEntity.getMainHandItem()).isEmpty()
            || livingEntity.level().isClientSide
            || (TruePowerCommonConfig.BLADE_ARTS_NEED_SHIFT.get() && !commands.contains(InputCommand.SNEAK))) {
            return null;
        }
        ResourceLocation id = null;
        InputStream inputStream = InputStream.getOrCreateInputStream(livingEntity);
        for (Map.Entry<LinkedList<InputStream.TimeLineKeyInput>, ResourceLocation> entry : TruePowerInputTimeLines.INPUTS.entrySet()) {
            if (inputStream.checkTimeLineInput(entry.getKey())) {
                id = entry.getValue();
                break;
            }
        }
        if (id != null) {
            JustSlashArtManager.resetJustCount(livingEntity);
        } else if (inputStream.checkInputWithTimeLineKeyInput(SUPER_SLASH_ARTS_SPRINT_INPUT_KEY)) {
            ResourceLocation ssa = tryReleaseSSA(livingEntity);
            if (ssa != null && !ssa.equals(ComboStateRegistry.NONE.getId())) {
                prepareDoSSA(livingEntity);
                id = ssa;
            } else {
                id = ComboStateRegistry.NONE.getId();
            }
        }
        return id;
    }
    
    public static void prepareDoSSA(LivingEntity livingEntity) {
        ItemStack itemStack = livingEntity.getMainHandItem();
        ITruePowerData data = ITruePowerData.get(livingEntity);
        
        itemStack.hurtAndBreak(itemStack.getMaxDamage() / 2, livingEntity, EquipmentSlot.MAINHAND);
        BladeStateAccess.of(livingEntity.getMainHandItem()).ifPresent(state -> state
            .updateComboSeq(livingEntity, ComboStateRegistry.NONE.getId()));
        livingEntity.setDeltaMovement(Vec3.ZERO);
        MinecraftServer server = livingEntity.getServer();
        if (server != null) {
            server.getPlayerList().broadcastAll(new ClientboundSetEntityMotionPacket(livingEntity.getId(), Vec3.ZERO));
        }
        
        data.setAvoidTrick(10);
        CompoundTag persistentData = livingEntity.getPersistentData();
        if (persistentData.contains(TrickHandler.TRICK_BEFORE_POS)) {
            Vec3 pos = NBTHelper.getVector3d(persistentData, TrickHandler.TRICK_BEFORE_POS);
            livingEntity.setPos(pos);
            if (livingEntity instanceof ServerPlayer serverPlayer) {
                serverPlayer.teleportTo(pos.x(), pos.y(), pos.z());
            }
        }
        persistentData.putInt(SlayerStyleArts.AVOID_TRICKUP_PATH, 10);
        persistentData.remove(SlayerStyleArts.AIRTRICK_COUNTER_PATH);
        persistentData.remove(SlayerStyleArts.AIRTRICK_TARGET_PATH);
        persistentData.remove(TrickHandler.TRICK_BEFORE_POS);
    }
    
    /**
     * @see SuperSlashArts#releaseSSA(ServerPlayer)
     */
    @Nullable
    public static ResourceLocation tryReleaseSSA(LivingEntity entity) {
        ItemStack itemStack = entity.getMainHandItem();
        return BladeStateAccess.of(itemStack).map(state -> {
            if (state.isBroken() || state.getDamage() > 0 || state.isSealed()
                || !SwordType.from(itemStack).contains(SwordType.BEWITCHED)
                || !SwordType.from(itemStack).contains(SwordType.FIERCEREDGE)) {
                return null;
            }
            
            Map.Entry<Integer, ResourceLocation> currentLocation = state.resolvCurrentComboStateTicks(entity);
            ResourceLocation superSlashArts = state.getSlashArts().doArts(SlashArts.ArtsType.Super, entity);
            if (!ComboStateRegistry.NONE.getId().equals(superSlashArts) && !currentLocation.getValue().equals(superSlashArts)) {
                return superSlashArts;
            }
            return null;
        }).orElse(null);
    }
}
