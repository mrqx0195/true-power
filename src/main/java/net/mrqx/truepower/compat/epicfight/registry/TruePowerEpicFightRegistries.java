package net.mrqx.truepower.compat.epicfight.registry;

import net.minecraft.core.Holder;
import net.minecraft.sounds.SoundEvents;
import net.mrqx.truepower.TruePowerMod;
import net.mrqx.truepower.compat.epicfight.skill.SlashBladeSkill;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import yesman.epicfight.api.ex_cap.data.Moveset;
import yesman.epicfight.gameasset.ColliderPreset;
import yesman.epicfight.registry.EpicFightRegistries;
import yesman.epicfight.registry.deferred.ItemPresetRegister;
import yesman.epicfight.registry.deferred.MovesetRegister;
import yesman.epicfight.registry.deferred.holders.DeferredMoveset;
import yesman.epicfight.registry.deferred.holders.DeferredWeapon;
import yesman.epicfight.registry.entries.EpicFightProviderConditionals;
import yesman.epicfight.skill.Skill;
import yesman.epicfight.skill.weaponinnate.SimpleWeaponInnateSkill;
import yesman.epicfight.world.capabilities.item.CapabilityItem;
import yesman.epicfight.world.capabilities.item.WeaponCapability;

public final class TruePowerEpicFightRegistries {
    public static final DeferredRegister<Skill> SKILL_REGISTRY = DeferredRegister.create(EpicFightRegistries.Keys.SKILL, TruePowerMod.MODID);
    public static final MovesetRegister MOVESET_REGISTRY = MovesetRegister.create(TruePowerMod.MODID);
    public static final ItemPresetRegister ITEM_PRESET_REGISTRY = ItemPresetRegister.create(TruePowerMod.MODID);
    
    public static final DeferredHolder<Skill, SlashBladeSkill> SLASHBLADE_SKILL = SKILL_REGISTRY.register("slashblade", key ->
        SimpleWeaponInnateSkill.createSimpleWeaponInnateBuilder(SlashBladeSkill::new)
            // just a dummy now...
            .build(key)
    );
    
    public static final DeferredMoveset SLASHBLADE_MOVESET = MOVESET_REGISTRY.registerMoveset("slashblade",
        () -> Moveset.builder()
            .addInnateSkill((itemStack, playerPatch) -> SLASHBLADE_SKILL.get())
    );
    
    public static final DeferredWeapon SLASHBLADE_WEAPON = ITEM_PRESET_REGISTRY.registerWeapon("slashblade",
        () -> WeaponCapability.builder()
            .collider(ColliderPreset.TACHI)
            .category(CapabilityItem.WeaponCategories.SWORD)
            .hitSound(Holder.direct(SoundEvents.EMPTY))
            .setTierValues(0, 0, 0.0, 0.0)
            .canBePlacedOffhand(false)
            .addConditionals(EpicFightProviderConditionals.DEFAULT_2H_WIELD_STYLE)
            .addMoveset(CapabilityItem.Styles.TWO_HAND, SLASHBLADE_MOVESET)
            .addTag(TruePowerMod.prefix("slashblade"))
    );
}
