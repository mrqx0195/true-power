package net.mrqx.truepower.entity;

import mods.flammpfeil.slashblade.ability.StunManager;
import mods.flammpfeil.slashblade.entity.EntityAbstractSummonedSword;
import mods.flammpfeil.slashblade.entity.Projectile;
import mods.flammpfeil.slashblade.item.ItemSlashBlade;
import mods.flammpfeil.slashblade.util.TargetSelector;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.PlayMessages;
import net.mrqx.truepower.TruePowerMod;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class EntityBlastSummonedSword extends EntityAbstractSummonedSword {
    private static final Map<UUID, List<LivingEntity>> PRE_SUMMON_MAP = new HashMap<>();
    private static final Map<UUID, List<EntityBlastSummonedSword>> PRE_BLAST_SWORD = new HashMap<>();

    public static List<LivingEntity> getPreSummonSwordList(LivingEntity owner) {
        if (!PRE_SUMMON_MAP.containsKey(owner.getUUID())) {
            PRE_SUMMON_MAP.put(owner.getUUID(), new ArrayList<>());
        }
        return PRE_SUMMON_MAP.get(owner.getUUID());
    }

    public static List<EntityBlastSummonedSword> getPreBlastSwordList(LivingEntity owner) {
        if (!PRE_BLAST_SWORD.containsKey(owner.getUUID())) {
            PRE_BLAST_SWORD.put(owner.getUUID(), new ArrayList<>());
        }
        return PRE_BLAST_SWORD.get(owner.getUUID());
    }

    public static void setPreBlastSwordList(LivingEntity owner, final int count) {
        List<EntityBlastSummonedSword> preBlastSwordList = getPreBlastSwordList(owner);
        owner.getMainHandItem().getCapability(ItemSlashBlade.BLADESTATE).ifPresent(state -> {
            int powerLevel = owner.getMainHandItem().getEnchantmentLevel(Enchantments.POWER_ARROWS);
            List<LivingEntity> preSummonSwordList = getPreSummonSwordList(owner);
            List<LivingEntity> summonList = new ArrayList<>(preSummonSwordList);
            summonList.forEach(target -> {
                Level level = target.level();
                Vec3 targetPos = target.getEyePosition();
                float radius = (count > 1) ? (count - 1) / 5.0f : 0.0f;
                for (int i = 0; i < count; i++) {
                    EntityBlastSummonedSword summonedSword = new EntityBlastSummonedSword(TruePowerMod.RegistryEvents.BlastSummonedSword, level);
                    preBlastSwordList.add(summonedSword);
                    double angle = i * (2 * Math.PI / count);
                    double x = targetPos.x + radius * Math.cos(angle);
                    double z = targetPos.z + radius * Math.sin(angle);
                    Vec3 pos = new Vec3(x, targetPos.y + 3, z);
                    summonedSword.setPos(pos);
                    summonedSword.setDamage(powerLevel / 5.0);
                    summonedSword.setBurstDamage(powerLevel / 2.0);

                    Vec3 dir = targetPos.subtract(pos).normalize();
                    summonedSword.shoot(dir.x, dir.y, dir.z, 3.0F, 0.0F);
                    summonedSword.setOwner(owner);
                    summonedSword.setColor(state.getColorCode());
                    level.addFreshEntity(summonedSword);
                    if (owner instanceof ServerPlayer serverPlayer) {
                        serverPlayer.playNotifySound(SoundEvents.CHORUS_FRUIT_TELEPORT, SoundSource.PLAYERS, 0.2F, 1.45F);
                    }
                }
            });
            preSummonSwordList.clear();
        });
    }


    private double burstDamage;

    public EntityBlastSummonedSword(EntityType<? extends Projectile> entityTypeIn, Level worldIn) {
        super(entityTypeIn, worldIn);
    }

    public void setBurstDamage(double damageIn) {
        this.burstDamage = damageIn;
    }

    public double getBurstDamage() {
        return this.burstDamage;
    }

    public static EntityBlastSummonedSword createInstance(PlayMessages.SpawnEntity packet, Level worldIn) {
        return new EntityBlastSummonedSword(TruePowerMod.RegistryEvents.BlastSummonedSword, worldIn);
    }

    @Override
    public void burst(List<MobEffectInstance> effects, @Nullable Entity focusEntity) {
        List<Entity> list = TargetSelector.getTargettableEntitiesWithinAABB(this.level(), 2.0F, this);
        this.setDamage(this.getBurstDamage());
        list.stream().filter((e) -> e instanceof LivingEntity).map((e) -> (LivingEntity) e).forEach(livingEntity -> {
            StunManager.setStun(livingEntity);
            this.doForceHitEntity(livingEntity);
            livingEntity.setDeltaMovement(0, livingEntity.getDeltaMovement().y >= 1 ? livingEntity.getDeltaMovement().y + 0.05 : 1, 0);
        });
        super.burst(effects, focusEntity);
    }

    @Override
    public float getOffsetYaw() {
        return this.getYRot();
    }
}
