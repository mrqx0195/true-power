package net.mrqx.truepower.event;

import mods.flammpfeil.slashblade.capability.slashblade.ISlashBladeState;
import mods.flammpfeil.slashblade.event.SlashBladeEvent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.Cancelable;

@Cancelable
public class ComboCancelEvent extends SlashBladeEvent {
    private final ServerPlayer serverPlayer;
    private final boolean isJump;

    public ComboCancelEvent(ItemStack blade, ISlashBladeState state, ServerPlayer serverPlayer, boolean isJump) {
        super(blade, state);
        this.serverPlayer = serverPlayer;
        this.isJump = isJump;
    }

    public ServerPlayer getServerPlayer() {
        return serverPlayer;
    }

    public boolean isJump() {
        return isJump;
    }
}
