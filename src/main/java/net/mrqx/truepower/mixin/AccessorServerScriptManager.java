package net.mrqx.truepower.mixin;

import dev.latvian.mods.kubejs.server.ServerScriptManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ServerScriptManager.class)
public interface AccessorServerScriptManager {
    @Accessor("staticInstance")
    static ServerScriptManager getStaticInstance() {
        return null;
    }
    
    @Accessor("staticInstance")
    static void setStaticInstance(ServerScriptManager staticInstance) {
    }
}
