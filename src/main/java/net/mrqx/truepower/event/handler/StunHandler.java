//package net.mrqx.truepower.event.handler;
//
//import mods.flammpfeil.slashblade.capability.mobeffect.CapabilityMobEffect;
//import net.minecraft.world.entity.LivingEntity;
//import net.minecraft.world.entity.PathfinderMob;
//import net.minecraftforge.event.entity.living.LivingEvent;
//import net.minecraftforge.eventbus.api.SubscribeEvent;
//import net.minecraftforge.fml.common.Mod;
//
//@Mod.EventBusSubscriber
//public class StunHandler {
//    @SubscribeEvent
//    public static void onEntityLivingUpdate(LivingEvent.LivingTickEvent event) {
//        LivingEntity target = event.getEntity();
//        if (target instanceof PathfinderMob mob) {
//            boolean onStun = mob.getCapability(CapabilityMobEffect.MOB_EFFECT).filter((state) -> state.isStun(target.level().getGameTime())).isPresent();
//            if (onStun) {
//                mob.setTarget(null);
//                mob.setLastHurtByMob(null);
//                mob.setLastHurtMob(null);
//                mob.setLastHurtByPlayer(null);
//                mob.getBrain().clearMemories();
//            }
//        }
//    }
//}
