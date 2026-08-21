package net.mrqx.truepower.event;

import mods.flammpfeil.slashblade.capability.slashblade.ISlashBladeState;
import mods.flammpfeil.slashblade.event.SlashBladeEvent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.mrqx.sbr_core.utils.InputStream;
import net.neoforged.bus.api.ICancellableEvent;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class PreInputEvent extends SlashBladeEvent implements ICancellableEvent {
    private final Level level;
    private final LivingEntity entity;
    private final InputStream inputStream;
    @Nullable
    private ResourceLocation preInputComboStateId;
    private final Map.Entry<Integer, ResourceLocation> currentLoc;
    
    public PreInputEvent(ItemStack blade, ISlashBladeState state, Level level, LivingEntity entity,
                         InputStream inputStream, @Nullable ResourceLocation preInputComboStateId, Map.Entry<Integer, ResourceLocation> currentLoc) {
        super(blade, state);
        this.level = level;
        this.entity = entity;
        this.inputStream = inputStream;
        this.preInputComboStateId = preInputComboStateId;
        this.currentLoc = currentLoc;
    }
    
    public LivingEntity getEntity() {
        return entity;
    }
    
    public Level getLevel() {
        return level;
    }
    
    public InputStream getInputStream() {
        return inputStream;
    }
    
    @Nullable
    public ResourceLocation getPreInputComboStateId() {
        return preInputComboStateId;
    }
    
    public void setPreInputComboStateId(@Nullable ResourceLocation preInputComboStateId) {
        this.preInputComboStateId = preInputComboStateId;
    }
    
    public Map.Entry<Integer, ResourceLocation> getCurrentLoc() {
        return currentLoc;
    }
}
