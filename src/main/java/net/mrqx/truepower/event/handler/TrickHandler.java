package net.mrqx.truepower.event.handler;

import mods.flammpfeil.slashblade.ability.SlayerStyleArts;
import mods.flammpfeil.slashblade.ability.Untouchable;
import mods.flammpfeil.slashblade.capability.slashblade.ISlashBladeState;
import mods.flammpfeil.slashblade.capability.slashblade.SlashBladeState;
import mods.flammpfeil.slashblade.event.InputCommandEvent;
import mods.flammpfeil.slashblade.item.ItemSlashBlade;
import mods.flammpfeil.slashblade.item.SwordType;
import mods.flammpfeil.slashblade.registry.ComboStateRegistry;
import mods.flammpfeil.slashblade.util.AdvancementHelper;
import mods.flammpfeil.slashblade.util.InputCommand;
import mods.flammpfeil.slashblade.util.NBTHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.mrqx.sbr_core.utils.InputStream;
import net.mrqx.truepower.util.JustSlashArtManager;

import java.util.EnumSet;
import java.util.LinkedList;

@Mod.EventBusSubscriber
public class TrickHandler {
    static final int TRICK_UNTOUCHABLE_TIME = 10;

    private static final LinkedList<InputStream.TimeLineKeyInput> TRICK_DOWN_INPUT_TIME_LINE = new LinkedList<>();

    static {
        TRICK_DOWN_INPUT_TIME_LINE.add(new InputStream.TimeLineKeyInput(3, 0, InputCommand.FORWARD, EnumSet.noneOf(InputCommand.class), InputStream.InputType.START));
        TRICK_DOWN_INPUT_TIME_LINE.add(new InputStream.TimeLineKeyInput(3, 0, InputCommand.BACK, EnumSet.noneOf(InputCommand.class), InputStream.InputType.START));
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void trickDown(InputCommandEvent event) {
        EnumSet<InputCommand> old = event.getOld();
        EnumSet<InputCommand> current = event.getCurrent();
        ServerPlayer sender = event.getEntity();
        ItemStack blade = sender.getMainHandItem();
        ISlashBladeState bladeState = blade.getCapability(ItemSlashBlade.BLADESTATE).orElse(new SlashBladeState(blade));

        InputStream inputStream = InputStream.getOrCreateInputStream(sender);

        if (bladeState.isBroken()
                || bladeState.isSealed()
                || !SwordType.from(blade).contains(SwordType.BEWITCHED)
                || (sender.getPersistentData().getInt("truepower.avoid.trick") > 0)) {
            return;
        }

        boolean onDown = !old.contains(InputCommand.SPRINT) && current.contains(InputCommand.SPRINT)
                && inputStream.checkTimeLineInput(TRICK_DOWN_INPUT_TIME_LINE);

        if (!onDown) {
            return;
        }

        if (sender.onGround()) {
            Untouchable.setUntouchable(sender, TRICK_UNTOUCHABLE_TIME);

            Vec3 input = new Vec3(0, 0, -1);

            sender.moveRelative(3.0f, input);

            Vec3 motion = maybeBackOffFromEdge(sender.getDeltaMovement(), sender);

            sender.playNotifySound(SoundEvents.ENDERMAN_TELEPORT, SoundSource.PLAYERS, 0.5F, 1.2f);

            sender.move(MoverType.SELF, motion);
            sender.isChangingDimension = true;

            sender.connection.send(new ClientboundSetEntityMotionPacket(sender.getId(), motion.scale(0.5)));

            sender.getPersistentData().putInt("truepower.avoid.trick", 2);

            sender.getPersistentData().putInt("sb.avoid.counter", 2);
            NBTHelper.putVector3d(sender.getPersistentData(), "sb.avoid.vec", sender.position());

            JustSlashArtManager.resetJustCount(sender);
            bladeState.updateComboSeq(sender, ComboStateRegistry.NONE.getId());

            AdvancementHelper.grantCriterion(sender, SlayerStyleArts.ADVANCEMENT_TRICK_DODGE);

            sender.getMainHandItem().getCapability(ItemSlashBlade.BLADESTATE)
                    .ifPresent(state -> state.updateComboSeq(sender, state.getComboRoot()));
        } else {
            Vec3 oldPos = sender.position();
            Vec3 motion = new Vec3(0, -512, 0);
            sender.move(MoverType.SELF, motion);
            if (sender.onGround()) {
                Untouchable.setUntouchable(sender, TRICK_UNTOUCHABLE_TIME);
                sender.isChangingDimension = true;

                sender.connection.send(new ClientboundSetEntityMotionPacket(sender.getId(), motion.scale(0.75)));

                sender.getPersistentData().putInt("truepower.avoid.trick", 2);

                sender.getPersistentData().putInt("sb.avoid.counter", 2);
                NBTHelper.putVector3d(sender.getPersistentData(), "sb.avoid.vec", sender.position());

                JustSlashArtManager.resetJustCount(sender);
                bladeState.updateComboSeq(sender, ComboStateRegistry.NONE.getId());

                AdvancementHelper.grantCriterion(sender, SlayerStyleArts.ADVANCEMENT_TRICK_DOWN);
                sender.playNotifySound(SoundEvents.ENDERMAN_TELEPORT, SoundSource.PLAYERS, 0.5f, 1.2f);
            } else {
                sender.setPos(oldPos);
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.LOW)
    public static void trickUp(InputCommandEvent event) {
        EnumSet<InputCommand> old = event.getOld();
        EnumSet<InputCommand> current = event.getCurrent();
        ServerPlayer sender = event.getEntity();
        ItemStack blade = sender.getMainHandItem();
        ISlashBladeState bladeState = blade.getCapability(ItemSlashBlade.BLADESTATE).orElse(new SlashBladeState(blade));
        CompoundTag persistentData = sender.getPersistentData();

        if (bladeState.isBroken()
                || bladeState.isSealed()
                || !SwordType.from(blade).contains(SwordType.BEWITCHED)
                || (persistentData.getInt("truepower.avoid.trick") > 0)
                || (persistentData.getInt("sb.avoid.trickup") > 0)
                || (!persistentData.getBoolean("truePower.canMove"))) {
            return;
        }

        boolean onDown = !old.contains(InputCommand.SPRINT) && current.contains(InputCommand.SPRINT);
        if (!onDown) {
            return;
        }

        Untouchable.setUntouchable(sender, 10);

        Vec3 motion = new Vec3(0.0F, 0.8, 0.0F);
        sender.move(MoverType.SELF, motion);
        sender.isChangingDimension = true;
        sender.connection.send(new ClientboundSetEntityMotionPacket(sender.getId(), motion.scale(0.75F)));

        persistentData.putInt("sb.avoid.trickup", 2);
        sender.setOnGround(false);
        persistentData.putInt("sb.avoid.counter", 2);
        NBTHelper.putVector3d(persistentData, "sb.avoid.vec", sender.position());

        JustSlashArtManager.resetJustCount(sender);

        AdvancementHelper.grantCriterion(sender, SlayerStyleArts.ADVANCEMENT_TRICK_UP);

        sender.playNotifySound(SoundEvents.ENDERMAN_TELEPORT, SoundSource.PLAYERS, 0.5F, 1.2F);
    }

    @SubscribeEvent
    public static void onTick(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.START) {
            return;
        }

        if (event.player.getPersistentData().getInt("truepower.avoid.trick") > 0) {
            int count = event.player.getPersistentData().getInt("truepower.avoid.trick");
            count--;
            if (count <= 0) {
                event.player.getPersistentData().remove("truepower.avoid.trick");
                if (event.player instanceof ServerPlayer) {
                    ((ServerPlayer) event.player).hasChangedDimension();
                }
            } else {
                event.player.getPersistentData().putInt("truepower.avoid.trick", count);
            }
        }
    }

    public static Vec3 maybeBackOffFromEdge(Vec3 vec, LivingEntity mover) {
        double d0 = vec.x;
        double d1 = vec.z;

        while (d0 != 0 && mover.level().noCollision(mover,
                mover.getBoundingBox().move(d0, -mover.maxUpStep(), 0))) {
            if (d0 < 0.05 && d0 >= -0.05) {
                d0 = 0;
            } else if (d0 > 0) {
                d0 -= 0.05;
            } else {
                d0 += 0.05;
            }
        }

        while (d1 != 0 && mover.level().noCollision(mover,
                mover.getBoundingBox().move(0, -mover.maxUpStep(), d1))) {
            if (d1 < 0.05 && d1 >= -0.05) {
                d1 = 0;
            } else if (d1 > 0) {
                d1 -= 0.05;
            } else {
                d1 += 0.05;
            }
        }

        while (d0 != 0 && d1 != 0 && mover.level().noCollision(mover,
                mover.getBoundingBox().move(d0, -mover.maxUpStep(), d1))) {
            if (d0 < 0.05 && d0 >= -0.05) {
                d0 = 0;
            } else if (d0 > 0) {
                d0 -= 0.05;
            } else {
                d0 += 0.05;
            }

            if (d1 < 0.05 && d1 >= -0.05) {
                d1 = 0;
            } else if (d1 > 0) {
                d1 -= 0.05;
            } else {
                d1 += 0.05;
            }
        }

        vec = new Vec3(d0, vec.y, d1);
        return vec;
    }
}
