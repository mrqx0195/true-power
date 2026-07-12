package net.mrqx.truepower.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.mrqx.truepower.TruePowerMod;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class TruePowerAttributeRegistry {
    public static final DeferredRegister<Attribute> ATTRIBUTES =
        DeferredRegister.create(Registries.ATTRIBUTE, TruePowerMod.MODID);
    
    public static final DeferredHolder<Attribute, RangedAttribute> STUN_RESISTANCE = ATTRIBUTES.register("stun_resistance", () -> new RangedAttribute("attribute.name.truepower.stun_resistance", 1, Integer.MIN_VALUE, Integer.MAX_VALUE)
    );
}
