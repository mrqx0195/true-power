package net.mrqx.truepower.compat.epicfight.skill;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import yesman.epicfight.api.utils.side.ClientOnly;
import yesman.epicfight.skill.SkillContainer;
import yesman.epicfight.skill.weaponinnate.SimpleWeaponInnateSkill;
import yesman.epicfight.skill.weaponinnate.WeaponInnateSkill;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;
import yesman.epicfight.world.capabilities.item.CapabilityItem;

import java.util.List;

/**
 * just a dummy now...
 */
public class SlashBladeSkill extends SimpleWeaponInnateSkill {
    public SlashBladeSkill(Builder builder) {
        super(builder);
    }
    
    @Override
    public boolean canExecute(SkillContainer container) {
        return false;
    }
    
    @Override
    @ClientOnly
    public boolean shouldDraw(SkillContainer container) {
        return false;
    }
    
    @Override
    public List<Component> getTooltipOnItem(ItemStack itemStack, CapabilityItem cap, PlayerPatch<?> playerCap) {
        return List.of();
    }
    
    @Override
    public void executeOnServer(SkillContainer container, CompoundTag arguments) {
    }
    
    @Override
    public WeaponInnateSkill registerPropertiesToAnimation() {
        return this;
    }
}
