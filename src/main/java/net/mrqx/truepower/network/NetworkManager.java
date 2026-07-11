package net.mrqx.truepower.network;

import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;
import net.mrqx.truepower.TruePowerMod;

public class NetworkManager {
    private static final String PROTOCOL_VERSION = "1";
    
    public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
        TruePowerMod.prefix("main"), () -> PROTOCOL_VERSION, PROTOCOL_VERSION::equals,
        PROTOCOL_VERSION::equals);
    
    public static void register() {
        int id = 0;
        INSTANCE.registerMessage(id++, ComboCancelMessage.class, ComboCancelMessage::encode, ComboCancelMessage::decode, ComboCancelMessage::handle);
        INSTANCE.registerMessage(id++, ComboSyncMessage.class, ComboSyncMessage::encode, ComboSyncMessage::decode, ComboSyncMessage::handle);
        INSTANCE.registerMessage(id++, LockOnTargetChangeMessage.class, LockOnTargetChangeMessage::encode, LockOnTargetChangeMessage::decode, LockOnTargetChangeMessage::handle);
    }
}
