package net.mrqx.truepower.network;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;
import net.mrqx.truepower.TruePowerMod;

public class NetworkManager {

    private static final String PROTOCOL_VERSION = "1";

    public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(TruePowerMod.MODID, "main"), () -> PROTOCOL_VERSION, PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals);

    public static void register() {
        INSTANCE.registerMessage(0, ComboCancelMessage.class, ComboCancelMessage::encode, ComboCancelMessage::decode, ComboCancelMessage::handle);
        INSTANCE.registerMessage(1, ComboSyncMessage.class, ComboSyncMessage::encode, ComboSyncMessage::decode, ComboSyncMessage::handle);
    }
}
