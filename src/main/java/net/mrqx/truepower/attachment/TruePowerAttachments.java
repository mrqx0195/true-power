package net.mrqx.truepower.attachment;

import net.minecraft.world.entity.LivingEntity;
import net.mrqx.truepower.TruePowerMod;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.function.Supplier;

public class TruePowerAttachments {
    public static final DeferredRegister<AttachmentType<?>> ATTACHMENTS =
        DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, TruePowerMod.MODID);
    
    public static final Supplier<AttachmentType<TruePowerData>> TRUE_POWER_DATA =
        ATTACHMENTS.register("true_power_data", () -> AttachmentType.serializable(TruePowerData::new).build());
    
    public static final Supplier<AttachmentType<TruePowerStunData>> TRUE_POWER_STUN_DATA =
        ATTACHMENTS.register("true_power_stun_data",
            () -> AttachmentType.serializable(holder -> {
                if (holder instanceof LivingEntity living) {
                    return new TruePowerStunData(living);
                }
                throw new IllegalArgumentException("Invalid attachment holder type");
            }).build());
}
