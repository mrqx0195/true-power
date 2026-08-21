package net.mrqx.truepower.util;

import net.minecraft.world.entity.LivingEntity;

/**
 * Use {@link net.mrqx.sbr_core.utils.JustSlashArtManager} instead.
 */
@SuppressWarnings("unused")
@Deprecated
public class JustSlashArtManager {
    public static int addJustCount(LivingEntity livingEntity) {
        return net.mrqx.sbr_core.utils.JustSlashArtManager.addJustCount(livingEntity);
    }
    
    public static int getJustCount(LivingEntity livingEntity) {
        return net.mrqx.sbr_core.utils.JustSlashArtManager.getJustCount(livingEntity);
    }
    
    public static void resetJustCount(LivingEntity livingEntity) {
        net.mrqx.sbr_core.utils.JustSlashArtManager.resetJustCount(livingEntity);
    }
    
    public static long getJustCooldown(LivingEntity livingEntity) {
        return net.mrqx.sbr_core.utils.JustSlashArtManager.getJustCooldown(livingEntity);
    }
    
    public static void setJustCooldown(LivingEntity livingEntity, long cooldown) {
        net.mrqx.sbr_core.utils.JustSlashArtManager.setJustCooldown(livingEntity, cooldown);
    }
}
