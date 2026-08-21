package net.mrqx.truepower.config;


import net.neoforged.neoforge.common.ModConfigSpec;

public class TruePowerClientConfig {
    public static final ModConfigSpec CLIENT_CONFIG;
    
    public static final ModConfigSpec.DoubleValue LOCK_ON_SPEED;
    public static final ModConfigSpec.BooleanValue LOCK_FOV;
    public static final ModConfigSpec.DoubleValue LOCKED_FOV_MODIFIER;
    
    public static final ModConfigSpec.BooleanValue OVERRIDE_CAMARA_OFFSET_WHILE_USING_SHOULDER_SURFING;
    public static final ModConfigSpec.DoubleValue SHOULDER_CAMERA_DOWN_OFFSET;
    public static final ModConfigSpec.DoubleValue SHOULDER_CAMERA_MAX_ZOOM_FACTOR;
    public static final ModConfigSpec.DoubleValue SHOULDER_CAMERA_MAX_LOCK_DISTANCE;
    public static final ModConfigSpec.BooleanValue ROTATE_INPUT_WHILE_USING_SHOULDER_SURFING;
    
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
        }
        {
            clientBuilder.pop()
                .push("Compat settings");
            {
                clientBuilder.push("Shoulder Surfing: Reload");
                
                OVERRIDE_CAMARA_OFFSET_WHILE_USING_SHOULDER_SURFING = clientBuilder
                    .comment("Override camera offset while using ShoulderSurfing. (default: false)")
                    .define("override_camara_offset_while_using_shoulder_surfing", false);
                
                SHOULDER_CAMERA_DOWN_OFFSET = clientBuilder
                    .comment("Camera downward offset when facing player front. (default: 0.2)")
                    .defineInRange("shoulder_camera_down_offset", 0.2, Double.MIN_VALUE, Double.MAX_VALUE);
                
                SHOULDER_CAMERA_MAX_ZOOM_FACTOR = clientBuilder
                    .comment("Max zoom multiplier when locked on target is off screen. (default: 3)")
                    .defineInRange("shoulder_camera_max_zoom_factor", 3, 0, Double.MAX_VALUE);
                
                SHOULDER_CAMERA_MAX_LOCK_DISTANCE = clientBuilder
                    .comment("Max distance to maintain lock-on camera behavior. (default: 16)")
                    .defineInRange("shoulder_camera_max_lock_distance", 16, 0, Double.MAX_VALUE);
                
                ROTATE_INPUT_WHILE_USING_SHOULDER_SURFING = clientBuilder
                    .comment("Rotate movement inputs while using ShoulderSurfing. (default: true)")
                    .define("rotate_input_while_using_shoulder_surfing", true);
                
                clientBuilder.pop();
            }
            clientBuilder.pop();
        }
        CLIENT_CONFIG = clientBuilder.build();
    }
}
