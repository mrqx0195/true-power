package net.mrqx.truepower.util;

import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap;
import mods.flammpfeil.slashblade.registry.ComboStateRegistry;
import mods.flammpfeil.slashblade.util.InputCommand;
import net.minecraft.resources.ResourceLocation;
import net.mrqx.sbr_core.utils.InputStream;
import net.mrqx.truepower.registry.TruePowerComboStateRegistry;

import java.util.EnumSet;
import java.util.LinkedList;
import java.util.SequencedMap;
import java.util.function.Function;

public class TruePowerInputTimeLines {
    public static final SequencedMap<LinkedList<InputStream.TimeLineKeyInput>, ResourceLocation> INPUTS = new Object2ObjectLinkedOpenHashMap<>();
    public static final SequencedMap<LinkedList<InputStream.TimeLineKeyInput>, ResourceLocation> PRE_INPUTS = new Object2ObjectLinkedOpenHashMap<>();
    
    public static final LinkedList<InputStream.TimeLineKeyInput> RAPID_SLASH_INPUT_TIME_LINE = new LinkedList<>();
    public static final LinkedList<InputStream.TimeLineKeyInput> UPPER_SLASH_INPUT_TIME_LINE = new LinkedList<>();
    public static final LinkedList<InputStream.TimeLineKeyInput> AERIAL_CLEAVE_INPUT_TIME_LINE = new LinkedList<>();
    public static final LinkedList<InputStream.TimeLineKeyInput> VOID_SLASH_INPUT_TIME_LINE = new LinkedList<>();
    
    public static final LinkedList<InputStream.TimeLineKeyInput> TRICK_DOWN_INPUT_TIME_LINE = new LinkedList<>();
    public static final LinkedList<InputStream.TimeLineKeyInput> EASY_TRICK_DOWN_INPUT_TIME_LINE = new LinkedList<>();
    
    private static final SequencedMap<LinkedList<InputStream.TimeLineKeyInput>, LinkedList<InputStream.TimeLineKeyInput>> CACHED_PRE_INPUT_TIME_LINE = new Object2ObjectLinkedOpenHashMap<>();
    
    public static final InputStream.TimeLineKeyInput PRE_CLICK_INPUT_KEY = new InputStream.TimeLineKeyInput(4, -2, InputCommand.R_DOWN, EnumSet.noneOf(InputCommand.class), null);
    
    public static final Function<LinkedList<InputStream.TimeLineKeyInput>, LinkedList<InputStream.TimeLineKeyInput>> PRE_INPUT_TIME_LINE_CONVERTER = original -> {
        LinkedList<InputStream.TimeLineKeyInput> preInputTimeLine = new LinkedList<>(original);
        preInputTimeLine.addFirst(PRE_CLICK_INPUT_KEY);
        CACHED_PRE_INPUT_TIME_LINE.put(original, preInputTimeLine);
        return preInputTimeLine;
    };
    
    static {
        RAPID_SLASH_INPUT_TIME_LINE.add(new InputStream.TimeLineKeyInput(4, -2,
            InputCommand.FORWARD, EnumSet.of(InputCommand.ON_GROUND), null));
        
        UPPER_SLASH_INPUT_TIME_LINE.add(new InputStream.TimeLineKeyInput(4, -2,
            InputCommand.BACK, EnumSet.of(InputCommand.ON_GROUND), null));
        
        AERIAL_CLEAVE_INPUT_TIME_LINE.add(new InputStream.TimeLineKeyInput(4, -2,
            InputCommand.BACK, EnumSet.of(InputCommand.ON_AIR), null));
        
        VOID_SLASH_INPUT_TIME_LINE.add(new InputStream.TimeLineKeyInput(5, -2,
            InputCommand.FORWARD, EnumSet.of(InputCommand.ON_GROUND), InputStream.InputType.START));
        VOID_SLASH_INPUT_TIME_LINE.add(new InputStream.TimeLineKeyInput(7, -2,
            InputCommand.BACK, EnumSet.of(InputCommand.ON_GROUND), null));
        
        TRICK_DOWN_INPUT_TIME_LINE.add(new InputStream.TimeLineKeyInput(5, -2,
            InputCommand.FORWARD, EnumSet.noneOf(InputCommand.class), InputStream.InputType.START));
        TRICK_DOWN_INPUT_TIME_LINE.add(new InputStream.TimeLineKeyInput(7, -2,
            InputCommand.BACK, EnumSet.noneOf(InputCommand.class), null));
        
        EASY_TRICK_DOWN_INPUT_TIME_LINE.add(new InputStream.TimeLineKeyInput(4, -2,
            InputCommand.BACK, EnumSet.noneOf(InputCommand.class), null));
        
        INPUTS.put(VOID_SLASH_INPUT_TIME_LINE, TruePowerComboStateRegistry.VOID_SLASH.getId());
        INPUTS.put(UPPER_SLASH_INPUT_TIME_LINE, ComboStateRegistry.UPPERSLASH.getId());
        INPUTS.put(RAPID_SLASH_INPUT_TIME_LINE, ComboStateRegistry.RAPID_SLASH.getId());
        
        INPUTS.put(AERIAL_CLEAVE_INPUT_TIME_LINE, ComboStateRegistry.AERIAL_CLEAVE.getId());
        
        INPUTS.forEach((timeLine, id) ->
            PRE_INPUTS.put(getPreInputTimeLine(timeLine), id));
    }
    
    public static LinkedList<InputStream.TimeLineKeyInput> getPreInputTimeLine(LinkedList<InputStream.TimeLineKeyInput> original) {
        return CACHED_PRE_INPUT_TIME_LINE.getOrDefault(original, PRE_INPUT_TIME_LINE_CONVERTER.apply(original));
    }
}
