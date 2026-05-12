package net.mrqx.truepower.config;


import net.neoforged.neoforge.common.ModConfigSpec;

public class TruePowerClientConfig {
    public static final ModConfigSpec CLIENT_CONFIG;
    
    public static final ModConfigSpec.DoubleValue LOCK_ON_SPEED;
    public static final ModConfigSpec.BooleanValue LOCK_FOV;
    public static final ModConfigSpec.DoubleValue LOCKED_FOV_MODIFIER;
    
    static {
        ModConfigSpec.Builder clientBuilder = new ModConfigSpec.Builder();
        clientBuilder.comment("True POWER client settings");
        {
            clientBuilder.push("Camera settings");
            
            LOCK_ON_SPEED = clientBuilder
                .comment("Speed of Lock On. (default: 4)")
                .defineInRange("lock_on_speed", 4, 0, Double.MAX_VALUE);
            
            LOCK_FOV = clientBuilder
                .comment("Lock player`s field of vision (FOV). (default: true)")
                .define("lock_fov", true);
            
            LOCKED_FOV_MODIFIER = clientBuilder
                .comment("FOV of Lock On. (default: 4)")
                .defineInRange("locked_fov_modifier", 1, 0, Float.MAX_VALUE);
            
            clientBuilder.pop();
        }
        CLIENT_CONFIG = clientBuilder.build();
    }
}
