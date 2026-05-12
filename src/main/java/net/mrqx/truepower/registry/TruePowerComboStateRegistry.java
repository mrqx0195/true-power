package net.mrqx.truepower.registry;

import mods.flammpfeil.slashblade.SlashBlade;
import mods.flammpfeil.slashblade.ability.StunManager;
import mods.flammpfeil.slashblade.event.handler.FallHandler;
import mods.flammpfeil.slashblade.registry.combo.ComboState;
import mods.flammpfeil.slashblade.util.AttackManager;
import net.mrqx.truepower.TruePowerMod;
import net.mrqx.truepower.config.TruePowerCommonConfig;
import net.mrqx.truepower.util.TruePowerAttackManager;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

@SuppressWarnings("unused")
public class TruePowerComboStateRegistry {
    public static final DeferredRegister<ComboState> COMBO_STATE = DeferredRegister.create(ComboState.REGISTRY_KEY,
        TruePowerMod.MODID);
    
    public static final DeferredHolder<ComboState, ComboState> VOID_SLASH = COMBO_STATE.register("void_slash", ComboState.Builder
        .newInstance().startAndEnd(2200, 2277).priority(500).speed(1.0F)
        .next(entity -> TruePowerMod.prefix("void_slash"))
        .nextOfTimeout(entity -> TruePowerMod.prefix("void_slash_sheath"))
        .addTickAction(entity -> entity.setDeltaMovement(0, entity.getDeltaMovement().y, 0))
        .addTickAction(ComboState.TimeLineTickAction.getBuilder()
            .put(16, livingEntity -> TruePowerAttackManager.doVoidSlashAttack(livingEntity, AttackManager.isPowered(livingEntity) ? TruePowerCommonConfig.POWERED_VOID_SLASH_DAMAGE_FIRST.get() : TruePowerCommonConfig.VOID_SLASH_DAMAGE.get()))
            .put(17, livingEntity -> {
                if (AttackManager.isPowered(livingEntity)) {
                    TruePowerAttackManager.doVoidSlashAttack(livingEntity, TruePowerCommonConfig.POWERED_VOID_SLASH_DAMAGE_SECOND.get());
                }
            })
            .build())
        .rotationKeyframe(16, -36)
        .rotationKeyframe(16 + 1, -72)
        .rotationKeyframe(16 + 2, -108)
        .rotationKeyframe(16 + 3, -144)
        .rotationKeyframe(16 + 4, -180)
        .rotationKeyframe(16 + 5, -180)
        .rotationKeyframe(57, -162)
        .rotationKeyframe(57 + 1, -144)
        .rotationKeyframe(57 + 2, -126)
        .rotationKeyframe(57 + 3, -108)
        .rotationKeyframe(57 + 4, -90)
        .rotationKeyframe(57 + 5, -72)
        .rotationKeyframe(57 + 6, -54)
        .rotationKeyframe(57 + 7, -36)
        .rotationKeyframe(57 + 8, -18)
        .rotationKeyframe(57 + 9, 0)
        .rotationKeyframe(57 + 10, 0)
        .addTickAction(FallHandler::fallDecrease)
        .addHitEffect((t, a) -> StunManager.setStun(t, 60))::build);
    
    public static final DeferredHolder<ComboState, ComboState> VOID_SLASH_SHEATH = COMBO_STATE.register("void_slash_sheath",
        ComboState.Builder.newInstance().startAndEnd(2278, 2299).priority(50)
            .next(entity -> SlashBlade.prefix("none")).nextOfTimeout(entity -> SlashBlade.prefix("none"))
            .addTickAction(FallHandler::fallDecrease)
            .addTickAction(ComboState.TimeLineTickAction.getBuilder()
                .put(0, AttackManager::playQuickSheathSoundAction).build())
            .releaseAction(ComboState::releaseActionQuickCharge)::build);
    
}
