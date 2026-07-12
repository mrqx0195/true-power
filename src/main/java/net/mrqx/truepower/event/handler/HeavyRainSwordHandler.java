package net.mrqx.truepower.event.handler;

import mods.flammpfeil.slashblade.SlashBladeConfig;
import mods.flammpfeil.slashblade.capability.concentrationrank.CapabilityConcentrationRank;
import mods.flammpfeil.slashblade.capability.slashblade.BladeStateAccess;
import mods.flammpfeil.slashblade.capability.slashblade.ISlashBladeState;
import mods.flammpfeil.slashblade.event.handler.InputCommandEvent;
import mods.flammpfeil.slashblade.item.SwordType;
import mods.flammpfeil.slashblade.util.InputCommand;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.mrqx.sbr_core.utils.InputStream;
import net.mrqx.sbr_core.utils.MrqxSummonedSwordArts;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;

import java.util.EnumSet;
import java.util.LinkedList;
import java.util.Optional;

@EventBusSubscriber
public final class HeavyRainSwordHandler {
    private static final LinkedList<InputStream.TimeLineKeyInput> HEAVY_RAIN_SWORD_TIME_LINE = new LinkedList<>();
    
    static {
        HEAVY_RAIN_SWORD_TIME_LINE.add(new InputStream.TimeLineKeyInput(5, -2, InputCommand.FORWARD, EnumSet.noneOf(InputCommand.class), InputStream.InputType.START));
        HEAVY_RAIN_SWORD_TIME_LINE.add(new InputStream.TimeLineKeyInput(7, -2, InputCommand.BACK, EnumSet.noneOf(InputCommand.class), InputStream.InputType.START));
    }
    
    
    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void heavyRainSword(InputCommandEvent event) {
        EnumSet<InputCommand> old = event.getOld();
        EnumSet<InputCommand> current = event.getCurrent();
        ServerPlayer entity = event.getEntity();
        ItemStack blade = entity.getMainHandItem();
        Optional<ISlashBladeState> bladeStateOptional = BladeStateAccess.of(blade);
        if (bladeStateOptional.isEmpty()) {
            return;
        }
        ISlashBladeState bladeState = bladeStateOptional.get();
        final int powerLevel = blade.getEnchantmentLevel(entity.registryAccess().holderOrThrow(Enchantments.POWER));
        
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
        int rank = entity.getData(CapabilityConcentrationRank.RANK_POINT).getRank(worldIn.getGameTime()).level;
        int count = 9 + Math.min(rank - 1, 0);
        
        Entity target = bladeState.getTargetEntity(worldIn);
        if (bladeState.getProudSoulCount() < SlashBladeConfig.SUMMON_SWORD_ART_COST.get()) {
            return;
        }
        bladeState.setProudSoulCount(bladeState.getProudSoulCount() - SlashBladeConfig.SUMMON_SWORD_ART_COST.get());
        
        if (target != null) {
            MrqxSummonedSwordArts.HEAVY_RAIN_SWORD.accept(entity, target, (double) powerLevel, count);
        }
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
