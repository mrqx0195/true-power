package net.mrqx.truepower.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class TruePowerClientConfig {
    public static final ForgeConfigSpec CLIENT_CONFIG;

    public static final ForgeConfigSpec.DoubleValue LOCK_ON_SPEED;
    public static final ForgeConfigSpec.BooleanValue LOCK_FOV;
    public static final ForgeConfigSpec.DoubleValue LOCKED_FOV_MODIFIER;

    static {
        ForgeConfigSpec.Builder clientBuilder = new ForgeConfigSpec.Builder();
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
