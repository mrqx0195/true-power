package net.mrqx.truepower.util;

import mods.flammpfeil.slashblade.util.RayTraceHelper;
import mods.flammpfeil.slashblade.util.TargetSelector;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.entity.PartEntity;

import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@OnlyIn(Dist.CLIENT)
public class LockOnUtils {
    @OnlyIn(Dist.CLIENT)
    public static List<Entity> getLockOnEntitySortedList(LivingEntity player) {
        Optional<HitResult> result = RayTraceHelper.rayTrace(player.level(), player, player.getEyePosition(1.0f),
            player.getLookAngle(), 40, 40, e -> true);
        List<Entity> foundEntities = new ArrayList<>();
        
        result.filter(r -> r.getType() == HitResult.Type.ENTITY).filter(r -> {
            EntityHitResult er = (EntityHitResult) r;
            Entity target = er.getEntity();
            
            if (target instanceof PartEntity) {
                target = ((PartEntity<?>) target).getParent();
            }
            
            boolean isMatch = false;
            
            if (target instanceof LivingEntity) {
                isMatch = TargetSelector.lockon.test(player, (LivingEntity) target);
            }
            
            return isMatch;
        }).map(r -> ((EntityHitResult) r).getEntity()).ifPresent(foundEntities::add);
        
        return getLockOnEntitySortedListWithoutRayTrace(player, foundEntities);
    }
    
    @OnlyIn(Dist.CLIENT)
    public static List<Entity> getLockOnEntitySortedListWithoutRayTrace(LivingEntity player, List<Entity> foundEntities) {
        foundEntities.addAll(player.level().getNearbyEntities(LivingEntity.class,
            TargetSelector.lockon, player, player.getBoundingBox().inflate(12.0D, 6.0D, 12.0D)));
        
        Map<Boolean, List<Entity>> collect = foundEntities.stream()
            .sorted(Comparator.comparingDouble(e -> e.distanceToSqr(player)))
            .collect(Collectors.groupingBy(e -> LockOnUtils.isVisible(e, Double.MAX_VALUE)));
        foundEntities.clear();
        foundEntities.addAll(collect.getOrDefault(true, Collections.emptyList()));
        foundEntities.addAll(collect.getOrDefault(false, Collections.emptyList()));
        
        return foundEntities;
    }
    
    @OnlyIn(Dist.CLIENT)
    public static boolean isVisible(Entity entity, double maxDistance) {
        Minecraft minecraft = Minecraft.getInstance();
        Level level = minecraft.level;
        if (level == null || minecraft.player == null) {
            return true;
        }
        Vec3 cameraPos = minecraft.gameRenderer.getMainCamera().getPosition();
        if (entity.distanceToSqr(cameraPos) > maxDistance * maxDistance) {
            return false;
        }
        AABB bb = entity.getBoundingBox();
        if (bb.getXsize() > 50 || bb.getYsize() > 50 || bb.getZsize() > 50) {
            return true;
        }
        float partialTick = minecraft.getTimer().getGameTimeDeltaPartialTick(true);
        if (!isInViewCone(cameraPos, bb, minecraft.player.getViewXRot(partialTick), minecraft.player.getViewYRot(partialTick))) {
            return false;
        }
        return !isOccluded(level, cameraPos, bb);
    }
    
    public static boolean isInViewCone(Vec3 cameraPos, AABB bb, float pitch, float yaw) {
        Vec3 center = bb.getCenter();
        Vec3 toCenter = center.subtract(cameraPos).normalize();
        double pitchRad = Math.toRadians(pitch);
        double yawRad = Math.toRadians(yaw);
        Vec3 lookVec = new Vec3(
            -Math.sin(yawRad) * Math.cos(pitchRad),
            -Math.sin(pitchRad),
            Math.cos(yawRad) * Math.cos(pitchRad));
        
        return toCenter.dot(lookVec) > 0.25;
    }
    
    public static boolean isFullInViewCone(Vec3 cameraPos, AABB bb, float pitch, float yaw) {
        double pitchRad = Math.toRadians(pitch);
        double yawRad = Math.toRadians(yaw);
        Vec3 lookVec = new Vec3(
            -Math.sin(yawRad) * Math.cos(pitchRad),
            -Math.sin(pitchRad),
            Math.cos(yawRad) * Math.cos(pitchRad));
        Vec3[] corners = {
            new Vec3(bb.minX, bb.minY, bb.minZ),
            new Vec3(bb.minX, bb.minY, bb.maxZ),
            new Vec3(bb.minX, bb.maxY, bb.minZ),
            new Vec3(bb.minX, bb.maxY, bb.maxZ),
            new Vec3(bb.maxX, bb.minY, bb.minZ),
            new Vec3(bb.maxX, bb.minY, bb.maxZ),
            new Vec3(bb.maxX, bb.maxY, bb.minZ),
            new Vec3(bb.maxX, bb.maxY, bb.maxZ)
        };
        for (Vec3 corner : corners) {
            if (corner.subtract(cameraPos).normalize().dot(lookVec) <= 0.25) {
                return false;
            }
        }
        return true;
    }
    
    public static boolean isOccluded(Level level, Vec3 cameraPos, AABB bb) {
        Vec3[] targets = {
            bb.getCenter(),
            new Vec3(bb.minX, bb.minY + bb.getYsize() * 0.5, bb.minZ + bb.getZsize() * 0.5),
            new Vec3(bb.maxX, bb.minY + bb.getYsize() * 0.5, bb.minZ + bb.getZsize() * 0.5),
            new Vec3(bb.minX + bb.getXsize() * 0.5, bb.minY + bb.getYsize() * 0.5, bb.minZ),
            new Vec3(bb.minX + bb.getXsize() * 0.5, bb.minY + bb.getYsize() * 0.5, bb.maxZ)
        };
        for (Vec3 target : targets) {
            if (!rayBlocked(level, cameraPos, target)) {
                return false;
            }
        }
        return true;
    }
    
    public static boolean rayBlocked(BlockGetter level, Vec3 from, Vec3 to) {
        Vec3 delta = to.subtract(from);
        double len = delta.length();
        if (len < 0.5) {
            return false;
        }
        Vec3 step = delta.normalize().scale(0.5);
        int maxSteps = (int) Math.ceil(len / 0.5);
        Vec3 pos = from;
        for (int i = 0; i < maxSteps; i++) {
            pos = pos.add(step);
            BlockPos bp = BlockPos.containing(pos);
            if (level.getBlockState(bp).isSolidRender(level, bp)) {
                return true;
            }
        }
        return false;
    }
    
    @Nullable
    public static EntityHitResult advanceRayTrace(Level worldIn, Entity entityIn, Vec3 start, Vec3 end, AABB boundingBox, Predicate<Entity> selector) {
        double currentDist = Double.MAX_VALUE;
        Entity resultEntity = null;
        
        for (Entity foundEntity : worldIn.getEntities(entityIn, boundingBox, selector)) {
            double distance = Math.max(foundEntity.position().distanceTo(start), 14.14);
            AABB aabb = foundEntity.getBoundingBox().inflate(distance * distance / 400);
            Optional<Vec3> optional = aabb.clip(start, end);
            if (optional.isPresent()) {
                double newDist = start.distanceToSqr(optional.get());
                if (newDist < currentDist) {
                    resultEntity = foundEntity;
                    currentDist = newDist;
                }
            }
        }
        
        if (resultEntity == null) {
            return null;
        } else {
            return new EntityHitResult(resultEntity);
        }
    }
    
    public static Vec3 getEntityCenterPosition(Entity entity, float partialTick) {
        Vec3 position = entity.getPosition(partialTick);
        Vec3 eyePosition = entity.getEyePosition(partialTick);
        return new Vec3((position.x() + eyePosition.x()) / 2, (position.y() + eyePosition.y()) / 2, (position.z() + eyePosition.z()) / 2);
    }
    
    public static AABB scaleAABB(AABB playerBB, double scale) {
        return playerBB.inflate(playerBB.getXsize() * scale, playerBB.getYsize() * scale, playerBB.getZsize() * scale);
    }
}
