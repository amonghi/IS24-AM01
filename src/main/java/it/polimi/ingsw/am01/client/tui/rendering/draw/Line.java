package it.polimi.ingsw.am01.client.tui.rendering.draw;

import it.polimi.ingsw.am01.client.tui.rendering.Dimensions;
import it.polimi.ingsw.am01.client.tui.rendering.Position;

import java.util.NoSuchElementException;

public class Line {
    private Line() {
    }

    public static void right(DrawArea a, Position p, int len, char c) {
        right(a, p.x(), p.y(), len, c);
    }

    public static void right(DrawArea a, int x, int y, int len, char c) {
        for (int i = 0; i < len; i++) {
            a.draw(x + i, y, c);
        }
    }

    public static void down(DrawArea a, Position p, int len, char c) {
        down(a, p.x(), p.y(), len, c);
    }

    public static void down(DrawArea a, int x, int y, int len, char c) {
        for (int i = 0; i < len; i++) {
            a.draw(x, y + i, c);
        }
    }

    public static void rectangle(DrawArea a, Position tl, Dimensions dim, Line.Style style) {
        int t = tl.y();
        int b = tl.y() + dim.height() - 1;
        int l = tl.x();
        int r = tl.x() + dim.width() - 1;

        // These positions represent where the angles of the box are
        // but .tr, .tl, ecc... in Line.Style represent what part of the line is "connected".
        // The tl angle of the box will be connected below and to the right,
        // so it will use the "opposite" angle in Line.Style.
        Position tr = Position.of(r, t);
        Position bl = Position.of(l, b);
        Position br = Position.of(r, b);

        Line.right(a, tl, dim.width(), style.lr());
        Line.right(a, bl, dim.width(), style.lr());

        Line.down(a, tl, dim.height(), style.tb());
        Line.down(a, tr, dim.height(), style.tb());

        a.draw(tl, style.br());
        a.draw(tr, style.bl());
        a.draw(bl, style.tr());
        a.draw(br, style.tl());
    }

    /**
     * Character are categorized based on "which sides are connected"
     * <pre>
     *   T
     * L ┼ R
     *   B
     * </pre>
     * <p>
     * The names are given following the order t, b, l, r
     */
    public record Style(
            char lr,
            char tb,
            char br,
            char bl,
            char tr,
            char tl,
            char tbr,
            char tbl,
            char blr,
            char tlr,
            char tblr
    ) {
        public static final Style DEFAULT = new Style('─', '│', '┌', '┐', '└', '┘', '├', '┤', '┬', '┴', '┼');
        public static final Style ROUNDED = new Style('─', '│', '╭', '╮', '╰', '╯', '├', '┤', '┬', '┴', '┼');

        public char get(boolean t, boolean b, boolean l, boolean r) {
            // thanks, Java, for not supporting pattern-matching with multiple variables
            byte bits = 0;
            if (t) bits += 0b1000;
            if (b) bits += 0b0100;
            if (l) bits += 0b0010;
            if (r) bits += 0b0001;

            return switch (bits) {
                case 0b0011 -> this.lr;
                case 0b1100 -> this.tb;
                case 0b0101 -> this.br;
                case 0b0110 -> this.bl;
                case 0b1001 -> this.tr;
                case 0b1010 -> this.tl;
                case 0b1101 -> this.tbr;
                case 0b1110 -> this.tbl;
                case 0b0111 -> this.blr;
                case 0b1011 -> this.tlr;
                case 0b1111 -> this.tblr;
                default -> throw new NoSuchElementException();
            };
        }
    }
}
