package net.mrqx.truepower;

import net.minecraftforge.common.ForgeConfigSpec;

public class TruePowerModConfig {
    public static final ForgeConfigSpec COMMON_CONFIG;

    public static final ForgeConfigSpec.LongValue POWERED_RANK_REQUIRE;
    public static final ForgeConfigSpec.LongValue RANK_INCREASE_FOR_HIT;
    public static final ForgeConfigSpec.LongValue RANK_INCREASE_FOR_KILL;

    public static final ForgeConfigSpec.BooleanValue CAN_NOT_MOVE_WHILE_COMBO;
    public static final ForgeConfigSpec.LongValue COMBO_TIMEOUT_FOR_RANK_INCREASE;
    public static final ForgeConfigSpec.LongValue COMBO_LIST_LENGTH;
    public static final ForgeConfigSpec.DoubleValue VOID_SLASH_DAMAGE;
    public static final ForgeConfigSpec.DoubleValue POWERED_VOID_SLASH_DAMAGE_FIRST;
    public static final ForgeConfigSpec.DoubleValue POWERED_VOID_SLASH_DAMAGE_SECOND;
    public static final ForgeConfigSpec.IntValue JUDGEMENT_CUT_EXTRA_TARGET;

    public static final ForgeConfigSpec.DoubleValue LOCK_ON_SPEED;
    public static final ForgeConfigSpec.BooleanValue STEP_WHEN_USING_COMBO;

    static {
        ForgeConfigSpec.Builder commonBuilder = new ForgeConfigSpec.Builder();
        commonBuilder.comment("True POWER settings")
                .push("Rank settings")
                .comment("SlashBlade Concentration Ranks:")
                .comment("None: 0 ~ 300")
                .comment("D: 300 ~ 600")
                .comment("C: 600 ~ 900")
                .comment("B: 900 ~ 1200")
                .comment("A: 1200 ~ 1500")
                .comment("S: 1500 ~ 1800")
                .comment("SS: 1800 ~ 2100")
                .comment("SSS: 2100 ~ 2400")
                .comment("");

        POWERED_RANK_REQUIRE = commonBuilder
                .comment("Set the rank required for each attempt to enter the powered state. (default: 2100)")
                .defineInRange("powered_rank_require", 2100, 0, Long.MAX_VALUE);

        RANK_INCREASE_FOR_HIT = commonBuilder
                .comment("Set the rank increase for each hit. (default: 30)")
                .defineInRange("rank_increase_for_hit", 30, 0, Long.MAX_VALUE);

        RANK_INCREASE_FOR_KILL = commonBuilder
                .comment("Set the rank increase for killing each living entity. (default: 200)")
                .defineInRange("rank_increase_for_kill", 200, 0, Long.MAX_VALUE);

        commonBuilder.pop()
                .push("Combo settings");

        CAN_NOT_MOVE_WHILE_COMBO = commonBuilder
                .comment("Can`t move while using some combo. (default: true)")
                .define("can_not_move_while_combo", true);

        COMBO_TIMEOUT_FOR_RANK_INCREASE = commonBuilder
                .comment("Setting combo no longer affects the timeout of rank. (default: 200)")
                .defineInRange("combo_timeout_for_rank_increase", 200, 0, Long.MAX_VALUE);

        COMBO_LIST_LENGTH = commonBuilder
                .comment("Setting the length of combo list. (default: 5)")
                .defineInRange("combo_list_length", 5, 0, Long.MAX_VALUE);

        VOID_SLASH_DAMAGE = commonBuilder
                .comment("Set the damage of Void Slash.(NOT the SA Void Slash!) (default: 2.5)")
                .defineInRange("void_slash_damage", 2.5, 0, Double.MAX_VALUE);

        POWERED_VOID_SLASH_DAMAGE_FIRST = commonBuilder
                .comment("Set the first hit`s damage of Void Slash while player is powered.(NOT the SA Void Slash!) (default: 2.8)")
                .defineInRange("powered_void_slash_damage_first", 2.8, 0, Double.MAX_VALUE);

        POWERED_VOID_SLASH_DAMAGE_SECOND = commonBuilder
                .comment("Set the second hit`s damage of Void Slash while player is powered.(NOT the SA Void Slash!) (default: 1.2)")
                .defineInRange("powered_void_slash_damage_second", 1.2, 0, Double.MAX_VALUE);

        JUDGEMENT_CUT_EXTRA_TARGET = commonBuilder
                .comment("Set the additional number of targets for Judgement Cut while player is powered. (default: 2)")
                .defineInRange("judgement_cut_extra_target", 2, 0, Integer.MAX_VALUE);

        commonBuilder.pop()
                .push("Other settings");

        LOCK_ON_SPEED = commonBuilder
                .comment("Speed of Lock On. (default: 4)")
                .defineInRange("lock_on_speed", 4, 0, Double.MAX_VALUE);

        commonBuilder.pop()
                .push("Experimental settings");

        STEP_WHEN_USING_COMBO = commonBuilder
                .comment("Move forward while using combo. (default: false)")
                .define("step_when_using_combo", false);

        COMMON_CONFIG = commonBuilder.build();
    }
}
