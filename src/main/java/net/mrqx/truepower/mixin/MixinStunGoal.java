package net.mrqx.truepower.mixin;

import mods.flammpfeil.slashblade.entity.ai.StunGoal;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(StunGoal.class)
public abstract class MixinStunGoal extends Goal {
    @Shadow(remap = false)
    @Final
    private PathfinderMob entity;

    @Override
    public boolean isInterruptable() {
        return false;
    }

    @SuppressWarnings("DataFlowIssue")
    @Override
    public void tick() {
        entity.setTarget(null);
        entity.setLastHurtByMob(null);
        entity.setLastHurtMob(null);
        entity.setLastHurtByPlayer(null);
        entity.getBrain().clearMemories();
        super.tick();
    }
}
