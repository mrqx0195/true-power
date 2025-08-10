package net.mrqx.truepower.registry;

import mods.flammpfeil.slashblade.SlashBlade;
import mods.flammpfeil.slashblade.ability.StunManager;
import mods.flammpfeil.slashblade.event.client.UserPoseOverrider;
import mods.flammpfeil.slashblade.event.handler.FallHandler;
import mods.flammpfeil.slashblade.registry.combo.ComboState;
import mods.flammpfeil.slashblade.util.AttackManager;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import net.mrqx.truepower.TruePowerMod;
import net.mrqx.truepower.TruePowerModConfig;
import net.mrqx.truepower.util.TruePowerAttackManager;

@SuppressWarnings("unused")
public class TruePowerComboStateRegistry {
    public static final DeferredRegister<ComboState> COMBO_STATE = DeferredRegister.create(ComboState.REGISTRY_KEY,
            TruePowerMod.MODID);

    public static final RegistryObject<ComboState> VOID_SLASH = COMBO_STATE.register("void_slash", ComboState.Builder
            .newInstance().startAndEnd(2200, 2277).priority(500).speed(1.0F)
            .next(entity -> TruePowerMod.prefix("void_slash"))
            .nextOfTimeout(entity -> TruePowerMod.prefix("void_slash_sheath"))
            .addTickAction(entity -> entity.setDeltaMovement(0, entity.getDeltaMovement().y, 0))
            .addTickAction(ComboState.TimeLineTickAction.getBuilder()
                    .put(16, livingEntity -> TruePowerAttackManager.doVoidSlashAttack(livingEntity, AttackManager.isPowered(livingEntity) ? TruePowerModConfig.POWERED_VOID_SLASH_DAMAGE_FIRST.get() : TruePowerModConfig.VOID_SLASH_DAMAGE.get()))
                    .put(17, livingEntity -> {
                        if (AttackManager.isPowered(livingEntity)) {
                            TruePowerAttackManager.doVoidSlashAttack(livingEntity, TruePowerModConfig.POWERED_VOID_SLASH_DAMAGE_SECOND.get());
                        }
                    })
                    .build())
            .addTickAction(ComboState.TimeLineTickAction.getBuilder()
                    .put(16, (entityIn) -> UserPoseOverrider.setRot(entityIn, -36, true))
                    .put(16 + 1, (entityIn) -> UserPoseOverrider.setRot(entityIn, -36, true))
                    .put(16 + 2, (entityIn) -> UserPoseOverrider.setRot(entityIn, -36, true))
                    .put(16 + 3, (entityIn) -> UserPoseOverrider.setRot(entityIn, -36, true))
                    .put(16 + 4, (entityIn) -> UserPoseOverrider.setRot(entityIn, -36, true))
                    .put(16 + 5, (entityIn) -> UserPoseOverrider.setRot(entityIn, 0, true))
                    .put(57, (entityIn) -> UserPoseOverrider.setRot(entityIn, 18, true))
                    .put(57 + 1, (entityIn) -> UserPoseOverrider.setRot(entityIn, 18, true))
                    .put(57 + 2, (entityIn) -> UserPoseOverrider.setRot(entityIn, 18, true))
                    .put(57 + 3, (entityIn) -> UserPoseOverrider.setRot(entityIn, 18, true))
                    .put(57 + 4, (entityIn) -> UserPoseOverrider.setRot(entityIn, 18, true))
                    .put(57 + 5, (entityIn) -> UserPoseOverrider.setRot(entityIn, 18, true))
                    .put(57 + 6, (entityIn) -> UserPoseOverrider.setRot(entityIn, 18, true))
                    .put(57 + 7, (entityIn) -> UserPoseOverrider.setRot(entityIn, 18, true))
                    .put(57 + 8, (entityIn) -> UserPoseOverrider.setRot(entityIn, 18, true))
                    .put(57 + 9, (entityIn) -> UserPoseOverrider.setRot(entityIn, 18, true))
                    .put(57 + 10, (entityIn) -> UserPoseOverrider.setRot(entityIn, 0, true)).build())
            .addTickAction(FallHandler::fallDecrease)
            .addHitEffect((t, a) -> StunManager.setStun(t, 60))::build);

    public static final RegistryObject<ComboState> VOID_SLASH_SHEATH = COMBO_STATE.register("void_slash_sheath",
            ComboState.Builder.newInstance().startAndEnd(2278, 2299).priority(50)
                    .next(entity -> SlashBlade.prefix("none")).nextOfTimeout(entity -> SlashBlade.prefix("none"))
                    .addTickAction(FallHandler::fallDecrease)
                    .addTickAction(UserPoseOverrider::resetRot)
                    .addTickAction(ComboState.TimeLineTickAction.getBuilder()
                            .put(0, AttackManager::playQuickSheathSoundAction).build())
                    .releaseAction(ComboState::releaseActionQuickCharge)::build);

}
