package net.mrqx.truepower.mixin;

import mods.flammpfeil.slashblade.capability.slashblade.BladeStateAccess;
import mods.flammpfeil.slashblade.registry.ComboStateRegistry;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Inventory.class)
public abstract class MixinInventory {
    @Shadow
    @Final
    public Player player;
    
    @Shadow
    public abstract ItemStack getSelected();
    
    @Inject(method = "swapPaint(D)V", at = @At("HEAD"), cancellable = true)
    private void onCycleHotbarSlot(double direction, CallbackInfo ci) {
        ItemStack itemStack = this.getSelected();
        if (itemStack.isEmpty()) {
            return;
        }
        BladeStateAccess.of(itemStack).ifPresent(state -> {
            if (!state.resolvCurrentComboState(player).equals(ComboStateRegistry.NONE.getId())) {
                ci.cancel();
            }
        });
    }
}
