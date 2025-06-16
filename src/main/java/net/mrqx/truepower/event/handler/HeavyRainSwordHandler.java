package net.mrqx.truepower.event.handler;

import mods.flammpfeil.slashblade.SlashBlade;
import mods.flammpfeil.slashblade.SlashBladeConfig;
import mods.flammpfeil.slashblade.ability.SummonedSwordArts;
import mods.flammpfeil.slashblade.capability.concentrationrank.CapabilityConcentrationRank;
import mods.flammpfeil.slashblade.capability.slashblade.ISlashBladeState;
import mods.flammpfeil.slashblade.capability.slashblade.SlashBladeState;
import mods.flammpfeil.slashblade.entity.EntityHeavyRainSwords;
import mods.flammpfeil.slashblade.event.InputCommandEvent;
import mods.flammpfeil.slashblade.item.ItemSlashBlade;
import mods.flammpfeil.slashblade.item.SwordType;
import mods.flammpfeil.slashblade.util.AdvancementHelper;
import mods.flammpfeil.slashblade.util.InputCommand;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
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
        
        entity.getMainHandItem().getCapability(ItemSlashBlade.BLADESTATE).ifPresent((state) -> {

            Level worldIn = entity.level();
            Entity target = state.getTargetEntity(worldIn);
            if (state.getProudSoulCount() < SlashBladeConfig.SUMMON_SWORD_ART_COST.get()) {
                return;
            }
            state.setProudSoulCount(state.getProudSoulCount() - SlashBladeConfig.SUMMON_SWORD_ART_COST.get());

            AdvancementHelper.grantCriterion(entity, SummonedSwordArts.ADVANCEMENT_HEAVY_RAIN_SWORDS);

            int rank = entity.getCapability(CapabilityConcentrationRank.RANK_POINT)
                    .map(r -> r.getRank(worldIn.getGameTime()).level).orElse(0);

            Vec3 basePos;

            if (target != null) {
                basePos = target.position();
            } else {
                Vec3 forwardDir = calculateViewVector(0, entity.getYRot());
                basePos = entity.getPosition(0).add(forwardDir.scale(5));
            }

            float yOffset = 7;
            basePos = basePos.add(0, yOffset, 0);

            {
                EntityHeavyRainSwords ss = new EntityHeavyRainSwords(SlashBlade.RegistryEvents.HeavyRainSwords, worldIn);

                ss.setOwner(entity);
                ss.setColor(state.getColorCode());
                ss.setRoll(0);
                ss.setDamage(powerLevel);
                ss.startRiding(entity, true);

                ss.setDelay(0);

                ss.setPos(basePos);

                ss.setXRot(-90);

                worldIn.addFreshEntity(ss);
            }

            int count = 9 + Math.min(rank - 1, 0);
            int multiplier = 2;
            for (int i = 0; i < count; i++) {
                for (int l = 0; l < multiplier; l++) {
                    EntityHeavyRainSwords ss = new EntityHeavyRainSwords(SlashBlade.RegistryEvents.HeavyRainSwords, worldIn);

                    ss.setOwner(entity);
                    ss.setColor(state.getColorCode());
                    ss.setRoll(0);
                    ss.setDamage(powerLevel);
                    ss.startRiding(entity, true);

                    ss.setDelay(i);

                    ss.setSpread(basePos);

                    ss.setXRot(-90);

                    worldIn.addFreshEntity(ss);

                    entity.playNotifySound(SoundEvents.CHORUS_FRUIT_TELEPORT, SoundSource.PLAYERS, 0.2F, 1.45F);
                }
            }
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
