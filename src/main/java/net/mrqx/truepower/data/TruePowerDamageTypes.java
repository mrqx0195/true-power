package net.mrqx.truepower.data;

import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageType;
import net.mrqx.truepower.TruePowerMod;

public final class TruePowerDamageTypes {
    public static final ResourceKey<DamageType>
        SUMMONED_SWORD = ResourceKey.create(Registries.DAMAGE_TYPE, TruePowerMod.prefix("summoned_sword"));
    
    public static void register(BootstrapContext<DamageType> context) {
        registerDamageType(context, SUMMONED_SWORD, 0);
    }
    
    @SuppressWarnings("SameParameterValue")
    private static void registerDamageType(BootstrapContext<DamageType> context, ResourceKey<DamageType> damageType, float pExhaustion) {
        context.register(damageType, new DamageType(damageType.location().getNamespace() + "." + damageType.location().getPath(), pExhaustion));
    }
}
