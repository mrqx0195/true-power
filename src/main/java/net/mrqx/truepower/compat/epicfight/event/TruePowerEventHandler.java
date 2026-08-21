package net.mrqx.truepower.compat.epicfight.event;

import net.mrqx.truepower.event.TruePowerStunEvent;
import net.neoforged.bus.api.SubscribeEvent;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;
import yesman.epicfight.world.capabilities.entitypatch.HurtableEntityPatch;
import yesman.epicfight.world.damagesource.StunType;

public final class TruePowerEventHandler {
    @SubscribeEvent
    public static void onStunTriggered(TruePowerStunEvent.StunTriggered event) {
        HurtableEntityPatch<?> entityPatch = EpicFightCapabilities.getEntityPatch(event.getEntity(), HurtableEntityPatch.class);
        if (entityPatch != null) {
            entityPatch.setStunReductionOnHit(StunType.HOLD);
            if (!entityPatch.applyStun(StunType.HOLD, event.getStunDuration() / 20f)) {
                event.setCanceled(true);
            }
        }
    }
}
