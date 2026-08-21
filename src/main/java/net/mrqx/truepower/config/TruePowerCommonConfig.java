package net.mrqx.truepower.config;


import net.mrqx.truepower.util.CollideAction;
import net.neoforged.neoforge.common.ModConfigSpec;

public class TruePowerCommonConfig {
    public static final ModConfigSpec COMMON_CONFIG;
    
    public static final ModConfigSpec.LongValue POWERED_RANK_REQUIRE;
    public static final ModConfigSpec.LongValue RANK_INCREASE_FOR_HIT;
    public static final ModConfigSpec.LongValue RANK_INCREASE_FOR_KILL;
    
    public static final ModConfigSpec.BooleanValue CAN_NOT_MOVE_WHILE_COMBO;
    public static final ModConfigSpec.LongValue COMBO_TIMEOUT_FOR_RANK_INCREASE;
    public static final ModConfigSpec.LongValue COMBO_LIST_LENGTH;
    public static final ModConfigSpec.DoubleValue VOID_SLASH_DAMAGE;
    public static final ModConfigSpec.DoubleValue POWERED_VOID_SLASH_DAMAGE_FIRST;
    public static final ModConfigSpec.DoubleValue POWERED_VOID_SLASH_DAMAGE_SECOND;
    public static final ModConfigSpec.IntValue JUDGEMENT_CUT_EXTRA_TARGET;
    public static final ModConfigSpec.BooleanValue BLADE_ARTS_NEED_SHIFT;
    public static final ModConfigSpec.BooleanValue ENABLE_PRE_INPUT;
    
    public static final ModConfigSpec.BooleanValue MODIFY_SUMMONED_SWORD_DAMAGE;
    public static final ModConfigSpec.DoubleValue SUMMONED_SWORD_DAMAGE_MULTIPLIER;
    
    public static final ModConfigSpec.BooleanValue EASY_TRICK_DOWN;
    
    public static final ModConfigSpec.EnumValue<CollideAction> COLLIDE_ACTION;
    public static final ModConfigSpec.BooleanValue STEP_WHEN_USING_COMBO;
    public static final ModConfigSpec.BooleanValue ENABLE_STUN_VALUE;
    public static final ModConfigSpec.BooleanValue POWERFUL_STUN;
    
    static {
        ModConfigSpec.Builder commonBuilder = new ModConfigSpec.Builder();
        commonBuilder.comment("True POWER common settings");
        {
            commonBuilder.push("Rank settings")
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
        }
        {
            commonBuilder.pop()
                .push("Combo settings");
            
            CAN_NOT_MOVE_WHILE_COMBO = commonBuilder
                .comment("Can't move while using some combo. (default: true)")
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
                .comment("Set the first hit's damage of Void Slash while player is powered.(NOT the SA Void Slash!) (default: 2.8)")
                .defineInRange("powered_void_slash_damage_first", 2.8, 0, Double.MAX_VALUE);
            
            POWERED_VOID_SLASH_DAMAGE_SECOND = commonBuilder
                .comment("Set the second hit's damage of Void Slash while player is powered.(NOT the SA Void Slash!) (default: 1.2)")
                .defineInRange("powered_void_slash_damage_second", 1.2, 0, Double.MAX_VALUE);
            
            JUDGEMENT_CUT_EXTRA_TARGET = commonBuilder
                .comment("Set the additional number of targets for Judgement Cut while player is powered. (default: 2)")
                .defineInRange("judgement_cut_extra_target", 2, 0, Integer.MAX_VALUE);
            
            BLADE_ARTS_NEED_SHIFT = commonBuilder
                .comment("If it is set to false, some Blade Arts that require shift (such as Rapid Slash) will no longer require holding down shift. (default: false)")
                .define("blade_arts_need_shift", false);
            
            ENABLE_PRE_INPUT = commonBuilder
                .comment("Enable TruePower's pre-input (or Input Buffering) system . (default: true)")
                .define("enable_pre_input", true);
        }
        {
            commonBuilder.pop()
                .push("Summoned Sword settings");
            
            MODIFY_SUMMONED_SWORD_DAMAGE = commonBuilder
                .comment("Modify the damage of summoned swords. This will cause the damage of Summoned Sword to be affected by the player's own attack damage. (default: true)")
                .define("modify_summoned_sword_damage", true);
            
            SUMMONED_SWORD_DAMAGE_MULTIPLIER = commonBuilder
                .comment("Damage multiplier for summoned swords. (default: 0.01)")
                .defineInRange("summoned_sword_damage_multiplier", 0.01, 0, Double.MAX_VALUE);
        }
        {
            commonBuilder.pop()
                .push("Trick settings");
            
            EASY_TRICK_DOWN = commonBuilder
                .comment("Make trick down easier. (default: false)")
                .define("easy_trick_down", false);
        }
        {
            commonBuilder.pop()
                .push("Misc settings");
            
            COLLIDE_ACTION = commonBuilder
                .comment("Collide action while using slashblade. (default: SOLID)")
                .defineEnum("collide_action", CollideAction.SOLID);
            
            STEP_WHEN_USING_COMBO = commonBuilder
                .comment("Move forward while using combo. (default: true)")
                .define("step_when_using_combo", true);
            
            ENABLE_STUN_VALUE = commonBuilder
                .comment("Enable TruePower's custom stun value system. (default: true)")
                .define("enable_stun_value", true);
            
            POWERFUL_STUN = commonBuilder
                .comment("Make stun powerful. (default: true)")
                .define("powerful_stun", true);
        }
        commonBuilder.pop();
        COMMON_CONFIG = commonBuilder.build();
    }
}
