package net.mrqx.truepower.network;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

@EventBusSubscriber
public class NetworkManager {
    private static final String PROTOCOL_VERSION = "1";
    
    @SubscribeEvent
    public static void onRegisterPayloadHandlersEvent(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar(PROTOCOL_VERSION);
        registrar.playToServer(ComboCancelMessage.TYPE, ComboCancelMessage.STREAM_CODEC, ComboCancelMessage::handle);
        registrar.playToClient(ComboSyncMessage.TYPE, ComboSyncMessage.STREAM_CODEC, ComboSyncMessage::handle);
    }
}
