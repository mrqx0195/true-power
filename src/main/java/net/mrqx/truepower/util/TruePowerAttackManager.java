package net.mrqx.truepower.util;

import mods.flammpfeil.slashblade.RegistryEvents;
import mods.flammpfeil.slashblade.capability.concentrationrank.CapabilityConcentrationRank;
import mods.flammpfeil.slashblade.capability.slashblade.BladeStateAccess;
import mods.flammpfeil.slashblade.capability.slashblade.ISlashBladeState;
import mods.flammpfeil.slashblade.entity.EntitySlashEffect;
import mods.flammpfeil.slashblade.util.KnockBacks;
import mods.flammpfeil.slashblade.util.VectorHelper;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

public class TruePowerAttackManager {
    public static void doVoidSlashAttack(LivingEntity living, double damage) {
        if (living.level().isClientSide()) {
            return;
        }
        
        Vec3 pos = living.position().add(0.0D, (double) living.getEyeHeight() * 0.75D, 0.0D)
            .add(living.getLookAngle().scale(0.3f));
        
        pos = pos.add(VectorHelper.getVectorForRotation(-90.0F, living.getViewYRot(0)).scale(Vec3.ZERO.y))
            .add(VectorHelper.getVectorForRotation(0, living.getViewYRot(0) + 90).scale(Vec3.ZERO.z))
            .add(living.getLookAngle().scale(Vec3.ZERO.z));
        
        EntitySlashEffect jc = getVoidSlashEffect(living, pos);
        
        jc.setDamage(damage);
        
        jc.setKnockBack(KnockBacks.cancel);
        
        jc.setRank(living.getData(CapabilityConcentrationRank.RANK_POINT).getRankLevel(living.level().getGameTime()));
        
        jc.setLifetime(36);
        
        living.level().addFreshEntity(jc);
    }
    
    private static EntitySlashEffect getVoidSlashEffect(LivingEntity living, Vec3 pos) {
        EntitySlashEffect jc = new EntitySlashEffect(RegistryEvents.SlashEffect, living.level()) {
            
            @Override
            public SoundEvent getSlashSound() {
                return SoundEvents.BLAZE_HURT;
            }
            
            @Override
            protected void tryDespawn() {
                if (!this.level().isClientSide()) {
                    if (this.getLifetime() < this.tickCount) {
                        this.level().playSound(null, this.getX(), this.getY(), this.getZ(),
                            SoundEvents.GLASS_BREAK, SoundSource.PLAYERS, 1.0F,
                            0.625F + 0.1f * this.random.nextFloat());
                        ((ServerLevel) this.level()).sendParticles(ParticleTypes.ENCHANTED_HIT, this.getX(),
                            this.getY(), this.getZ(), 16, 0.7, 0.7, 0.7, 0.02);
                        this.getAlreadyHits().forEach(entity -> {
                            
                            if (entity.isAlive()) {
                                float yRot = this.getOwner() != null ? this.getOwner().getYRot() : 0;
                                entity.addDeltaMovement(new Vec3(
                                    Math.sin(yRot * (float) Math.PI / 180.0F) * 0.5,
                                    0.05D,
                                    -Math.cos(yRot * (float) Math.PI / 180.0F) * 0.5));
                                
                            }
                        });
                        this.remove(RemovalReason.DISCARDED);
                    }
                }
            }
        };
        jc.setPos(pos.x, pos.y, pos.z);
        
        jc.setOwner(living);
        
        jc.setRotationRoll(180);
        jc.setYRot(living.getYRot() - 22.5F);
        jc.setXRot(0);
        
        int colorCode = BladeStateAccess.of(living.getMainHandItem())
            .map(ISlashBladeState::getColorCode).orElse(0xFFFFFF);
        jc.setColor(colorCode);
        
        jc.setMute(false);
        jc.setIsCritical(false);
        
        return jc;
    }
    
    public static Vec3 maybeBackOffFromEdge(Vec3 vec, LivingEntity mover) {
        return maybeBackOffFromEdge(vec, mover, true);
    }
    
    public static Vec3 maybeBackOffFromEdge(Vec3 vec, LivingEntity mover, boolean shouldDownStep) {
        double d0 = vec.x;
        double d1 = vec.z;
        float y = shouldDownStep ? -mover.maxUpStep() : 0;
        
        while (d0 != 0 && mover.level().noCollision(mover,
            mover.getBoundingBox().move(d0, y, 0))) {
            if (d0 < 0.05 && d0 >= -0.05) {
                d0 = 0;
            } else if (d0 > 0) {
                d0 -= 0.05;
            } else {
                d0 += 0.05;
            }
        }
        
        while (d1 != 0 && mover.level().noCollision(mover,
            mover.getBoundingBox().move(0, y, d1))) {
            if (d1 < 0.05 && d1 >= -0.05) {
                d1 = 0;
            } else if (d1 > 0) {
                d1 -= 0.05;
            } else {
                d1 += 0.05;
            }
        }
        
        while (d0 != 0 && d1 != 0 && mover.level().noCollision(mover,
            mover.getBoundingBox().move(d0, y, d1))) {
            if (d0 < 0.05 && d0 >= -0.05) {
                d0 = 0;
            } else if (d0 > 0) {
                d0 -= 0.05;
            } else {
                d0 += 0.05;
            }
            
            if (d1 < 0.05 && d1 >= -0.05) {
                d1 = 0;
            } else if (d1 > 0) {
                d1 -= 0.05;
            } else {
                d1 += 0.05;
            }
        }
        
        vec = new Vec3(d0, vec.y, d1);
        return vec;
    }
}
