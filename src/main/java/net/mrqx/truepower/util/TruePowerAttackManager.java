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
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageSources;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.mrqx.truepower.data.TruePowerDamageTypes;

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
        return maybeBackOffFromEdge(vec, mover, false);
    }
    
    /**
     * @see Player#maybeBackOffFromEdge(Vec3, MoverType)
     */
    public static Vec3 maybeBackOffFromEdge(Vec3 vec, LivingEntity mover, boolean shouldDownStep) {
        double d0 = vec.x;
        double d1 = vec.z;
        float y = shouldDownStep ? -mover.maxUpStep() : 0;
        
        double d3 = Math.signum(d0) * 0.05;
        double d4;
        for (d4 = Math.signum(d1) * 0.05; d0 != 0.0 && canFallAtLeast(mover, d0, 0.0, y); d0 -= d3) {
            if (Math.abs(d0) <= 0.05) {
                d0 = 0.0;
                break;
            }
        }
        
        while (d1 != 0.0 && canFallAtLeast(mover, 0.0, d1, y)) {
            if (Math.abs(d1) <= 0.05) {
                d1 = 0.0;
                break;
            }
            
            d1 -= d4;
        }
        
        while (d0 != 0.0 && d1 != 0.0 && canFallAtLeast(mover, d0, d1, y)) {
            if (Math.abs(d0) <= 0.05) {
                d0 = 0.0;
            } else {
                d0 -= d3;
            }
            
            if (Math.abs(d1) <= 0.05) {
                d1 = 0.0;
            } else {
                d1 -= d4;
            }
        }
        
        vec = new Vec3(d0, vec.y, d1);
        return vec;
    }
    
    public static boolean canFallAtLeast(Entity mover, double x, double z, float distance) {
        AABB aabb = mover.getBoundingBox();
        return mover.level()
            .noCollision(
                mover,
                new AABB(
                    aabb.minX + x,
                    aabb.minY - (double) distance - 1.0E-5F,
                    aabb.minZ + z,
                    aabb.maxX + x,
                    aabb.minY,
                    aabb.maxZ + z
                )
            );
    }
    
    public static DamageSource getSummonedSwordDamageSource(DamageSources instance, Entity causingEntity, Entity directEntity) {
        return new DamageSource(instance.damageTypes.getHolderOrThrow(TruePowerDamageTypes.SUMMONED_SWORD), causingEntity, directEntity);
    }
}
