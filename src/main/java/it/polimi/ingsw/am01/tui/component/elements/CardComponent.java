package it.polimi.ingsw.am01.tui.component.elements;

import it.polimi.ingsw.am01.tui.component.Component;
import it.polimi.ingsw.am01.tui.rendering.Constraint;
import it.polimi.ingsw.am01.tui.rendering.Dimensions;
import it.polimi.ingsw.am01.tui.rendering.Sized;
import it.polimi.ingsw.am01.tui.rendering.draw.DrawArea;

import java.util.List;

public class CardComponent implements Component {
    private static final char[][] CARD_BUF;
    private static final int CARD_W;
    private static final int CARD_H;

    static {
        CARD_BUF = toCharMatrix("""
                ╭────┬────┬───┬────┬────╮
                │    │    │ 3 │    │ \uD83C\uDF44 │
                ├────┘    └───┘    └────┤
                │                       │
                │         A I P         │
                │                       │
                ├────┐   ┌─────┐   ┌────┤
                │ \uD83C\uDF31 │   │FFFFF│   │    │
                ╰────┴───┴─────┴───┴────╯
                """);
        CARD_H = CARD_BUF.length;
        CARD_W = CARD_BUF[0].length;
    }

    private static char[][] toCharMatrix(String s) {
        String[] lines = s.split("\n");
        char[][] matrix = new char[lines.length][];
        for (int i = 0; i < lines.length; i++) {
            matrix[i] = lines[i].toCharArray();
        }
        return matrix;
    }

    @Override
    public Sized layout(Constraint constraint) {
        Dimensions d = Dimensions.constrained(constraint, CARD_W, CARD_H);
        return new Sized(this, d, List.of());
    }

    @Override
    public void drawSelf(DrawArea a) {
        // TODO: render a card for real
        for (int y = 0; y < CARD_H && y < a.dimensions().height(); y++) {
            for (int x = 0; x < CARD_W && x < a.dimensions().width(); x++) {
                a.draw(x, y, CARD_BUF[y][x]);
            }
        }
    }
}
