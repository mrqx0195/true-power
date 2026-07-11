package net.mrqx.truepower.mixin;

import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.CloseableResourceManager;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.server.packs.resources.ReloadableResourceManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;

@Mixin(ReloadableResourceManager.class)
public interface AccessorReloadableResourceManager {
    @Accessor("resources")
    CloseableResourceManager getResources();
    
    @Accessor("resources")
    void setResources(CloseableResourceManager resources);
    
    @Accessor("type")
    PackType getType();
    
    @Accessor("listeners")
    List<PreparableReloadListener> getListeners();
}
