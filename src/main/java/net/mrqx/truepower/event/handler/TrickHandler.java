package net.mrqx.truepower.event.handler;

import mods.flammpfeil.slashblade.ability.SlayerStyleArts;
import mods.flammpfeil.slashblade.ability.Untouchable;
import mods.flammpfeil.slashblade.capability.inputstate.CapabilityInputState;
import mods.flammpfeil.slashblade.capability.slashblade.BladeStateAccess;
import mods.flammpfeil.slashblade.capability.slashblade.ISlashBladeState;
import mods.flammpfeil.slashblade.event.handler.InputCommandEvent;
import mods.flammpfeil.slashblade.item.SwordType;
import mods.flammpfeil.slashblade.registry.ComboStateRegistry;
import mods.flammpfeil.slashblade.util.AdvancementHelper;
import mods.flammpfeil.slashblade.util.InputCommand;
import mods.flammpfeil.slashblade.util.NBTHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.mrqx.sbr_core.utils.InputStream;
import net.mrqx.sbr_core.utils.JustSlashArtManager;
import net.mrqx.truepower.attachment.ITruePowerData;
import net.mrqx.truepower.config.TruePowerCommonConfig;
import net.mrqx.truepower.mixin.AccessorServerPlayer;
import net.mrqx.truepower.util.TruePowerAttackManager;
import net.mrqx.truepower.util.TruePowerInputTimeLines;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

import java.util.EnumSet;
import java.util.Optional;

@EventBusSubscriber
public final class TrickHandler {
    static final int TRICK_UNTOUCHABLE_TIME = 10;
    
    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void trickDown(InputCommandEvent event) {
        EnumSet<InputCommand> old = event.getOld();
        EnumSet<InputCommand> current = event.getCurrent();
        ServerPlayer sender = event.getEntity();
        ITruePowerData data = ITruePowerData.get(sender);
        ItemStack blade = sender.getMainHandItem();
        Optional<ISlashBladeState> bladeStateOptional = BladeStateAccess.of(blade);
        if (bladeStateOptional.isEmpty()) {
            return;
        }
        ISlashBladeState bladeState = bladeStateOptional.get();
        
        if (bladeState.isBroken() || bladeState.isSealed() || !SwordType.from(blade).contains(SwordType.BEWITCHED) || data.getAvoidTrick() > 0) {
            return;
        }
        
        boolean onDown = !old.contains(InputCommand.SPRINT) && current.contains(InputCommand.SPRINT)
            && (InputStream.getOrCreateInputStream(sender).checkTimeLineInput(
            TruePowerCommonConfig.EASY_TRICK_DOWN.get()
                ? TruePowerInputTimeLines.EASY_TRICK_DOWN_INPUT_TIME_LINE
                : TruePowerInputTimeLines.TRICK_DOWN_INPUT_TIME_LINE
        ));
        
        if (!onDown) {
            return;
        }
        
        doTrickDown(sender, data, bladeState);
    }
    
    @SubscribeEvent(priority = EventPriority.LOW)
    public static void trickUp(InputCommandEvent event) {
        EnumSet<InputCommand> old = event.getOld();
        EnumSet<InputCommand> current = event.getCurrent();
        ServerPlayer sender = event.getEntity();
        ITruePowerData data = ITruePowerData.get(sender);
        ItemStack blade = sender.getMainHandItem();
        Optional<ISlashBladeState> bladeStateOptional = BladeStateAccess.of(blade);
        if (bladeStateOptional.isEmpty()) {
            return;
        }
        ISlashBladeState bladeState = bladeStateOptional.get();
        CompoundTag persistentData = sender.getPersistentData();
        
        if (bladeState.isBroken()
            || bladeState.isSealed()
            || !SwordType.from(blade).contains(SwordType.BEWITCHED)
            || data.getAvoidTrick() > 0
            || persistentData.getInt(SlayerStyleArts.AVOID_TRICKUP_PATH) > 0
            || !data.canMove()) {
            return;
        }
        
        boolean onDown = !old.contains(InputCommand.SPRINT) && current.contains(InputCommand.SPRINT);
        if (!onDown) {
            return;
        }
        
        Untouchable.setUntouchable(sender, 10);
        
        Vec3 motion = new Vec3(0.0F, 0.8, 0.0F);
        sender.move(MoverType.SELF, motion);
        ((AccessorServerPlayer) sender).setIsChangingDimension(true);
        sender.connection.send(new ClientboundSetEntityMotionPacket(sender.getId(), motion.scale(0.75F)));
        
        persistentData.putInt(SlayerStyleArts.AVOID_TRICKUP_PATH, 2);
        sender.setOnGround(false);
        persistentData.putInt(SlayerStyleArts.AVOID_COUNTER_PATH, 2);
        NBTHelper.putVector3d(persistentData, SlayerStyleArts.AVOID_VEC_PATH, sender.position());
        
        JustSlashArtManager.resetJustCount(sender);
        
        AdvancementHelper.grantCriterion(sender, SlayerStyleArts.ADVANCEMENT_TRICK_UP);
        
        bladeState.updateComboSeq(sender, ComboStateRegistry.NONE.getId());
        
        sender.playNotifySound(SoundEvents.ENDERMAN_TELEPORT, SoundSource.PLAYERS, 0.5F, 1.2F);
    }
    
    @SubscribeEvent
    public static void onTick(PlayerTickEvent.Pre event) {
        Player player = event.getEntity();
        ITruePowerData data = ITruePowerData.get(player);
        if (data.getAvoidTrick() <= 0) {
            return;
        }
        
        int count = data.getAvoidTrick() - 1;
        data.setAvoidTrick(count);
        if (count <= 0 && player instanceof ServerPlayer serverPlayer) {
            serverPlayer.hasChangedDimension();
            if (data.isTrickDowning()) {
                if (serverPlayer.getData(CapabilityInputState.INPUT_STATE.get())
                    .getCommands(serverPlayer).contains(InputCommand.SPRINT)) {
                    if (InputStream.getOrCreateInputStream(serverPlayer)
                        .checkInputWithRangedTime(InputCommand.SPRINT, InputStream.InputType.START, Integer.MAX_VALUE, 10)) {
                        BladeStateAccess.of(serverPlayer.getMainHandItem()).ifPresent(state ->
                            doTrickDown(serverPlayer, data, state)
                        );
                    }
                } else {
                    data.setTrickDowning(false);
                }
            }
        }
    }
    
    public static void doTrickDown(ServerPlayer sender, ITruePowerData data, ISlashBladeState bladeState) {
        if (sender.onGround()) {
            Untouchable.setUntouchable(sender, TRICK_UNTOUCHABLE_TIME);
            
            Vec3 input = new Vec3(0, 0, -1);
            
            sender.moveRelative(1.0f, input);
            
            Vec3 motion = TruePowerAttackManager.maybeBackOffFromEdge(sender.getDeltaMovement(), sender);
            
            sender.playNotifySound(SoundEvents.ENDERMAN_TELEPORT, SoundSource.PLAYERS, 0.5F, 1.2f);
            
            sender.move(MoverType.SELF, motion);
            ((AccessorServerPlayer) sender).setIsChangingDimension(true);
            
            MinecraftServer server = sender.getServer();
            if (server != null) {
                server.getPlayerList().broadcastAll(new ClientboundSetEntityMotionPacket(sender.getId(), motion));
            }
            
            data.setAvoidTrick(2);
            data.setTrickDowning(true);
            
            sender.getPersistentData().putInt(SlayerStyleArts.AVOID_COUNTER_PATH, 2);
            NBTHelper.putVector3d(sender.getPersistentData(), SlayerStyleArts.AVOID_VEC_PATH, sender.position());
            
            JustSlashArtManager.resetJustCount(sender);
            
            AdvancementHelper.grantCriterion(sender, SlayerStyleArts.ADVANCEMENT_TRICK_DODGE);
            
            bladeState.updateComboSeq(sender, bladeState.getComboRoot());
        } else {
            Vec3 oldPos = sender.position();
            Vec3 motion = new Vec3(0, -512, 0);
            sender.move(MoverType.SELF, motion);
            if (sender.onGround()) {
                Untouchable.setUntouchable(sender, TRICK_UNTOUCHABLE_TIME);
                ((AccessorServerPlayer) sender).setIsChangingDimension(true);
                
                MinecraftServer server = sender.getServer();
                if (server != null) {
                    server.getPlayerList().broadcastAll(new ClientboundSetEntityMotionPacket(sender));
                }
                
                data.setAvoidTrick(2);
                data.setTrickDowning(true);
                
                sender.getPersistentData().putInt(SlayerStyleArts.AVOID_COUNTER_PATH, 2);
                NBTHelper.putVector3d(sender.getPersistentData(), SlayerStyleArts.AVOID_VEC_PATH, sender.position());
                
                JustSlashArtManager.resetJustCount(sender);
                bladeState.updateComboSeq(sender, ComboStateRegistry.NONE.getId());
                
                AdvancementHelper.grantCriterion(sender, SlayerStyleArts.ADVANCEMENT_TRICK_DOWN);
                sender.playNotifySound(SoundEvents.ENDERMAN_TELEPORT, SoundSource.PLAYERS, 0.5f, 1.2f);
            } else {
                sender.setPos(oldPos);
            }
        }
    }
}
