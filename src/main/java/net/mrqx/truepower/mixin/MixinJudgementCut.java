package net.mrqx.truepower.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import mods.flammpfeil.slashblade.RegistryEvents;
import mods.flammpfeil.slashblade.capability.concentrationrank.CapabilityConcentrationRank;
import mods.flammpfeil.slashblade.capability.slashblade.BladeStateAccess;
import mods.flammpfeil.slashblade.entity.EntityJudgementCut;
import mods.flammpfeil.slashblade.registry.ComboStateRegistry;
import mods.flammpfeil.slashblade.slasharts.JudgementCut;
import mods.flammpfeil.slashblade.util.AttackManager;
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
    private static void injectDoJudgementCut(LivingEntity user, CallbackInfoReturnable<EntityJudgementCut> cir, @Local(name = "pos") Vec3 pos) {
        if (!AttackManager.isPowered(user)) {
            return;
        }
        List<LivingEntity> entities = new ArrayList<>(user.level()
            .getNearbyEntities(LivingEntity.class, TargetSelector.lockon, user, user.getBoundingBox().inflate(12.0F, 6.0F, 12.0F))
            .stream().filter(livingEntity -> !livingEntity.position().add(0, livingEntity.getEyeHeight(), 0).equals(pos) && !livingEntity.position().add(0, livingEntity.getEyeHeight() / 2, 0).equals(pos))
            .toList());
        if (!entities.isEmpty()) {
            for (int i = 0; i < TruePowerCommonConfig.JUDGEMENT_CUT_EXTRA_TARGET.get(); i++) {
                if (entities.isEmpty()) {
                    break;
                }
                LivingEntity entity = entities.get(user.getRandom().nextInt(entities.size()));
                entities.remove(entity);
                Vec3 position = entity.position().add(0, entity.getEyeHeight() / 2.0F, 0);
                Level level = entity.level();
                EntityJudgementCut judgementCut = new EntityJudgementCut(RegistryEvents.JudgementCut, level);
                judgementCut.setPos(position.x, position.y, position.z);
                judgementCut.setOwner(user);
                BladeStateAccess.of(user.getMainHandItem()).ifPresent(state -> {
                    judgementCut.setColor(state.getColorCode());
                    if (state.getComboSeq().equals(ComboStateRegistry.JUDGEMENT_CUT_SLASH_JUST.getId())) {
                        judgementCut.setIsCritical(true);
                    }
                });
                judgementCut.setRank(entity.getData(CapabilityConcentrationRank.RANK_POINT.get()).getRankLevel(level.getGameTime()));
                
                level.addFreshEntity(judgementCut);
                level.playSound(null, judgementCut.getX(), judgementCut.getY(), judgementCut.getZ(), SoundEvents.ENDERMAN_TELEPORT, SoundSource.PLAYERS, 0.5F, 0.8F / (user.getRandom().nextFloat() * 0.4F + 0.8F));
            }
        }
    }
}
