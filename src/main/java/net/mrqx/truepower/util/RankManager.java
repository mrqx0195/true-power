package net.mrqx.truepower.util;

import com.mojang.datafixers.util.Pair;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.mrqx.truepower.config.TruePowerCommonConfig;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.UUID;

public class RankManager {
    private static final Map<UUID, Long> PRE_ADD_RANK_MAP = new HashMap<>();
    private static final Map<UUID, LinkedList<Pair<Long, ResourceLocation>>> COMBO_LIST_MAP = new HashMap<>();
    private static final Map<UUID, Long> RANK_COOLDOWN_COUNTER = new HashMap<>();
    
    public synchronized static long getPreAddRank(LivingEntity livingEntity) {
        if (!PRE_ADD_RANK_MAP.containsKey(livingEntity.getUUID())) {
            PRE_ADD_RANK_MAP.put(livingEntity.getUUID(), 0L);
        }
        return PRE_ADD_RANK_MAP.get(livingEntity.getUUID());
    }
    
    public synchronized static void setPreAddRank(LivingEntity livingEntity, long rank) {
        PRE_ADD_RANK_MAP.put(livingEntity.getUUID(), rank);
    }
    
    public synchronized static LinkedList<Pair<Long, ResourceLocation>> getComboList(LivingEntity livingEntity) {
        if (!COMBO_LIST_MAP.containsKey(livingEntity.getUUID())) {
            COMBO_LIST_MAP.put(livingEntity.getUUID(), new LinkedList<>());
        }
        return COMBO_LIST_MAP.get(livingEntity.getUUID());
    }
    
    public synchronized static boolean checkCombo(LivingEntity livingEntity, ResourceLocation combo, long timeLimit) {
        cleanTimeoutCombo(livingEntity);
        LinkedList<Pair<Long, ResourceLocation>> comboList = getComboList(livingEntity);
        return comboList.stream().anyMatch(entry
            -> entry.getSecond().equals(combo)
            && entry.getFirst() >= livingEntity.level().getGameTime() - timeLimit);
    }
    
    public synchronized static boolean addCombo(LivingEntity livingEntity, ResourceLocation combo) {
        cleanTimeoutCombo(livingEntity);
        LinkedList<Pair<Long, ResourceLocation>> comboList = getComboList(livingEntity);
        if (checkCombo(livingEntity, combo, TruePowerCommonConfig.COMBO_TIMEOUT_FOR_RANK_INCREASE.get())) {
            return false;
        }
        comboList.addFirst(new Pair<>(livingEntity.level().getGameTime(), combo));
        return true;
    }
    
    public synchronized static void cleanTimeoutCombo(LivingEntity livingEntity) {
        LinkedList<Pair<Long, ResourceLocation>> comboList = getComboList(livingEntity);
        while (!comboList.isEmpty()) {
            Pair<Long, ResourceLocation> entry = comboList.getLast();
            if (comboList.size() > TruePowerCommonConfig.COMBO_LIST_LENGTH.get()
                || entry.getFirst() < livingEntity.level().getGameTime() - TruePowerCommonConfig.COMBO_TIMEOUT_FOR_RANK_INCREASE.get()) {
                comboList.remove(entry);
            } else {
                break;
            }
        }
    }
    
    public synchronized static long getRankCooldown(LivingEntity livingEntity) {
        if (!RANK_COOLDOWN_COUNTER.containsKey(livingEntity.getUUID())) {
            RANK_COOLDOWN_COUNTER.put(livingEntity.getUUID(), 0L);
        }
        return RANK_COOLDOWN_COUNTER.get(livingEntity.getUUID());
    }
    
    public synchronized static void setRankCooldown(LivingEntity livingEntity, long cooldown) {
        RANK_COOLDOWN_COUNTER.put(livingEntity.getUUID(), cooldown);
    }
}
