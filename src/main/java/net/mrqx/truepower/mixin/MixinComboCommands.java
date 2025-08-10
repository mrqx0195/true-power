package net.mrqx.truepower.mixin;

import mods.flammpfeil.slashblade.registry.combo.ComboCommands;
import mods.flammpfeil.slashblade.util.InputCommand;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.mrqx.truepower.event.handler.combo.VoidSlashHandler;
import net.mrqx.truepower.registry.TruePowerComboStateRegistry;
import net.mrqx.truepower.util.JustSlashArtManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.EnumSet;
import java.util.Map;

@Mixin(ComboCommands.class)
public abstract class MixinComboCommands {
    @Inject(method = "initStandByCommand(Lnet/minecraft/world/entity/LivingEntity;Ljava/util/Map;)Lnet/minecraft/resources/ResourceLocation;",
            at = @At(value = "RETURN"),
            remap = false,
            locals = LocalCapture.CAPTURE_FAILHARD,
            cancellable = true)
    private static void injectInitStandByCommand(LivingEntity a, Map<EnumSet<InputCommand>, ResourceLocation> map, CallbackInfoReturnable<ResourceLocation> cir, EnumSet<InputCommand> commands) {
        if (VoidSlashHandler.doVoidSlash(a, commands)) {
            JustSlashArtManager.resetJustCount(a);
            cir.setReturnValue(TruePowerComboStateRegistry.VOID_SLASH.getId());
        }
    }
}
