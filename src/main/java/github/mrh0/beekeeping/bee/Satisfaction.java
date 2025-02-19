package github.mrh0.beekeeping.bee;

import net.minecraft.network.chat.TranslatableComponent;

public enum Satisfaction {
    SATISFIED("tooltip.beekeeping.apiary.satisfied"),
    UNSATISFIED("tooltip.beekeeping.apiary.unsatisfied"),
    NOT_WORKING("tooltip.beekeeping.apiary.not_working");

    public final TranslatableComponent component;

    Satisfaction(String key) {
        this.component = new TranslatableComponent(key);
    }

    public static Satisfaction calc(Satisfaction...list) {
        Satisfaction satisfaction = SATISFIED;
        for(Satisfaction s : list) {
            if(s == NOT_WORKING)
                return NOT_WORKING;
            if(s == UNSATISFIED)
                satisfaction = UNSATISFIED;
        }
        return satisfaction;
    }

    public static Satisfaction of(int index) {
        return switch(index) {
            case 0 -> SATISFIED;
            case 1 -> UNSATISFIED;
            default -> NOT_WORKING;
        };
    }

    @Deprecated
    public static Satisfaction map(int value, int satisfied, int unsatisfied) {
        if(value < unsatisfied)
            return Satisfaction.NOT_WORKING;
        else if(value < satisfied)
            return Satisfaction.UNSATISFIED;
        return  Satisfaction.SATISFIED;
    }
}