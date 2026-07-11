package net.mrqx.truepower.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import mods.flammpfeil.slashblade.capability.slashblade.ISlashBladeState;
import mods.flammpfeil.slashblade.item.ItemSlashBlade;
import mods.flammpfeil.slashblade.registry.ComboStateRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.util.LazyOptional;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

import javax.annotation.Nullable;

@Mixin(Minecraft.class)
public abstract class MixinMinecraft {
    @Shadow
    @Nullable
    public LocalPlayer player;
    
    @WrapOperation(method = "handleKeybinds()V", at = @At(value = "FIELD", target = "Lnet/minecraft/world/entity/player/Inventory;selected:I", opcode = Opcodes.PUTFIELD))
    private void wrapSetSelected(Inventory instance, int value, Operation<Void> original) {
        if (!(player == null)) {
            ItemStack itemStack = player.getMainHandItem();
            LazyOptional<ISlashBladeState> optional = itemStack.getCapability(ItemSlashBlade.BLADESTATE);
            if (!itemStack.isEmpty() && optional.isPresent()
                && optional.filter(state -> state.resolvCurrentComboState(player).equals(ComboStateRegistry.NONE.getId())).isEmpty()) {
                return;
            }
        }
        original.call(instance, value);
    }
}
