package net.mrqx.truepower.registry;

import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.mrqx.truepower.TruePowerMod;

public final class TruePowerAttributeRegistry {
    public static final DeferredRegister<Attribute> ATTRIBUTES = DeferredRegister.create(ForgeRegistries.ATTRIBUTES, TruePowerMod.MODID);
    
    public static final RegistryObject<Attribute> STUN_RESISTANCE = ATTRIBUTES.register("stun_resistance", () ->
        new RangedAttribute("attribute.name.truepower.stun_resistance", 1, Integer.MIN_VALUE, Integer.MAX_VALUE));
}
