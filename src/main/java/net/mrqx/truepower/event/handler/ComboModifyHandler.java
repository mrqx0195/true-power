package net.mrqx.truepower.event.handler;

import mods.flammpfeil.slashblade.item.ItemSlashBlade;
import mods.flammpfeil.slashblade.registry.combo.ComboState;
import mods.flammpfeil.slashblade.slasharts.SlashArts;
import mods.flammpfeil.slashblade.util.AdvancementHelper;
import mods.flammpfeil.slashblade.util.AttackManager;
import mods.flammpfeil.slashblade.util.KnockBacks;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.PacketDistributor;
import net.mrqx.sbr_core.events.ComboStateRegistryEvent;
import net.mrqx.truepower.TruePowerModConfig;
import net.mrqx.truepower.entity.EntityBlastSummonedSword;
import net.mrqx.truepower.network.ComboSyncMessage;
import net.mrqx.truepower.network.NetworkManager;
import net.mrqx.truepower.util.RankManager;
import net.mrqx.truepower.util.TruePowerComboHelper;

import java.util.EnumSet;
import java.util.concurrent.atomic.AtomicBoolean;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class ComboModifyHandler {
    @SuppressWarnings("unused")
    private enum ComboMovementModifiers {
        COMBO_A1(1, 10, 100, 2, true),
        COMBO_A1_END(10, 21, 100, 0, true),
        COMBO_A2(100, 115, 100),
        COMBO_A2_END(115, 132, 100),

        COMBO_C(400, 459, 100, 20, true),
        COMBO_C_END(459, 488, 100, 0),

        COMBO_A3(200, 218, 100, 10, true),
        COMBO_A3_END(218, 230, 100, 0, true),
        COMBO_A3_END2(230, 281, 100),
        COMBO_A4(500, 576, 100, 15),
        COMBO_A4_EX(800, 839, 100, 25),
        COMBO_A5(900, 1013, 100, 70),

        COMBO_B1(700, 720, 100),
        COMBO_B_LOOPS(710, 720, 100),
        COMBO_B_END(720, 743, 100, 12, true),
        COMBO_B7(710, 764, 100, 15, true),

        AERIAL_RAVE_A1(710, 764, 100),
        AERIAL_RAVE_A2(1200, 1210, 100),
        AERIAL_RAVE_A3(1300, 1328, 100),
        AERIAL_RAVE_B3(710, 764, 100),
        AERIAL_RAVE_B4(1500, 1537, 100),

        UPPER_SLASH(1600, 1659, 90),
        UPPER_SLASH_JUMP(1700, 1713, 90),

        AERIAL_CLEAVE(1800, 1812, 70),
        AERIAL_CLEAVE_LOOP(1812, 1817, 70),
        AERIAL_CLEAVE_LANDING(1816, 1859, 70),

        RAPID_SLASH(2000, 2019, 70),
        RAPID_SLASH_END(2019, 2054, 70),
        RISING_STAR(2100, 2137, 80),

        JUDGEMENT_CUT(1900, 1923, 50),
        JUDGEMENT_CUT_SLASH(1923, 1928, 50),
        JUDGEMENT_CUT_SLASH_JUST(1923, 1928, 45),

        VOID_SLASH(2200, 2277, 50, 20, true),
        TRUE_VOID_SLASH(2200, 2277, 500, 20, true);

        public final int startFrame;
        public final int endFrame;
        public final int priority;

        public final int canCancelFrame;
        public final boolean jumpCancelOnly;

        ComboMovementModifiers(int startFrame, int endFrame, int priority) {
            this.startFrame = startFrame;
            this.endFrame = endFrame;
            this.priority = priority;
            this.canCancelFrame = -1;
            this.jumpCancelOnly = false;
        }

        ComboMovementModifiers(int startFrame, int endFrame, int priority, int canCancelFrame) {
            this.startFrame = startFrame;
            this.endFrame = endFrame;
            this.priority = priority;
            this.canCancelFrame = canCancelFrame;
            this.jumpCancelOnly = false;
        }

        ComboMovementModifiers(int startFrame, int endFrame, int priority, int canCancelFrame, boolean jumpCancelOnly) {
            this.startFrame = startFrame;
            this.endFrame = endFrame;
            this.priority = priority;
            this.canCancelFrame = canCancelFrame;
            this.jumpCancelOnly = jumpCancelOnly;
        }
    }

    @SuppressWarnings("unused")
    private enum RemoveReleaseAction {
        COMBO_A3_END3(281, 306, 100),
        COMBO_A4_END(576, 608, 100),
        COMBO_A4_EX_END2(877, 894, 100),
        COMBO_B7_END3(764, 787, 100);

        public final int startFrame;
        public final int endFrame;
        public final int priority;

        RemoveReleaseAction(int startFrame, int endFrame, int priority) {
            this.startFrame = startFrame;
            this.endFrame = endFrame;
            this.priority = priority;
        }
    }

    @SubscribeEvent
    public static void onComboStateRegistryEvent(ComboStateRegistryEvent event) {
        ComboState.Builder builder = event.getBuilder();
        ComboState combo = event.getCombo();

        if (combo.getStartFrame() == 0
                && combo.getEndFrame() == 1
                && combo.getPriority() == 1000) {
            builder.addTickAction(livingEntity -> RankManager.setPreAddRank(livingEntity, 0));
        }

        EnumSet.allOf(RemoveReleaseAction.class).forEach(remover -> {
            if (combo.getStartFrame() == remover.startFrame
                    && combo.getEndFrame() == remover.endFrame
                    && combo.getPriority() == remover.priority) {
                builder.releaseAction((livingEntity, integer) -> SlashArts.ArtsType.Fail);
            }
        });

        AtomicBoolean flag = new AtomicBoolean(false);

        EnumSet.allOf(ComboMovementModifiers.class).forEach(modifier -> {
            if (combo.getStartFrame() == modifier.startFrame
                    && combo.getEndFrame() == modifier.endFrame
                    && combo.getPriority() == modifier.priority) {
                flag.set(true);

                builder.addTickAction(livingEntity -> livingEntity.getMainHandItem().getCapability(ItemSlashBlade.BLADESTATE).ifPresent(state -> {
                    if (!livingEntity.level().isClientSide) {
                        livingEntity.getPersistentData().putString("truePower.combo", state.getComboSeq().toString());

                        long elapsedTime = state.getElapsedTime(livingEntity);
                        livingEntity.getPersistentData().putBoolean("truePower.canMove",
                                (modifier.canCancelFrame != -1) && (elapsedTime >= modifier.canCancelFrame));

                        livingEntity.getPersistentData().putBoolean("truePower.jumpCancelOnly", modifier.jumpCancelOnly);
                        livingEntity.getPersistentData().putBoolean("truePower.noMoveEnable", TruePowerModConfig.CAN_NOT_MOVE_WHILE_COMBO.get());

                        if (livingEntity instanceof ServerPlayer serverPlayer) {
                            CompoundTag persistentData = serverPlayer.getPersistentData();
                            ComboSyncMessage comboSyncMessage = new ComboSyncMessage();

                            comboSyncMessage.comboState = state.getComboSeq();
                            comboSyncMessage.lastActionTime = state.getLastActionTime();
                            comboSyncMessage.canMove = persistentData.getBoolean("truePower.canMove");
                            comboSyncMessage.jumpCancelOnly = persistentData.getBoolean("truePower.jumpCancelOnly");
                            comboSyncMessage.noMoveEnable = persistentData.getBoolean("truePower.noMoveEnable");
                            comboSyncMessage.syncCombo = false;

                            NetworkManager.INSTANCE.send(PacketDistributor.PLAYER.with(() -> serverPlayer), comboSyncMessage);
                        }
                    }
                }));

                if ((modifier.equals(ComboMovementModifiers.RISING_STAR) || modifier.equals(ComboMovementModifiers.UPPER_SLASH_JUMP)) && combo.isAerial()) {
                    builder.addTickAction((entityIn) -> {

                        long elapsed = ComboState.getElapsed(entityIn);

                        if (elapsed < 3) {
                            entityIn.setDeltaMovement(0, entityIn.getDeltaMovement().y * 1.1, 0);

                            if (entityIn instanceof ServerPlayer serverPlayer) {
                                serverPlayer.connection.send(new ClientboundSetEntityMotionPacket(serverPlayer));
                            }
                        }
                    });
                } else if (modifier.equals(ComboMovementModifiers.AERIAL_RAVE_B3) && combo.isAerial()) {
                    builder.addTickAction(ComboState.TimeLineTickAction.getBuilder()
                            .put(6, (entityIn) -> AttackManager.doSlash(entityIn, 180 + 57, Vec3.ZERO, false, false, 0.4, KnockBacks.toss))
                            .put(7, (entityIn) -> AttackManager.doSlash(entityIn, 180 + 57, Vec3.ZERO, false, false, 0.4, KnockBacks.toss))
                            .put(8, (entityIn) -> AttackManager.doSlash(entityIn, 180 + 57, Vec3.ZERO, false, false, 0.4, KnockBacks.toss))
                            .put(9, (entityIn) -> AttackManager.doSlash(entityIn, 180 + 57, Vec3.ZERO, false, false, 0.4, KnockBacks.toss))
                            .build());
                }

                if (modifier.equals(ComboMovementModifiers.COMBO_A3_END2)) {
                    builder.releaseAction(ComboState::releaseActionQuickCharge);
                } else if (modifier.equals(ComboMovementModifiers.COMBO_A4)) {
                    builder.releaseAction((livingEntity, integer) -> TruePowerComboHelper.releaseActionQuickCharge(livingEntity, integer, 15));
                } else if (modifier.equals(ComboMovementModifiers.COMBO_A4_EX)) {
                    builder.releaseAction((livingEntity, integer) -> TruePowerComboHelper.releaseActionQuickCharge(livingEntity, integer, 25));
                } else if (modifier.equals(ComboMovementModifiers.COMBO_A5)) {
                    builder.releaseAction((livingEntity, integer) -> TruePowerComboHelper.releaseActionQuickCharge(livingEntity, integer, 30, 50));
                } else if (modifier.equals(ComboMovementModifiers.COMBO_B_END)) {
                    builder.releaseAction((livingEntity, integer) -> TruePowerComboHelper.releaseActionQuickCharge(livingEntity, integer, 12));
                } else if (modifier.equals(ComboMovementModifiers.COMBO_B7)) {
                    builder.releaseAction((livingEntity, integer) -> TruePowerComboHelper.releaseActionQuickCharge(livingEntity, integer, 15));
                } else if (modifier.equals(ComboMovementModifiers.RAPID_SLASH)) {
                    builder.clickAction((livingEntity) -> {
                        AdvancementHelper.grantCriterion(livingEntity, AdvancementHelper.ADVANCEMENT_RAPID_SLASH);
                        AttackManager.doSlash(livingEntity, -30, AttackManager.genRushOffset(livingEntity), false, true, 0.2f);
                    });
                } else if (modifier.equals(ComboMovementModifiers.COMBO_C)) {
                    builder.addTickAction(ComboState.TimeLineTickAction.getBuilder()
                            .put(10, (livingEntity) -> EntityBlastSummonedSword.setPreBlastSwordList(livingEntity, AttackManager.isPowered(livingEntity) ? 6 : 1))
                            .build());
                } else if (modifier.equals(ComboMovementModifiers.RAPID_SLASH_END)) {
                    builder.clickAction((livingEntity) -> {
                        if (AttackManager.isPowered(livingEntity)) {
                            EntityBlastSummonedSword.setPreBlastSwordList(livingEntity, 1);
                        }
                    });
                }

                if (modifier.equals(ComboMovementModifiers.COMBO_A1)) {
                    builder.addTickAction(ComboState.TimeLineTickAction.getBuilder()
                            .put(0, (livingEntity) -> step(livingEntity, 0.5))
                            .build());
                } else if (modifier.equals(ComboMovementModifiers.COMBO_A2)) {
                    builder.addTickAction(ComboState.TimeLineTickAction.getBuilder()
                            .put(0, (livingEntity) -> step(livingEntity, 1.5))
                            .build());
                } else if (modifier.equals(ComboMovementModifiers.COMBO_A3)) {
                    builder.addTickAction(ComboState.TimeLineTickAction.getBuilder()
                            .put(1, (livingEntity) -> step(livingEntity, 1))
                            .put(5, (livingEntity) -> step(livingEntity, 1))
                            .build());
                } else if (modifier.equals(ComboMovementModifiers.COMBO_A4)) {
                    builder.addTickAction(ComboState.TimeLineTickAction.getBuilder()
                            .put(7, (livingEntity) -> step(livingEntity, 1.5))
                            .build());
                } else if (modifier.equals(ComboMovementModifiers.COMBO_A4_EX)) {
                    builder.addTickAction(ComboState.TimeLineTickAction.getBuilder()
                            .put(6, (livingEntity) -> step(livingEntity, 2))
                            .build());
                } else if (modifier.equals(ComboMovementModifiers.COMBO_A5)) {
                    builder.addTickAction(ComboState.TimeLineTickAction.getBuilder()
                            .put(14, (livingEntity) -> step(livingEntity, 3))
                            .build());
                } else if (modifier.equals(ComboMovementModifiers.COMBO_B1)) {
                    builder.addTickAction(ComboState.TimeLineTickAction.getBuilder()
                            .put(0, (livingEntity) -> step(livingEntity, 1.5))
                            .build());
                } else if (modifier.equals(ComboMovementModifiers.COMBO_B_END)) {
                    builder.addTickAction(ComboState.TimeLineTickAction.getBuilder()
                            .put(0, (livingEntity) -> step(livingEntity, 1.5))
                            .build());
                } else if (modifier.equals(ComboMovementModifiers.COMBO_B7)) {
                    builder.addTickAction(ComboState.TimeLineTickAction.getBuilder()
                            .put(0, (livingEntity) -> step(livingEntity, 1.5))
                            .build());
                }

                if (modifier.equals(ComboMovementModifiers.AERIAL_RAVE_A1)) {
                    builder.addTickAction(ComboState.TimeLineTickAction.getBuilder()
                            .put(0, (livingEntity) -> livingEntity.setDeltaMovement(0, 0, 0))
                            .build());
                }
            }
        });
        if (!flag.get()) {
            builder.addTickAction(livingEntity -> livingEntity.getMainHandItem().getCapability(ItemSlashBlade.BLADESTATE).ifPresent(state -> {
                if (!livingEntity.level().isClientSide) {
                    livingEntity.getPersistentData().putString("truePower.combo", state.getComboSeq().toString());

                    livingEntity.getPersistentData().putBoolean("truePower.canMove", true);

                    livingEntity.getPersistentData().putBoolean("truePower.jumpCancelOnly", false);

                    livingEntity.getPersistentData().putBoolean("truePower.noMoveEnable", TruePowerModConfig.CAN_NOT_MOVE_WHILE_COMBO.get());

                    if (livingEntity instanceof ServerPlayer serverPlayer) {
                        CompoundTag persistentData = serverPlayer.getPersistentData();
                        ComboSyncMessage comboSyncMessage = new ComboSyncMessage();

                        comboSyncMessage.comboState = state.getComboSeq();
                        comboSyncMessage.lastActionTime = state.getLastActionTime();
                        comboSyncMessage.canMove = persistentData.getBoolean("truePower.canMove");
                        comboSyncMessage.jumpCancelOnly = persistentData.getBoolean("truePower.jumpCancelOnly");
                        comboSyncMessage.noMoveEnable = persistentData.getBoolean("truePower.noMoveEnable");
                        comboSyncMessage.syncCombo = false;

                        NetworkManager.INSTANCE.send(PacketDistributor.PLAYER.with(() -> serverPlayer), comboSyncMessage);
                    }
                }
            }));
        }
    }

    public static void step(LivingEntity livingEntity, double step) {
        if (TruePowerModConfig.STEP_WHEN_USING_COMBO.get()) {
            Vec3 input = new Vec3(0, 0, step);

            livingEntity.moveRelative(1, input);

            Vec3 motion = TrickHandler.maybeBackOffFromEdge(livingEntity.getDeltaMovement(), livingEntity);

            livingEntity.move(MoverType.SELF, motion);
        }
    }
}
