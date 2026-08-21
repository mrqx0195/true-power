package net.mrqx.truepower.compat.shouldersurfing;

import com.github.exopandora.shouldersurfing.api.event.IEventBus;
import com.github.exopandora.shouldersurfing.api.plugin.IShoulderSurfingPlugin;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@SuppressWarnings("unused")
@OnlyIn(Dist.CLIENT)
public class TruePowerShoulderSurfingPlugin implements IShoulderSurfingPlugin {
    @Override
    public void register(IEventBus eventBus) {
        eventBus.register(ShoulderSurfingEventHandler::onTickEvent);
        eventBus.register(ShoulderSurfingEventHandler::onComputeCameraCouplingEvent);
        eventBus.register(ShoulderSurfingEventHandler::onForceVanillaPlayerInputEvent);
        eventBus.register(ShoulderSurfingEventHandler::onComputeTargetCameraOffsetEvent);
        eventBus.register(ShoulderSurfingEventHandler::onComputePlayerAimStateEvent);
        eventBus.register(ShoulderSurfingEventHandler::onComputePlayerUseItemStateEvent);
        eventBus.register(ShoulderSurfingEventHandler::onComputePlayerInteractionStateEvent);
        eventBus.register(ShoulderSurfingEventHandler::onComputePlayerAttackStateEvent);
        eventBus.register(ShoulderSurfingEventHandler::onComputePlayerPickStateEvent);
    }
}
