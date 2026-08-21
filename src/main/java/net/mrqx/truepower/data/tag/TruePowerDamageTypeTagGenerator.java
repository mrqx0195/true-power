package net.mrqx.truepower.data.tag;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageType;
import net.mrqx.truepower.TruePowerMod;
import net.mrqx.truepower.data.TruePowerDamageTypes;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import javax.annotation.Nullable;
import java.util.concurrent.CompletableFuture;

public final class TruePowerDamageTypeTagGenerator extends TagsProvider<DamageType> {
    public TruePowerDamageTypeTagGenerator(PackOutput pOutput, CompletableFuture<HolderLookup.Provider> pLookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(pOutput, Registries.DAMAGE_TYPE, pLookupProvider, TruePowerMod.MODID, existingFileHelper);
    }
    
    @Override
    protected void addTags(HolderLookup.Provider pProvider) {
        this.tag(DamageTypeTags.BYPASSES_ARMOR)
            .add(TruePowerDamageTypes.SUMMONED_SWORD);
        
        this.tag(DamageTypeTags.BYPASSES_WOLF_ARMOR)
            .add(TruePowerDamageTypes.SUMMONED_SWORD);
        
        this.tag(DamageTypeTags.PANIC_CAUSES)
            .add(TruePowerDamageTypes.SUMMONED_SWORD);
        
        this.tag(DamageTypeTags.NO_KNOCKBACK)
            .add(TruePowerDamageTypes.SUMMONED_SWORD);
        
        this.tag(DamageTypeTags.BYPASSES_COOLDOWN)
            .add(TruePowerDamageTypes.SUMMONED_SWORD);
    }
}
