package net.mrqx.truepower.util;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.mrqx.truepower.TruePowerModConfig;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.UUID;

public class RankManager {
    private static final Map<UUID, Long> PRE_ADD_RANK_MAP = new HashMap<>();
    private static final Map<UUID, LinkedList<Map.Entry<Long, ResourceLocation>>> COMBO_LIST_MAP = new HashMap<>();
    private static final Map<UUID, Long> RANK_COOLDOWN_COUNTER = new HashMap<>();

    public static long getPreAddRank(LivingEntity livingEntity) {
        if (!PRE_ADD_RANK_MAP.containsKey(livingEntity.getUUID())) {
            PRE_ADD_RANK_MAP.put(livingEntity.getUUID(), 0L);
        }
        return PRE_ADD_RANK_MAP.get(livingEntity.getUUID());
    }

    public static void setPreAddRank(LivingEntity livingEntity, long rank) {
        PRE_ADD_RANK_MAP.put(livingEntity.getUUID(), rank);
    }

    public static LinkedList<Map.Entry<Long, ResourceLocation>> getComboList(LivingEntity livingEntity) {
        if (!COMBO_LIST_MAP.containsKey(livingEntity.getUUID())) {
            COMBO_LIST_MAP.put(livingEntity.getUUID(), new LinkedList<>());
        }
        return COMBO_LIST_MAP.get(livingEntity.getUUID());
    }

    public static boolean checkCombo(LivingEntity livingEntity, ResourceLocation combo, long timeLimit) {
        cleanTimeoutCombo(livingEntity);
        LinkedList<Map.Entry<Long, ResourceLocation>> comboList = getComboList(livingEntity);
        return comboList.stream().anyMatch(entry
                -> entry.getValue().equals(combo)
                && entry.getKey() >= livingEntity.level().getGameTime() - timeLimit);
    }

    public static boolean addCombo(LivingEntity livingEntity, ResourceLocation combo) {
        cleanTimeoutCombo(livingEntity);
        LinkedList<Map.Entry<Long, ResourceLocation>> comboList = getComboList(livingEntity);
        if (checkCombo(livingEntity, combo, TruePowerModConfig.COMBO_TIMEOUT_FOR_RANK_INCREASE.get())) {
            return false;
        }
        comboList.addFirst(new ComboEntry(livingEntity.level().getGameTime(), combo));
        return true;
    }

    public static void cleanTimeoutCombo(LivingEntity livingEntity) {
        LinkedList<Map.Entry<Long, ResourceLocation>> comboList = getComboList(livingEntity);
        while (!comboList.isEmpty()) {
            Map.Entry<Long, ResourceLocation> entry = comboList.getLast();
            if (comboList.size() > TruePowerModConfig.COMBO_LIST_LENGTH.get()
                    || entry.getKey() < livingEntity.level().getGameTime() - TruePowerModConfig.COMBO_TIMEOUT_FOR_RANK_INCREASE.get()) {
                comboList.removeLast();
            } else {
                break;
            }
        }
    }

    public static long getRankCooldown(LivingEntity livingEntity) {
        if (!RANK_COOLDOWN_COUNTER.containsKey(livingEntity.getUUID())) {
            RANK_COOLDOWN_COUNTER.put(livingEntity.getUUID(), 0L);
        }
        return RANK_COOLDOWN_COUNTER.get(livingEntity.getUUID());
    }

    public static void setRankCooldown(LivingEntity livingEntity, long cooldown) {
        RANK_COOLDOWN_COUNTER.put(livingEntity.getUUID(), cooldown);
    }

    public static class ComboEntry implements Map.Entry<Long, ResourceLocation> {
        private final long key;
        private final ResourceLocation combo;

        public ComboEntry(long time, ResourceLocation combo) {
            this.key = time;
            this.combo = combo;
        }

        @Override
        public Long getKey() {
            return key;
        }

        @Override
        public ResourceLocation getValue() {
            return combo;
        }

        @Override
        public ResourceLocation setValue(ResourceLocation value) {
            throw new UnsupportedOperationException();
        }
    }
}
