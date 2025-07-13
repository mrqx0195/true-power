package net.mrqx.truepower.mixin;

import mods.flammpfeil.slashblade.capability.concentrationrank.CapabilityConcentrationRank;
import mods.flammpfeil.slashblade.item.ItemSlashBlade;
import mods.flammpfeil.slashblade.util.AttackManager;
import net.minecraft.world.entity.LivingEntity;
import net.mrqx.truepower.entity.EntityBlastSummonedSword;
import net.mrqx.truepower.util.RankManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;

@Mixin(AttackManager.class)
public abstract class MixinAttackManager {
    @Inject(method = "playQuickSheathSoundAction(Lnet/minecraft/world/entity/LivingEntity;)V", at = @At("HEAD"))
    private static void playQuickSheathSoundAction(LivingEntity entity, CallbackInfo ci) {
        if (entity.level().isClientSide) {
            return;
        }
        entity.getMainHandItem().getCapability(ItemSlashBlade.BLADESTATE).ifPresent(state -> {
            List<EntityBlastSummonedSword> preBlastSwordList = EntityBlastSummonedSword.getPreBlastSwordList(entity);
            List<EntityBlastSummonedSword> preBlastSwordList1 = new ArrayList<>(preBlastSwordList);
            preBlastSwordList1.forEach(e -> {
                if (e.isAlive() && e.getId() >= 0) {
                    e.burst();
                }
            });
            preBlastSwordList.clear();
            if (!entity.level().isClientSide()) {
                entity.getCapability(CapabilityConcentrationRank.RANK_POINT).ifPresent(rank -> {
                    rank.addRankPoint(entity, RankManager.getPreAddRank(entity));
                    RankManager.setPreAddRank(entity, 0);
                });
            }
        });
    }
}
