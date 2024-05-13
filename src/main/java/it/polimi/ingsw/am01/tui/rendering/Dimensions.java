package it.polimi.ingsw.am01.tui.rendering;

// FIXME: disallow negative values
public record Dimensions(int width, int height) {
    public static final Dimensions ZERO = new Dimensions(0, 0);

    public static Dimensions of(int width, int height) {
        return new Dimensions(width, height);
    }

    public static Dimensions constrained(Constraint constraint, int preferredWidth, int preferredHeight) {
        int w = preferredWidth;
        w = Math.max(w, constraint.min().width); // ensure w is not smaller than min.width
        w = Math.min(w, constraint.max().width); // ensure w is not bigger that max.width

        int h = preferredHeight;
        h = Math.max(h, constraint.min().height);
        h = Math.min(h, constraint.max().height);

        return new Dimensions(w, h);
    }

    public Dimensions constrain(Constraint constraint) {
        return Dimensions.constrained(constraint, width, height);
    }

    public Dimensions shrink(int shrinkW, int shrinkH) {
        return new Dimensions(width - shrinkW, height - shrinkH);
    }

    public Dimensions grow(int growW, int growH) {
        return new Dimensions(width + growW, height + growH);
    }
}
