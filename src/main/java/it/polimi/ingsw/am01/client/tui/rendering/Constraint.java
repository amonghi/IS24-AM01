package it.polimi.ingsw.am01.client.tui.rendering;

public record Constraint(Dimensions min, Dimensions max) {
    public static Constraint of(int minW, int maxW, int minH, int maxH) {
        return new Constraint(new Dimensions(minW, minH), new Dimensions(maxW, maxH));
    }

    public static Constraint minMax(Dimensions min, Dimensions max) {
        return new Constraint(min, max);
    }

    public static Constraint max(Dimensions max) {
        return new Constraint(Dimensions.ZERO, max);
    }

    public Constraint growMin(int growW, int growH) {
        return new Constraint(min.grow(growW, growH), max);
    }

    public Constraint shrinkMax(int shrinkW, int shrinkH) {
        return new Constraint(min, max.shrink(shrinkW, shrinkH));
    }
}
