package net.mrqx.truepower.mixin;

import mods.flammpfeil.slashblade.SlashBlade;
import mods.flammpfeil.slashblade.capability.concentrationrank.ConcentrationRankCapabilityProvider;
import mods.flammpfeil.slashblade.entity.EntityJudgementCut;
import mods.flammpfeil.slashblade.item.ItemSlashBlade;
import mods.flammpfeil.slashblade.registry.ComboStateRegistry;
import mods.flammpfeil.slashblade.slasharts.JudgementCut;
import mods.flammpfeil.slashblade.util.TargetSelector;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.mrqx.truepower.config.TruePowerCommonConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;

@Mixin(JudgementCut.class)
public abstract class MixinJudgementCut {
    @Inject(method = "doJudgementCut(Lnet/minecraft/world/entity/LivingEntity;)Lmods/flammpfeil/slashblade/entity/EntityJudgementCut;", at = @At("RETURN"), remap = false)
    private static void injectDoJudgementCut(LivingEntity user, CallbackInfoReturnable<EntityJudgementCut> cir) {
        List<LivingEntity> entities = new ArrayList<>(user.level()
            .getNearbyEntities(LivingEntity.class, TargetSelector.lockon, user, user.getBoundingBox().inflate(12.0F, 6.0F, 12.0F)));
        if (!entities.isEmpty()) {
            for (int i = 0; i < TruePowerCommonConfig.JUDGEMENT_CUT_EXTRA_TARGET.get(); i++) {
                if (entities.isEmpty()) {
                    break;
                }
                LivingEntity entity = entities.get(user.getRandom().nextInt(entities.size()));
                entities.remove(entity);
                Vec3 position = entity.position().add(0, entity.getEyeHeight() / 2.0F, 0);
                Level level = entity.level();
                EntityJudgementCut judgementCut = new EntityJudgementCut(SlashBlade.RegistryEvents.JudgementCut, level);
                judgementCut.setPos(position.x, position.y, position.z);
                judgementCut.setOwner(user);
                user.getMainHandItem().getCapability(ItemSlashBlade.BLADESTATE).ifPresent(state -> {
                    judgementCut.setColor(state.getColorCode());
                    if (state.getComboSeq().equals(ComboStateRegistry.JUDGEMENT_CUT_SLASH_JUST.getId())) {
                        judgementCut.setIsCritical(true);
                    }
                });
                user.getCapability(ConcentrationRankCapabilityProvider.RANK_POINT).ifPresent((rank) -> judgementCut.setRank(rank.getRankLevel(level.getGameTime())));
                
                level.addFreshEntity(judgementCut);
                level.playSound(null, judgementCut.getX(), judgementCut.getY(), judgementCut.getZ(), SoundEvents.ENDERMAN_TELEPORT, SoundSource.PLAYERS, 0.5F, 0.8F / (user.getRandom().nextFloat() * 0.4F + 0.8F));
            }
        }
    }
}
