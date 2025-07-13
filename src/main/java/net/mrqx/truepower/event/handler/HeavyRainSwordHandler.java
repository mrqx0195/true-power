package net.mrqx.truepower.event.handler;

import mods.flammpfeil.slashblade.SlashBladeConfig;
import mods.flammpfeil.slashblade.capability.concentrationrank.CapabilityConcentrationRank;
import mods.flammpfeil.slashblade.capability.slashblade.ISlashBladeState;
import mods.flammpfeil.slashblade.capability.slashblade.SlashBladeState;
import mods.flammpfeil.slashblade.event.handler.InputCommandEvent;
import mods.flammpfeil.slashblade.item.ItemSlashBlade;
import mods.flammpfeil.slashblade.item.SwordType;
import mods.flammpfeil.slashblade.util.InputCommand;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.mrqx.sbr_core.utils.InputStream;
import net.mrqx.sbr_core.utils.MrqxSummonedSwordArts;

import java.util.EnumSet;
import java.util.LinkedList;

@Mod.EventBusSubscriber
public class HeavyRainSwordHandler {
    private static final LinkedList<InputStream.TimeLineKeyInput> HEAVY_RAIN_SWORD_TIME_LINE = new LinkedList<>();

    static {
        HEAVY_RAIN_SWORD_TIME_LINE.add(new InputStream.TimeLineKeyInput(3, 0, InputCommand.FORWARD, EnumSet.noneOf(InputCommand.class), InputStream.InputType.START));
        HEAVY_RAIN_SWORD_TIME_LINE.add(new InputStream.TimeLineKeyInput(3, 0, InputCommand.BACK, EnumSet.noneOf(InputCommand.class), InputStream.InputType.START));
    }


    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void heavyRainSword(InputCommandEvent event) {
        EnumSet<InputCommand> old = event.getOld();
        EnumSet<InputCommand> current = event.getCurrent();
        ServerPlayer entity = event.getEntity();
        ItemStack blade = entity.getMainHandItem();
        ISlashBladeState bladeState = blade.getCapability(ItemSlashBlade.BLADESTATE).orElse(new SlashBladeState(blade));
        final int powerLevel = blade.getEnchantmentLevel(Enchantments.POWER_ARROWS);

        InputStream inputStream = InputStream.getOrCreateInputStream(entity);

        if (bladeState.isBroken()
                || bladeState.isSealed()
                || !SwordType.from(blade).contains(SwordType.BEWITCHED)) {
            return;
        }

        boolean onDown = !old.contains(InputCommand.M_DOWN) && current.contains(InputCommand.M_DOWN)
                && inputStream.checkTimeLineInput(HEAVY_RAIN_SWORD_TIME_LINE);

        if (!onDown) {
            return;
        }
        Level worldIn = entity.level();
        int rank = entity.getCapability(CapabilityConcentrationRank.RANK_POINT)
                .map(r -> r.getRank(worldIn.getGameTime()).level).orElse(0);
        int count = 9 + Math.min(rank - 1, 0);

        entity.getMainHandItem().getCapability(ItemSlashBlade.BLADESTATE).ifPresent((state) -> {

            Entity target = state.getTargetEntity(worldIn);
            if (state.getProudSoulCount() < SlashBladeConfig.SUMMON_SWORD_ART_COST.get()) {
                return;
            }
            state.setProudSoulCount(state.getProudSoulCount() - SlashBladeConfig.SUMMON_SWORD_ART_COST.get());

            MrqxSummonedSwordArts.HEAVY_RAIN_SWORD.accept(entity, target, (double) powerLevel, count);
        });

    }

    private static Vec3 calculateViewVector(float x, float y) {
        float f = x * ((float) Math.PI / 180F);
        float f1 = -y * ((float) Math.PI / 180F);
        float f2 = Mth.cos(f1);
        float f3 = Mth.sin(f1);
        float f4 = Mth.cos(f);
        float f5 = Mth.sin(f);
        return new Vec3(f3 * f4, -f5, f2 * f4);
    }
}
